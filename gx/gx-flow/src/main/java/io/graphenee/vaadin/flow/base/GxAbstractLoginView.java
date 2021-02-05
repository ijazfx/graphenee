package io.graphenee.vaadin.flow.base;

import javax.annotation.PostConstruct;

import com.vaadin.flow.component.login.AbstractLogin.ForgotPasswordEvent;
import com.vaadin.flow.component.login.AbstractLogin.LoginEvent;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.server.VaadinSession;

import io.graphenee.core.exception.AuthenticationFailedException;
import io.graphenee.core.exception.PasswordChangeRequiredException;
import io.graphenee.core.model.GxAuthenticatedUser;
import io.graphenee.vaadin.flow.security.GxSecurityGroupListView;
import io.graphenee.vaadin.flow.security.GxSecurityPolicyListView;
import io.graphenee.vaadin.flow.security.GxUserAccountListView;

public abstract class GxAbstractLoginView extends VerticalLayout {

    private static final long serialVersionUID = 1L;

    public GxAbstractLoginView() {
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
    }

    @PostConstruct
    private void postBuild() {
        LoginForm loginForm = new LoginForm();
        loginForm.setForgotPasswordButtonVisible(true);
        LoginI18n loginI18n = LoginI18n.createDefault();
        loginI18n.getForm().setTitle(flowSetup().appTitle());
        loginForm.setI18n(loginI18n);

        add(loginForm);

        loginForm.addLoginListener(e -> {
            try {
                GxAuthenticatedUser user = onLogin(e);
                VaadinSession.getCurrent().setAttribute(GxAuthenticatedUser.class, user);
                RouteConfiguration rc = RouteConfiguration.forSessionScope();
                registerRoutesOnSuccessfulAuthentication(rc, user);
                if (user.canDoAction("view", GxUserAccountListView.VIEW_NAME))
                    rc.setRoute(GxUserAccountListView.VIEW_NAME, GxUserAccountListView.class, flowSetup().routerLayout());
                if (user.canDoAction(GxSecurityGroupListView.VIEW_NAME, "view"))
                    rc.setRoute(GxSecurityGroupListView.VIEW_NAME, GxSecurityGroupListView.class, flowSetup().routerLayout());
                if (user.canDoAction(GxSecurityPolicyListView.VIEW_NAME, "view"))
                    rc.setRoute(GxSecurityPolicyListView.VIEW_NAME, GxSecurityPolicyListView.class, flowSetup().routerLayout());
                getUI().ifPresent(ui -> {
                    ui.navigate(flowSetup().defaultRoute());
                });
            } catch (AuthenticationFailedException afe) {
                Notification.show(afe.getMessage(), 5000, Position.BOTTOM_CENTER);
            } catch (PasswordChangeRequiredException pcre) {
                getUI().ifPresent(ui -> {
                    ui.navigate("reset-password");
                });
            } finally {
                loginForm.setEnabled(true);
            }
        });
        loginForm.addForgotPasswordListener(e -> {
            onForgotPassword(e);
        });
    }

    protected abstract GxAbstractFlowSetup flowSetup();

    protected abstract GxAuthenticatedUser onLogin(LoginEvent event) throws AuthenticationFailedException, PasswordChangeRequiredException;

    protected void onForgotPassword(ForgotPasswordEvent event) {
        getUI().ifPresent(ui -> {
            ui.navigate("reset-password");
        });
    }

    protected abstract void registerRoutesOnSuccessfulAuthentication(RouteConfiguration rc, GxAuthenticatedUser user);

}
