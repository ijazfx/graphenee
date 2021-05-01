package io.graphenee.workshop.vaadin;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.login.AbstractLogin.LoginEvent;
import com.vaadin.flow.router.Route;

import io.graphenee.core.exception.AuthenticationFailedException;
import io.graphenee.core.exception.PasswordChangeRequiredException;
import io.graphenee.core.model.GxAuthenticatedUser;
import io.graphenee.core.model.api.GxDataService;
import io.graphenee.core.model.bean.GxUserAccountBean;
import io.graphenee.vaadin.flow.base.GxAbstractFlowSetup;
import io.graphenee.vaadin.flow.base.GxAbstractLoginView;
import io.graphenee.vaadin.flow.security.GxUserAccountAuthenticatedUser;

@Route("login")
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

	@Override
	protected GxAuthenticatedUser onLogin(LoginEvent event) throws AuthenticationFailedException, PasswordChangeRequiredException {
		String username = event.getUsername();
		String password = event.getPassword();
		GxUserAccountBean user = dataService.findUserAccountByUsernameAndPassword(username, password);
		if (user == null) {
			throw new AuthenticationFailedException("Invalid credentials, please try again.");
		}
		if (user.getIsLocked()) {
			throw new AuthenticationFailedException("The account is locked, please contact system administrator.");
		}
		if (user.getIsPasswordChangeRequired()) {
			throw new PasswordChangeRequiredException();
		}
		return new GxUserAccountAuthenticatedUser(user);
	}

}
