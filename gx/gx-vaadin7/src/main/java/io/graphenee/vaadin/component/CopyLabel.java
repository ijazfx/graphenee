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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import com.vaadin.annotations.JavaScript;
import com.vaadin.data.util.converter.Converter.ConversionException;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Image;

import io.graphenee.gx.theme.graphenee.GrapheneeTheme;

@JavaScript({ "copy-label.js" })
public class CopyLabel extends CustomField<String> {

	public static final Logger L = LoggerFactory.getLogger(CopyLabel.class);

	private static final long serialVersionUID = 1L;

	private Image copyText;
	private MLabel textLabel;

	public CopyLabel() {
		this(null);
	}

	public CopyLabel(String caption) {
		textLabel = new MLabel();
		setCaption(caption);
	}

	public CopyLabel(String caption, String value) {
		setCaption(caption);
		textLabel = new MLabel(value);
	}

	@Override
	public void setValue(String newFieldValue) throws ReadOnlyException, ConversionException {
		textLabel.setValue(newFieldValue);
	}

	@Override
	protected Component initContent() {
		MHorizontalLayout layout = new MHorizontalLayout().withDefaultComponentAlignment(Alignment.TOP_LEFT).withWidthUndefined();
		copyText = new Image();
		copyText.setHeight("12px");
		copyText.setSource(GrapheneeTheme.COPY);
		copyText.setDescription("copy to clipboard");
		copyText.addClickListener(event -> {
			String value = textLabel.getValue();
			String statement = String.format("setClipboardText('%s')", value);
			com.vaadin.ui.JavaScript current = com.vaadin.ui.JavaScript.getCurrent();
			current.execute(statement);
			copyText.setStyleName(GrapheneeTheme.STYLE_ELEVATED);
			copyText.setDescription("copied");
		});

		layout.addComponents(textLabel, copyText);
		return layout;
	}

	@Override
	public void setReadOnly(boolean readOnly) {

	}

	@Override
	public Class<? extends String> getType() {
		return String.class;
	}

}
