package com.demidov.projects.stockexhangehelper.service;

import com.demidov.projects.stockexhangehelper.data.StockShareParameters;
import com.demidov.projects.stockexhangehelper.data.statistic.StatisticData;

import java.util.List;

public interface StockExchangeRequestService {
    List<StatisticData> getStockShareHistoryInfo(StockShareParameters parameters);
}
