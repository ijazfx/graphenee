package io.graphenee.vaadin.flow.base;

import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.builder.ChartBuilder;
import com.github.appreciated.apexcharts.config.builder.DataLabelsBuilder;
import com.github.appreciated.apexcharts.config.builder.FillBuilder;
import com.github.appreciated.apexcharts.config.builder.PlotOptionsBuilder;
import com.github.appreciated.apexcharts.config.builder.StrokeBuilder;
import com.github.appreciated.apexcharts.config.builder.XAxisBuilder;
import com.github.appreciated.apexcharts.config.builder.YAxisBuilder;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.plotoptions.builder.BarBuilder;
import com.github.appreciated.apexcharts.helper.Series;
import com.vaadin.flow.component.html.Div;

public class GxStackedBarChart extends Div {

    private static final long serialVersionUID = 1L;
    private ApexCharts stackedBarChart;

    public void initializeWithLabelsAndSeries(String[] xAxisLabel, Series... series) {
        stackedBarChart = ApexChartsBuilder.get().withChart(ChartBuilder.get().withType(Type.bar).withStacked(true).build())
                .withPlotOptions(PlotOptionsBuilder.get().withBar(BarBuilder.get().withHorizontal(false).withColumnWidth("55%").build()).build())
                .withDataLabels(DataLabelsBuilder.get().withEnabled(false).build()).withStroke(StrokeBuilder.get().withShow(true).withWidth(2.0).withColors("transparent").build())
                .withYaxis(YAxisBuilder.get().build()).withXaxis(XAxisBuilder.get().withCategories(xAxisLabel).build()).withFill(FillBuilder.get().withOpacity(1.0).build())
                .withSeries(series).build();
        removeAll();
        add(stackedBarChart);
        stackedBarChart.setWidth("100%");
    }
}
