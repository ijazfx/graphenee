package io.graphenee.vaadin.flow.base;

import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.builder.ChartBuilder;
import com.github.appreciated.apexcharts.config.builder.DataLabelsBuilder;
import com.github.appreciated.apexcharts.config.builder.LegendBuilder;
import com.github.appreciated.apexcharts.config.builder.StrokeBuilder;
import com.github.appreciated.apexcharts.config.builder.TitleSubtitleBuilder;
import com.github.appreciated.apexcharts.config.builder.XAxisBuilder;
import com.github.appreciated.apexcharts.config.builder.YAxisBuilder;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.chart.builder.ZoomBuilder;
import com.github.appreciated.apexcharts.config.legend.HorizontalAlign;
import com.github.appreciated.apexcharts.config.stroke.Curve;
import com.github.appreciated.apexcharts.config.subtitle.Align;
import com.github.appreciated.apexcharts.config.xaxis.XAxisType;
import com.github.appreciated.apexcharts.helper.Series;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class GxAreaChart extends VerticalLayout {

	private static final long serialVersionUID = 1L;
	private ApexCharts areaChart;

	public void initializeWithLabelsAndSeries(String[] xAxisLabel, Series<?>... series) {
		areaChart = ApexChartsBuilder.get().withChart(ChartBuilder.get().withType(Type.AREA).withZoom(ZoomBuilder.get().withEnabled(false).build()).build())
				.withDataLabels(DataLabelsBuilder.get().withEnabled(false).build()).withStroke(StrokeBuilder.get().withCurve(Curve.STRAIGHT).build()).withSeries(series)
				.withSubtitle(TitleSubtitleBuilder.get().withAlign(Align.LEFT).build()).withLabels(xAxisLabel).withXaxis(XAxisBuilder.get().withType(XAxisType.CATEGORIES).build())
				.withYaxis(YAxisBuilder.get().withOpposite(true).build()).withLegend(LegendBuilder.get().withHorizontalAlign(HorizontalAlign.LEFT).build()).build();
		removeAll();
		add(areaChart);
		areaChart.setWidth("100%");
	}
}
