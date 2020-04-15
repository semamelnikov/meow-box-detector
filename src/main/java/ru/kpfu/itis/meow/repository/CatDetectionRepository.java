package ru.kpfu.itis.meow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kpfu.itis.meow.model.entity.CatDetection;

import java.util.Date;
import java.util.List;

public interface CatDetectionRepository extends JpaRepository<CatDetection, Long> {
    List<CatDetection> findAllByTimestampBetween(Date start, Date end);
}
