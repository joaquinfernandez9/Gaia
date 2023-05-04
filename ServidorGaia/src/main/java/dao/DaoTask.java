package dao;

public interface DaoTask {
    // add task = insert into task (id, task_name, initial_time, end_time, completed, username) values (?, ?, ?, ?, 0, ?)
    String addTask(String taskName, String initialTime, String endTime, String username);

    // get task = select * from task where task_name = ? and username = ?
    String getTask(String taskName, String username);

    // delete task = delete from task where task_name = ? and username = ?
    String deleteTask(String taskName, String username);

    // get tasks = select * from task where username = ?
    String getTasks(String username);

    // update task = update task set completed = 1 where task_name = ? and username = ?
    //TODO: esto deberia actualizar tambien el valor del progreso en el arbol
    String updateTask(String taskName, String username);

    // delete completed tasks = delete from task where completed = 1 and username = ?
    String deleteCompletedTasks(String username);
}
