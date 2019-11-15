package com.demidov.projects.stockexhangehelper.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DesireStockPriceRequest {
    private double boughtPrice;
    private int numBoughtItems;
    private String name;
    private double brokerPercent;

    private double desireProfitPrice;
}
