package domain.services;

import domain.model.Account;

import java.time.LocalDateTime;
import java.util.List;

public interface ServicesLogin {
    Account login(String user, String password);

    boolean register(Account acc, String activationCode, LocalDateTime activationMoment);

    boolean activate(String activationCode, LocalDateTime activationMoment);

    List<Account> get();
}
