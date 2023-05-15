package dao.impl;

import dao.DaoFriend;
import dao.db.DaoDB;
import dao.queries.Queries;
import domain.model.Friend;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import domain.error.DataBaseDownException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class DaoFriendImpl implements DaoFriend {

    private DaoDB db;

    @Inject
    public DaoFriendImpl(DaoDB db) {
        this.db = db;
    }


    // user1 send the petition to user2
    @Override
    public Friend sendRequest(String username1, String username2) {
        try (Connection con = db.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                    Queries.INSERT_INTO_FRIENDS_USERNAME_1_USERNAME_2_VALUE_REQUEST_DATE_VALUES_0_NOW
            );
            ps.setString(1, username1);
            ps.setString(2, username2);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return new Friend(
                        username1,
                        username2,
                        0,
                        rs.getTimestamp(4).toLocalDateTime().toLocalDate()
                );
            } else {
                throw new SQLException("No keys generated");
            }
        } catch (SQLException e) {
            DaoFriendImpl.log.error(e.getMessage());
            throw new DataBaseDownException(e.getMessage());
        }
    }

    @Override
    public Friend acceptRequest(String username1, String username2) {
        try (Connection con = db.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                    Queries.UPDATE_FRIENDS_SET_VALUE_1_WHERE_USERNAME_1_AND_USERNAME_2
            );
            ps.setString(1, username1);
            ps.setString(2, username2);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return new Friend(
                        username1,
                        username2,
                        0,
                        rs.getTimestamp(4).toLocalDateTime().toLocalDate()
                );
            } else {
                throw new SQLException("No keys generated");
            }
        } catch (SQLException e) {
            DaoFriendImpl.log.error(e.getMessage());
            throw new DataBaseDownException(e.getMessage());
        }
    }

    @Override
    public Friend rejectRequest(String username1, String username2) {
        try (Connection con = db.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                    Queries.DELETE_FROM_FRIENDS_WHERE_USERNAME_1_AND_USERNAME_2
            );
            ps.setString(1, username1);
            ps.setString(2, username2);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return new Friend(
                        username1,
                        username2,
                        0,
                        rs.getTimestamp(4).toLocalDateTime().toLocalDate()
                );
            } else {
                throw new SQLException("No keys generated");
            }

        } catch (SQLException e) {
            DaoFriendImpl.log.error(e.getMessage());
            throw new DataBaseDownException(e.getMessage());
        }
    }

    @Override
    public List<Friend> getFriends(String username) {
        try (Connection con = db.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                    Queries.SELECT_FROM_FRIENDS_WHERE_USERNAME_1_OR_USERNAME_2_AND_VALUE_1
            );
            ps.setString(1, username);
            ps.setString(2, username);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            return readRS(rs);
        } catch (SQLException e) {
            DaoFriendImpl.log.error(e.getMessage());
            throw new DataBaseDownException(e.getMessage());
        }
    }


    @Override
    public List<Friend> getRequests(String username) {
        try (Connection con = db.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                    Queries.SELECT_FROM_FRIENDS_WHERE_USERNAME_2_AND_VALUE_0
            );
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            return readRS(rs);
        } catch (SQLException e) {
            DaoFriendImpl.log.error(e.getMessage());
            throw new DataBaseDownException(e.getMessage());
        }
    }
    
    private List<Friend> readRS(ResultSet rs) throws SQLException {
        List<Friend> friends = new ArrayList<>();
        while (rs.next()) {
            friends.add(new Friend(
                    rs.getString("username1"),
                    rs.getString("username2"),
                    rs.getInt("value"),
                    rs.getTimestamp("request_date").toLocalDateTime().toLocalDate()
            ));
        }
        return friends;
    }


}
