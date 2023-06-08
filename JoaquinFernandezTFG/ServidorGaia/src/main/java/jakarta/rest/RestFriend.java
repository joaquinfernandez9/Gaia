package jakarta.rest;


import domain.model.Friend;
import domain.model.Tree;
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
    public Response sendRequest(@PathParam("username1") String username1, @PathParam("username2") String username2) {
        Friend friendResponse = friend.sendRequest(username1, username2);
        if (friendResponse == null) return Response.status(Response.Status.BAD_REQUEST).build();
        else return Response.ok(friendResponse).build();
    }

    @POST
    @Path("/acceptRequest/{username1}/{username2}")
    public Response acceptRequest(@PathParam("username1") String username1, @PathParam("username2") String username2) {
        Friend friendResponse = friend.acceptRequest(username1, username2);
        if (friendResponse == null) return Response.status(Response.Status.BAD_REQUEST).build();
        else return Response.ok(friendResponse).build();
    }

    @DELETE
    @Path("/rejectRequest/{username1}/{username2}")
    public Response rejectRequest(@PathParam("username1") String username1, @PathParam("username2") String username2) {
        Friend friendResponse = friend.rejectRequest(username1, username2);
        if (friendResponse == null) return Response.status(Response.Status.BAD_REQUEST).build();
        else return Response.ok(friendResponse).build();
    }

    @GET
    @Path("/getFriends/{username}")
    public Response getFriends(@PathParam("username") String username) {
        List<Friend> friends = friend.getFriends(username);
        if (friends == null) {
            return Response.status(404).build();
        } else if (friends.isEmpty()) {
            return Response.status(204).build();
        } else {
            return Response.ok(friends).build();
        }
    }

    @GET
    @Path("/getFriendsTree/{username}")
    public Response getFriendsTree(@PathParam("username") String username) {
        List<Tree> friends = friend.getFriendsTree(username);
        if (friends == null) {
            return Response.status(404).build();
        } else if (friends.isEmpty()) {
            return Response.status(204).build();
        } else {
            return Response.ok(friends).build();
        }
    }

    @GET
    @Path("/getRequests/{username}")
    public Response getRequests(@PathParam("username") String username) {
        List<Friend> friends = friend.getRequests(username);
        if (friends == null) {
            return Response.status(404).build();
        } else if (friends.isEmpty()) {
            return Response.status(204).build();
        } else {
            return Response.ok(friends).build();
        }
    }

}
