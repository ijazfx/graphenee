package io.graphenee.vaadin.flow.component;

public enum DialogVariant {

	NO_PADDING("no-padding");

	private final String variant;

	DialogVariant(String variant) {
		this.variant = variant;
	}

	public String getVariantName() {
		return variant;
	}

}
