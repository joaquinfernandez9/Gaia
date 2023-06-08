package dao;

import domain.model.Account;

import java.time.LocalDateTime;
import java.util.List;

public interface DaoLogin {
    //get user
    Account login(String username, String password);

    boolean doLogin(String username, String password);

    boolean register(Account acc, String activartionCode, LocalDateTime activationMoment);

    //check if activated
    boolean checkActive(String activationCode, LocalDateTime activationMoment);

    boolean activate(String activationCode);

    List<Account> get();
}
