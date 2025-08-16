package io.graphenee.vaadin.flow.component;

import java.util.List;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.formlayout.FormLayout;

@SuppressWarnings("serial")
public class GxFormLayout extends FormLayout {

	public GxFormLayout() {
		this(5);
	}

	public GxFormLayout(Integer maxCols) {
		addClassName("gx-form-layout");
		new ResponsiveStep("10rem", maxCols);
	}

	public void expand(Component... c) {
		List.of(c).forEach(comp -> {
			setColspan(comp, Integer.MAX_VALUE);
		});
	}

	protected void shrink(Component... c) {
		List.of(c).forEach(comp -> {
			setColspan(comp, 1);
		});
	}

}