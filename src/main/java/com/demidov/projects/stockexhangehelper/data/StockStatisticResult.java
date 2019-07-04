package com.demidov.projects.stockexhangehelper.data;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StockStatisticResult {
    private Double[] prices;
    private Double[] maPrices;
    private int windowSize;

}
