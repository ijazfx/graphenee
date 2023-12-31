package io.graphenee.core.flow.email_template;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.router.AfterNavigationEvent;

import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.vaadin.flow.GxSecuredView;
import io.graphenee.vaadin.flow.GxVerticalLayoutView;

@SuppressWarnings("serial")
@GxSecuredView(GxEmailTemplateListView.VIEW_NAME)
public class GxEmailTemplateListView extends GxVerticalLayoutView {

	public static final String VIEW_NAME = "email-templates";

	@Autowired
	private GxEmailTemplateList list;

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
		return "Message Templates";
	}
}
