package io.graphenee.vaadin.flow.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;

import io.graphenee.core.api.GxUserSessionDetailDataService;
import io.graphenee.vaadin.flow.utils.DashboardUtils;

@Component
public class UserSignOutEventListener implements ApplicationListener<UserSignOutEvent> {

    @Autowired
    GxUserSessionDetailDataService userSessionDetailDataService;

    @Autowired
	HttpServletRequest httpServletRequest;

    @Override
    public void onApplicationEvent(UserSignOutEvent event) {
        Boolean userSignedIn = userSessionDetailDataService.isUserSignedIn(
                DashboardUtils.getMacAddress() + getBrowserName(
                        httpServletRequest.getHeader("User-Agent")));
        if (!userSignedIn) {
            UI.getCurrent().navigate("login");
            VaadinSession.getCurrent().close();
        }
    }

    private static String getBrowserName(String userAgent) {
        if (userAgent.contains("Chrome")) {
            return "Chrome";
        } else if (userAgent.contains("Firefox")) {
            return "Mozilla Firefox";
        } else if (userAgent.contains("Safari") && !userAgent.contains("Chrome")) {
            return "Safari";
        } else if (userAgent.contains("Edge")) {
            return "Microsoft Edge";
        } else if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
            return "Internet Explorer";
        } else {
            return "Unknown Browser";
        }
    }

}
