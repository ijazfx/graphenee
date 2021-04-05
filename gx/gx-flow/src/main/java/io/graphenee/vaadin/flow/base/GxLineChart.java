package io.graphenee.vaadin.flow.base;

import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.builder.ChartBuilder;
import com.github.appreciated.apexcharts.config.builder.GridBuilder;
import com.github.appreciated.apexcharts.config.builder.StrokeBuilder;
import com.github.appreciated.apexcharts.config.builder.XAxisBuilder;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.chart.builder.ZoomBuilder;
import com.github.appreciated.apexcharts.config.grid.builder.RowBuilder;
import com.github.appreciated.apexcharts.config.stroke.Curve;
import com.github.appreciated.apexcharts.helper.Series;
import com.vaadin.flow.component.html.Div;

public class GxLineChart extends Div {

    private static final long serialVersionUID = 1L;
    private ApexCharts lineChart;

    public void initializeWithLabelsAndSeries(String[] xAxisLabel, Series... series) {
        lineChart = ApexChartsBuilder.get().withChart(ChartBuilder.get().withType(Type.line).withZoom(ZoomBuilder.get().withEnabled(false).build()).build())
                .withStroke(StrokeBuilder.get().withCurve(Curve.straight).build())
                .withGrid(GridBuilder.get().withRow(RowBuilder.get().withColors("#f3f3f3", "transparent").withOpacity(0.5).build()).build())
                .withXaxis(XAxisBuilder.get().withCategories(xAxisLabel).build()).withSeries(series).build();
        removeAll();
        add(lineChart);
        lineChart.setWidth("100%");
    }
}
