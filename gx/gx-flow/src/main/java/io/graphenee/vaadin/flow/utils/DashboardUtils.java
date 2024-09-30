package io.graphenee.vaadin.flow.utils;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinSession;

import io.graphenee.core.model.GxAuthenticatedUser;

public class DashboardUtils {

	private static final Map<GxAuthenticatedUser, UI> UI_MAP = new ConcurrentHashMap<>(new HashMap<>());

	public static void setCurrentUI(GxAuthenticatedUser user, UI ui) {
		UI_MAP.put(user, ui);
	}

	public static UI getCurrentUI(GxAuthenticatedUser user) {
		UI ui = UI_MAP.get(user);
		if (ui != null && !ui.isClosing())
			return ui;
		UI_MAP.remove(user);
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T extends GxAuthenticatedUser> T getLoggedInUser() {
		return (T) VaadinSession.getCurrent().getAttribute(GxAuthenticatedUser.class);
	}

	@SuppressWarnings("unchecked")
	public static <T extends GxAuthenticatedUser> T getLoggedInUser(VaadinSession vaadinSession) {
		return (T) vaadinSession.getAttribute(GxAuthenticatedUser.class.getName());
	}

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

	@SuppressWarnings("unchecked")
	public static Map<String, String[]> getQueryMap() {
		Map<String, String[]> map = (Map<String, String[]>) VaadinSession.getCurrent().getAttribute("gx-QueryMap");
		if (map == null) {
			map = new HashMap<>();
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, String[]> getQueryMap(VaadinSession vaadinSession) {
		Map<String, String[]> map = (Map<String, String[]>) vaadinSession.getAttribute("gx-QueryMap");
		if (map == null) {
			map = new HashMap<>();
		}
		return map;
	}

	public static String getRemoteAddress() {
		return VaadinRequest.getCurrent().getRemoteAddr();
	}

	public static String getMacAddress() {
		try {
			Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
			while (networks.hasMoreElements()) {
				NetworkInterface network = networks.nextElement();
				if (network.isUp() && !network.isLoopback() && !network.isVirtual()) {
					byte[] mac = network.getHardwareAddress();
					if (mac != null) {
						StringBuilder sb = new StringBuilder();
						for (byte b : mac) {
							sb.append(String.format("%02X:", b));
						}
						if (sb.length() > 0) {
							sb.deleteCharAt(sb.length() - 1);
						}
						return sb.toString();
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return null; // MAC address not found
	}

}