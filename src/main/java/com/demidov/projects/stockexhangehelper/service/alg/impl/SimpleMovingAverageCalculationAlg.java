package com.demidov.projects.stockexhangehelper.service.alg.impl;

import com.demidov.projects.stockexhangehelper.service.alg.MovingAverageCalculationAlg;
import org.springframework.util.Assert;

import java.util.Arrays;


public class SimpleMovingAverageCalculationAlg implements MovingAverageCalculationAlg {

    @Override
    public Double[] calculateMovingAverage(Double[] data, int windowSize) {
        Assert.isTrue(windowSize > 1, "Window size should be more 1");
        Assert.isTrue(data != null, "Data array should be not null");
        Assert.isTrue(data.length > (windowSize-1), "Incorrect data array size");

        Double[] res = new Double[data.length - windowSize + 1];
        Arrays.fill(res, 0);

        for (int i = res.length; i > 0; i--) {
            for (int j = 0; j < windowSize - 1; j++) {
                res[i] += res[j];
            }
            res[i] /= windowSize;
        }

        return res;
    }
}
