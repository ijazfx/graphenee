package io.graphenee.workshop.vaadin;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.login.AbstractLogin.LoginEvent;
import com.vaadin.flow.router.Route;

import io.graphenee.common.GxAuthenticatedUser;
import io.graphenee.common.exception.AuthenticationFailedException;
import io.graphenee.common.exception.PasswordChangeRequiredException;
import io.graphenee.core.GxDataService;
import io.graphenee.core.flow.GxUserAccountDashboardUser;
import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.core.model.entity.GxUserAccount;
import io.graphenee.vaadin.flow.GxAbstractFlowSetup;
import io.graphenee.vaadin.flow.GxAbstractLoginView;

@Route(value = "login")
public class LoginView extends GxAbstractLoginView {

	private static final long serialVersionUID = 1L;

	@Autowired
	GxAbstractFlowSetup flowSetup;

	@Autowired
	GxDataService dataService;

	@Override
	protected GxAbstractFlowSetup flowSetup() {
		return flowSetup;
	}

	@Autowired
	GxNamespace namespace;

	@Override
	protected GxAuthenticatedUser onLogin(LoginEvent event) throws AuthenticationFailedException, PasswordChangeRequiredException {
		String username = event.getUsername();
		String password = event.getPassword();
		GxUserAccount user = dataService.findUserAccountByUsernamePasswordAndNamespace(username, password, namespace);
		if (user == null) {
			user = dataService.findUserAccountByUsernamePasswordAndNamespace(username, password, dataService.systemNamespace());
		}
		if (user == null) {
			throw new AuthenticationFailedException("Invalid credentials, please try again.");
		}
		if (user.getIsLocked()) {
			throw new AuthenticationFailedException("The account is locked, please contact system administrator.");
		}
		if (user.getIsPasswordChangeRequired()) {
			throw new PasswordChangeRequiredException();
		}
		return new GxUserAccountDashboardUser(user);
	}

}
