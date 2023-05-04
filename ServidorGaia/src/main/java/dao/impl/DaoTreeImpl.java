package dao.impl;

import dao.DaoTree;
import dao.db.DaoDB;
import dao.queries.Queries;
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

    @Override public String getLevel(String username) {
        try (Connection con = db.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                    Queries.SELECT_FROM_TREE_WHERE_USERNAME
            );
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("level");
            } else {
                return "Error getting level";
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return "Error getting level";
        }
    }

    @Override public String updateLevel(String username) {
        try (Connection con = db.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                    Queries.UPDATE_TREE_SET_LEVEL_LEVEL_1_PROGRESS_0_WHERE_USERNAME
            );
            ps.setString(1, username);
            ps.executeUpdate();
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("level");
            } else {
                return "Error updating level";
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return "Error updating level";
        }
    }
}
