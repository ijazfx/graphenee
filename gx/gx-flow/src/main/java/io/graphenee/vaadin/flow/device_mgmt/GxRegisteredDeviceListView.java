package io.graphenee.vaadin.flow.device_mgmt;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.router.AfterNavigationEvent;

import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.vaadin.flow.GxSecuredView;
import io.graphenee.vaadin.flow.GxVerticalLayoutView;

@GxSecuredView(GxRegisteredDeviceListView.VIEW_NAME)
public class GxRegisteredDeviceListView extends GxVerticalLayoutView {
	private static final long serialVersionUID = 1L;

	public static final String VIEW_NAME = "registered-devices";

	@Autowired
	private GxRegisteredDeviceList list;

	@Autowired(required = false)
	private GxNamespace namespace;

	@Override
	protected void decorateLayout(HasComponents rootLayout) {
		rootLayout.add(list);
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		list.initializeWithNamespace(namespace);
	}

	@Override
	protected String getCaption() {
		return "Registered Device";
	}
}
