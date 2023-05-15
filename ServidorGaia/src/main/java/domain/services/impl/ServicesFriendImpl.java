package domain.services.impl;

import dao.DaoFriend;
import domain.model.Friend;
import domain.services.ServicesFriend;
import jakarta.inject.Inject;

import java.util.List;

public class ServicesFriendImpl implements ServicesFriend {

    private final DaoFriend dao;

    @Inject
    public ServicesFriendImpl(DaoFriend dao) {
        this.dao = dao;
    }

    @Override
    public Friend sendRequest(String username1, String username2) {
        return dao.sendRequest(username1, username2);
    }

    @Override
    public Friend acceptRequest(String username1, String username2) {
        return dao.acceptRequest(username1, username2);
    }

    @Override
    public Friend rejectRequest(String username1, String username2) {
        return dao.rejectRequest(username1, username2);
    }

    @Override
    public List<Friend> getFriends(String username) {
        return dao.getFriends(username);
    }

    @Override
    public List<Friend> getRequests(String username) {
        return dao.getRequests(username);
    }
}
