package dan.ms.tp.mspedidos.modelo;

import java.time.Instant;

import lombok.Data;

@Data
public class HistorialEstado {
    private EstadoPedido estado;
    private Instant fechaEstado;
    private String userEstado;
    private String detalle;

    public HistorialEstado(EstadoPedido estado, Instant fecha, String userEstado, String detalle) {
        this.estado = estado;
        this.fechaEstado = fecha;
        this.userEstado = userEstado;
        this.detalle = detalle;
    }

}
