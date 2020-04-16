package ru.kpfu.itis.meow.model.statistics;

import lombok.Builder;
import lombok.Data;
import ru.kpfu.itis.meow.model.entity.DayTime;

import java.util.List;

@Data
@Builder
public class Statistics {
    private TotalCatInfo year;
    private TotalCatInfo month;
    private TotalCatInfo day;
    private TotalCatInfo allTime;
    private List<TotalCatInfo> monthTopTen;
    private List<TotalCatInfo> yearTopHundred;
    private DayTime topTimeInMonth;
    private List<CountAggregationResult> monthAggregationByDayTime;
    private List<CountAggregationResult> aggregationByBreed;
    private List<CountAggregationResult> yearDetectionAggregationByMonth;
}
