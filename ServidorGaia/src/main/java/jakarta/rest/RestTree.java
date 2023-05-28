package jakarta.rest;


import domain.model.Tree;
import domain.services.ServicesTree;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

@Path("/tree")
@Produces("application/json")
@Consumes("application/json")
public class RestTree {

    private final ServicesTree tree;

    @Inject
    public RestTree(ServicesTree tree) {
        this.tree = tree;
    }


    @GET
    @Path("/getTree/{username}")
    public Response get(@PathParam("username") String username) {
        Tree response = tree.getLevel(username);
        if (response != null) {
            return Response.ok(response).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Path("/updateLevel/{username}")
    public Tree updateLevel(@PathParam("username")String username) {
        return tree.updateLevel(username);
    }



}
