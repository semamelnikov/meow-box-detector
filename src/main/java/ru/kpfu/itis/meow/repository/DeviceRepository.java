package ru.kpfu.itis.meow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kpfu.itis.meow.model.entity.Device;

import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, Long> {
   Optional<Device> findByName(String name);
}
