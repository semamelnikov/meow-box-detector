package ru.kpfu.itis.meow.model.totals;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TotalStatistics {
    private TotalCatInfo year;
    private TotalCatInfo month;
    private TotalCatInfo day;
    private TotalCatInfo allTime;
    private List<TotalCatInfo> monthTopTen;
    private List<TotalCatInfo> yearTopHundred;
    private DayTime topTimeInMonth;
}
