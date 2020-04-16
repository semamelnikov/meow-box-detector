package ru.kpfu.itis.meow.service;

import ru.kpfu.itis.meow.model.entity.DeviceSound;
import ru.kpfu.itis.meow.model.params.Token;

public interface DeviceService {
    boolean isDeviceNameValid(String deviceName);

    boolean isDeviceNameAvailable(String deviceName);

    DeviceSound changeSoundStatus(Token token);

    DeviceSound getSoundStatus(Token token);
}
