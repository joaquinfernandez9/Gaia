package domain.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Friend {
    private String usuario1;
    private String usuario2;
    private int valor;
    private LocalDateTime requestDate;

    public Friend(String usuario1, String usuario2, int valor, LocalDateTime fecha_solicitud) {
        this.usuario1 = usuario1;
        this.usuario2 = usuario2;
        this.valor = valor;
        this.requestDate = fecha_solicitud;
    }

    public Friend() {
    }
}
