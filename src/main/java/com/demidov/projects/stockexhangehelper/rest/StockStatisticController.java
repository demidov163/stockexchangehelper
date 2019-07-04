package com.demidov.projects.stockexhangehelper.rest;

import com.demidov.projects.stockexhangehelper.data.StockStatisticParameters;
import com.demidov.projects.stockexhangehelper.data.StockStatisticResult;
import com.demidov.projects.stockexhangehelper.executors.impl.StockStatisticExecutor;
import com.demidov.projects.stockexhangehelper.rest.data.StockStatisticRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/stockexhange")
public class StockStatisticController {

    private final StockStatisticExecutor stockStatisticExecutor;

    public StockStatisticController(StockStatisticExecutor stockStatisticExecutor) {
        this.stockStatisticExecutor = stockStatisticExecutor;
    }

    @RequestMapping(value = "/statistic", method = RequestMethod.POST)
    @ResponseBody
    public StockStatisticResult getStockStatisticResult(@RequestBody StockStatisticRequest params) {
        StockStatisticParameters parameters = StockStatisticParameters.builder()
                .priceAttributeName("CLOSE")
                .shareName(params.getShareName())
                .windowSize(params.getWindowSize()).build();
        return stockStatisticExecutor.executeStockStatistic(parameters);

    }
}
