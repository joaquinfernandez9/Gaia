package dao;

import domain.model.Account;
import domain.model.Task;

import java.util.List;

public interface DaoTask {
    Task add(Task task);

    Task get(Task task);

    Task delete(Task task);

    List<Task> getAllByUser(Account account);

    Task update(Task task);

    int deleteCompletedTasks(Account account);
}
