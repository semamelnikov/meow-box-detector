package ru.kpfu.itis.meow.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.meow.model.entity.Cat;
import ru.kpfu.itis.meow.model.entity.Device;
import ru.kpfu.itis.meow.model.entity.User;
import ru.kpfu.itis.meow.model.params.RegistrationParams;
import ru.kpfu.itis.meow.model.params.UserCredentials;
import ru.kpfu.itis.meow.repository.CatRepository;
import ru.kpfu.itis.meow.repository.DeviceRepository;
import ru.kpfu.itis.meow.repository.UserRepository;
import ru.kpfu.itis.meow.service.AuthService;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {
    private final CatRepository catRepository;
    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AuthServiceImpl(CatRepository catRepository, UserRepository userRepository,
                           DeviceRepository deviceRepository, BCryptPasswordEncoder passwordEncoder) {
        this.catRepository = catRepository;
        this.userRepository = userRepository;
        this.deviceRepository = deviceRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean isCredentialsValid(UserCredentials userCredentials) {
        final Optional<User> userOptional = userRepository.findByLogin(userCredentials.getLogin());
        return userOptional.map(
                user -> passwordEncoder.matches(userCredentials.getPassword(), user.getHashPassword())
        ).orElse(false);
    }

    @Override
    public String createLoginToken(UserCredentials userCredentials) {
        final Optional<User> userOptional = userRepository.findByLogin(userCredentials.getLogin());
        return userOptional.map(this::createFreshToken)
                .orElseThrow(
                        () -> new IllegalArgumentException("Something went wrong!")
                );
    }

    private String createFreshToken(User user) {
        return "This is a brand new token. I do not know what JWT is.";
    }

    @Override
    public boolean register(RegistrationParams params) {
        final Device device = deviceRepository.findByName(params.getDeviceName()).orElseThrow(
                () -> new IllegalArgumentException("Device with name " + params.getDeviceName() + " does not exist")
        );
        final User user = User.builder()
                .hashPassword(passwordEncoder.encode(params.getPassword()))
                .login(params.getLogin())
                .build();
        userRepository.save(user);

        final Cat cat = Cat.builder()
                .name(params.getName())
                .age(params.getAge())
                .breed(params.getBreed())
                .weight(params.getWeight())
                .deviceId(device.getId())
                .build();
        catRepository.save(cat);

        device.setCat(cat);
        deviceRepository.save(device);
        return user.getId() != null && cat.getId() != null;
    }

    @Override
    public boolean isLoginValid(String login) {
        return !userRepository.findByLogin(login).isPresent();
    }
}
