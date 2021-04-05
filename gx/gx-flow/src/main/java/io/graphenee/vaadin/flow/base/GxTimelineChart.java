package io.graphenee.vaadin.flow.base;

import java.util.Date;
import java.util.List;

import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.builder.ChartBuilder;
import com.github.appreciated.apexcharts.config.builder.DataLabelsBuilder;
import com.github.appreciated.apexcharts.config.builder.GridBuilder;
import com.github.appreciated.apexcharts.config.builder.LegendBuilder;
import com.github.appreciated.apexcharts.config.builder.PlotOptionsBuilder;
import com.github.appreciated.apexcharts.config.builder.XAxisBuilder;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.legend.HorizontalAlign;
import com.github.appreciated.apexcharts.config.legend.Position;
import com.github.appreciated.apexcharts.config.plotoptions.builder.BarBuilder;
import com.github.appreciated.apexcharts.config.xaxis.XAxisType;
import com.github.appreciated.apexcharts.config.xaxis.builder.LabelsBuilder;
import com.github.appreciated.apexcharts.helper.Series;
import com.vaadin.flow.component.html.Div;

public class GxTimelineChart extends Div {

    private static final long serialVersionUID = 1L;

    private ApexCharts chart;

    public GxTimelineChart() {
        chart = ApexChartsBuilder.get()//.withColors("#00ACC1", "#ECEFF1").withFill(FillBuilder.get().withColors(Arrays.asList("#00ACC1", "#ECEFF1")).build())
                .withChart(ChartBuilder.get().withType(Type.rangeBar).build()).withGrid(GridBuilder.get().withShow(true).withStrokeDashArray(5.0).build())
                .withPlotOptions(PlotOptionsBuilder.get().withBar(BarBuilder.get().withDistributed(false).withHorizontal(true).withBarHeight("100%").build()).build())
                .withDataLabels(DataLabelsBuilder.get().withEnabled(false).build())
                .withXaxis(XAxisBuilder.get().withType(XAxisType.datetime).withPosition("top").withLabels(LabelsBuilder.get().withFormat("d/M").build()).build())
                .withLegend(LegendBuilder.get().withShow(true).withPosition(Position.top).withHorizontalAlign(HorizontalAlign.left).build()).build();
        add(chart);
        chart.setHeight("600px");
    }

    public void initializeWithLabelsAndSeries(List<String> labels, List<TimelineSeries> series) {
        if (series != null && !series.isEmpty()) {
            chart.updateSeries(series.toArray(new Series[series.size()]));
        }
        int width = labels.size() * 20;
        chart.setWidth(width + "px");
    }

    public static class TimelineSeries extends Series<TimelineData> {

        public TimelineSeries(String name) {
            setName(name);
            // setType(SeriesType.column);
        }

        public void addData(TimelineData... data) {
            setData(data);
        }

    }

    public static class TimelineData {

        private String label;
        private Date startDate, endDate;

        public TimelineData(String label, Date startDate, Date endDate) {
            this.label = label;
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public String getX() {
            return label;
        }

        public Long[] getY() {
            return new Long[] { startDate.getTime(), endDate.getTime() };
        }

    }

}
