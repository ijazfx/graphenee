package io.graphenee.workshop.vaadin;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;

import io.graphenee.core.flow.GxCoreMenuItemFactory;
import io.graphenee.vaadin.flow.GxAbstractFlowSetup;
import io.graphenee.vaadin.flow.GxMenuItem;

@Component
@VaadinSessionScope
public class FlowSetup extends GxAbstractFlowSetup {

	@Override
	public List<GxMenuItem> menuItems() {
		List<GxMenuItem> items = new ArrayList<>();

		items.add(GxCoreMenuItemFactory.setupMenuItem());
		items.add(GxCoreMenuItemFactory.documentsMenuItem());
		items.add(GxCoreMenuItemFactory.messageTemplateMenuItem());
		items.add(GxMenuItem.create("Playground", VaadinIcon.PENCIL.create(), PlaygroundView.class));

		return items;
	}

	@Override
	public Class<? extends RouterLayout> routerLayout() {
		return MainLayout.class;
	}

}
