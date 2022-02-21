package io.graphenee.vaadin.flow.component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.stream.IntStream;

import com.vaadin.flow.component.combobox.ComboBox;

import io.graphenee.util.TRCalendarUtil;
import io.graphenee.vaadin.flow.component.GxWeekPicker.WeekRange;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.Getter;

/**
 * GxWeekPicker
 */
public class GxWeekPicker extends ComboBox<WeekRange> {
    private static final long serialVersionUID = 1L;

    public GxWeekPicker() {
        setLabel("Week Picker");
        setItems(IntStream.rangeClosed(1, 52).mapToObj(i -> new WeekRange(i)));
    }

    @Getter
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    public static class WeekRange {

        @Include
        private Integer weekNumber;

        private Date startDate;
        private Date endDate;

        public WeekRange(Integer weekNumber) {
            this.weekNumber = weekNumber;
            LocalDate localDate = LocalDate.now().with(IsoFields.WEEK_OF_WEEK_BASED_YEAR, weekNumber).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            Date date = TRCalendarUtil.toDateFromLocalDate(localDate);
            this.startDate = TRCalendarUtil.startOfWeek(date);
            this.endDate = TRCalendarUtil.endOfWeek(date);
        }

        @Override
        public String toString() {
            return getWeekNumber().toString();
        }

    }
}