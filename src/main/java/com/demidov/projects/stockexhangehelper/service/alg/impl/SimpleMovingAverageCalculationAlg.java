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
        Arrays.fill(res, 0.0);

        int i = data.length - 1;
        int c = res.length - 1;
        while (i > data.length - 1 - res.length) {
            for (int j = 0; j < windowSize - 1; j++) {
                res[c] += data[i - j];
            }
            res[c] /= windowSize;
            i--;
            c--;
        }

        return res;
    }
}
