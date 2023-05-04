package dao.impl;

import dao.DaoFriend;
import dao.db.DaoDB;
import dao.queries.Queries;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import utils.exception.DataBaseDownException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Log4j2
public class DaoFriendImpl implements DaoFriend {

    private DaoDB db;

    @Inject
    public DaoFriendImpl(DaoDB db) {
        this.db = db;
    }


    @Override
    public int sendRequest(String username1, String username2) {
        try (Connection con = db.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                    Queries.INSERT_INTO_FRIENDS_USERNAME_1_USERNAME_2_VALUE_REQUEST_DATE_VALUES_0_NOW
            );
            ps.setString(1, username1);
            ps.setString(2, username2);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(3);
            } else {
                throw new SQLException("No keys generated");
            }
        } catch (SQLException e) {
            DaoFriendImpl.log.error(e.getMessage());
            throw new DataBaseDownException(e.getMessage());
        }
    }

    @Override
    public void acceptRequest(String username1, String username2) {
        try (Connection con = db.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                    Queries.UPDATE_FRIENDS_SET_VALUE_1_WHERE_USERNAME_1_AND_USERNAME_2
            );
            ps.setString(1, username1);
            ps.setString(2, username2);
            ps.executeUpdate();
        } catch (SQLException e) {
            DaoFriendImpl.log.error(e.getMessage());
            throw new DataBaseDownException(e.getMessage());
        }
    }

    @Override
    public void rejectRequest(String username1, String username2) {
        try (Connection con = db.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                    Queries.DELETE_FROM_FRIENDS_WHERE_USERNAME_1_AND_USERNAME_2
            );
            ps.setString(1, username1);
            ps.setString(2, username2);
            ps.executeUpdate();
        } catch (SQLException e) {
            DaoFriendImpl.log.error(e.getMessage());
            throw new DataBaseDownException(e.getMessage());
        }
    }

    @Override
    public void getFriends(String username) {
        try (Connection con = db.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                    Queries.SELECT_FROM_FRIENDS_WHERE_USERNAME_1_OR_USERNAME_2_AND_VALUE_1
            );
            ps.setString(1, username);
            ps.setString(2, username);
            ps.executeUpdate();
        } catch (SQLException e) {
            DaoFriendImpl.log.error(e.getMessage());
            throw new DataBaseDownException(e.getMessage());
        }
    }

    @Override
    public void getRequests(String username) {
        try (Connection con = db.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                    Queries.SELECT_FROM_FRIENDS_WHERE_USERNAME_2_AND_VALUE_0
            );
            ps.setString(1, username);
            ps.executeUpdate();
        } catch (SQLException e) {
            DaoFriendImpl.log.error(e.getMessage());
            throw new DataBaseDownException(e.getMessage());
        }
    }

}
