package io.graphenee.vaadin.flow.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.YAxis;
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
import com.github.appreciated.apexcharts.config.xaxis.builder.LabelsBuilder;
import com.github.appreciated.apexcharts.config.yaxis.builder.TitleBuilder;
import com.github.appreciated.apexcharts.helper.Series;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import io.graphenee.util.TRCalendarUtil;

public class GxStackedBarChart extends VerticalLayout {

	private static final long serialVersionUID = 1L;
	private ApexCharts chart;

	public GxStackedBarChart() {
		String[] colors = new String[] { "#F3B415", "#F27036", "#4E88B4", "#46AF78", "#A93F55", "#2176FF", "#33A1FD", "#BAEE29" };
		chart = ApexChartsBuilder.get().withColors(colors).withFill(FillBuilder.get().withColors(Arrays.asList(colors)).build())
				.withChart(ChartBuilder.get().withType(Type.BAR).withStacked(true).withStackType(StackType.NORMAL).build())
				.withPlotOptions(PlotOptionsBuilder.get().withBar(BarBuilder.get().withHorizontal(false).withColumnWidth("80%").build()).build())
				.withDataLabels(DataLabelsBuilder.get().withEnabled(false).build())
				.withLegend(LegendBuilder.get().withShow(false).withPosition(Position.TOP).withHorizontalAlign(HorizontalAlign.LEFT).build()).build();
		add(chart);
		chart.setHeight("200px");
		setWidthFull();
	}

	public void initializeWithTitleAndMaxYAndSeries(String verticalTitle, Double maxY, Date fromDate, Date toDate, List<Series<Double>> series) {
		List<String> dateList = new ArrayList<>();
		for (Date date = fromDate; date.before(toDate); date = TRCalendarUtil.addDaysToDate(date, 1)) {
			dateList.add(TRCalendarUtil.getFormattedDate(date, "dd.MM"));
		}
		if (series != null && !series.isEmpty()) {
			chart.updateSeries(series.toArray(new Series[series.size()]));
			chart.setXaxis(XAxisBuilder.get().withLabels(LabelsBuilder.get().withRotate(-90.0).build()).withCategories(dateList).build());
		}
		chart.setYaxis(new YAxis[] { YAxisBuilder.get().withMax(maxY).withTitle(TitleBuilder.get().withText(verticalTitle).build()).build() });
	}
}
