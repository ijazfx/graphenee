package io.graphenee.vaadin.flow.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class GxCopyToClipboardWrapper extends HorizontalLayout {

	private static final long serialVersionUID = 1L;

	public GxCopyToClipboardWrapper(Component c) {
		setSpacing(false);
		add(new GxCopyToClipboardButton(() -> {
			Object value = null;
			if (c instanceof HasValue<?, ?>) {
				value = ((HasValue<?, ?>) c).getValue();
			} else if (c instanceof HasText) {
				value = ((HasText) c).getText();
			}
			if (value != null)
				return value.toString();
			return null;
		}));
		addAndExpand(c);
		setAlignItems(Alignment.BASELINE);
	}

}
