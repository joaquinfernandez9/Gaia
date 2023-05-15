package domain.services.impl;

import dao.DaoTree;
import domain.model.Tree;
import domain.services.ServicesTree;
import jakarta.inject.Inject;

public class ServicesTreeImpl implements ServicesTree {

    private DaoTree tree;

    @Inject
    public ServicesTreeImpl(DaoTree tree) {
        this.tree = tree;
    }

    @Override
    public Tree getLevel(String username) {
        return tree.get(username);
    }


    @Override
    public Tree updateLevel(String username) {
        return tree.updateLevel(username);
    }
}
