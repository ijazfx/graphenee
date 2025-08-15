package io.graphenee.core.flow.security;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.router.AfterNavigationEvent;

import io.graphenee.core.flow.GxUserAccountDashboardUser;
import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.core.model.entity.GxPasswordPolicy;
import io.graphenee.security.GxPasswordPolicyDataService;
import io.graphenee.vaadin.flow.GxAbstractEntityForm.EntityFormDelegate;
import io.graphenee.vaadin.flow.GxSecuredView;
import io.graphenee.vaadin.flow.GxVerticalLayoutView;

@GxSecuredView(GxPasswordPolicyView.VIEW_NAME)
public class GxPasswordPolicyView extends GxVerticalLayoutView {
	public static final String VIEW_NAME = "gx-password-policy";

	private static final long serialVersionUID = 1L;

	@Autowired
	GxPasswordPolicyDataService passwordPolicyDataService;

	@Autowired
	GxPasswordPolicyForm form;

	@Override
	protected void decorateLayout(HasComponents rootLayout) {
		form.setDelegate(new EntityFormDelegate<GxPasswordPolicy>() {

			@Override
			public void onSave(GxPasswordPolicy entity) {
				passwordPolicyDataService.save(entity);
			}

		});
		rootLayout.add(form);
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		if (loggedInUser() instanceof GxUserAccountDashboardUser) {
			GxNamespace namespace = ((GxUserAccountDashboardUser) loggedInUser()).getUser().getNamespace();
			GxPasswordPolicy policy = passwordPolicyDataService.findPasswordPolicyByNamespace(namespace);
			if (policy == null) {
				policy = new GxPasswordPolicy();
				policy.setNamespace(namespace);
			}
			form.setEntity(policy);
		}
	}

	@Override
	protected String getCaption() {
		return "Password Policy";
	}

}
