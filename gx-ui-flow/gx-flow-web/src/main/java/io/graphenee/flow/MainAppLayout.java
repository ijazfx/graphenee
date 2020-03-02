package io.graphenee.flow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

@PWA(name = "gx-flow-web", shortName = "gx-flow-web")
@Theme(value = Lumo.class, variant = Lumo.LIGHT)
public class MainAppLayout extends GxAppLayout {

	private static final long serialVersionUID = 1L;

	@Override
	protected Collection<GxMenuItem> menuItems() {
		List<GxMenuItem> menuItems = new ArrayList<>();
		menuItems.add(GxMenuItem.builder().caption("Form").component(FormView.class).build());
		menuItems.add(GxMenuItem.builder().caption("List").component(ListView.class).build());
		menuItems.add(GxMenuItem.builder().caption("Master Detail").component(MasterDetailView.class).build());
		menuItems.add(GxMenuItem.builder().caption("Card List").component(CardListView.class).build());
		return menuItems;
	}
}
