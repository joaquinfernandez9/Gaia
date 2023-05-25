package jakarta.rest;


import domain.model.Friend;
import domain.services.ServicesFriend;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/friend")
@Produces("application/json")
@Consumes("application/json")
public class RestFriend {

    private final ServicesFriend friend;

    @Inject
    public RestFriend(ServicesFriend friend) {
        this.friend = friend;
    }

    @POST
    @Path("/sendRequest/{username1}/{username2}")
    public Friend sendRequest(@PathParam("username1") String username1, @PathParam("username2") String username2) {
        return friend.sendRequest(username1, username2);
    }

    @POST
    @Path("/acceptRequest/{username1}/{username2}")
    public Response acceptRequest(@PathParam("username1") String username1, @PathParam("username2") String username2) {
        friend.acceptRequest(username1, username2);
        return Response.ok().build();
    }

    @DELETE
    @Path("/rejectRequest/{username1}/{username2}")
    public Response rejectRequest(@PathParam("username1") String username1, @PathParam("username2") String username2) {
        friend.rejectRequest(username1, username2);
        return Response.ok().build();
    }

    @GET
    @Path("/getFriends/{username}")
    public List<Friend> getFriends(@PathParam("username") String username) {
        return friend.getFriends(username);
    }

    @GET
    @Path("/getRequests/{username}")
    public List<Friend> getRequests(@PathParam("username") String username) {
        return friend.getRequests(username);
    }

}
