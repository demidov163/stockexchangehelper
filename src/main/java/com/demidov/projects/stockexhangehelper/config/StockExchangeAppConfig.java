package com.demidov.projects.stockexhangehelper.config;

import com.demidov.projects.stockexhangehelper.executors.impl.StockStatisticExecutor;
import com.demidov.projects.stockexhangehelper.service.StockExchangeRequestService;
import com.demidov.projects.stockexhangehelper.service.alg.MovingAverageCalculationAlg;
import com.demidov.projects.stockexhangehelper.service.alg.impl.SimpleMovingAverageCalculationAlg;
import com.demidov.projects.stockexhangehelper.service.impl.FutureStockPriceCalculationServiceImpl;
import com.demidov.projects.stockexhangehelper.service.impl.PlotPrinterImpl;
import com.demidov.projects.stockexhangehelper.service.impl.StockExchangeRequestServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StockExchangeAppConfig {

    @Bean
    public StockExchangeRequestService getStockExchangeRequestService() {
        return new StockExchangeRequestServiceImpl();
    }

    @Bean(name = "simpleMovingAverageCalculationAlg")
    public MovingAverageCalculationAlg getSimpleMovingAverageCalculationAlg() {
        return new SimpleMovingAverageCalculationAlg();
    }

    @Bean
    public StockStatisticExecutor getStockStatisticExecutor() {
        return new StockStatisticExecutor();
    }

    @Bean
    public PlotPrinterImpl getPlotPrinter() {
        return new PlotPrinterImpl();
    }

    @Bean
    public FutureStockPriceCalculationServiceImpl futureStockPriceCalculationService() {
        return new FutureStockPriceCalculationServiceImpl();
    }
}
