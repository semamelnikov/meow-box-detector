package ru.kpfu.itis.meow.model.params;

import lombok.Data;

@Data
public class RegistrationParams {
    private String deviceName;
    private String login;
    private String password;
    private String name;
    private Double weight;
    private Integer age;
    private String breed;
}
