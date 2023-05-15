package domain.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class Friend {
    private String usuario1;
    private String usuario2;
    private int valor;
    private LocalDate fecha_solicitud;

    public Friend(String usuario1, String usuario2, int valor, LocalDate fecha_solicitud) {
        this.usuario1 = usuario1;
        this.usuario2 = usuario2;
        this.valor = valor;
        this.fecha_solicitud = fecha_solicitud;
    }

    public Friend() {
    }
}
