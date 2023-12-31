package io.graphenee.vaadin.flow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.Route;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GxMenuItem implements Serializable {

	private static final long serialVersionUID = 1L;

	private String label;

	private Component icon;
	private String route;
	private Class<? extends Component> componentClass;

	private List<GxMenuItem> children;

	public GxMenuItem(String label, Component icon, String route) {
		this.label = label;
		this.icon = icon;
		this.route = route;
	}

	public GxMenuItem(String label, Component icon, Class<? extends Component> componentClass) {
		this.label = label;
		this.icon = icon;
		this.componentClass = componentClass;
		this.route = determineRoute(componentClass);
	}

	public GxMenuItem(String label, Component icon) {
		this.label = label;
		this.icon = icon;
	}

	public GxMenuItem(String label, String route) {
		this.label = label;
		this.route = route;
	}

	public GxMenuItem(String label) {
		this.label = label;
	}

	public GxMenuItem(String label, Class<? extends Component> componentClass) {
		this.label = label;
		this.componentClass = componentClass;
		this.icon = VaadinIcon.CHEVRON_RIGHT.create();
		this.route = determineRoute(componentClass);
	}

	public GxMenuItem add(GxMenuItem child) {
		if (children == null) {
			children = new ArrayList<>();
		}
		children.add(child);
		return this;
	}

	public boolean hasChildren() {
		return children != null && !children.isEmpty();
	}

	public static GxMenuItem create(String label, String route) {
		GxMenuItem mi = new GxMenuItem(label, route);
		return mi;
	}

	public static GxMenuItem create(String label, Component icon, String route) {
		GxMenuItem mi = new GxMenuItem(label, icon, route);
		return mi;
	}

	public static GxMenuItem create(String label, Component icon, Class<? extends Component> componentClass) {
		GxMenuItem mi = new GxMenuItem(label, icon, componentClass);
		return mi;
	}

	public static GxMenuItem create(String label, Class<? extends Component> componentClass) {
		GxMenuItem mi = new GxMenuItem(label, componentClass);
		return mi;
	}

	public static GxMenuItem create(String label, Component icon) {
		GxMenuItem mi = new GxMenuItem(label, icon);
		return mi;
	}

	public static GxMenuItem create(String label) {
		GxMenuItem mi = new GxMenuItem(label);
		return mi;
	}

	private String determineRoute(Class<? extends Component> navigationTarget) {
		if (navigationTarget.isAnnotationPresent(Route.class)) {
			Route annotation = navigationTarget.getAnnotation(Route.class);
			return annotation.value();
		}
		if (navigationTarget.isAnnotationPresent(GxSecuredView.class)) {
			GxSecuredView annotation = navigationTarget.getAnnotation(GxSecuredView.class);
			return annotation.value();
		}
		return null;
	}

}
