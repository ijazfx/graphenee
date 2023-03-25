package io.graphenee.vaadin.flow.component;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class GxCopyToClipboardWrapper<T extends AbstractField<?, ?>> extends HorizontalLayout {

	private static final long serialVersionUID = 1L;

	public GxCopyToClipboardWrapper(T wrappedComponent) {
		addAndExpand(wrappedComponent);
		add(new GxCopyToClipboardButton(() -> {
			Object value = wrappedComponent.getValue();
			if (value != null)
				return value.toString();
			return null;
		}));
		setAlignItems(Alignment.BASELINE);
	}

}
