package io.graphenee.vaadin.flow.component;

/**
 * An enum that represents the variants of a dialog.
 */
public enum DialogVariant {

	/**
	 * A dialog with no padding.
	 */
	NO_PADDING("no-padding");

	private final String variant;

	DialogVariant(String variant) {
		this.variant = variant;
	}

	/**
	 * Gets the variant name.
	 * @return The variant name.
	 */
	public String getVariantName() {
		return variant;
	}

}
