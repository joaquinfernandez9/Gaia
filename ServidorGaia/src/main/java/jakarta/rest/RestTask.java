package jakarta.rest;


import domain.model.Task;
import domain.services.ServicesTask;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import java.util.List;

//@Path("/task")
@Produces("application/json")
@Consumes("application/json")
public class RestTask {
    private final ServicesTask task;

    @Inject
    public RestTask(ServicesTask task) {
        this.task = task;
    }

    @POST
    @Path("/addTask")
    public Task addTask(String username, String taskName, String taskDescription, String taskDate) {
        return task.addTask(username, taskName, taskDescription, taskDate);
    }

    @POST
    @Path("/getTask")
    public Task getTask(String username, String taskName) {
        return task.getTask(username, taskName);
    }

    @POST
    @Path("/deleteTask")
    public Task deleteTask(String username, String taskName) {
        return task.deleteTask(username, taskName);
    }

    @POST
    @Path("/getTasks")
    public List<Task> getTasks(String username) {
        return task.getTasks(username);
    }

    @POST
    @Path("/updateTask")
    public Task updateTask(String username, String taskName) {
        return task.updateTask(taskName, username);
    }

    @POST
    @Path("/deleteCompletedTasks")
    public List<Task> deleteCompletedTasks(String username) {
        return task.deleteCompletedTasks(username);
    }



}
