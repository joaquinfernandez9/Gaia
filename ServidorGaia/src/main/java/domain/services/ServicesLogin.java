package domain.services;

import domain.model.Account;

import java.time.LocalDateTime;

public interface ServicesLogin {
    Account login(String user, String password);

    boolean register(Account acc);

    boolean activate(String activationCode, LocalDateTime activationMoment);
}
