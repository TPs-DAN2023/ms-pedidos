package dan.ms.tp.mspedidos.controller;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dan.ms.tp.mspedidos.exception.InvalidOperationException;
import dan.ms.tp.mspedidos.exception.NotFoundException;
import dan.ms.tp.mspedidos.modelo.Pedido;
import dan.ms.tp.mspedidos.service.PedidoService;


@RestController
@RequestMapping("api/pedidos")
public class PedidoController {
    

    @Autowired
    PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<Pedido> guardar(@RequestBody @Validated Pedido pedido) {
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
    public ResponseEntity<List<Pedido>> buscar(@RequestParam(required = false) String razonSocial, 
            @RequestParam(required = false) Instant desde, @RequestParam(required = false) Instant hasta) {
        
        try {
            List<Pedido> pedidos = pedidoService.getPedidosFilteredBy(razonSocial, desde, hasta);
            return ResponseEntity.ok().body(pedidos);
        } catch (Exception e) {
            System.out.println(e.toString());
            return ResponseEntity.status(500).build();
        }

    }

    /*
     * Busqueda por id
     */
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable String id){
        try {
            Pedido p = pedidoService.getPedidoById(id);
            return ResponseEntity.ok().body(p);
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    /*
     * Actualizacion de estado de pedido a CANCELADO
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Pedido> cancelar(@PathVariable String id) {
        try {
            Pedido p = pedidoService.cancelarPedido(id);
            return ResponseEntity.ok().body(p);
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).build();
        } catch (InvalidOperationException e) {
            return ResponseEntity.status(405).build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }


}
