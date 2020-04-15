package ru.kpfu.itis.meow.model.totals;

import lombok.Builder;
import lombok.Data;
import ru.kpfu.itis.meow.model.entity.Cat;

@Data
@Builder
public class TotalCatInfo {
    private String userName;
    private Cat cat;
}
