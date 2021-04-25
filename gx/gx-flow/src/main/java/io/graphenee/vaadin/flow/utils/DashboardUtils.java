package io.graphenee.vaadin.flow.utils;

import com.vaadin.flow.server.VaadinSession;

import io.graphenee.core.model.GxAuthenticatedUser;

public class DashboardUtils {

    @SuppressWarnings("unchecked")
    public static <T extends GxAuthenticatedUser> T getLoggedInUser() {
        return (T) VaadinSession.getCurrent().getAttribute(GxAuthenticatedUser.class);
    }

}