package io.graphenee.flow;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import io.graphenee.core.model.api.GxDataService;
import io.graphenee.core.model.bean.GxTermBean;

@Route(value = "list", layout = MainAppLayout.class)
@PageTitle("List")
@CssImport("styles/list-view.css")
public class ListView extends Div implements AfterNavigationObserver {

	@Autowired
	private GxDataService service;
	private final Grid<GxTermBean> grid;

	public ListView() {
		setId("list-view");
		grid = new Grid<>();
		grid.setId("list");
		grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
		grid.setHeightFull();
		grid.addColumn(new ComponentRenderer<>(employee -> {
			H3 h3 = new H3(employee.getTermKey() + ", " + employee.getTermSingular());
			Anchor anchor = new Anchor("mailto:" + employee.getTermKey(), employee.getTermSingular());
			anchor.getElement().getThemeList().add("font-size-xs");
			Div div = new Div(h3, anchor);
			div.addClassName("employee-column");
			return div;
		}));

		add(grid);
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		// Lazy init of the grid items, happens only when we are sure the view will be
		// shown to the user
		grid.setItems(service.findTermByLocale(Locale.ENGLISH));
	}
}
