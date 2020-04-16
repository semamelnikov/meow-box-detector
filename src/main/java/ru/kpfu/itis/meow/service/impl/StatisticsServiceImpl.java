package ru.kpfu.itis.meow.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.meow.model.entity.Cat;
import ru.kpfu.itis.meow.model.entity.CatDetection;
import ru.kpfu.itis.meow.model.entity.DayTime;
import ru.kpfu.itis.meow.model.statistics.CountAggregationResult;
import ru.kpfu.itis.meow.model.statistics.Statistics;
import ru.kpfu.itis.meow.model.statistics.TotalCatInfo;
import ru.kpfu.itis.meow.repository.CatDetectionRepository;
import ru.kpfu.itis.meow.repository.CatRepository;
import ru.kpfu.itis.meow.repository.UserRepository;
import ru.kpfu.itis.meow.service.StatisticsService;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class StatisticsServiceImpl implements StatisticsService {
    private final CatRepository catRepository;
    private final UserRepository userRepository;
    private final CatDetectionRepository catDetectionRepository;

    @Autowired
    public StatisticsServiceImpl(CatRepository catRepository, UserRepository userRepository,
                                 CatDetectionRepository catDetectionRepository) {
        this.catRepository = catRepository;
        this.userRepository = userRepository;
        this.catDetectionRepository = catDetectionRepository;
    }

    @Override
    public Statistics getTotals() {
        LocalDate now = LocalDate.now();

        List<Cat> topHundredOfTheYear = getTopCatOfTheYear(now.getYear(), 100);
        Cat catOfTheYear = topHundredOfTheYear.stream().findFirst().orElse(null);

        List<Cat> topTenOfTheMonth = getTopOfTheMonth(now, 10);
        Cat catOfTheMonth = topTenOfTheMonth.stream().findFirst().orElse(null);

        Cat catOfTheDay = getCatOfTheDay(now);
        Cat catOfAllTime = getCatOfAllTime();

        return Statistics.builder()
                .year(getInfo(catOfTheYear))
                .month(getInfo(catOfTheMonth))
                .day(getInfo(catOfTheDay))
                .allTime(getInfo(catOfAllTime))
                .monthTopTen(getInfo(topTenOfTheMonth))
                .yearTopHundred(getInfo(topHundredOfTheYear))
                .topTimeInMonth(getTopTimeInMonth(now))
                .aggregationByBreed(getAggregationByBreed())
                .yearDetectionAggregationByMonth(getYearDetectionAggregationByMonth(now))
                .monthAggregationByDayTime(getMonthAggregationByDayTime(now))
                .build();
    }

    private List<CountAggregationResult> getYearDetectionAggregationByMonth(LocalDate now) {
        List<CatDetection> yearDetections = getYearDetections(now.getYear());
        return aggregationMapToCountAggregationResults(
                yearDetections.stream()
                        .map(CatDetection::getTimestamp)
                        .map(date -> date.toInstant().atZone(ZoneId.systemDefault()).getMonth().toString())
                        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
        );
    }

    private List<CountAggregationResult> getMonthAggregationByDayTime(LocalDate now) {
        List<CatDetection> monthDetections = getMonthDetections(now.getYear(), now.getMonth().ordinal(), now.getDayOfMonth());
        return aggregationMapToCountAggregationResults(
                monthDetections.stream()
                        .collect(Collectors.groupingBy(catDetection -> catDetection.getDayTime().toString(), Collectors.counting()))
        );
    }

    private List<CountAggregationResult> getAggregationByBreed() {
        List<CatDetection> detections = catDetectionRepository.findAll();
        return aggregationMapToCountAggregationResults(
                detections.stream()
                        .map(catDetection -> catRepository.getOne(catDetection.getCatId()))
                        .collect(Collectors.groupingBy(Cat::getBreed, Collectors.counting()))
        );
    }

    private List<CountAggregationResult> aggregationMapToCountAggregationResults(Map<String, Long> aggregations) {
        return aggregations.entrySet()
                .stream()
                .map(
                        entry -> CountAggregationResult.builder()
                                .name(entry.getKey())
                                .count(entry.getValue())
                                .build()
                )
                .collect(Collectors.toList());
    }

    private DayTime getTopTimeInMonth(LocalDate now) {
        List<CatDetection> monthDetections = getMonthDetections(now.getYear(), now.getMonth().ordinal(), now.getDayOfMonth());
        return monthDetections.stream()
                .collect(Collectors.groupingBy(CatDetection::getDayTime, Collectors.counting()))
                .entrySet()
                .stream()
                .max(Comparator.comparing(Map.Entry::getValue))
                .map(Map.Entry::getKey).orElse(null);
    }

    private List<Cat> getTopOfTheMonth(LocalDate now, int top) {
        List<CatDetection> monthDetections = getMonthDetections(now.getYear(), now.getMonth().ordinal(), now.getDayOfMonth());
        return getTopCatsInDetections(monthDetections, top);
    }

    private List<Cat> getTopCatOfTheYear(int year, int top) {
        List<CatDetection> yearDetections = getYearDetections(year);
        return getTopCatsInDetections(yearDetections, top);
    }

    private Cat getCatOfTheDay(LocalDate now) {
        List<CatDetection> detections = getDayDetections(now.getYear(), now.getMonth().ordinal(), now.getDayOfMonth());
        return getTopOneCatInDetections(detections);
    }

    private Cat getCatOfAllTime() {
        List<CatDetection> detections = catDetectionRepository.findAll();
        return getTopOneCatInDetections(detections);
    }

    private List<Cat> getTopCatsInDetections(List<CatDetection> detections, int top) {
        return detections.stream()
                .collect(Collectors.groupingBy(CatDetection::getCatId, Collectors.counting()))
                .entrySet()
                .stream()
                .sorted((e1, e2) -> (-1) * e1.getValue().compareTo(e2.getValue()))
                .map(Map.Entry::getKey)
                .limit(top)
                .map(catRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private Cat getTopOneCatInDetections(List<CatDetection> detections) {
        return detections.stream()
                .collect(Collectors.groupingBy(CatDetection::getCatId, Collectors.counting()))
                .entrySet()
                .stream()
                .max(Comparator.comparing(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .map(catRepository::findById)
                .map(Optional::get)
                .orElse(null);
    }

    private List<CatDetection> getDayDetections(int year, int month, int dayOfMonth) {
        Date start = new GregorianCalendar(year, month, dayOfMonth, 0, 0).getTime();
        Date end = new GregorianCalendar(year, month, dayOfMonth, 23, 59).getTime();
        return catDetectionRepository.findAllByTimestampBetween(start, end);
    }

    private List<CatDetection> getMonthDetections(int year, int month, int dayOfMonth) {
        Date start = new GregorianCalendar(year, month, 1).getTime();
        Date end = new GregorianCalendar(year, month, dayOfMonth + 1).getTime();
        return catDetectionRepository.findAllByTimestampBetween(start, end);
    }

    private List<CatDetection> getYearDetections(int year) {
        Date start = new GregorianCalendar(year, Calendar.JANUARY, 1).getTime();
        Date end = new GregorianCalendar(year, Calendar.DECEMBER, 31).getTime();
        return catDetectionRepository.findAllByTimestampBetween(start, end);
    }

    private TotalCatInfo getInfo(Cat catOfAllTime) {
        return TotalCatInfo.builder()
                .cat(catOfAllTime)
                .userName(userRepository.getOne(catOfAllTime.getId()).getLogin())
                .build();
    }

    private List<TotalCatInfo> getInfo(List<Cat> topCats) {
        return topCats.stream()
                .map(
                        cat -> TotalCatInfo.builder()
                                .cat(cat)
                                .userName(userRepository.getOne(cat.getUserId()).getLogin())
                                .build()
                ).collect(Collectors.toList());
    }
}
