package dan.ms.tp.mspedidos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dan.ms.tp.mspedidos.modelo.Pedido;
import dan.ms.tp.mspedidos.service.PedidoService;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("api/pedidos")
public class PedidoController {
    

    @Autowired
    PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<Pedido> guardar(@RequestBody Pedido pedido){
        //TODO
        return ResponseEntity.ok().build();
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
