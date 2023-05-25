package jakarta.rest;


import domain.model.Tree;
import domain.services.ServicesTree;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;

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
    public Tree get(@PathParam("username") String username) {
        return tree.getLevel(username);
    }

    @POST
    @Path("/updateLevel/{username}")
    public Tree updateLevel(@PathParam("username")String username) {
        return tree.updateLevel(username);
    }



}
