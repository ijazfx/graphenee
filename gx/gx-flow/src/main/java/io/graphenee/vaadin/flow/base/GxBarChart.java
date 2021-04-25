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
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class GxBarChart extends VerticalLayout {

    private static final long serialVersionUID = 1L;

    private ApexCharts barChart;

    public void initializeWithLabelsAndSeries(String[] xAxisLabel, Series... series) {
        barChart = ApexChartsBuilder.get().withChart(ChartBuilder.get().withType(Type.bar).build())
                .withPlotOptions(PlotOptionsBuilder.get().withBar(BarBuilder.get().withHorizontal(false).withColumnWidth("55%").build()).build())
                .withDataLabels(DataLabelsBuilder.get().withEnabled(false).build()).withStroke(StrokeBuilder.get().withShow(true).withWidth(2.0).withColors("transparent").build())
                .withYaxis(YAxisBuilder.get().build()).withXaxis(XAxisBuilder.get().withCategories(xAxisLabel).build()).withFill(FillBuilder.get().withOpacity(1.0).build())
                .withSeries(series).build();
        removeAll();
        add(barChart);
        barChart.setWidth("100%");
    }
}
