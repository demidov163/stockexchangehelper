package com.demidov.projects.stockexhangehelper.service;

import com.demidov.projects.stockexhangehelper.data.StockShareParameters;

import java.util.List;

public interface StockExchangeRequestService {
    List<String> getStockShareHistoryInfo(StockShareParameters parameters);
}
