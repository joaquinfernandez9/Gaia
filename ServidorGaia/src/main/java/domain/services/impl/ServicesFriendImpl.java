package domain.services.impl;

import dao.DaoFriend;
import domain.services.ServicesFriend;
import jakarta.inject.Inject;

public class ServicesFriendImpl implements ServicesFriend {

    private DaoFriend friend;

    @Inject
    public ServicesFriendImpl(DaoFriend friend) {
        this.friend = friend;
    }

    @Override public int sendRequest(String username1, String username2){
        return friend.sendRequest(username1, username2);
    }

    @Override public void acceptRequest(String username1, String username2){
        friend.acceptRequest(username1, username2);
    }

    @Override public void rejectRequest(String username1, String username2){
        friend.rejectRequest(username1, username2);
    }

    @Override public void getFriends(String username){
        friend.getFriends(username);
    }

    @Override public void getRequests(String username){
        friend.getRequests(username);
    }

}
