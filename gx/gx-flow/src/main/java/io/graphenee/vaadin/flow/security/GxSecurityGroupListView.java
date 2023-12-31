package io.graphenee.vaadin.flow.security;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.router.AfterNavigationEvent;

import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.vaadin.flow.GxSecuredView;
import io.graphenee.vaadin.flow.GxVerticalLayoutView;

@GxSecuredView(GxSecurityGroupListView.VIEW_NAME)
public class GxSecurityGroupListView extends GxVerticalLayoutView {
	private static final long serialVersionUID = 1L;

	public static final String VIEW_NAME = "security-groups";

	@Autowired
	GxSecurityGroupList list;

	@Autowired(required = false)
	GxNamespace namespace;

	@Override
	protected void decorateLayout(HasComponents rootLayout) {
		rootLayout.add(list);
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		list.initializeWithNamespace(namespace);
	}

	@Override
	protected String getCaption() {
		return "Security Groups";
	}

}