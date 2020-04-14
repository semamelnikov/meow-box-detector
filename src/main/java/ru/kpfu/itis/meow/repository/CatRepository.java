package ru.kpfu.itis.meow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kpfu.itis.meow.model.entity.Cat;

public interface CatRepository extends JpaRepository<Cat, Long> {
}
