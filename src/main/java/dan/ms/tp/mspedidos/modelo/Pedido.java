package dan.ms.tp.mspedidos.modelo;

import java.time.Instant;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Document(collection = "dan_pedidos")
public class Pedido {

    @Id
    @NotNull private String id;
    private Instant fecha;
    @NotNull private Integer numeroPedido;
    private String user;
    private String observaciones;
    @NotNull private Cliente cliente;
    
    @NotNull
    @Size(min = 1, max=1000, message = "El pedido debe tener entre 1 y 1000 productos")
    private List<PedidoDetalle> detallePedido;
    private List<HistorialEstado> estados;
    private Double total;
}
