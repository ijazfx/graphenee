package io.graphenee.vaadin.flow.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinSession;

import io.graphenee.common.GxAuthenticatedUser;

/**
 * A utility class for the dashboard.
 */
public class DashboardUtils {

	private static final Map<GxAuthenticatedUser, UI> UI_MAP = new ConcurrentHashMap<>(new HashMap<>());

	/**
	 * Sets the current UI for a user.
	 * 
	 * @param user The user.
	 * @param ui   The UI.
	 */
	public static void setCurrentUI(GxAuthenticatedUser user, UI ui) {
		UI_MAP.put(user, ui);
	}

	/**
	 * Gets the current UI for a user.
	 * 
	 * @param user The user.
	 * @return The UI.
	 */
	public static UI getCurrentUI(GxAuthenticatedUser user) {
		UI ui = UI_MAP.get(user);
		if (ui != null && !ui.isClosing())
			return ui;
		UI_MAP.remove(user);
		return null;
	}

	/**
	 * Gets the logged in user.
	 * 
	 * @param <T> The user type.
	 * @return The logged in user.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends GxAuthenticatedUser> T getLoggedInUser() {
		return (T) VaadinSession.getCurrent().getAttribute(GxAuthenticatedUser.class);
	}

	/**
	 * Gets the logged in user.
	 * 
	 * @param <T>           The user type.
	 * @param vaadinSession The Vaadin session.
	 * @return The logged in user.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends GxAuthenticatedUser> T getLoggedInUser(VaadinSession vaadinSession) {
		return (T) vaadinSession.getAttribute(GxAuthenticatedUser.class.getName());
	}

	/**
	 * Gets the logged in username.
	 * 
	 * @return The logged in username.
	 */
	public static String getLoggedInUsername() {
		GxAuthenticatedUser loggedInUser = DashboardUtils.getLoggedInUser();
		final String targetUser;
		if (loggedInUser != null) {
			targetUser = loggedInUser.getUsername();
		} else {
			targetUser = "system";
		}
		return targetUser;
	}

	/**
	 * Gets the query map.
	 * 
	 * @return The query map.
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String[]> getQueryMap() {
		Map<String, String[]> map = (Map<String, String[]>) VaadinSession.getCurrent().getAttribute("gx-QueryMap");
		if (map == null) {
			map = new HashMap<>();
		}
		return map;
	}

	/**
	 * Gets the query map.
	 * 
	 * @param vaadinSession The Vaadin session.
	 * @return The query map.
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String[]> getQueryMap(VaadinSession vaadinSession) {
		Map<String, String[]> map = (Map<String, String[]>) vaadinSession.getAttribute("gx-QueryMap");
		if (map == null) {
			map = new HashMap<>();
		}
		return map;
	}

	/**
	 * Gets the remote address.
	 * 
	 * @return The remote address.
	 */
	public static String getRemoteAddress() {
		return VaadinRequest.getCurrent().getRemoteAddr();
	}

}