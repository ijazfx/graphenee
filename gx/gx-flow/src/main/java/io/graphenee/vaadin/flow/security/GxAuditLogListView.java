package io.graphenee.vaadin.flow.security;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.router.AfterNavigationEvent;

import io.graphenee.vaadin.flow.base.GxSecuredView;
import io.graphenee.vaadin.flow.base.GxVerticalLayoutView;

@GxSecuredView(GxAuditLogListView.VIEW_NAME)
public class GxAuditLogListView extends GxVerticalLayoutView {
	public static final String VIEW_NAME = "audit-log";

	private static final long serialVersionUID = 1L;

	@Autowired
	GxAuditLogList list;

	@Override
	protected void decorateLayout(HasComponents rootLayout) {
		rootLayout.add(list);
		list.setEditable(false);
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		list.refresh();
	}

	@Override
	protected String getCaption() {
		return "Audit Log";
	}

}
