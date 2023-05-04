package dao;

public interface DaoTree {
    // get level = select * from tree where username = ?
    String getLevel(String username);

    // update level+1 and progress = 0 == UPDATE tree SET level = level+1, progress = 0 WHERE username = ?
    String updateLevel(String username);
}
