package ru.kpfu.itis.meow.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kpfu.itis.meow.model.totals.TotalStatistics;
import ru.kpfu.itis.meow.service.StatisticsService;

@RestController
@RequestMapping("/statistics")
@CrossOrigin(origins = "*")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @Autowired
    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/totals")
    public TotalStatistics getTotalStatistics() {
        return statisticsService.getTotals();
    }
}
