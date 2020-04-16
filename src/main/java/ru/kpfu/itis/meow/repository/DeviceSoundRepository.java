package ru.kpfu.itis.meow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kpfu.itis.meow.model.entity.DeviceSound;

import java.util.Optional;

public interface DeviceSoundRepository extends JpaRepository<DeviceSound, Long> {
    Optional<DeviceSound> findByDeviceId(Long deviceId);
}
