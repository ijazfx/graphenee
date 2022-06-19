package io.graphenee.vaadin.flow.base;

import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.builder.ChartBuilder;
import com.github.appreciated.apexcharts.config.builder.LegendBuilder;
import com.github.appreciated.apexcharts.config.builder.ResponsiveBuilder;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.legend.Position;
import com.github.appreciated.apexcharts.config.responsive.builder.OptionsBuilder;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class GxPieChart extends VerticalLayout {
	private static final long serialVersionUID = 1L;

	private ApexCharts chart;
	private String width = "100%";

	public void initializeWithLabelsAndSeries(String[] labels, Double[] series) {
		chart = ApexChartsBuilder.get().withChart(ChartBuilder.get().withType(Type.pie).build()).withLegend(LegendBuilder.get().withPosition(Position.right).build())
				.withResponsive(ResponsiveBuilder.get().withBreakpoint(480.0)
						.withOptions(OptionsBuilder.get().withLegend(LegendBuilder.get().withPosition(Position.bottom).build()).build()).build())
				.withLabels(labels).withSeries(series).build();

		removeAll();
		add(chart);
		chart.setWidth(width);
	}
}
