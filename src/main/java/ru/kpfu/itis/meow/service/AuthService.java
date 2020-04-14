package ru.kpfu.itis.meow.service;

import ru.kpfu.itis.meow.model.params.RegistrationParams;
import ru.kpfu.itis.meow.model.params.UserCredentials;

public interface AuthService {
    boolean isCredentialsValid(UserCredentials userCredentials);

    String createLoginToken(UserCredentials userCredentials);

    boolean register(RegistrationParams params);

    boolean isLoginValid(String login);
}
