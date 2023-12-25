package io.graphenee.vaadin.flow.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GridPreferences implements Serializable {

	private static final long serialVersionUID = 1L;

	String gridName;
	List<ColumnPreferences> columns = new ArrayList<>();

	public GridPreferences() {
	}

	public static GridPreferences newInstance(String gridName) {
		GridPreferences c = new GridPreferences();
		c.setGridName(gridName);
		return c;
	}

	void removeColumn(String columnName) {
		columns.removeIf(c -> c.getColumnName().equals(columnName));
	}

	public ColumnPreferences addColumn(String columnName) {
		Optional<ColumnPreferences> column = columns.stream().filter(c -> c.getColumnName().equals(columnName)).findFirst();
		if (column.isPresent())
			return column.get();
		ColumnPreferences p = ColumnPreferences.newInstance(columnName);
		columns.add(p);
		return p;
	}

	public List<ColumnPreferences> visibleColumns() {
		return columns.stream().filter(c -> c.getVisible()).sorted((a, b) -> a.getSortOrder() < b.getSortOrder() ? -1 : 1).collect(Collectors.toList());
	}

}