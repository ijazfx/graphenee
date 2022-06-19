package io.graphenee.vaadin.flow.base;

import java.util.Date;

import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.builder.ChartBuilder;
import com.github.appreciated.apexcharts.config.builder.DataLabelsBuilder;
import com.github.appreciated.apexcharts.config.builder.FillBuilder;
import com.github.appreciated.apexcharts.config.builder.GridBuilder;
import com.github.appreciated.apexcharts.config.builder.LegendBuilder;
import com.github.appreciated.apexcharts.config.builder.PlotOptionsBuilder;
import com.github.appreciated.apexcharts.config.builder.XAxisBuilder;
import com.github.appreciated.apexcharts.config.builder.YAxisBuilder;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.chart.builder.AnimationsBuilder;
import com.github.appreciated.apexcharts.config.legend.HorizontalAlign;
import com.github.appreciated.apexcharts.config.legend.Position;
import com.github.appreciated.apexcharts.config.plotoptions.builder.BarBuilder;
import com.github.appreciated.apexcharts.config.xaxis.XAxisType;
import com.github.appreciated.apexcharts.config.xaxis.builder.LabelsBuilder;
import com.github.appreciated.apexcharts.config.yaxis.Align;
import com.github.appreciated.apexcharts.helper.Series;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;

public class GxTimelineChart extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private ApexCharts chart;

	public GxTimelineChart() {
		chart = ApexChartsBuilder.get().withColors("#B7BFC6", "#0ED374")
				.withChart(ChartBuilder.get().withAnimations(AnimationsBuilder.get().withEnabled(false).build()).withType(Type.rangeBar).build())
				.withGrid(GridBuilder.get().withShow(true).withStrokeDashArray(5.0).build())
				.withPlotOptions(PlotOptionsBuilder.get().withBar(BarBuilder.get().withHorizontal(true).withRangeBarGroupRows(true).withBarHeight("80%").build()).build())
				.withDataLabels(DataLabelsBuilder.get().withEnabled(false).build()).withFill(FillBuilder.get().withOpacity(1.0).build())
				.withYaxis(YAxisBuilder.get()
						.withLabels(com.github.appreciated.apexcharts.config.yaxis.builder.LabelsBuilder.get().withOffsetX(-50.0).withMinWidth(100.0).withMaxWidth(400.0)
								.withAlign(Align.left).build())
						.build())
				.withXaxis(XAxisBuilder.get().withType(XAxisType.datetime).withPosition("top").withLabels(LabelsBuilder.get().withFormat("dd.MM.yyyy").build()).build())
				.withLegend(LegendBuilder.get().withShow(true).withPosition(Position.top).withHorizontalAlign(HorizontalAlign.left).build()).build();
		add(chart);
		setWidthFull();
	}

	public void initializeWithLabelsAndSeries(TimelineSeries... series) {
		int size = 0;
		for (int i = 0; i < series.length; i++) {
			size += series[i].getData().length;
		}
		size = size * 5 + 200;
		chart.setHeight(size + "px");
		chart.updateSeries(series);
	}

	@EqualsAndHashCode(onlyExplicitlyIncluded = true)
	public static class TimelineSeries extends Series<TimelineData> {

		@Include
		private String label;

		public TimelineSeries(String name) {
			setName(name);
			this.label = name;
			// setType(SeriesType.column);
		}

		public void addData(TimelineData... data) {
			setData(data);
		}

	}

	@EqualsAndHashCode(onlyExplicitlyIncluded = true)
	public static class TimelineData {

		@Include
		private String label;

		@Include
		private Date startDate;

		@Include
		private Date endDate;

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
