package dao.impl;

import dao.DaoTask;
import dao.db.DaoDB;
import dao.queries.Queries;
import domain.model.Account;
import domain.model.Task;
import jakarta.ejb.Local;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;

import java.sql.*;
import java.time.LocalDateTime;
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
    public Task add(Task task) {
        try (Connection con = db.getConnection()) {
            con.setAutoCommit(false);
            if (task.getEndTime().isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("End time is in the past.");
            } else {
                PreparedStatement ps = con.prepareStatement(Queries.INSERT_INTO_TASK_ID_TASK_NAME_INITIAL_TIME_END_TIME_COMPLETED_USERNAME_VALUES_0, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, task.getTaskName());
                ps.setTimestamp(2, Timestamp.valueOf(task.getInitTime()));
                ps.setTimestamp(3, Timestamp.valueOf(task.getEndTime()));
                ps.setString(4, task.getUsername());
                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                if (rs != null) {
                    con.commit();
                    return new Task(task.getTaskName(),
                            task.getInitTime(),
                            task.getEndTime(),
                            0,
                            task.getUsername());
                } else {
                    throw new SQLException("No keys generated");
                }
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public Task get(Task task) {
        try (Connection con = db.getConnection()) {
            PreparedStatement ps = con.prepareStatement(Queries.SELECT_FROM_TASK_WHERE_TASK_NAME_AND_USERNAME);
            ps.setString(1, task.getTaskName());
            ps.setString(2, task.getUsername());
            ResultSet rs = ps.executeQuery();
            return readRS(rs).get(0);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public Task delete(Task task) {
        try (Connection con = db.getConnection()) {
            PreparedStatement ps = con.prepareStatement(Queries.DELETE_FROM_TASK_WHERE_TASK_NAME_AND_USERNAME);
            ps.setString(1, task.getTaskName());
            ps.setString(2, task.getUsername());

            int response = ps.executeUpdate();
            if (response == 0) {
                return null;
            } else {
                return task;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Task> getAllByUser(Account account) {
        try (Connection con = db.getConnection()) {
            List<Task> tasks;
            PreparedStatement ps = con.prepareStatement(Queries.SELECT_FROM_TASK_WHERE_USERNAME);
            ps.setString(1, account.getUsername());
            ResultSet rs = ps.executeQuery();
            tasks = readRS(rs);
            return tasks;
        } catch (Exception e) {
            log.error(e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public Task update(Task task) {
        try (Connection con = db.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(
                    Queries.UPDATE_TASK_SET_COMPLETED_1_WHERE_TASK_NAME_AND_USERNAME);
                 PreparedStatement ps2 = con.prepareStatement(
                         Queries.UPDATE_TREE_SET_PROGRESS_PROGRESS_1_WHERE_USERNAME);
                 PreparedStatement ps3 = con.prepareStatement(
                         Queries.UPDATE_TREE_SET_PROGRESS_0_WHERE_USER
                 )) {
                con.setAutoCommit(false);
                ps.setString(1, task.getTaskName());
                ps.setString(2, task.getUsername());
                int rs = ps.executeUpdate();
                if (rs != 0) {
                    ps2.setString(1, task.getUsername());
                    ps2.executeUpdate();
                    if (checkIfLeveledUp(task.getUsername())) {
                        ps3.setString(1, task.getUsername());
                        ps3.executeUpdate();
                        con.commit();
                    } else {
                        con.commit();
                        return task;
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
        return null;
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
    public int deleteCompletedTasks(Account account) {

        try (Connection con = db.getConnection()) {
            PreparedStatement ps = con.prepareStatement(Queries.DELETE_FROM_TASK_WHERE_COMPLETED_1_AND_USERNAME);
            ps.setString(1, account.getUsername());
            return ps.executeUpdate();
        } catch (Exception e) {
            log.error(e.getMessage());
            return 0;
        }

    }

    private List<Task> readRS(ResultSet rs) {
        List<Task> response = new ArrayList<>();
        try {
            while (rs.next()) {
                response.add(new Task(rs.getString("task_name"),
                        rs.getTimestamp("init_time").toLocalDateTime(),
                        rs.getTimestamp("end_time").toLocalDateTime(),
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
