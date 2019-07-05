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
import java.util.List;


public class StockStatisticExecutor {

    private DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    private StockExchangeRequestService stockExchangeRequestService;

    @Autowired
    private MovingAverageCalculationAlg movingAverageCalculationAlg;

    public StockStatisticResult executeStockStatistic(StockStatisticParameters stockParameters) {
        StockShareParameters stockShareParameters = StockShareParameters.builder()
                .shareName(stockParameters.getShareName())
                .attributeIndex(stockParameters.getPriceAttributeName())
                .tillDate(LocalDate.now().format(dateFormat))
                .fromDate(LocalDate.now().minusDays(stockParameters.getWindowSize() * 2).format(dateFormat))
                .build();
        List<StatisticData> stockShareHistoryInfo = stockExchangeRequestService.getStockShareHistoryInfo(stockShareParameters);

        Assert.isTrue(!CollectionUtils.isEmpty(stockShareHistoryInfo), "No history info for " + stockParameters.getShareName());

        Double[] data = stockShareHistoryInfo.stream().map(StatisticData::getPrice).toArray(Double[]::new);

        Double[] movingAverage = movingAverageCalculationAlg.calculateMovingAverage(data, stockParameters.getWindowSize());

        return StockStatisticResult.builder().prices(data)
                .maPrices(movingAverage)
                .windowSize(stockParameters.getWindowSize()).build();
    }
}
