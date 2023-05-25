package domain.services.impl;

import dao.DaoTask;
import domain.model.Account;
import domain.model.Task;
import domain.services.ServicesTask;
import jakarta.inject.Inject;

import java.util.List;

public class ServicesTaskImpl implements ServicesTask {

    private DaoTask daoTask;

    @Inject
    public ServicesTaskImpl(DaoTask task) {
        this.daoTask = task;
    }

    @Override public Task add(Task task){
        return daoTask.add(task);
    }

    @Override public Task get(Task task){
        return daoTask.get(task);
    }

    @Override public Task delete(Task task){
        return daoTask.delete(task);
    }

    @Override public List<Task> get(Account account){
        return daoTask.getAllByUser(account);
    }

    @Override public Task update(Task task){
        return daoTask.update(task);
    }

    @Override public int deleteCompletedTasks(Account account){
        return daoTask.deleteCompletedTasks(account);
    }


}
