package dao;

import domain.model.Tree;

public interface DaoTree {
    // get level = select * from tree where username = ?
    Tree get(String username);

    // update level+1 and progress = 0 == UPDATE tree SET level = level+1, progress = 0 WHERE username = ?
    Tree updateLevel(String username);
}
