package ru.kpfu.itis.meow.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.kpfu.itis.meow.model.params.RegistrationParams;
import ru.kpfu.itis.meow.model.params.RequestStatus;
import ru.kpfu.itis.meow.model.params.UserCredentials;
import ru.kpfu.itis.meow.service.AuthService;
import ru.kpfu.itis.meow.service.DeviceService;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;
    private final DeviceService deviceService;

    @Autowired
    public AuthController(AuthService authService, DeviceService deviceService) {
        this.authService = authService;
        this.deviceService = deviceService;
    }

    @PostMapping("/login")
    public ResponseEntity<RequestStatus> login(@RequestBody @Valid final UserCredentials userCredentials) {
        if (authService.isCredentialsValid(userCredentials)) {
            return ResponseEntity.ok(
                    RequestStatus.builder()
                            .isSuccess(true)
                            .text(authService.createLoginToken(userCredentials))
                            .build()
            );
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/registration")
    public ResponseEntity<RequestStatus> registration(@RequestBody @Valid final RegistrationParams params) {
        if (!deviceService.isDeviceNameAvailable(params.getDeviceName()) ||
                !authService.isLoginValid(params.getLogin())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(
                RequestStatus.builder()
                        .isSuccess(authService.register(params))
                        .build()
        );
    }
}
