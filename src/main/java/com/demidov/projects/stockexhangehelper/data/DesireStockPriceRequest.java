package com.demidov.projects.stockexhangehelper.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DesireStockPriceRequest {
    private Double boughtPrice;
    private Integer numBoughtItems;
    private String name;
    private Double brokerPercent;
    private Double additionalCharge;

    private Double desireProfitPrice;
}
