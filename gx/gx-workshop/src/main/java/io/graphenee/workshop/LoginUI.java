package io.graphenee.workshop;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Viewport;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.spring.annotation.SpringUI;

import io.graphenee.core.exception.AuthenticationFailedException;
import io.graphenee.core.model.GxAuthenticatedUser;
import io.graphenee.core.model.GxUsernamePasswordCredentials;
import io.graphenee.core.model.api.GxDataService;
import io.graphenee.core.model.bean.GxUserAccountBean;
import io.graphenee.vaadin.AbstractDashboardSetup;
import io.graphenee.vaadin.AbstractLoginUI;
import io.graphenee.vaadin.domain.GxDashboardUser;
import io.graphenee.workshop.vaadin.WorkshopDashboardSetup;

@SuppressWarnings("serial")
@SpringUI(path = "/login")
@Theme("graphenee")
@Push(transport = Transport.WEBSOCKET, value = PushMode.MANUAL)
@Viewport(value = "width=device-width")
public class LoginUI extends AbstractLoginUI<GxUsernamePasswordCredentials, GxAuthenticatedUser> {

	@Autowired
	WorkshopDashboardSetup dashboardSetup;

	@Autowired
	GxDataService gxDataService;

	@Override
	protected GxAuthenticatedUser authenticate(GxUsernamePasswordCredentials credentials) throws AuthenticationFailedException {
		String username = credentials.getUsername();
		String password = credentials.getPassword();

		HttpServletRequest request = (HttpServletRequest) VaadinService.getCurrentRequest();
		if (request == null) {
			throw new AuthenticationFailedException("Cannot authenticate due to invalid http request.");
		}

		String domain = request.getServerName();

		GxUserAccountBean userAccount = gxDataService.findUserAccountByUsernameAndPassword(username, password);
		if (userAccount != null) {
			if (userAccount.getIsLocked()) {
				throw new AuthenticationFailedException("Your access is blocked, please contact administrator to unblock your access.");
			}
			if (!userAccount.getIsActive()) {
				throw new AuthenticationFailedException("Your account is not active, please contact administrator to activate your account.");
			}
			return new GxDashboardUser(userAccount);
		}

		return null;
	}

	@Override
	protected AbstractDashboardSetup dashboardSetup() {
		return dashboardSetup;
	}

}
