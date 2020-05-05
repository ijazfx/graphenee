package io.graphenee.workshop;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Viewport;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Notification.Type;

import io.graphenee.core.exception.ChangePasswordFailedException;
import io.graphenee.core.exception.UserCommunicationException;
import io.graphenee.core.model.api.GxDataService;
import io.graphenee.core.model.bean.GxUserAccountBean;
import io.graphenee.vaadin.AbstractDashboardSetup;
import io.graphenee.vaadin.AbstractResetPasswordUI;
import io.graphenee.vaadin.ui.GxNotification;
import io.graphenee.workshop.vaadin.WorkshopDashboardSetup;

@SuppressWarnings("serial")
@SpringUI(path = "/reset-password")
@Theme("graphenee")
@Push(transport = Transport.WEBSOCKET, value = PushMode.MANUAL)
@Viewport(value = "width=device-width")
public class ResetPasswordUI extends AbstractResetPasswordUI {

	@Autowired
	WorkshopDashboardSetup dashboardSetup;

	@Autowired
	GxDataService gxDataService;

	@Override
	protected AbstractDashboardSetup dashboardSetup() {
		return dashboardSetup;
	}

	@Override
	protected void sendResetKeyToUser(String token, String usernameOrEmail) throws UserCommunicationException {
		// TODO: Send the token to user identified by usernameOrEmail through any
		// mechanism. Following code is just for demo purpose and should not be used at
		// all.
		String message = String.format("Your password reset token is %s", token);
		GxNotification notification = GxNotification.closable(message, Type.ERROR_MESSAGE);
		notification.setPosition(Position.BOTTOM_CENTER);
		notification.show(Page.getCurrent());
	}

	@Override
	protected void changeUserPassword(String usernameOrEmail, String password) throws ChangePasswordFailedException {
		// TODO Retrieve the user using usernameOrEmail and change it's password.
		GxUserAccountBean user = gxDataService.findUserAccountByUsername(usernameOrEmail);
		user.setPassword(password);
		gxDataService.save(user);
	}

}
