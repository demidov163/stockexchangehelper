package com.demidov.projects.stockexhangehelper.rest;

import com.demidov.projects.stockexhangehelper.data.DesireStockPriceRequest;
import com.demidov.projects.stockexhangehelper.data.DesireStockPriceResponse;
import com.demidov.projects.stockexhangehelper.data.StockStatisticParameters;
import com.demidov.projects.stockexhangehelper.data.StockStatisticResult;
import com.demidov.projects.stockexhangehelper.executors.impl.StockStatisticExecutor;
import com.demidov.projects.stockexhangehelper.rest.data.StockStatisticRequest;
import com.demidov.projects.stockexhangehelper.service.impl.FutureStockPriceCalculationServiceImpl;
import com.demidov.projects.stockexhangehelper.service.impl.PlotData;
import com.demidov.projects.stockexhangehelper.service.impl.PlotPrinterImpl;
import lombok.extern.slf4j.Slf4j;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.markers.SeriesMarkers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping(value = "/stockexhange")
@Slf4j
public class StockStatisticController {

    private final StockStatisticExecutor stockStatisticExecutor;
    private final PlotPrinterImpl plotPrinter;
    private final FutureStockPriceCalculationServiceImpl stockPriceCalculationService;

    public StockStatisticController(StockStatisticExecutor stockStatisticExecutor, PlotPrinterImpl plotPrinter, FutureStockPriceCalculationServiceImpl stockPriceCalculationService) {
        this.stockStatisticExecutor = stockStatisticExecutor;
        this.plotPrinter = plotPrinter;
        this.stockPriceCalculationService = stockPriceCalculationService;
    }

    @RequestMapping(value = "/movingaverage", method = RequestMethod.POST)
    @ResponseBody
    public List<StockStatisticResult> getStockStatisticResult(@RequestBody StockStatisticRequest params) {
        final List<Integer> windowSizes = params.getWindowSizes();
        if (windowSizes.isEmpty()) {
            return null;
        }
        List<StockStatisticResult> stockStatisticResults = new ArrayList<>();
        for (String shareName : params.getShareNames()) {
            StockStatisticParameters parameters = StockStatisticParameters.builder()
                    .priceAttributeName("CLOSE")
                    .shareName(shareName)
                    .viewSize(params.getViewSize())
                    .windowSizes(params.getWindowSizes()).build();
            stockStatisticResults.addAll(stockStatisticExecutor.executeStockStatistic(parameters));
        }

        try {
            plotPrinter.plotPrint(preparePlotDataList(stockStatisticResults));
        } catch (IOException e) {
           log.error("Error in plot print", e);
        }
        return stockStatisticResults;

    }

    //reduce price array to moving average array size
    private List<PlotData> preparePlotDataList(List<StockStatisticResult> stockStatisticResults) {
        List<PlotData> res = new ArrayList<>();
        //1 find biggest price array
        StockStatisticResult maxResult = stockStatisticResults.get(0);
        for (int i = 1; i < stockStatisticResults.size(); i++) {
            if (stockStatisticResults.get(i).getPrices().length > maxResult.getPrices().length) {
                maxResult = stockStatisticResults.get(i);
            }
        }

        res.add(getPlotData(maxResult.getPrices(), "Prices"));
        //2 add MA arrays
        for (StockStatisticResult ssr : stockStatisticResults) {
            double[] tmp = new double[maxResult.getPrices().length];
            Arrays.fill(tmp, 0.0);
            int i = tmp.length - 1;
            for (int j = ssr.getMaPrices().length - 1; j >= 0; j--) {
                tmp[i--] = ssr.getMaPrices()[j];
            }
            res.add(getPlotData(tmp, "Prices " + ssr.getWindowSize()));
        }
        return res;
    }

    private PlotData getPlotData(double[] data, String name) {
        return PlotData.builder()
                .dataY(data)
                .name(name).build();
    }

    @RequestMapping(value = "/plot", method = RequestMethod.GET)
    @ResponseBody
    public String printPlot() throws IOException {
        double[] yData = new double[] { 2.0, 1.0, 0.0 };
        // Create Chart
        XYChart chart = new XYChart(500, 400);
        chart.setTitle("Sample Chart");
        chart.setXAxisTitle("X");
        chart.setYAxisTitle("Y");
        XYSeries series = chart.addSeries("y(x)", null, yData);
        series.setMarker(SeriesMarkers.CIRCLE);

        BitmapEncoder.saveBitmap(chart, "./Sample_Chart", BitmapEncoder.BitmapFormat.PNG);

        return "0";
    }

    @RequestMapping(value = "/futureprice", method = RequestMethod.POST)
    @ResponseBody
    public DesireStockPriceResponse calculatePrice(@RequestBody DesireStockPriceRequest desireStockPrice) {
         return stockPriceCalculationService.calcDesirePrice(desireStockPrice);
    }
}
//