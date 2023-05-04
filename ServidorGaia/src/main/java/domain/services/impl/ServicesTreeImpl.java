package domain.services.impl;

import dao.DaoTree;
import domain.services.ServicesTree;
import jakarta.inject.Inject;

public class ServicesTreeImpl implements ServicesTree {

    private DaoTree tree;

    @Inject
    public ServicesTreeImpl(DaoTree tree) {
        this.tree = tree;
    }

    @Override public String getLevel(String username){
        return tree.getLevel(username);
    }

    @Override public String updateLevel(String username){
        return tree.updateLevel(username);
    }
}
