package dao;

public class DaoTask {
    // add task = insert into task (id, task_name, initial_time, end_time, completed, username) values (?, ?, ?, ?, 0, ?)

    // get task = select * from task where task_name = ? and username = ?

    // delete task = delete from task where task_name = ? and username = ?

    // get tasks = select * from task where username = ?

    // update task = update task set completed = 1 where task_name = ? and username = ?
    //TODO: esto deberia actualizar tambien el valor del progreso en el arbol


    // delete completed tasks = delete from task where completed = 1 and username = ?


}
