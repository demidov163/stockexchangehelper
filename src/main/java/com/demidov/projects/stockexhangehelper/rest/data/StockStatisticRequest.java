package com.demidov.projects.stockexhangehelper.rest.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockStatisticRequest {
    private String shareName;
    private int windowSize;

}
