package domain.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalTime;
import java.util.List;

@Data
public class Account {
    private String email;
    private String password;
    private String username;
    private int activated;
    private String activationCode;
    private LocalTime activationTime;



    public Account() {
    }

    public Account(String email, String password, String username, int activated, String activationCode, LocalTime activationTime) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.activated = activated;
        this.activationCode = activationCode;
        this.activationTime = activationTime;
    }

    public Account(String password, String username) {
        this.password = password;
        this.username = username;
    }

}
