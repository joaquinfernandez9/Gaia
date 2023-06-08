package dao.impl;

import dao.DaoTree;
import dao.db.DaoDB;
import dao.queries.Queries;
import domain.model.Tree;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Log4j2
public class DaoTreeImpl implements DaoTree {

    private final DaoDB db;

    @Inject
    public DaoTreeImpl(DaoDB db) {
        this.db = db;
    }

    @Override
    public Tree get(String username) {
        try (Connection con = db.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                    Queries.SELECT_FROM_TREE_WHERE_USERNAME
            );
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Tree(
                        rs.getString("username"),
                        rs.getInt("level"),
                        rs.getInt("progress"));
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public Tree updateLevel(String username) {
        try (Connection con = db.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                    Queries.UPDATE_TREE_SET_LEVEL_LEVEL_1_PROGRESS_0_WHERE_USERNAME
            );
            ps.setString(1, username);
            ps.executeUpdate();
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Tree(
                        rs.getString("username"),
                        rs.getInt("level"),
                        rs.getInt("progress"));
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
