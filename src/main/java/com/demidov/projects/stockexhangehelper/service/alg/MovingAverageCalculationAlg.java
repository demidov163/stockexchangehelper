package com.demidov.projects.stockexhangehelper.service.alg;

public interface MovingAverageCalculationAlg {
    Double[] calculateMovingAverage(Double[] data, int windowSize);
}
