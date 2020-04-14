package ru.kpfu.itis.meow.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.kpfu.itis.meow.model.params.RegistrationParams;
import ru.kpfu.itis.meow.model.params.UserCredentials;
import ru.kpfu.itis.meow.service.AuthService;
import ru.kpfu.itis.meow.service.DeviceService;

import javax.validation.Valid;

@RestController
public class AuthController {

    private final AuthService authService;
    private final DeviceService deviceService;

    @Autowired
    public AuthController(AuthService authService, DeviceService deviceService) {
        this.authService = authService;
        this.deviceService = deviceService;
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid final UserCredentials userCredentials) {
        if (authService.isCredentialsValid(userCredentials)) {
            return ResponseEntity.ok(authService.createLoginToken(userCredentials));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/registration")
    public ResponseEntity registration(@RequestBody @Valid final RegistrationParams params) {
        if (!deviceService.isDeviceNameAvailable(params.getDeviceName()) ||
                !authService.isLoginValid(params.getLogin())) {
            return ResponseEntity.badRequest().body("Validation failed");
        }
        return ResponseEntity.ok(authService.register(params));
    }
}
