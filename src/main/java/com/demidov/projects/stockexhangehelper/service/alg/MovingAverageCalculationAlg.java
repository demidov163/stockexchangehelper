package com.demidov.projects.stockexhangehelper.service.alg;

public interface MovingAverageCalculationAlg {
    double[] calculateMovingAverage(double[] data, int windowSize);
}
