package io.graphenee.vaadin.flow.base;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinSession;

import io.graphenee.core.model.GxAuthenticatedUser;
import io.graphenee.vaadin.flow.security.UserSignOutEvent;
import io.graphenee.vaadin.flow.utils.DashboardUtils;

public class GxVerticalLayoutView extends GxAbstractLayoutView {
	private static final long serialVersionUID = 1L;

	public GxVerticalLayoutView() {
		addClassName("gx-vertical-layout-view");
		addClickListener(l -> {
			GxAuthenticatedUser user = VaadinSession.getCurrent().getAttribute(GxAuthenticatedUser.class);
			if (user != null) {
				String identifier = user.getUsername() + DashboardUtils.getMacAddress()
						+ VaadinRequest.getCurrent().getHeader("User-Agent").replaceAll(" ", "");
				eventPublisher.publishEvent(new UserSignOutEvent(0, identifier));
			}
		});
	}

	@Override
	protected Component getLayoutComponent() {
		VerticalLayout layoutComponent = new VerticalLayout();
		layoutComponent.setSizeFull();
		layoutComponent.setMargin(false);
		layoutComponent.setPadding(false);
		layoutComponent.setSpacing(false);
		return layoutComponent;
	}

}
