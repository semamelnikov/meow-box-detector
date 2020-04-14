package ru.kpfu.itis.meow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kpfu.itis.meow.model.entity.CatDetection;

public interface CatDetectionRepository extends JpaRepository<CatDetection, Long> {
}
