package io.graphenee.vaadin.flow.base;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.formlayout.FormLayout;

@SuppressWarnings("serial")
public class GxFormLayout extends FormLayout {

	public GxFormLayout() {
		super();
		configure();
	}

	public GxFormLayout(Component... components) {
		super(components);
		configure();
	}

	private void configure() {
		addClassName("gx-form-layout");
		getStyle().setPadding("0.75rem");
		setResponsiveSteps(new ResponsiveStep("100px", 5));
	}

	public void expand(Component component) {
		setColspan(component, Integer.MAX_VALUE);
	}

	public void shrink(Component component) {
		setColspan(component, 1);
	}

}