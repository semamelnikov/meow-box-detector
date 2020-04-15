package ru.kpfu.itis.meow.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.meow.model.entity.Cat;
import ru.kpfu.itis.meow.model.entity.CatDetection;
import ru.kpfu.itis.meow.model.entity.Device;
import ru.kpfu.itis.meow.model.params.CatDetectionParams;
import ru.kpfu.itis.meow.model.totals.DayTime;
import ru.kpfu.itis.meow.repository.CatDetectionRepository;
import ru.kpfu.itis.meow.repository.DeviceRepository;
import ru.kpfu.itis.meow.service.CatService;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
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

        LocalDateTime localDateTime = LocalDateTime.now();

        final CatDetection catDetection = CatDetection.builder()
                .catId(cat.getId())
                .deviceId(device.getId())
                .timestamp(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()))
                .dayTime(getCurrentDayTime(localDateTime))
                .build();
        catDetectionRepository.save(catDetection);
    }

    private DayTime getCurrentDayTime(LocalDateTime now) {
        int year = now.getYear();
        Month month = now.getMonth();
        int dayOfMonth = now.getDayOfMonth();

        LocalDateTime morningStart = LocalDateTime.of(year, month, dayOfMonth, 7, 0);
        LocalDateTime dayStart = LocalDateTime.of(year, month, dayOfMonth, 10, 0);
        LocalDateTime eveningStart = LocalDateTime.of(year, month, dayOfMonth, 16, 0);
        LocalDateTime nightStart = LocalDateTime.of(year, month, dayOfMonth, 22, 0);

        if (now.isAfter(morningStart) && now.isBefore(dayStart)) {
            return DayTime.MORNING;
        } else if (now.isAfter(dayStart) && now.isBefore(eveningStart)) {
            return DayTime.DAY;
        } else if (now.isAfter(eveningStart) && now.isBefore(nightStart)) {
            return DayTime.EVENING;
        }
        return DayTime.NIGHT;
    }
}
