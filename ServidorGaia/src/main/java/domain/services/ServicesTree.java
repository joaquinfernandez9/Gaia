package domain.services;

import domain.model.Tree;

public interface ServicesTree {
    Tree getLevel(String username);

    Tree updateLevel(String username);
}

