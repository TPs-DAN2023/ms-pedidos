package dan.ms.tp.mspedidos.service;

import java.time.Instant;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import dan.ms.tp.mspedidos.dao.PedidoRepository;
import dan.ms.tp.mspedidos.exception.NotFoundException;
import dan.ms.tp.mspedidos.exception.UnexpectedResponseException;
import dan.ms.tp.mspedidos.modelo.Cliente;
import dan.ms.tp.mspedidos.modelo.EstadoPedido;
import dan.ms.tp.mspedidos.modelo.HistorialEstado;
import dan.ms.tp.mspedidos.modelo.Pedido;
import dan.ms.tp.mspedidos.modelo.PedidoDetalle;
import dan.ms.tp.mspedidos.modelo.Producto;

@Service
public class PedidoService {
    
    @Autowired PedidoRepository repo;

    //TODO: complete with API's urls
    public Pedido save(Pedido pedido) throws NotFoundException, UnexpectedResponseException {

        RestTemplate restTemplate = new RestTemplate();
        pedido.setFecha(Instant.now());
        pedido.setEstados(new ArrayList<>());
        
        try {
            
            Cliente cliente = restTemplate.exchange(
                "..../api/cliente/"+pedido.getCliente().getId(),
                HttpMethod.GET, null,
                Cliente.class
            ).getBody();

            if (cliente == null) throw new NotFoundException("Cliente");

            Double total = 0D;
            for(PedidoDetalle dp : pedido.getDetallePedido()){
                //TODO: check values
                dp.setTotal(dp.getCantidad() * dp.getProducto().getPrecio() * (1-dp.getDescuento()));
                total += dp.getTotal();

                Producto prod = restTemplate.exchange(
                    "..../api/productos/"+dp.getProducto().getId(),
                    HttpMethod.GET, null,
                    Producto.class
                ).getBody();

                if (prod == null) throw new NotFoundException("Producto");

                if (prod.getStock() < dp.getCantidad() && pedido.getEstados().isEmpty()) {
                    pedido.getEstados().add( new HistorialEstado(
                        EstadoPedido.SIN_STOCK,
                        pedido.getFecha(),
                        "Sistema pedidos",
                        "No hay stock suficiente."+prod.getStock()+"<"+dp.getCantidad()
                    ));
                }
            }
            
            pedido.setTotal(total);

            if (cliente.getMaximoCuentaCorriente() < pedido.getTotal()) {
                HistorialEstado estado = new HistorialEstado(EstadoPedido.RECHAZADO, pedido.getFecha(), "Sistema pedidos", "El cliente no tiene saldo suficiente");
                if (pedido.getEstados().size() > 0)
                    pedido.getEstados().clear();
                pedido.getEstados().add(estado);
            }
            if (pedido.getEstados().isEmpty()) {
                HistorialEstado estado = new HistorialEstado(EstadoPedido.RECIBIDO, pedido.getFecha(), "Sistema pedidos", "Pedido recibido correctamente");
                pedido.getEstados().add(estado);
            }

            return repo.save(pedido);

        } catch (RestClientException exc) {
            throw new UnexpectedResponseException(exc.getMessage());
        }
    }

}
