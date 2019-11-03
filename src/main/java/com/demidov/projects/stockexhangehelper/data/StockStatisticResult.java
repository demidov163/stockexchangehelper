package com.demidov.projects.stockexhangehelper.data;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class StockStatisticResult {
    private double[] prices;
    private double[] maPrices;
    private int windowSize;
    private String legend;

}
