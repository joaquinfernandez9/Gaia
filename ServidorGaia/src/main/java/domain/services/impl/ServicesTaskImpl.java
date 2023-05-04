package domain.services.impl;

import dao.DaoTask;
import domain.services.ServicesTask;
import jakarta.inject.Inject;

public class ServicesTaskImpl implements ServicesTask {

    private DaoTask task;

    @Inject
    public ServicesTaskImpl(DaoTask task) {
        this.task = task;
    }

    @Override public String addTask(String taskName, String initialTime, String endTime, String username){
        return task.addTask(taskName, initialTime, endTime, username);
    }

    @Override public String getTask(String taskName, String username){
        return task.getTask(taskName, username);
    }

    @Override public String deleteTask(String taskName, String username){
        return task.deleteTask(taskName, username);
    }

    @Override public String getTasks(String username){
        return task.getTasks(username);
    }

    @Override public String updateTask(String taskName, String username){
        return task.updateTask(taskName, username);
    }

    @Override public String deleteCompletedTasks(String username){
        return task.deleteCompletedTasks(username);
    }


}
