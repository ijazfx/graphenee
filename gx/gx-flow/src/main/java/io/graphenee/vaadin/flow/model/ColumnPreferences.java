package io.graphenee.vaadin.flow.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ColumnPreferences implements Serializable {

	private static final long serialVersionUID = 1L;

	String columnName;
	Boolean visible = true;
	Integer sortOrder = Integer.MAX_VALUE;
	String label;
	String width = "auto";

	public ColumnPreferences() {
	}

	public static ColumnPreferences newInstance(String columnName) {
		ColumnPreferences c = new ColumnPreferences();
		c.setColumnName(columnName);
		return c;
	}

	public ColumnPreferences withVisible(Boolean visible) {
		setVisible(visible);
		return this;
	}

	public ColumnPreferences withSortOrder(Integer sortOrder) {
		setSortOrder(sortOrder);
		return this;
	}

	public ColumnPreferences withLabel(String label) {
		setLabel(label);
		return this;
	}

	public ColumnPreferences withWidth(String width) {
		setWidth(width);
		return this;
	}

}