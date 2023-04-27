package dao;

import dao.db.DaoDB;
import domain.model.Account;
import jakarta.ejb.Local;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.log4j.Log4j2;
import utils.exception.AccountNotActiveException;
import utils.exception.DataBaseDownException;

import java.sql.*;
import java.time.LocalDateTime;

@Log4j2
public class DaoLogin {

    private final DaoDB db;

    @Inject
    public DaoLogin(DaoDB db) {
        this.db = db;
    }

    //get user
    public Account login(String username){
        try(Connection con = db.getConnection()){
            PreparedStatement ps = con.prepareStatement("SELECT * from account where username = ?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
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
        } catch (SQLException e){
            throw new DataBaseDownException(e.getMessage());
        }
    }

    public boolean doLogin(String username, String password){
        try (Connection con = db.getConnection()){
            PreparedStatement ps = con.prepareStatement(
                    "SELECT password, activated from account where username = ?"
            );
            ps.setString(1,username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                String dbPass = rs.getString(1);
                int activated = rs.getInt(2);
                if (activated==0){
                    throw new AccountNotActiveException("Either the user can't be found or the account isn't activated.");
                } else if (password.equals(dbPass) && activated==1){
                    return true;
                } else {
                    throw new NotFoundException("User or password were incorrect.");
                }
            } else {
                throw new NotFoundException("User or password were incorrect.");
            }
        } catch (SQLException e){
            throw new DataBaseDownException(e.getMessage());
        }
    }

    public boolean register(Account acc){
        try (Connection con = db.getConnection()){
            PreparedStatement register = con
                    .prepareStatement("INSERT INTO account (email, password, username, activated, activation_code, activation_time) values (?,?,?,?,?,?)");
            register.setString(1,acc.getEmail());
            register.setString(2,acc.getPassword());
            register.setString(3,acc.getUsername());
            register.setInt(4,acc.getActivated());
            register.setString(5,acc.getActivationCode());
            register.setTime(6, Time.valueOf(acc.getActivationTime()));
            register.executeUpdate();
            ResultSet rs = register.getGeneratedKeys();
            if (rs.next()){
                return true;
            } else {
                throw new NotFoundException("User or password were incorrect.");
            }
        } catch (SQLException e){
            log.error(e.getMessage());
            return false;
        }
    }

    //check if activated
    public boolean checkActive(String activationCode, LocalDateTime activationMoment){
        boolean active = false;
        try (Connection con = db.getConnection()){
            PreparedStatement ps = con.prepareStatement("SELECT * from account where activation_code = ? and activation_time = ?");
            ps.setString(1,activationCode);
            ps.setTime(2,Time.valueOf(activationMoment.toLocalTime()));
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                active = true;
            }
        } catch (SQLException e){
            log.error(e.getMessage());
        }
        return active;
    }

    public boolean activate(String activationCode){
        try (Connection con = db.getConnection()){
            PreparedStatement ps = con.prepareStatement("UPDATE account set activated = 1 where activation_code = ?");
            ps.setString(1,activationCode);
            ps.executeUpdate();
            return true;
        } catch (SQLException e){
            log.error(e.getMessage());
            return false;
        }
    }


}
