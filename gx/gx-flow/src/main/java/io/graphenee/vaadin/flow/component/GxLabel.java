package io.graphenee.vaadin.flow.component;

import com.vaadin.flow.component.html.NativeLabel;

public class GxLabel {

	public static NativeLabel infoLabel(String text) {
		NativeLabel label = new NativeLabel(text);
		label.getStyle().set("color", "var(--lumo-primary-color)");
		label.addClassName("gx-label");
		label.addClassName("gx-label-info");
		return label;
	}

	public static NativeLabel warningLabel(String text) {
		NativeLabel label = new NativeLabel(text);
		label.getStyle().set("color", "var(--lumo-error-color)");
		label.addClassName("gx-label");
		label.addClassName("gx-label-warning");
		return label;
	}

	public static NativeLabel successLabel(String text) {
		NativeLabel label = new NativeLabel(text);
		label.getStyle().set("color", "var(--lumo-success-color)");
		label.addClassName("gx-label");
		label.addClassName("gx-label-success");
		return label;
	}

}
