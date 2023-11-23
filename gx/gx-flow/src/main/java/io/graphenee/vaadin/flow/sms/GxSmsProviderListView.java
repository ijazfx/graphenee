package io.graphenee.vaadin.flow.sms;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.router.AfterNavigationEvent;

import io.graphenee.vaadin.flow.base.GxSecuredView;
import io.graphenee.vaadin.flow.base.GxVerticalLayoutView;

@GxSecuredView(GxSmsProviderListView.VIEW_NAME)
public class GxSmsProviderListView extends GxVerticalLayoutView {
	public static final String VIEW_NAME = "sms-providers";

	private static final long serialVersionUID = 1L;

	@Autowired
	GxSmsProviderListPanel list;

	@Override
	protected void decorateLayout(HasComponents rootLayout) {
		rootLayout.add(list);
	}

	public void afterNavigation(AfterNavigationEvent event) {
		list.refresh();
	}

	@Override
	protected String getCaption() {
		return "SMS Providers";
	}

}
