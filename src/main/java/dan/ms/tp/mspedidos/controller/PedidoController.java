package dan.ms.tp.mspedidos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dan.ms.tp.mspedidos.modelo.Pedido;
import dan.ms.tp.mspedidos.service.PedidoService;

@RestController
@RequestMapping("api/pedido")
public class PedidoController {
    

    @Autowired
    PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<Pedido> guardar(@RequestBody Pedido pedido){
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Pedido>> buscar(){
        return ResponseEntity.ok().build();
    }
}
