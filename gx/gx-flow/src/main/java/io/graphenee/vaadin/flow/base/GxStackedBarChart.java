package io.graphenee.vaadin.flow.base;

import java.util.Arrays;
import java.util.List;

import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.builder.ChartBuilder;
import com.github.appreciated.apexcharts.config.builder.DataLabelsBuilder;
import com.github.appreciated.apexcharts.config.builder.FillBuilder;
import com.github.appreciated.apexcharts.config.builder.LegendBuilder;
import com.github.appreciated.apexcharts.config.builder.PlotOptionsBuilder;
import com.github.appreciated.apexcharts.config.builder.XAxisBuilder;
import com.github.appreciated.apexcharts.config.builder.YAxisBuilder;
import com.github.appreciated.apexcharts.config.chart.StackType;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.legend.HorizontalAlign;
import com.github.appreciated.apexcharts.config.legend.Position;
import com.github.appreciated.apexcharts.config.plotoptions.builder.BarBuilder;
import com.github.appreciated.apexcharts.config.yaxis.builder.TitleBuilder;
import com.github.appreciated.apexcharts.helper.Series;
import com.vaadin.flow.component.html.Div;

public class GxStackedBarChart extends Div {

    private static final long serialVersionUID = 1L;
    private ApexCharts chart;

    public GxStackedBarChart() {
        chart = ApexChartsBuilder.get().withColors("#00ACC1", "#ECEFF1").withFill(FillBuilder.get().withColors(Arrays.asList("#00ACC1", "#ECEFF1")).build())
                .withChart(ChartBuilder.get().withType(Type.bar).withStacked(true).withStackType(StackType.normal).build())
                .withPlotOptions(PlotOptionsBuilder.get().withBar(BarBuilder.get().withHorizontal(false).withColumnWidth("100%").build()).build())
                .withDataLabels(DataLabelsBuilder.get().withEnabled(false).build())
                .withLegend(LegendBuilder.get().withShow(true).withPosition(Position.top).withHorizontalAlign(HorizontalAlign.left).build()).build();
        add(chart);
        chart.setHeight("200px");
    }

    public void initializeWithLabelsAndSeries(String verticalTitle, List<String> labels, List<Series<Double>> series) {
        if (labels != null && !labels.isEmpty()) {
            chart.setXaxis(XAxisBuilder.get().withCategories(labels).build());
        }
        if (series != null && !series.isEmpty()) {
            chart.updateSeries(series.toArray(new Series[series.size()]));
        }
        chart.setYaxis(YAxisBuilder.get().withTitle(TitleBuilder.get().withText(verticalTitle).build()).build());
        int width = labels.size() * 30;
        chart.setWidth(width + "px");
    }
}
