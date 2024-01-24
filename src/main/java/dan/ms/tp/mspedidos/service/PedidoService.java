package dan.ms.tp.mspedidos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dan.ms.tp.mspedidos.dao.PedidoRepository;

@Service
public class PedidoService {
    
    @Autowired PedidoRepository repo;

}
