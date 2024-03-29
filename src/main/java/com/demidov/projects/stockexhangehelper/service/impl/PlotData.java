package com.demidov.projects.stockexhangehelper.service.impl;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PlotData {
    private double[] dataX;
    private double[] dataY;
    private String name;
}
