package ru.kpfu.itis.meow.service;

public interface DeviceService {
    boolean isDeviceNameValid(String deviceName);

    boolean isDeviceNameAvailable(String deviceName);
}
