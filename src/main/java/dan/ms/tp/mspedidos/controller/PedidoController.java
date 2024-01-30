package dan.ms.tp.mspedidos.controller;

import java.util.List;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dan.ms.tp.mspedidos.exception.NotFoundException;
import dan.ms.tp.mspedidos.modelo.Pedido;
import dan.ms.tp.mspedidos.modelo.PedidoDetalle;
import dan.ms.tp.mspedidos.modelo.Producto;
import dan.ms.tp.mspedidos.service.PedidoService;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("api/pedidos")
public class PedidoController {
    

    @Autowired
    PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<Pedido> guardar(@RequestBody Pedido pedido){
        
        // check required fields
        if(pedido.getNumeroPedido() == null || pedido.getCliente() == null || pedido.getDetallePedido() == null)
            return ResponseEntity.status(400).build();

        // check cliente relevant data
        if (pedido.getCliente().getId() == null)
            return ResponseEntity.status(400).build();

        // check producto relevant data & detalle data
        Predicate<Producto> productoValido = p -> p.getId() != null && p.getPrecio() != null;
        Predicate<PedidoDetalle> detalleValido = dp ->  dp.getCantidad() != null  && dp.getDescuento() != null &&
                                                        dp.getProducto() != null && productoValido.test(dp.getProducto());
        if(!pedido.getDetallePedido().stream().allMatch(detalleValido))
            return ResponseEntity.status(400).build();

        try {
            Pedido pedidoGuardado = pedidoService.save(pedido);
            return ResponseEntity.status(201).body(pedidoGuardado);
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    /*
     * Busqueda por cliente y/o fecha
     */
    @GetMapping
    public ResponseEntity<List<Pedido>> buscar(@RequestParam(required = false) String idCliente, @RequestParam(required = false) String fecha){
        //TODO
        return ResponseEntity.ok().build();
    }

    /*
     * Busqueda por id
     */
    @GetMapping("/{id}")
    public ResponseEntity<List<Pedido>> buscarPorId(@PathVariable String id){
        //TODO
        return ResponseEntity.ok().build();
    }

    /*
     * Actualizacion de estado de pedido a CANCELADO
     */
    @PutMapping()
    public ResponseEntity<Pedido> cancelar(@RequestBody Pedido entity) {
        //TODO
        return ResponseEntity.ok().build();
    }


}
