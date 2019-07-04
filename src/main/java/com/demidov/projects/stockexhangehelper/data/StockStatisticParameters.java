package com.demidov.projects.stockexhangehelper.data;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StockStatisticParameters {
    private String shareName;
    private String priceAttributeName;
    private int windowSize;


}
