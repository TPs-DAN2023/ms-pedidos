package dan.ms.tp.mspedidos.modelo;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PedidoDetalle {
    @NotNull
    @Valid
    private Producto producto;
    @NotNull
    private Integer cantidad;
    @NotNull
    private Double descuento;
    private Double total;
}
