package io.graphenee.vaadin.flow.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * A class that represents the preferences for a column in a grid.
 */
@Getter
@Setter
public class ColumnPreferences implements Serializable {

	private static final long serialVersionUID = 1L;

	String columnName;
	Boolean visible = true;
	Integer sortOrder = Integer.MAX_VALUE;
	String label;
	String width = "auto";

	/**
	 * Creates a new instance of this class.
	 */
	public ColumnPreferences() {
	}

	/**
	 * Creates a new instance of this class.
	 * @param columnName The name of the column.
	 * @return The new instance.
	 */
	public static ColumnPreferences newInstance(String columnName) {
		ColumnPreferences c = new ColumnPreferences();
		c.setColumnName(columnName);
		return c;
	}

	/**
	 * Sets the visibility of the column.
	 * @param visible The visibility of the column.
	 * @return This instance.
	 */
	public ColumnPreferences withVisible(Boolean visible) {
		setVisible(visible);
		return this;
	}

	/**
	 * Sets the sort order of the column.
	 * @param sortOrder The sort order of the column.
	 * @return This instance.
	 */
	public ColumnPreferences withSortOrder(Integer sortOrder) {
		setSortOrder(sortOrder);
		return this;
	}

	/**
	 * Sets the label of the column.
	 * @param label The label of the column.
	 * @return This instance.
	 */
	public ColumnPreferences withLabel(String label) {
		setLabel(label);
		return this;
	}

	/**
	 * Sets the width of the column.
	 * @param width The width of the column.
	 * @return This instance.
	 */
	public ColumnPreferences withWidth(String width) {
		setWidth(width);
		return this;
	}

}