/*******************************************************************************
 * Copyright (c) 2016, 2018 Farrukh Ijaz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package io.graphenee.vaadin.component;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.vaadin.viritin.label.MLabel;

import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class MTimeField extends CustomField<Timestamp> {

	private static final String TIME_SEPARATOR = "&nbsp;:&nbsp;";
	int minuteStepSize = 1;
	int secondStepSize = 1;
	private Resolution resolution = Resolution.SECOND;
	private ComboBox hourComboBox;
	private ComboBox minuteComboBox;
	private ComboBox secondComboBox;

	public MTimeField() {
	}

	public MTimeField(String caption) {
		setCaption(caption);
	}

	@Override
	protected Component initContent() {
		CssLayout layout = new CssLayout();
		layout.setCaption(getCaption());
		layout.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

		hourComboBox = new ComboBox() {
			public String getItemCaption(Object itemId) {
				return String.format("%02d", itemId);
			};
		};
		hourComboBox.setWidth("60px");
		hourComboBox.setInputPrompt("hh");
		hourComboBox.setStyleName(ValoTheme.COMBOBOX_SMALL);
		hourComboBox.setStyleName(ValoTheme.COMBOBOX_ALIGN_CENTER);
		hourComboBox.addValueChangeListener(event -> {
			if (getValue() != null) {
				Calendar gcal = GregorianCalendar.getInstance();
				gcal.setTime(getValue());
				Integer hour = (Integer) hourComboBox.getValue();
				if (hour == null) {
					gcal.set(Calendar.HOUR_OF_DAY, 0);
				} else {
					gcal.set(Calendar.HOUR_OF_DAY, hour);
				}
				setValue(new Timestamp(gcal.getTime().getTime()));
			}
		});
		minuteComboBox = new ComboBox() {
			public String getItemCaption(Object itemId) {
				return String.format("%02d", itemId);
			};
		};
		minuteComboBox.setWidth("60px");
		minuteComboBox.setInputPrompt("mm");
		minuteComboBox.setStyleName(ValoTheme.COMBOBOX_SMALL);
		minuteComboBox.setStyleName(ValoTheme.COMBOBOX_ALIGN_CENTER);
		minuteComboBox.addValueChangeListener(event -> {
			if (getValue() != null) {
				Calendar gcal = GregorianCalendar.getInstance();
				gcal.setTime(getValue());
				Integer hour = (Integer) minuteComboBox.getValue();
				if (hour == null) {
					gcal.set(Calendar.MINUTE, 0);
				} else {
					gcal.set(Calendar.MINUTE, hour);
				}
				setValue(new Timestamp(gcal.getTime().getTime()));
			}
		});
		secondComboBox = new ComboBox() {
			public String getItemCaption(Object itemId) {
				return String.format("%02d", itemId);
			};
		};
		secondComboBox.setWidth("60px");
		secondComboBox.setInputPrompt("ss");
		secondComboBox.setStyleName(ValoTheme.COMBOBOX_SMALL);
		secondComboBox.setStyleName(ValoTheme.COMBOBOX_ALIGN_CENTER);
		secondComboBox.addValueChangeListener(event -> {
			if (getValue() != null) {
				Calendar gcal = GregorianCalendar.getInstance();
				gcal.setTime(getValue());
				Integer hour = (Integer) secondComboBox.getValue();
				if (hour == null) {
					gcal.set(Calendar.SECOND, 0);
				} else {
					gcal.set(Calendar.SECOND, hour);
				}
				setValue(new Timestamp(gcal.getTime().getTime()));
			}
		});
		for (int i = 0; i < 24; i++) {
			hourComboBox.addItem(i);
		}

		for (int i = 0; i < 60; i += minuteStepSize) {
			minuteComboBox.addItem(i);
		}

		for (int i = 0; i < 60; i += secondStepSize) {
			secondComboBox.addItem(i);
		}

		layout.addComponent(hourComboBox);
		if (resolution == Resolution.MINUTE || resolution == Resolution.SECOND) {
			MLabel label = new MLabel(TIME_SEPARATOR).withWidthUndefined().withContentMode(ContentMode.HTML).withStyleName(ValoTheme.LABEL_BOLD);
			layout.addComponents(label, minuteComboBox);
		}
		if (resolution == Resolution.SECOND) {
			MLabel label = new MLabel(TIME_SEPARATOR).withWidthUndefined().withContentMode(ContentMode.HTML).withStyleName(ValoTheme.LABEL_BOLD);
			layout.addComponents(label, secondComboBox);
		}

		addValueChangeListener(event -> {
			if (getValue() == null) {
				hourComboBox.setValue(null);
				minuteComboBox.setValue(null);
				secondComboBox.setValue(null);
			} else {
				Calendar gcal = GregorianCalendar.getInstance();
				gcal.setTime(getValue());
				hourComboBox.setValue(gcal.get(Calendar.HOUR_OF_DAY));
				int minute = gcal.get(Calendar.MINUTE);
				minute = (minute / minuteStepSize) * minuteStepSize;
				minuteComboBox.setValue(minute);
				int second = gcal.get(Calendar.SECOND);
				second = (second / secondStepSize) * secondStepSize;
				secondComboBox.setValue(second);
			}
		});

		fireValueChange(true);

		return layout;
	}

	@Override
	public Class<? extends Timestamp> getType() {
		return Timestamp.class;
	}

	public MTimeField withCaption(String caption) {
		setCaption(caption);
		return this;
	}

	public MTimeField withRequired(boolean required) {
		setRequired(required);
		return this;
	}

	public MTimeField withMinuteStepSize(int stepSize) {
		this.minuteStepSize = stepSize;
		return this;
	}

	public MTimeField withSecondStepSize(int stepSize) {
		this.secondStepSize = stepSize;
		return this;
	}

	public MTimeField withResolution(Resolution resolution) {
		this.resolution = resolution;
		return this;
	}

}
