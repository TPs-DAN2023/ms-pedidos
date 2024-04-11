package dan.ms.tp.mspedidos.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import dan.ms.tp.mspedidos.dao.PedidoRepository;
import dan.ms.tp.mspedidos.exception.InvalidOperationException;
import dan.ms.tp.mspedidos.exception.NotFoundException;
import dan.ms.tp.mspedidos.exception.UnexpectedResponseException;
import dan.ms.tp.mspedidos.modelo.Cliente;
import dan.ms.tp.mspedidos.modelo.EstadoPedido;
import dan.ms.tp.mspedidos.modelo.HistorialEstado;
import dan.ms.tp.mspedidos.modelo.Pedido;
import dan.ms.tp.mspedidos.modelo.PedidoDetalle;
import dan.ms.tp.mspedidos.modelo.Producto;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import java.util.function.Supplier;

@Service
public class PedidoService {

  @Autowired
  PedidoRepository repo;
  @Autowired
  Environment env;

  @Autowired
  private CircuitBreakerRegistry circuitBreakerRegistry;

  public Pedido save(Pedido pedido) throws NotFoundException, UnexpectedResponseException {

    RestTemplate restTemplate = new RestTemplate();
    pedido.setFecha(Instant.now());
    pedido.setEstados(new ArrayList<>());

    try {
      CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
          .recordException(e -> e instanceof RuntimeException)
          .build();

      CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("myCircuitBreaker", circuitBreakerConfig);

      System.out.println("Circuit Breaker State: " + circuitBreaker.getState());

      Cliente cliente = restTemplate.exchange(
          env.getProperty("env.cliente.url") + pedido.getCliente().getId(),
          HttpMethod.GET, null,
          Cliente.class).getBody();

      if (cliente == null)
        throw new NotFoundException("Cliente");

      pedido.setCliente(cliente);

      Double total = 0D;
      for (PedidoDetalle dp : pedido.getDetallePedido()) {
        dp.setTotal(dp.getCantidad() * dp.getProducto().getPrecio() * (1 - dp.getDescuento()));
        total += dp.getTotal();

        Supplier<Producto> productoSupplier = CircuitBreaker
            .decorateSupplier(circuitBreaker, () -> restTemplate.exchange(
                env.getProperty("env.producto.url") + dp.getProducto().getId(),
                HttpMethod.GET, null,
                Producto.class).getBody());

        Producto prod = circuitBreaker.executeSupplier(productoSupplier);

        if (prod == null)
          throw new NotFoundException("Producto");
        dp.getProducto().setStockActual(prod.getStockActual());
        dp.getProducto().setNombre(prod.getNombre());
        if (prod.getStockActual() < dp.getCantidad() && pedido.getEstados().isEmpty()) {
          pedido.getEstados().add(new HistorialEstado(
              EstadoPedido.SIN_STOCK,
              pedido.getFecha(),
              "Sistema pedidos",
              "No hay stock suficiente." + prod.getStockActual() + "<" + dp.getCantidad()));
        }
      }

      pedido.setTotal(total);

      if (cliente.getMaximoCuentaCorriente() < pedido.getTotal()) {
        HistorialEstado estado = new HistorialEstado(EstadoPedido.RECHAZADO, pedido.getFecha(), "Sistema pedidos",
            "El cliente no tiene saldo suficiente");
        if (pedido.getEstados().size() > 0)
          pedido.getEstados().clear();
        pedido.getEstados().add(estado);
      }
      if (pedido.getEstados().isEmpty()) {
        HistorialEstado estado = new HistorialEstado(EstadoPedido.RECIBIDO, pedido.getFecha(), "Sistema pedidos",
            "Pedido recibido correctamente");
        pedido.getEstados().add(estado);
      }

      return repo.save(pedido);

    } catch (CallNotPermittedException exc) {
      System.out.println(exc.toString());
      throw new UnexpectedResponseException("Circuit breaker is open");
    } catch (Exception exc) {
      System.out.println(exc.toString());
      throw new UnexpectedResponseException("Unable to connect to the service");
    }
  }

  public List<Pedido> getPedidosFilteredBy(String razonSocial, Instant desde, Instant hasta) {

    // el repo lo transforma a DATE
    if (desde == null)
      desde = new Date(Long.MIN_VALUE).toInstant();
    if (hasta == null)
      hasta = new Date(Long.MAX_VALUE).toInstant();

    return razonSocial == null ? repo.findByFecha(desde, hasta) : repo.findByClienteFecha(razonSocial, desde, hasta);
  }

  public Pedido getPedidoById(String id) throws NotFoundException {
    return repo.findById(id)
        .orElseThrow(() -> new NotFoundException("Pedido"));
  }

  public Pedido cancelarPedido(String idPedido) throws NotFoundException, InvalidOperationException {
    Pedido pedido = repo.findById(idPedido)
        .orElseThrow(() -> new NotFoundException("Pedido"));

    List<EstadoPedido> invalidos = List.of(EstadoPedido.CANCELADO, EstadoPedido.ENTREGADO, EstadoPedido.RECHAZADO,
        EstadoPedido.EN_DISTRIBUCION);
    if (pedido.getEstados().stream().anyMatch(e -> invalidos.contains(e.getEstado())))
      throw new InvalidOperationException("El pedido id: " + idPedido + " no puede ser cancelado");

    HistorialEstado estado = new HistorialEstado(EstadoPedido.CANCELADO, Instant.now(), "Sistema pedidos",
        "Pedido cancelado");
    pedido.getEstados().add(estado);
    return repo.save(pedido);
  }

}
