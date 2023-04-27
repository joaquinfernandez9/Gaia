package domain.model;

import lombok.Data;

import java.time.LocalTime;

@Data
public class Account {
    private String email;
    private String password;
    private String username;
    private String token;
    private int activated;
    private String activationCode;
    private LocalTime activationTime;
}
