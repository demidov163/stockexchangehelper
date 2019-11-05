package com.demidov.projects.stockexhangehelper.executors.impl;

import com.demidov.projects.stockexhangehelper.data.StockStatisticParameters;
import com.demidov.projects.stockexhangehelper.data.StockShareParameters;
import com.demidov.projects.stockexhangehelper.data.StockStatisticResult;
import com.demidov.projects.stockexhangehelper.data.statistic.StatisticData;
import com.demidov.projects.stockexhangehelper.service.StockExchangeRequestService;
import com.demidov.projects.stockexhangehelper.service.alg.MovingAverageCalculationAlg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public class StockStatisticExecutor {

    private static final int DAYS_TO_SUBTRACT = 10;
    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    private StockExchangeRequestService stockExchangeRequestService;

    @Autowired
    private MovingAverageCalculationAlg movingAverageCalculationAlg;

    //assumption, windowSize <= viewSize
    public List<StockStatisticResult> executeStockStatistic(StockStatisticParameters stockParameters) {

        int wDaysLeast = stockParameters.getViewSize() * 2;
        List<StatisticData> stockShareHistoryInfo = new ArrayList<>();
        LocalDate fromDate = LocalDate.now().minusDays(wDaysLeast);
        LocalDate tillDate = LocalDate.now();

        while (wDaysLeast > 0) {
            StockShareParameters stockShareParameters = StockShareParameters.builder()
                    .shareName(stockParameters.getShareName())
                    .attributeIndex(stockParameters.getPriceAttributeName())
                    .tillDate(tillDate.format(dateFormat))
                    .fromDate(fromDate.format(dateFormat))
                    .build();
            List<StatisticData> statisticDataTmp = stockExchangeRequestService.getStockShareHistoryInfo(stockShareParameters);

            if (statisticDataTmp.size() >= stockParameters.getViewSize() * 2) {
                statisticDataTmp = statisticDataTmp.subList(statisticDataTmp.size() - stockParameters.getViewSize(),
                        statisticDataTmp.size());
            }
            stockShareHistoryInfo.addAll(0, statisticDataTmp);
            wDaysLeast -= statisticDataTmp.size();
            tillDate = fromDate.minusDays(1);
            fromDate = tillDate.minusDays(DAYS_TO_SUBTRACT);
        }

        Assert.isTrue(!CollectionUtils.isEmpty(stockShareHistoryInfo), "No history info for " + stockParameters.getShareName());

        double[] data = stockShareHistoryInfo.stream().mapToDouble(StatisticData::getPrice).toArray();
        return stockParameters.getWindowSizes().stream()
                .map(ws -> StockStatisticResult.builder().prices(data)
                           .maPrices(movingAverageCalculationAlg.calculateMovingAverage(data, ws))
                           .windowSize(ws).build())
                .collect(Collectors.toList());
    }
}
