package dao.impl;

import dao.DaoTask;
import dao.db.DaoDB;
import dao.queries.Queries;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Log4j2
public class DaoTaskImpl implements DaoTask {

    private final DaoDB db;

    @Inject
    public DaoTaskImpl(DaoDB db) {
        this.db = db;
    }

    @Override
    public String addTask(String taskName, String initialTime, String endTime, String username) {
        try (Connection con = db.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                    Queries.INSERT_INTO_TASK_ID_TASK_NAME_INITIAL_TIME_END_TIME_COMPLETED_USERNAME_VALUES_0
            );
            ps.setString(1, taskName);
            ps.setString(2, initialTime);
            ps.setString(3, endTime);
            ps.setString(4, username);
            ps.executeUpdate();
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("task_name");
            } else {
                return "Error adding task";
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return "Error adding task";
        }
    }

    @Override
    public String getTask(String taskName, String username) {
        try (Connection con = db.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                    Queries.SELECT_FROM_TASK_WHERE_TASK_NAME_AND_USERNAME
            );
            ps.setString(1, taskName);
            ps.setString(2, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("task_name");
            } else {
                return "Error getting task";
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return "Error getting task";
        }
    }

    @Override
    public String deleteTask(String taskName, String username) {
        try (Connection con = db.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                    Queries.DELETE_FROM_TASK_WHERE_TASK_NAME_AND_USERNAME
            );
            ps.setString(1, taskName);
            ps.setString(2, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("task_name");
            } else {
                return "Error deleting task";
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return "Error deleting task";
        }
    }

    @Override
    public String getTasks(String username) {
        try (Connection con = db.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                    Queries.SELECT_FROM_TASK_WHERE_USERNAME
            );
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("task_name");
            } else {
                return "Error getting tasks";
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return "Error getting tasks";
        }
    }

    @Override
    public String updateTask(String taskName, String username) {
        try (Connection con = db.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(
                    Queries.UPDATE_TASK_SET_COMPLETED_1_WHERE_TASK_NAME_AND_USERNAME);
                 PreparedStatement ps2 = con.prepareStatement(
                         Queries.UPDATE_TREE_SET_PROGRESS_PROGRESS_1_WHERE_USERNAME
                 )) {
                con.setAutoCommit(false);
                ps.setString(1, taskName);
                ps.setString(2, username);
                ps.executeUpdate();
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    ps2.setString(1, username);
                    ps2.executeUpdate();
                } else {
                    return "Error updating task";
                }
                con.commit();
                return rs.getString("task_name");
            } catch (Exception e) {
                con.rollback();
                log.error(e.getMessage());
                return "Error updating task";
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            return "Error updating task";
        }
    }


    @Override
    public String deleteCompletedTasks(String username) {
        try (Connection con = db.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                    Queries.DELETE_FROM_TASK_WHERE_COMPLETED_1_AND_USERNAME
            );
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("task_name");
            } else {
                return "Error deleting completed tasks";
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return "Error deleting completed tasks";
        }
    }


}
