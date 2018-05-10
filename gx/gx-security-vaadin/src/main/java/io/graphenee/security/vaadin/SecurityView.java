package io.graphenee.security.vaadin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;

import io.graphenee.vaadin.AbstractDashboardPanel;
import io.graphenee.vaadin.TRView;

@SpringView(name = SecurityView.VIEW_NAME)
@Scope("prototype")
public class SecurityView extends AbstractDashboardPanel implements TRView {

	public static final String VIEW_NAME = "gx-security";

	@Autowired
	GxSecurityGroupListPanel securityGroupListPanel;

	@Autowired
	GxSecurityPolicyListPanel securityPolicyListPanel;

	@Autowired
	GxUserAccountListPanel userAccountListPanel;

	@Override
	protected String panelTitle() {
		return "Security";
	}

	@Override
	protected void postInitialize() {
		MenuBar menuBar = new MenuBar();
		MenuItem manageMenu = menuBar.addItem("Manage", null);
		manageMenu.addItem("User Accounts", event -> {
			securityGroupListPanel.setVisible(false);
			securityPolicyListPanel.setVisible(false);
			userAccountListPanel.setVisible(true);
			userAccountListPanel.refresh();
		});
		manageMenu.addItem("Security Groups", event -> {
			userAccountListPanel.setVisible(false);
			securityPolicyListPanel.setVisible(false);
			securityGroupListPanel.setVisible(true);
			securityGroupListPanel.refresh();
		});
		manageMenu.addItem("Security Policies", event -> {
			userAccountListPanel.setVisible(false);
			securityGroupListPanel.setVisible(false);
			securityPolicyListPanel.setVisible(true);
			userAccountListPanel.refresh();
		});
		addComponentsToToolbar(menuBar);
		addComponent(userAccountListPanel.build().withVisible(true));
		addComponent(securityGroupListPanel.build().withVisible(false));
		addComponent(securityPolicyListPanel.build().withVisible(false));
	}

	@Override
	public void enter(ViewChangeEvent event) {
		if (userAccountListPanel.isVisible()) {
			userAccountListPanel.refresh();
		}
		if (securityGroupListPanel.isVisible()) {
			securityGroupListPanel.refresh();
		}
		if (securityPolicyListPanel.isVisible()) {
			securityPolicyListPanel.refresh();
		}
	}

	@Override
	protected boolean shouldShowHeader() {
		return true;
	}

}
