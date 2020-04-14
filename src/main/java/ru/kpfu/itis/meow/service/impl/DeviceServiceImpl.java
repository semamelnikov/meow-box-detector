package ru.kpfu.itis.meow.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.meow.model.entity.Device;
import ru.kpfu.itis.meow.repository.DeviceRepository;
import ru.kpfu.itis.meow.service.DeviceService;

import java.util.Optional;

@Service
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;

    @Autowired
    public DeviceServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public boolean isDeviceNameValid(String deviceName) {
        Optional<Device> deviceOptional = deviceRepository.findByName(deviceName);
        if (!deviceOptional.isPresent()) {
            return false;
        }
        Device device = deviceOptional.get();
        return device.getCat() != null;
    }

    @Override
    public boolean isDeviceNameAvailable(String deviceName) {
        Optional<Device> deviceOptional = deviceRepository.findByName(deviceName);
        if (!deviceOptional.isPresent()) {
            return false;
        }
        Device device = deviceOptional.get();
        return device.getCat() == null;
    }
}
