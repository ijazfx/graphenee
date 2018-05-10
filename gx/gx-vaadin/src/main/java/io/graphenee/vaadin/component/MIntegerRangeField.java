package io.graphenee.vaadin.component;

import java.util.Arrays;

import com.vaadin.ui.ComboBox;

@SuppressWarnings("serial")
public class MIntegerRangeField extends ComboBox {

	public MIntegerRangeField(String caption, Integer from, Integer to, Integer stepSize, Integer... startWith) {
		super(caption);
		if (startWith != null) {
			addItems(Arrays.asList(startWith));
		}
		for (int i = from; i <= to; i += stepSize) {
			addItem(i);
		}
	}

	public MIntegerRangeField(String caption, Integer from, Integer to, Integer stepSize) {
		super(caption);
		for (int i = from; i <= to; i += stepSize) {
			addItem(i);
		}
	}

}
