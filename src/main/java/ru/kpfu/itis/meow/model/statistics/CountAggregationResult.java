package ru.kpfu.itis.meow.model.statistics;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CountAggregationResult {
    private String name;
    private Long value;
}
