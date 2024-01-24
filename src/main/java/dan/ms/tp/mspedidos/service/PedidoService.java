package dan.ms.tp.mspedidos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dan.ms.tp.mspedidos.dao.PedidoRepository;
import dan.ms.tp.mspedidos.exception.UnimplementedMethodException;
import dan.ms.tp.mspedidos.modelo.Pedido;

@Service
public class PedidoService {
    
    @Autowired PedidoRepository repo;

    public Pedido save(Pedido pedido) throws UnimplementedMethodException{
        throw new UnimplementedMethodException();
    }

}
