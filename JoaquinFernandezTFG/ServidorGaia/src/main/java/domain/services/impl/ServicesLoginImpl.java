package domain.services.impl;

import dao.DaoLogin;
import domain.model.Account;
import domain.services.ServicesLogin;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class ServicesLoginImpl implements ServicesLogin {
    private final DaoLogin login;

    @Inject
    public ServicesLoginImpl(DaoLogin login) {
        this.login = login;
    }


    @Override
    public Account login(String user, String password) {
        if (login.doLogin(user, password)) {
            return login.login(user, password);
        } else {
            return null;
        }
    }

    @Override
    public boolean register(Account acc, String activationCode, LocalDateTime activationMoment) {
        return login.register(acc, activationCode, activationMoment);
    }

    @Override
    public boolean activate(String activationCode, LocalDateTime activationMoment) {
        if (login.checkActive(activationCode, activationMoment)) {
            return login.activate(activationCode);
        } else {
            return false;
        }
    }

    @Override
    public List<Account> get() {
        return login.get();
    }


}
