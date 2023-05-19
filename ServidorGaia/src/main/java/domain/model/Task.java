package domain.model;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class Task {
    private int id;
    private String nombre_tarea;
    // hh:mm:ss
    private String hora_inicio;
    private String hora_fin;
    private int completada;
    private String username;

    public Task() {
    }

    public Task(int id, String nombre_tarea, String hora_inicio, String hora_fin, int completada, String username) {
        this.id = id;
        this.nombre_tarea = nombre_tarea;
        this.hora_inicio = hora_inicio;
        this.hora_fin = hora_fin;
        this.completada = completada;
        this.username = username;
    }
}
