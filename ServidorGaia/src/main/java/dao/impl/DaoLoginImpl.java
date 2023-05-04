package dao.impl;

import dao.DaoLogin;
import dao.db.DaoDB;
import dao.queries.Queries;
import domain.model.Account;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.log4j.Log4j2;
import utils.exception.AccountNotActiveException;
import utils.exception.DataBaseDownException;

import java.sql.*;
import java.time.LocalDateTime;

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
                account.setToken(rs.getString(4));
                account.setActivated(rs.getInt(5));
                account.setActivationCode(rs.getString(6));
                account.setActivationTime(rs.getTime(7).toLocalTime());
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
    public boolean register(Account acc) {
        try (Connection con = db.getConnection()) {
            PreparedStatement register = con
                    .prepareStatement(Queries.INSERT_INTO_ACCOUNT_EMAIL_PASSWORD_USERNAME_ACTIVATED_ACTIVATION_CODE_ACTIVATION_TIME_VALUES);
            register.setString(1, acc.getEmail());
            register.setString(2, acc.getPassword());
            register.setString(3, acc.getUsername());
            register.setInt(4, acc.getActivated());
            register.setString(5, acc.getActivationCode());
            register.setTime(6, Time.valueOf(acc.getActivationTime()));
            register.executeUpdate();
            ResultSet rs = register.getGeneratedKeys();
            if (rs.next()) {
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
            ps.setTime(2, Time.valueOf(activationMoment.toLocalTime()));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                active = true;
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return active;
    }

    @Override
    public boolean activate(String activationCode) {
        try (Connection con = db.getConnection()) {
            PreparedStatement ps = con.prepareStatement(Queries.UPDATE_ACCOUNT_SET_ACTIVATED_1_WHERE_ACTIVATION_CODE);
            ps.setString(1, activationCode);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            log.error(e.getMessage());
            return false;
        }
    }


}
