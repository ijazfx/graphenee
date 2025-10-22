package io.graphenee.core.flow.security;

import io.graphenee.common.GxAuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.router.AfterNavigationEvent;

import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.core.model.entity.GxUserAccount;
import io.graphenee.vaadin.flow.GxSecuredView;
import io.graphenee.vaadin.flow.GxVerticalLayoutView;

@GxSecuredView(GxUserAccountListView.VIEW_NAME)
public class GxUserAccountListView extends GxVerticalLayoutView {
	public static final String VIEW_NAME = "user-accounts";

	private static final long serialVersionUID = 1L;

	@Autowired
	GxUserAccountList list;

	@Override
	protected void decorateLayout(HasComponents rootLayout) {
		rootLayout.add(list);
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		list.initializeWithNamespace(getNamespace());
	}

	@Override
	protected String getCaption() {
		return "User Accounts";
	}

	public GxNamespace getNamespace() {
		GxAuthenticatedUser user = loggedInUser();
		if (user instanceof GxUserAccount) {
			return ((GxUserAccount) user).getNamespace();
		}
		return null;
	}

}
