package domain.services;

import domain.model.Account;
import domain.model.Task;

import java.util.List;

public interface ServicesTask {
    Task add(Task task);

    Task get(Task task);

    Task delete(Task task);

    List<Task> get(Account account);

    Task update(Task task);

    int deleteCompletedTasks(Account account);
}
