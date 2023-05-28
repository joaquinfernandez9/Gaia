package jakarta.rest;


import domain.model.Account;
import domain.model.Task;
import domain.services.ServicesTask;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/task")
@Produces("application/json")
@Consumes("application/json")
public class RestTask {
    private final ServicesTask taskService;

    @Inject
    public RestTask(ServicesTask task) {
        this.taskService = task;
    }

    @POST
    @Path("/addTask")
    public Response addTask(Task task) {
        Task taskResponse = taskService.add(task);
        if (taskResponse == null) return Response.status(Response.Status.BAD_REQUEST).build();
        else return Response.ok(taskResponse).build();
    }

    @GET
    @Path("/getTask/{username}/{taskName}")
    public Response getTask(@PathParam("username") String username, @PathParam("taskName") String taskName) {
        Task task = taskService.get(new Task(taskName, username));
        if (task == null) {
            return Response.status(404).build();
        } else {
            return Response.ok(task).build();
        }

    }

    @DELETE
    @Path("/deleteTask/{username}/{taskName}")
    public Response deleteTask(@PathParam("username") String username, @PathParam("taskName") String taskName) {
        Task task = new Task(taskName, username);
        Task tas2 = taskService.delete(task);
        if (tas2 == null) {
            return Response.status(404).build();
        } else {
            return Response.ok(tas2).build();
        }
    }

    @GET
    @Path("/getTasks/{username}")
    public Response getTasks(@PathParam("username") String username) {
        List<Task> tasks = taskService.get(new Account(username));
        if (tasks == null) {
            return Response.status(404).build();
        } else if (tasks.isEmpty()) {
            return Response.status(204).build();
        } else {
            return Response.ok(tasks).build();
        }
    }

    @PUT
    @Path("/updateTask")
    public Response updateTask(Task task) {
        Task taskResponse = taskService.update(task);
        if (taskResponse == null) return Response.status(Response.Status.BAD_REQUEST).build();
        else return Response.ok(taskResponse).build();
    }

    @DELETE
    @Path("/deleteCompletedTasks/{username}")
    public Response deleteCompletedTasks(@PathParam("username") String username) {
        int rs = taskService.deleteCompletedTasks(new Account(username));
        if (rs == 0) {
            return Response.status(404).build();
        } else {
            return Response.ok(rs).build();
        }
    }


}
