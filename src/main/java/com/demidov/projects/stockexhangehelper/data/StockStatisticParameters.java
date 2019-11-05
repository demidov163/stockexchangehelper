package com.demidov.projects.stockexhangehelper.data;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class StockStatisticParameters {
    private String shareName;
    private String priceAttributeName;
    private List<Integer> windowSizes;
    private int viewSize;


}
