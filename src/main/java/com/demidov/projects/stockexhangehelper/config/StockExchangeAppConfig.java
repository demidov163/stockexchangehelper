package com.demidov.projects.stockexhangehelper.config;

import com.demidov.projects.stockexhangehelper.service.StockExchangeRequestService;
import com.demidov.projects.stockexhangehelper.service.impl.StockExchangeRequestServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StockExchangeAppConfig {

    @Bean
    public StockExchangeRequestService getStockExchangeRequestService() {
        return new StockExchangeRequestServiceImpl();
    }

}
