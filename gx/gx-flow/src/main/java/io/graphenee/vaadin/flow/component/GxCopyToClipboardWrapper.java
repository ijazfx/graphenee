package io.graphenee.vaadin.flow.component;

import com.google.common.base.Strings;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class GxCopyToClipboardWrapper extends HorizontalLayout {

	private static final long serialVersionUID = 1L;

	public GxCopyToClipboardWrapper(Component c) {
		addClassName("gx-c2c-wrapper");
		final Object value;
		if (c instanceof HasValue<?, ?>) {
			value = ((HasValue<?, ?>) c).getValue();
		} else if (c instanceof HasText) {
			value = ((HasText) c).getText();
		} else {
			value = null;
		}
		setSpacing(false);
		setMargin(false);
		setPadding(false);
		if (value != null && !Strings.isNullOrEmpty(value.toString())) {
			GxCopyToClipboardButton b = new GxCopyToClipboardButton(() -> {
				return value.toString();
			});
			b.addClassName("gx-c2c-button");
			add(b);
		}
		addAndExpand(c);
		setAlignItems(Alignment.BASELINE);
	}

}
