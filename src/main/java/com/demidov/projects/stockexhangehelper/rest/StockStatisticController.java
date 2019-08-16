package com.demidov.projects.stockexhangehelper.rest;

import com.demidov.projects.stockexhangehelper.data.StockStatisticParameters;
import com.demidov.projects.stockexhangehelper.data.StockStatisticResult;
import com.demidov.projects.stockexhangehelper.executors.impl.StockStatisticExecutor;
import com.demidov.projects.stockexhangehelper.rest.data.StockStatisticRequest;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping(value = "/stockexhange")
@Slf4j
public class StockStatisticController {

    private final StockStatisticExecutor stockStatisticExecutor;
    private final PlotPrinterImpl plotPrinter;

    public StockStatisticController(StockStatisticExecutor stockStatisticExecutor, PlotPrinterImpl plotPrinter) {
        this.stockStatisticExecutor = stockStatisticExecutor;
        this.plotPrinter = plotPrinter;
    }

    @RequestMapping(value = "/statistic", method = RequestMethod.POST)
    @ResponseBody
    public List<StockStatisticResult> getStockStatisticResult(@RequestBody StockStatisticRequest params) {
        final List<Integer> windowSizes = params.getWindowSize();
        if (windowSizes.isEmpty()) {
            return null;
        }
        List<StockStatisticResult> stockStatisticResults = new ArrayList<>();
        for (Integer windowSize : windowSizes) {
            StockStatisticParameters parameters = StockStatisticParameters.builder()
                    .priceAttributeName("CLOSE")
                    .shareName(params.getShareName())
                    .windowSize(windowSize).build();
            stockStatisticResults.add(stockStatisticExecutor.executeStockStatistic(parameters));
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
        res.add(getPlotData(stockStatisticResults.get(0).getMaPrices(), "Prices"));
        res.addAll(stockStatisticResults.stream()
                .map(ssr -> getPlotData(Arrays.copyOfRange(ssr.getPrices(),
                        ssr.getPrices().length - ssr.getMaPrices().length,
                        ssr.getPrices().length), "Prices " + ssr.getWindowSize())).collect(Collectors.toList()));
        return res;
    }

    private PlotData getPlotData(Double[] data, String name) {
        return PlotData.builder()
                .dataY(Stream.of(data).mapToDouble(Double::doubleValue).toArray())
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
        chart.setXAxisTitle("Y");
        XYSeries series = chart.addSeries("y(x)", null, yData);
        series.setMarker(SeriesMarkers.CIRCLE);

        BitmapEncoder.saveBitmap(chart, "./Sample_Chart", BitmapEncoder.BitmapFormat.PNG);

        return "0";
    }
}
//