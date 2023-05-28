package domain.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Friend {
    private String username1;
    private String username2;
    private int value;
    private LocalDateTime requestDate;

    public Friend(String usuario1, String usuario2, int valor, LocalDateTime fecha_solicitud) {
        this.username1 = usuario1;
        this.username2 = usuario2;
        this.value = valor;
        this.requestDate = fecha_solicitud;
    }

    public Friend() {
    }
}
