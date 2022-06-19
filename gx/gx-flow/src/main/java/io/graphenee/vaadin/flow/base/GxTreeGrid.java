package io.graphenee.vaadin.flow.base;

import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.hierarchy.HierarchicalDataProvider;

public class GxTreeGrid<T> extends TreeGrid<T> {

	private static final long serialVersionUID = 1L;

	public GxTreeGrid(HierarchicalDataProvider<T, ?> dataProvider) {
		super(dataProvider);
	}

	public GxTreeGrid() {
	}

	public GxTreeGrid(Class<T> beanType) {
		super(beanType);
	}

	public GxTreeGrid(Class<T> beanType, boolean autoCreateColumns) {
		super(beanType);
		if (!autoCreateColumns) {
			removeAllColumns();
		}
	}

}
