package ru.kpfu.itis.meow.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.meow.model.entity.Device;
import ru.kpfu.itis.meow.model.entity.DeviceSound;
import ru.kpfu.itis.meow.model.params.Token;
import ru.kpfu.itis.meow.repository.DeviceRepository;
import ru.kpfu.itis.meow.repository.DeviceSoundRepository;
import ru.kpfu.itis.meow.service.DeviceService;
import ru.kpfu.itis.meow.util.MqttSender;
import ru.kpfu.itis.meow.util.TokenUtil;

import java.util.Optional;

@Service
public class DeviceServiceImpl implements DeviceService {

    private final MqttSender mqttSender;
    private final DeviceRepository deviceRepository;
    private final DeviceSoundRepository deviceSoundRepository;

    @Autowired
    public DeviceServiceImpl(MqttSender mqttSender, DeviceRepository deviceRepository,
                             DeviceSoundRepository deviceSoundRepository) {
        this.mqttSender = mqttSender;
        this.deviceRepository = deviceRepository;
        this.deviceSoundRepository = deviceSoundRepository;
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

    @Override
    public DeviceSound changeSoundStatus(Token token) {
        long deviceId = TokenUtil.getDeviceIdFromToken(token.getValue());
        Optional<DeviceSound> deviceSoundOptional = deviceSoundRepository.findByDeviceId(deviceId);
        if (deviceSoundOptional.isPresent()) {
            DeviceSound deviceSound = deviceSoundOptional.get();
            deviceSound.setSoundOn(!deviceSound.isSoundOn());
            mqttSender.sendMqttMessage(deviceSound.isSoundOn(), TokenUtil.getDeviceNameFromToken(token.getValue()));
            deviceSoundRepository.save(deviceSound);
            return deviceSound;
        }
        return null;

    }

    @Override
    public DeviceSound getSoundStatus(Token token) {
        long deviceId = TokenUtil.getDeviceIdFromToken(token.getValue());
        return deviceSoundRepository.findByDeviceId(deviceId).orElse(null);
    }
}
