package dan.ms.tp.mspedidos.modelo;

import java.time.Instant;

import lombok.Data;

@Data
public class HistorialEstado {
    private EstadoPedido estado;
    private Instant fechaEstado;
    private String userEstado;
    private String detalle;

    public HistorialEstado(EstadoPedido estado, Instant fechaEstado, String userEstado, String detalle) {
        this.estado = estado;
        this.fechaEstado = fechaEstado;
        this.userEstado = userEstado;
        this.detalle = detalle;
    }

}
