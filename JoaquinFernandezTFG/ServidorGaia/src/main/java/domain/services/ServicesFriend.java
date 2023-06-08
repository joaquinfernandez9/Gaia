package domain.services;

import domain.model.Friend;
import domain.model.Tree;

import java.util.List;

public interface ServicesFriend {
    List<Tree> getFriendsTree(String username);
    Friend sendRequest(String username1, String username2);

    Friend acceptRequest(String username1, String username2);

    Friend rejectRequest(String username1, String username2);

    List<Friend> getFriends(String username);

    List<Friend> getRequests(String username);
}
