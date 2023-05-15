package dao;

import domain.model.Task;

import java.util.List;

public interface DaoTask {
    Task add(String taskName, String initialTime, String endTime, String username);

    Task get(String taskName, String username);

    Task delete(String taskName, String username);

    List<Task> getAllByUser(String username);

    Task update(String taskName, String username);

    List<Task> deleteCompletedTasks(String username);
}
