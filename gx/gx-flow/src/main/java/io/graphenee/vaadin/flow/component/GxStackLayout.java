package io.graphenee.vaadin.flow.component;

import java.util.LinkedList;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@SuppressWarnings("serial")
public class GxStackLayout extends VerticalLayout {

	LinkedList<Component> stack = new LinkedList<>();

	public GxStackLayout() {
		setSizeFull();
		setMargin(false);
		setSpacing(false);
		setPadding(false);
	}

	/*
	 * 1. check if there is any content
	 * if yes then push that content to stack and add new content
	 * if no then add new content
	 */

	public void push(Component c) {
		if (getComponentCount() > 0) {
			stack.push(getComponentAt(0));
			removeAll();
		}
		add(c);
		setVisible(true);
	}

	/*
	 * 1. check if stack is empty
	 * if yes, do nothing
	 * if no, then pop from stack and display new content
	 */
	public void pop() {
		try {
			Component c = stack.pop();
			removeAll();
			add(c);
			setVisible(true);
		} catch (Exception ex) {
			removeAll();
			setVisible(false);
		}
	}

	public boolean isEmpty() {
		return getComponentCount() == 0;
	}

	public Component top() {
		return isEmpty() ? null : getComponentAt(0);
	}

}
