package io.graphenee.vaadin.flow.base;

import java.io.Serializable;

import com.vaadin.flow.component.Component;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GxTabItem implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer index;
	private String label;
	private Component component;

	public GxTabItem(Integer index, String label, Component component) {
		this.index = index;
		this.label = label;
		this.component = component;
	}

	public static GxTabItem create(Integer index, String label, Component component) {
		return new GxTabItem(index, label, component);
	}
}
