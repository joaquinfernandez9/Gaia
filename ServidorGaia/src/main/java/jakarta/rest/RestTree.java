package jakarta.rest;


import domain.model.Tree;
import domain.services.ServicesTree;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

//@Path("/tree")
@Produces("application/json")
@Consumes("application/json")
public class RestTree {

    private final ServicesTree tree;

    @Inject
    public RestTree(ServicesTree tree) {
        this.tree = tree;
    }


    @POST
    @Path("/getTree")
    public Tree addTree(String username) {
        return tree.getLevel(username);
    }

    @POST
    @Path("/updateLevel")
    public Tree updateLevel(String username) {
        return tree.updateLevel(username);
    }



}
