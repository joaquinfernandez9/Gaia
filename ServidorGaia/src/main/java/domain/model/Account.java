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
    private String token;
    private int activated;
    private String activationCode;
    private LocalTime activationTime;



    public Account() {
    }

    public Account(String password, String username) {
        this.password = password;
        this.username = username;
    }

}
