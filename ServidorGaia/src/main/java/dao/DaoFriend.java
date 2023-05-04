package dao;

import utils.exception.DataBaseDownException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface DaoFriend {

    int sendRequest(String username1, String username2);

    void acceptRequest(String username1, String username2);

    void rejectRequest(String username1, String username2);

    void getFriends(String username);

    void getRequests(String username);

}
