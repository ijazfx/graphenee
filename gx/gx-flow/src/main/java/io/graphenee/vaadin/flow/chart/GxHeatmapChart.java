package io.graphenee.vaadin.flow.chart;

import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.builder.ChartBuilder;
import com.github.appreciated.apexcharts.config.builder.DataLabelsBuilder;
import com.github.appreciated.apexcharts.config.builder.XAxisBuilder;
import com.github.appreciated.apexcharts.config.builder.YAxisBuilder;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.xaxis.XAxisType;
import com.github.appreciated.apexcharts.helper.Series;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class GxHeatmapChart extends VerticalLayout {

	private static final long serialVersionUID = 1L;
	private ApexCharts heatChart;

	public void initializeWithLabelsAndSeries(String[] xAxisLabel, Series<?>... series) {
		heatChart = ApexChartsBuilder.get().withChart(ChartBuilder.get().withType(Type.HEATMAP).build()).withDataLabels(DataLabelsBuilder.get().withEnabled(false).build())
				.withColors("#008FFB").withSeries(series).withXaxis(XAxisBuilder.get().withType(XAxisType.CATEGORIES).withCategories(xAxisLabel).build())
				.withYaxis(YAxisBuilder.get().withMax(70.0).build()).build();
		removeAll();
		add(heatChart);
		heatChart.setWidth("100%");
	}
}
