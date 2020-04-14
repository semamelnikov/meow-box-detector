package ru.kpfu.itis.meow.model.params;

import lombok.Data;

@Data
public class UserCredentials {
    private String login;
    private String password;
}
