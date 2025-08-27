package io.graphenee.vaadin.flow.component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.FlexLayout.FlexWrap;

@SuppressWarnings("serial")
public class GxFormLayout extends FormLayout {

	private Map<String, FlexLayout> groupedMap = new ConcurrentHashMap<>();

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

	public void expand(String... name) {
		List.of(name).forEach(n -> {
			FlexLayout grouped = groupedMap.get(n);
			if(grouped != null) {
				expand(grouped);
			}
		});
	}

	protected void shrink(String... name) {
		List.of(name).forEach(n -> {
			FlexLayout grouped = groupedMap.get(n);
			if(grouped != null) {
				shrink(grouped);
			}
		});
	}

	public void shrink(Component... c) {
		List.of(c).forEach(comp -> {
			setColspan(comp, 1);
		});
	}

	public FlexLayout group(String name, Component... c) {
		if(groupedMap.containsKey(name))
			throw new IllegalArgumentException(name + " is already assigned to another group.");
		FlexLayout grouped = new FlexLayout(c);
		groupedMap.put(name, grouped);
		grouped.setWidthFull();
		grouped.setFlexWrap(FlexWrap.WRAP);
		grouped.addClassName("gx-layout-grouped");
		return grouped;
	}

	public List<Component> ungroup(String name) {
		FlexLayout grouped = groupedMap.get(name);
		if(grouped != null) {
			groupedMap.remove(name);
			List<Component> clist = new ArrayList<>();
			clist.addAll(grouped.getChildren().toList());
			grouped.removeFromParent();
			return clist;
		}
		return Collections.emptyList();
	}

}