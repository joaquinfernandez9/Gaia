package dao;

import domain.model.Account;

import java.time.LocalDateTime;

public interface DaoLogin {
    //get user
    Account login(String username, String password);

    boolean doLogin(String username, String password);

    boolean register(Account acc);

    //check if activated
    boolean checkActive(String activationCode, LocalDateTime activationMoment);

    boolean activate(String activationCode);
}
