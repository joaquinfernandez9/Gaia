package dao;

import domain.model.Friend;

import java.util.List;

public interface DaoFriend {

    Friend sendRequest(String username1, String username2);

    Friend acceptRequest(String username1, String username2);

    Friend rejectRequest(String username1, String username2);

    List<Friend> getFriends(String username);

    List<Friend> getRequests(String username);

}
