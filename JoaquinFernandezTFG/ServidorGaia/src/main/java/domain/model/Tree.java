package domain.model;

import lombok.Data;

@Data
public class Tree {
    private String username;
    private int level;
    private int progress;

    public Tree() {
    }

    public Tree(String username, int nivel, int progreso) {
        this.username = username;
        this.level = nivel;
        this.progress = progreso;
    }
}
