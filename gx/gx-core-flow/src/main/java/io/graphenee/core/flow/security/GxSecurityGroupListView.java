package io.graphenee.core.flow.security;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.router.AfterNavigationEvent;

import io.graphenee.common.GxAuthenticatedUser;
import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.core.model.entity.GxUserAccount;
import io.graphenee.vaadin.flow.GxSecuredView;
import io.graphenee.vaadin.flow.GxVerticalLayoutView;

@GxSecuredView(GxSecurityGroupListView.VIEW_NAME)
public class GxSecurityGroupListView extends GxVerticalLayoutView {
	private static final long serialVersionUID = 1L;

	public static final String VIEW_NAME = "security-groups";

	@Autowired
	GxSecurityGroupList list;

	@Override
	protected void decorateLayout(HasComponents rootLayout) {
		rootLayout.add(list);
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		if(loggedInUser() instanceof GxUserAccount) {
			list.initializeWithNamespace(((GxUserAccount) loggedInUser()).getNamespace());
		}
	}

	@Override
	protected String getCaption() {
		return "Security Groups";
	}

}