package domain.services.impl;

import dao.DaoLogin;
import domain.model.Account;
import domain.services.ServicesLogin;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class ServicesLoginImpl implements ServicesLogin {
    private DaoLogin login;

    @Inject
    public ServicesLoginImpl(DaoLogin login) {
        this.login = login;
    }


    @Override public Account login(String user, String password){
        if (login.doLogin(user, password)){
            return login.login(user, password);
        }else{
            return null;
        }
    }

    @Override public boolean register(Account acc){
        acc.setActivationTime(LocalTime.from(LocalDateTime.now()));
        return login.register(acc);
    }

    @Override public boolean activate(String activationCode, LocalDateTime activationMoment){
        if (login.checkActive(activationCode, activationMoment)){
            return login.activate(activationCode);
        }else{
            return false;
        }
    }


}
