package io.graphenee.vaadin.flow.component;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.formlayout.FormLayout;

@SuppressWarnings("serial")
public class GxFormLayout extends FormLayout {

	int maxCols = 5;

	public GxFormLayout() {
		this.maxCols = 2;
		configure();
	}

	public GxFormLayout(Integer maxCols) {
		this.maxCols = maxCols;
		configure();
	}

	private void configure() {
		addClassName("gx-form-layout");
		List<ResponsiveStep> steps = new ArrayList<>();
		for (int i = 0; i < maxCols; i++) {
			int minWidth = 100 * (i / 10);
			steps.add(new ResponsiveStep(minWidth + "px", i));
		}
		setResponsiveSteps(steps);
	}

	public void expand(Component component, int cols) {
		if (cols < 1)
			setColspan(component, 1);
		else if (cols > maxCols)
			setColspan(component, maxCols);
		else
			setColspan(component, cols);
	}

	public void expand(Component component) {
		expand(component, maxCols);
	}

	public void shrink(Component component) {
		setColspan(component, 1);
	}

}