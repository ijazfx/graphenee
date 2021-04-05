package io.graphenee.vaadin.flow.base;

import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.builder.ChartBuilder;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.helper.Series;
import com.vaadin.flow.component.html.Div;

public class GxRadarChart extends Div {

    private static final long serialVersionUID = 1L;
    private ApexCharts radarChart;

    public void initializeWithLabelsAndSeries(String[] label, Series... series) {

        radarChart = ApexChartsBuilder.get().withChart(ChartBuilder.get().withType(Type.radar).build()).withSeries(series).withLabels(label).build();
        removeAll();
        add(radarChart);
        radarChart.setWidth("100%");
    }
}
