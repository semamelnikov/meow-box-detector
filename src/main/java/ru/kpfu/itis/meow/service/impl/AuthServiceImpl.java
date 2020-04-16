package ru.kpfu.itis.meow.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.meow.model.entity.Cat;
import ru.kpfu.itis.meow.model.entity.Device;
import ru.kpfu.itis.meow.model.entity.DeviceSound;
import ru.kpfu.itis.meow.model.entity.User;
import ru.kpfu.itis.meow.model.params.RegistrationParams;
import ru.kpfu.itis.meow.model.params.UserCredentials;
import ru.kpfu.itis.meow.repository.CatRepository;
import ru.kpfu.itis.meow.repository.DeviceRepository;
import ru.kpfu.itis.meow.repository.DeviceSoundRepository;
import ru.kpfu.itis.meow.repository.UserRepository;
import ru.kpfu.itis.meow.service.AuthService;
import ru.kpfu.itis.meow.util.TokenUtil;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {
    private final CatRepository catRepository;
    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;
    private final DeviceSoundRepository deviceSoundRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AuthServiceImpl(CatRepository catRepository, UserRepository userRepository,
                           DeviceRepository deviceRepository, DeviceSoundRepository deviceSoundRepository,
                           BCryptPasswordEncoder passwordEncoder) {
        this.catRepository = catRepository;
        this.userRepository = userRepository;
        this.deviceRepository = deviceRepository;
        this.deviceSoundRepository = deviceSoundRepository;
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
        return userOptional.map(user -> createFreshToken(deviceRepository.getOne(user.getDeviceId())))
                .orElseThrow(
                        () -> new IllegalArgumentException("Something went wrong!")
                );
    }

    private String createFreshToken(Device device) {
        return TokenUtil.createToken(device);
    }

    @Override
    public boolean register(RegistrationParams params) {
        final Device device = deviceRepository.findByName(params.getDeviceName()).orElseThrow(
                () -> new IllegalArgumentException("Device with name " + params.getDeviceName() + " does not exist")
        );

        final DeviceSound deviceSound = DeviceSound.builder()
                .deviceId(device.getId())
                .isSoundOn(true)
                .build();
        deviceSoundRepository.save(deviceSound);

        final User user = User.builder()
                .hashPassword(passwordEncoder.encode(params.getPassword()))
                .login(params.getLogin())
                .deviceId(device.getId())
                .build();
        userRepository.save(user);

        final Cat cat = Cat.builder()
                .name(params.getName())
                .age(params.getAge())
                .breed(params.getBreed())
                .weight(params.getWeight())
                .deviceId(device.getId())
                .userId(user.getId())
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
