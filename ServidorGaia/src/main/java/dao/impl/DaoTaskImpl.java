package dao.impl;

import dao.DaoTask;
import dao.db.DaoDB;
import dao.queries.Queries;
import domain.model.Task;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Log4j2
public class DaoTaskImpl implements DaoTask {

    private final DaoDB db;

    @Inject
    public DaoTaskImpl(DaoDB db) {
        this.db = db;
    }

    @Override
    public Task add(String taskName, String initialTime, String endTime, String username) {
        try (Connection con = db.getConnection()) {
            PreparedStatement ps = con.prepareStatement(Queries.INSERT_INTO_TASK_ID_TASK_NAME_INITIAL_TIME_END_TIME_COMPLETED_USERNAME_VALUES_0);
            ps.setString(1, taskName);
            ps.setString(2, initialTime);
            ps.setString(3, endTime);
            ps.setString(4, username);
            ps.executeUpdate();
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Task(rs.getInt("id"), rs.getString("task_name"), rs.getString("initial_time"), rs.getString("end_time"), rs.getInt("completed"), rs.getString("username"));
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public Task get(String taskName, String username) {
        try (Connection con = db.getConnection()) {
            PreparedStatement ps = con.prepareStatement(Queries.SELECT_FROM_TASK_WHERE_TASK_NAME_AND_USERNAME);
            ps.setString(1, taskName);
            ps.setString(2, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Task(rs.getInt("id"), rs.getString("task_name"), rs.getString("initial_time"), rs.getString("end_time"), rs.getInt("completed"), rs.getString("username"));
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public Task delete(String taskName, String username) {
        try (Connection con = db.getConnection()) {
            PreparedStatement ps = con.prepareStatement(Queries.DELETE_FROM_TASK_WHERE_TASK_NAME_AND_USERNAME);
            ps.setString(1, taskName);
            ps.setString(2, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Task(rs.getInt("id"), rs.getString("task_name"), rs.getString("initial_time"), rs.getString("end_time"), rs.getInt("completed"), rs.getString("username"));
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Task> getAllByUser(String username) {
        try (Connection con = db.getConnection()) {
            PreparedStatement ps = con.prepareStatement(Queries.SELECT_FROM_TASK_WHERE_USERNAME);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            List<Task> tasks = new ArrayList<>();
            while (rs.next()) {
                tasks.add(new Task(rs.getInt("id"), rs.getString("task_name"), rs.getString("initial_time"), rs.getString("end_time"), rs.getInt("completed"), rs.getString("username")));
            }
            return tasks;

        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public Task update(String taskName, String username) {
        try (Connection con = db.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(
                    Queries.UPDATE_TASK_SET_COMPLETED_1_WHERE_TASK_NAME_AND_USERNAME);
                 PreparedStatement ps2 = con.prepareStatement(
                         Queries.UPDATE_TREE_SET_PROGRESS_PROGRESS_1_WHERE_USERNAME)) {
                con.setAutoCommit(false);
                ps.setString(1, taskName);
                ps.setString(2, username);
                ps.executeUpdate();
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    ps2.setString(1, username);
                    ps2.executeUpdate();
                    if (checkIfLeveledUp(username)) {
                        con.commit();
                        return new Task(rs.getInt("id"),
                                rs.getString("task_name"),
                                rs.getString("initial_time"),
                                rs.getString("end_time"),
                                rs.getInt("completed"),
                                rs.getString("username"));
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }

            } catch (Exception e) {
                con.rollback();
                log.error(e.getMessage());
                return null;
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    private boolean checkIfLeveledUp(String username) {
        try (Connection con = db.getConnection()) {
            PreparedStatement ps = con.
                    prepareStatement(Queries.SELECT_FROM_TREE_WHERE_USERNAME);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int progress = rs.getInt("progress");
                int level = rs.getInt("level");
                if (progress == 100) {
                    PreparedStatement ps2 = con.
                            prepareStatement(Queries.UPDATE_TREE_SET_PROGRESS_0_LEVEL_LEVEL_PLUS_1_WHERE_USERNAME);
                    ps2.setString(1, username);
                    ps2.setInt(2, level + 1);
                    ps2.executeUpdate();
                    return true;
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
        return false;
    }

    @Override
    public List<Task> deleteCompletedTasks(String username) {
        List<Task> response = new ArrayList<>();
        try (Connection con = db.getConnection()) {
            PreparedStatement ps = con.prepareStatement(Queries.DELETE_FROM_TASK_WHERE_COMPLETED_1_AND_USERNAME);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            response = readRS(rs);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Collections.emptyList();
        }
        return response;
    }

    private List<Task> readRS(ResultSet rs) {
        List<Task> response = new ArrayList<>();
        try {
            while (rs.next()) {
                response.add(new Task(rs.getInt("id"),
                        rs.getString("task_name"),
                        rs.getString("initial_time"),
                        rs.getString("end_time"),
                        rs.getInt("completed"),
                        rs.getString("username")));
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            return Collections.emptyList();
        }
        return response;
    }

}
