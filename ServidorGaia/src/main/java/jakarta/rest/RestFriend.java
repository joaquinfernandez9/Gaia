package jakarta.rest;


import domain.model.Friend;
import domain.services.ServicesFriend;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

//@Path("/friend")
@Produces("application/json")
@Consumes("application/json")
public class RestFriend {

    private final ServicesFriend friend;


    @Inject
    public RestFriend(ServicesFriend friend) {
        this.friend = friend;
    }

    @POST
    @Path("/sendRequest")
    public Friend sendRequest(String username1, String username2) {
        return friend.sendRequest(username1, username2);
    }

    @POST
    @Path("/acceptRequest")
    public Response acceptRequest(String username1, String username2) {
        friend.acceptRequest(username1, username2);
        return Response.ok().build();
    }

    @POST
    @Path("/rejectRequest")
    public Response rejectRequest(String username1, String username2) {
        friend.rejectRequest(username1, username2);
        return Response.ok().build();
    }

    @POST
    @Path("/getFriends")
    public Response getFriends(String username) {
        friend.getFriends(username);
        return Response.ok().build();
    }

    @POST
    @Path("/getRequests")
    public Response getRequests(String username) {
        friend.getRequests(username);
        return Response.ok().build();
    }



}
