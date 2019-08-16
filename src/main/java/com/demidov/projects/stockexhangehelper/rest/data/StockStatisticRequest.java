package com.demidov.projects.stockexhangehelper.rest.data;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StockStatisticRequest {
    private String shareName;
    private List<Integer> windowSize;

}
