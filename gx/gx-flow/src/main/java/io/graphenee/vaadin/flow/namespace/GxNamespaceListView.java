package io.graphenee.vaadin.flow.namespace;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.router.AfterNavigationEvent;

import io.graphenee.vaadin.flow.base.GxSecuredView;
import io.graphenee.vaadin.flow.base.GxVerticalLayoutView;

@GxSecuredView(GxNamespaceListView.VIEW_NAME)
public class GxNamespaceListView extends GxVerticalLayoutView {
	private static final long serialVersionUID = 1L;

	public static final String VIEW_NAME = "namespaces";

	@Autowired
	private GxNamespaceList list;

	@Override
	protected void decorateLayout(HasComponents rootLayout) {
		rootLayout.add(list);
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		list.refresh();
	}

	@Override
	protected String getCaption() {
		return "Namespaces";
	}
}
