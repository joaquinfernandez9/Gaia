package domain.services;

import domain.model.Task;

import java.util.List;

public interface ServicesTask {
    Task addTask(String taskName, String initialTime, String endTime, String username);

    Task getTask(String taskName, String username);

    Task deleteTask(String taskName, String username);

    List<Task> getTasks(String username);

    Task updateTask(String taskName, String username);

    List<Task> deleteCompletedTasks(String username);
}
