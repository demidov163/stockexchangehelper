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
import java.util.List;


public class StockStatisticExecutor {

    private static final int DAYS_TO_SUBTRACT = 10;
    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    private StockExchangeRequestService stockExchangeRequestService;

    @Autowired
    private MovingAverageCalculationAlg movingAverageCalculationAlg;

    public StockStatisticResult executeStockStatistic(StockStatisticParameters stockParameters) {

        int wDaysLeast = stockParameters.getWindowSize() * 2;
        List<StatisticData> stockShareHistoryInfo = new ArrayList<>();
        LocalDate fromDate = LocalDate.now().minusDays(DAYS_TO_SUBTRACT);
        LocalDate tillDate = LocalDate.now();

        while (wDaysLeast > 0) {
            StockShareParameters stockShareParameters = StockShareParameters.builder()
                    .shareName(stockParameters.getShareName())
                    .attributeIndex(stockParameters.getPriceAttributeName())
                    .tillDate(tillDate.format(dateFormat))
                    .fromDate(fromDate.format(dateFormat))
                    .build();
            List<StatisticData> statisticDataTmp = stockExchangeRequestService.getStockShareHistoryInfo(stockShareParameters);

            if (statisticDataTmp.size() >= stockParameters.getWindowSize() * 2) {
                statisticDataTmp = statisticDataTmp.subList(statisticDataTmp.size() - stockParameters.getWindowSize(),
                        statisticDataTmp.size());
            }
            stockShareHistoryInfo.addAll(0, statisticDataTmp);
            wDaysLeast -= statisticDataTmp.size();
            tillDate = fromDate.minusDays(1);
            fromDate = tillDate.minusDays(DAYS_TO_SUBTRACT);
        }

        Assert.isTrue(!CollectionUtils.isEmpty(stockShareHistoryInfo), "No history info for " + stockParameters.getShareName());

        double[] data = stockShareHistoryInfo.stream().mapToDouble(StatisticData::getPrice).toArray();

        double[] movingAverage = movingAverageCalculationAlg.calculateMovingAverage(data, stockParameters.getWindowSize());

        return StockStatisticResult.builder().prices(data)
            .maPrices(movingAverage)
            .windowSize(stockParameters.getWindowSize()).build();
    }
}
