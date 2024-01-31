package dan.ms.tp.mspedidos.modelo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PedidoDetalle {
    @NotNull
    private Producto producto;
    @NotNull
    private Integer cantidad;
    @NotNull
    private Double descuento;
    private Double total;
}
