package ru.kpfu.itis.meow.service;

import ru.kpfu.itis.meow.model.params.CatDetectionParams;

public interface CatService {
    void detect(CatDetectionParams params);
}
