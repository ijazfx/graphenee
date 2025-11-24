package io.graphenee.core.flow.device_mgmt;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.router.AfterNavigationEvent;

import io.graphenee.core.model.entity.GxUserAccount;
import io.graphenee.vaadin.flow.GxSecuredView;
import io.graphenee.vaadin.flow.GxVerticalLayoutView;

@GxSecuredView(GxRegisteredDeviceListView.VIEW_NAME)
public class GxRegisteredDeviceListView extends GxVerticalLayoutView {
	private static final long serialVersionUID = 1L;

	public static final String VIEW_NAME = "registered-devices";

	@Autowired
	private GxRegisteredDeviceList list;

	@Override
	protected void decorateLayout(HasComponents rootLayout) {
		rootLayout.add(list);
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		if(loggedInUser() instanceof GxUserAccount) {
			list.initializeWithNamespace(((GxUserAccount) loggedInUser()).getNamespace());
		}
	}

	@Override
	protected String getCaption() {
		return "Registered Device";
	}
}
