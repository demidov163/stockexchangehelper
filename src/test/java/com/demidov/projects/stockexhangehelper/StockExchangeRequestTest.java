package com.demidov.projects.stockexhangehelper;

import com.demidov.projects.stockexhangehelper.config.StockExchangeAppConfig;
import com.demidov.projects.stockexhangehelper.data.StockShareParameters;
import com.demidov.projects.stockexhangehelper.service.StockExchangeRequestService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {StockExchangeAppConfig.class, TestConfig.class})
@Slf4j
public class StockExchangeRequestTest {

    @Autowired
    private StockExchangeRequestService stockExchangeRequestService;

    @Test
    public void StockExchangeRequest() {
        StockShareParameters parameters = StockShareParameters
                .builder()
                .attributeIndex("CLOSE")
                .shareName("ABRD")
                .build();


        List<String> stockShareInfo = stockExchangeRequestService.getStockShareHistoryInfo(parameters);
        log.debug("stockShareInfo {}", stockShareInfo);
    }
}
