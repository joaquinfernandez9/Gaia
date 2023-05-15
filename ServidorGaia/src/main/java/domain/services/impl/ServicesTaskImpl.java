package domain.services.impl;

import dao.DaoTask;
import domain.model.Task;
import domain.services.ServicesTask;
import jakarta.inject.Inject;

import java.util.List;

public class ServicesTaskImpl implements ServicesTask {

    private DaoTask task;

    @Inject
    public ServicesTaskImpl(DaoTask task) {
        this.task = task;
    }

    @Override public Task addTask(String taskName, String initialTime, String endTime, String username){
        return task.add(taskName, initialTime, endTime, username);
    }

    @Override public Task getTask(String taskName, String username){
        return task.get(taskName, username);
    }

    @Override public Task deleteTask(String taskName, String username){
        return task.delete(taskName, username);
    }

    @Override public List<Task> getTasks(String username){
        return task.getAllByUser(username);
    }

    @Override public Task updateTask(String taskName, String username){
        return task.update(taskName, username);
    }

    @Override public List<Task> deleteCompletedTasks(String username){
        return task.deleteCompletedTasks(username);
    }


}
