package domain.services;

public interface ServicesTask {
    String addTask(String taskName, String initialTime, String endTime, String username);

    String getTask(String taskName, String username);

    String deleteTask(String taskName, String username);

    String getTasks(String username);

    String updateTask(String taskName, String username);

    String deleteCompletedTasks(String username);
}
