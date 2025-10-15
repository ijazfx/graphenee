package io.graphenee.vaadin.flow.component;

import java.util.Collection;
import java.util.LinkedList;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class GxStackLayout extends VerticalLayout {

	LinkedList<Component> stack = new LinkedList<>();

	public GxStackLayout() {
		setSizeFull();
		addClassName("gx-stack-layout");
		setMargin(false);
		setSpacing(false);
		setPadding(false);
	}

	@Override
	public void add(Collection<Component> components) {
		if (components.size() > 1) {
			throw new IllegalArgumentException("Adding collection of components is not allowed because only one component at a time can be visible.");
		}
		if (getComponentCount() > 0) {
			stack.push(getComponentAt(0));
			removeAll();
		}
		super.add(components);
	}

	@Override
	public void remove(Collection<Component> components) {
		stack.removeAll(components);
		if (getComponentCount() > 0 && components.contains(getComponentAt(0))) {
			removeAll();
		}
		if (!stack.isEmpty()) {
			Component c = stack.removeLast();
			super.add(c);
		}

	}

}
