package com.demidov.projects.stockexhangehelper.data.statistic;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StatisticData {
    private String date;
    private double price;
}
