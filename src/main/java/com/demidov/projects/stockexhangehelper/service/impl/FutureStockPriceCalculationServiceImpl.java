package com.demidov.projects.stockexhangehelper.service.impl;

import com.demidov.projects.stockexhangehelper.data.DesireStockPriceRequest;
import com.demidov.projects.stockexhangehelper.data.DesireStockPriceResponse;

public class FutureStockPriceCalculationServiceImpl {
    public DesireStockPriceResponse calcDesirePrice(DesireStockPriceRequest request) {
        DesireStockPriceResponse desireStockPriceResponse = new DesireStockPriceResponse();
        double v = request.getBoughtPrice() * request.getNumBoughtItems();
        double necessaryStockPrice = (v + v * (request.getBrokerPercent() / 100 ) * 2 + request.getDesireProfitPrice() + request.getAdditionalCharge()) / request.getNumBoughtItems();
        desireStockPriceResponse.setNecessaryItemPrice(necessaryStockPrice);

        return desireStockPriceResponse;


    }
}
