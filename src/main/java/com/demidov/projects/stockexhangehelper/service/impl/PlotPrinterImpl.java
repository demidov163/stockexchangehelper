package com.demidov.projects.stockexhangehelper.service.impl;

import org.apache.commons.lang3.RandomUtils;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.markers.SeriesMarkers;

import java.io.IOException;
import java.util.List;

public class PlotPrinterImpl {
    public void plotPrint(List<PlotData> data) throws IOException {
        XYChart chart = new XYChart(500, 400);
        chart.setTitle("Sample Chart");
        chart.setXAxisTitle("X");
        chart.setXAxisTitle("Y");

        data.forEach(d -> {
            //todo fix xData
            XYSeries series = chart.addSeries(d.getName(), d.getDataY());
            series.setMarker(SeriesMarkers.CIRCLE);
        });

        BitmapEncoder.saveBitmap(chart, "./Sample_Chart" + RandomUtils.nextDouble(), BitmapEncoder.BitmapFormat.PNG);
    }
}
