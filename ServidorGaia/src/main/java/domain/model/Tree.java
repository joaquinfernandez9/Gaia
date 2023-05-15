package domain.model;

import lombok.Data;

@Data
public class Tree {
    private String username;
    private int nivel;
    private int progreso;

    public Tree() {
    }

    public Tree(String username, int nivel, int progreso) {
        this.username = username;
        this.nivel = nivel;
        this.progreso = progreso;
    }
}
