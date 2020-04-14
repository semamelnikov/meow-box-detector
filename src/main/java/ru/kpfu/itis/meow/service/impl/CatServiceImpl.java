package ru.kpfu.itis.meow.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.meow.model.entity.Cat;
import ru.kpfu.itis.meow.model.entity.CatDetection;
import ru.kpfu.itis.meow.model.entity.Device;
import ru.kpfu.itis.meow.model.params.CatDetectionParams;
import ru.kpfu.itis.meow.repository.CatDetectionRepository;
import ru.kpfu.itis.meow.repository.DeviceRepository;
import ru.kpfu.itis.meow.service.CatService;

import java.time.Instant;
import java.util.Date;

@Service
public class CatServiceImpl implements CatService {
    private final DeviceRepository deviceRepository;
    private final CatDetectionRepository catDetectionRepository;

    @Autowired
    public CatServiceImpl(DeviceRepository deviceRepository, CatDetectionRepository catDetectionRepository) {
        this.deviceRepository = deviceRepository;
        this.catDetectionRepository = catDetectionRepository;
    }

    @Override
    public void detect(CatDetectionParams params) {
        final Device device = deviceRepository.findByName(params.getDeviceName()).orElseThrow(
                () -> new IllegalArgumentException("Device with name " + params.getDeviceName() + " does not exist")
        );
        final Cat cat = device.getCat();
        final CatDetection catDetection = CatDetection.builder()
                .catId(cat.getId())
                .deviceId(device.getId())
                .timestamp(Date.from(Instant.now()))
                .build();
        catDetectionRepository.save(catDetection);
    }
}
