package dan.ms.tp.mspedidos.modelo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class Producto {
    
    @NotNull
    private Integer id;
    private String nombre;
    @NotNull
    @Positive
    private Double precio;
    private Integer stockActual;
}
