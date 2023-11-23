package io.graphenee.vaadin.flow.security;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.router.AfterNavigationEvent;

import io.graphenee.core.model.bean.GxNamespaceBean;
import io.graphenee.vaadin.flow.base.GxSecuredView;
import io.graphenee.vaadin.flow.base.GxVerticalLayoutView;

@GxSecuredView(GxSecurityPolicyListView.VIEW_NAME)
public class GxSecurityPolicyListView extends GxVerticalLayoutView {
	public static final String VIEW_NAME = "security-policies";

	private static final long serialVersionUID = 1L;

	@Autowired
	GxSecurityPolicyList list;

	@Autowired(required = false)
	GxNamespaceBean namespace;

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
		return "Security Policies";
	}

}
