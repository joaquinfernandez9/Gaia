package dao.impl;

import dao.DaoLogin;
import dao.db.DaoDB;
import dao.queries.Queries;
import domain.model.Account;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.log4j.Log4j2;
import domain.error.AccountNotActiveException;
import domain.error.DataBaseDownException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Log4j2
public class DaoLoginImpl implements DaoLogin {

    private final DaoDB db;

    @Inject
    public DaoLoginImpl(DaoDB db) {
        this.db = db;
    }

    @Override
    public Account login(String username, String password) {
        try (Connection con = db.getConnection()) {
            PreparedStatement ps = con.prepareStatement(Queries.SELECT_FROM_ACCOUNT_WHERE_USERNAME_AND_PASSWORD);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Account account = new Account();
                account.setEmail(rs.getString(1));
                account.setPassword(rs.getString(2));
                account.setUsername(rs.getString(3));
                account.setActivated(rs.getInt(4));
                account.setActivationCode(rs.getString(5));
                account.setActivationTime(rs.getTime(6).toLocalTime());
                return account;
            } else {
                throw new NotFoundException("No user found");
            }
        } catch (SQLException e) {
            throw new DataBaseDownException(e.getMessage());
        }
    }

    @Override
    public boolean doLogin(String username, String password) {
        try (Connection con = db.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                    Queries.SELECT_PASSWORD_ACTIVATED_FROM_ACCOUNT_WHERE_USERNAME
            );
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String dbPass = rs.getString(1);
                int activated = rs.getInt(2);
                if (activated == 0) {
                    throw new AccountNotActiveException("Either the user can't be found or the account isn't activated.");
                } else if (password.equals(dbPass) && activated == 1) {
                    return true;
                } else {
                    throw new NotFoundException("User or password were incorrect.");
                }
            } else {
                throw new NotFoundException("User or password were incorrect.");
            }
        } catch (SQLException e) {
            throw new DataBaseDownException(e.getMessage());
        }
    }

    @Override
    public boolean register(Account acc, String activationCode, LocalDateTime activationMoment) {
        try (Connection con = db.getConnection()) {
            con.setAutoCommit(false);
            PreparedStatement register = con
                    .prepareStatement(Queries.INSERT_INTO_ACCOUNT_EMAIL_PASSWORD_USERNAME_ACTIVATED_ACTIVATION_CODE_ACTIVATION_TIME_VALUES);
            register.setString(1, acc.getEmail());
            register.setString(2, acc.getPassword());
            register.setString(3, acc.getUsername());
            register.setInt(4, acc.getActivated());
            register.setString(5, activationCode);
            register.setTimestamp(6, Timestamp.valueOf(activationMoment));
            int rs = register.executeUpdate();
            if (rs == 1) {
                con.commit();
                return true;
            } else {
                throw new NotFoundException("User or password were incorrect.");
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    //check if activated
    @Override
    public boolean checkActive(String activationCode, LocalDateTime activationMoment) {
        boolean active = false;
        try (Connection con = db.getConnection()) {
            PreparedStatement ps = con.prepareStatement(Queries.SELECT_FROM_ACCOUNT_WHERE_ACTIVATION_CODE_AND_ACTIVATION_TIME);
            ps.setString(1, activationCode);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                LocalTime dbActivationTime = rs.getTime(1).toLocalTime();
                if (dbActivationTime.isAfter(activationMoment.toLocalTime().minusMinutes(5))) {
                    active = true;
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return active;
    }

    @Override
    public boolean activate(String activationCode) {
        try (Connection con = db.getConnection()) {
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement(Queries.UPDATE_ACCOUNT_SET_ACTIVATED_1_WHERE_ACTIVATION_CODE);
            ps.setString(1, activationCode);
            int rs = ps.executeUpdate();
            if (rs == 1){
                PreparedStatement getUser = con.prepareStatement(Queries.SELECT_FROM_ACCOUNT_WHERE_ACTIVATION_CODE);
                getUser.setString(1, activationCode);
                ResultSet rs1 = getUser.executeQuery();
                rs1.next();
                Account acc = new Account(  rs1.getString(1),
                                            rs1.getString(2),
                                            rs1.getString(3),
                                            rs1.getInt(4),
                                            rs1.getString(5),
                                            rs1.getTime(6).toLocalTime());

                PreparedStatement createTree = con.prepareStatement(Queries.INSERT_INTO_TREE_USERNAME_VALUES);
                createTree.setString(1, acc.getUsername());
                int rs2 = createTree.executeUpdate();
                if (rs2 == 1) {
                    con.commit();
                    return true;
                } else {
                    con.rollback();
                    throw new NotFoundException("User or password were incorrect.");
                }
            } else {
                throw new NotFoundException("User or password were incorrect.");
            }

        } catch (SQLException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public List<Account> get() {
        try (Connection con = db.getConnection()) {
            PreparedStatement ps = con.prepareStatement(Queries.GET_ALL_USERS);
            ResultSet rs = ps.executeQuery();
            return readRS(rs);
        } catch (SQLException e) {
            throw new DataBaseDownException(e.getMessage());
        }
    }

    private List<Account> readRS(ResultSet rs) {
        List<Account> response = new ArrayList<>();
        try {
            while (rs.next()) {
                response.add(new Account(rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getInt("activated"),
                        rs.getString("activation_code"),
                        rs.getTime("activation_time").toLocalTime()));
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            return Collections.emptyList();
        }
        return response;
    }


}
