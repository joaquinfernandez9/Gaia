package domain.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Task {
    private int id;
    private String taskName;
    // hh:mm:ss
    private LocalDateTime initTime;
    private LocalDateTime endTime;
    private int completed;
    private String username;

    public Task() {
    }

    public Task(String taskName) {
        this.taskName = taskName;
    }

    public Task(String taskName, LocalDateTime initTime, LocalDateTime endTime, int completed, String username) {
        this.taskName = taskName;
        this.initTime = initTime;
        this.endTime = endTime;
        this.completed = completed;
        this.username = username;
    }

    public Task(int id, String taskName, LocalDateTime initTime, LocalDateTime endTime, int completada, String username) {
        this.id = id;
        this.taskName = taskName;
        this.initTime = initTime;
        this.endTime = endTime;
        this.completed = completada;
        this.username = username;
    }

    public Task(String taskName, String username) {
        this.taskName = taskName;
        this.username = username;
    }
}
