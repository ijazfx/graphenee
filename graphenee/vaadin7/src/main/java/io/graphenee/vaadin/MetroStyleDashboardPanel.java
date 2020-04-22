/*******************************************************************************
 * Copyright (c) 2016, 2018 Farrukh Ijaz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package io.graphenee.vaadin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MPanel;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

import io.graphenee.gx.theme.graphenee.GrapheneeTheme;

@SuppressWarnings("serial")
public class MetroStyleDashboardPanel extends AbstractDashboardPanel {

	AbstractDashboardSetup dashboardSetup;
	//private int maxTileCount = 7;
	private CssLayout rootLayout;

	public MetroStyleDashboardPanel(AbstractDashboardSetup dashboardSetup) {
		this.dashboardSetup = dashboardSetup;
	}

	@Override
	protected String panelTitle() {
		return dashboardSetup.applicationTitle();
	}

	@Override
	protected void postInitialize() {
		rootLayout = new CssLayout(); //.withMargin(false).withSpacing(true).withFullWidth();
		List<TRMenuItem> menuItems = dashboardSetup.menuItems();
		generateTiles(rootLayout, menuItems);
		addComponent(rootLayout);
	}

	private void generateTiles(CssLayout mainLayout, Collection<TRMenuItem> menuItems) {
		Iterator<TRMenuItem> iter = menuItems.iterator();
		//		MHorizontalLayout rowLayout = new MHorizontalLayout().withMargin(false).withSpacing(true);
		Random random = new Random(menuItems.size());
		Random colorRandom = new Random(System.currentTimeMillis());
		Set<Integer> colorUsedSet = new HashSet<>();
		int tileCount = 0;
		int color = 0;
		int maxColors = 8;
		int lastColor = 0;
		while (iter.hasNext()) {
			TRMenuItem menuItem = iter.next();
			MPanel panel = new MPanel();
			MLabel icon = new MLabel().withStyleName(ValoTheme.LABEL_NO_MARGIN, "tile-icon").withWidthUndefined();
			icon.setIcon(menuItem.icon());
			MHorizontalLayout iconLayout = new MHorizontalLayout().withFullWidth().withDefaultComponentAlignment(Alignment.TOP_CENTER);
			iconLayout.add(icon);

			MLabel label = new MLabel(menuItem.caption()).withStyleName(ValoTheme.LABEL_NO_MARGIN);
			MVerticalLayout iconLabelLayout = new MVerticalLayout(iconLayout, label);
			iconLabelLayout.setComponentAlignment(iconLayout, Alignment.MIDDLE_CENTER);
			iconLabelLayout.setExpandRatio(iconLayout, 1);
			iconLabelLayout.setHeight("120px");
			panel.setContent(iconLabelLayout);
			panel.addClickListener(event -> {
				if (menuItem.hasChildren()) {
					mainLayout.removeAllComponents();
					List<TRMenuItem> subMenuItems = new ArrayList<>(menuItem.getChildren());
					TRSimpleMenuItem backMenuItem = TRSimpleMenuItem.createMenuItem("Back", GrapheneeTheme.BACK_ICON, event2 -> {
						mainLayout.removeAllComponents();
						if (menuItem.getParent() != null) {
							generateTiles(mainLayout, menuItem.getParent().getChildren());
						} else {
							generateTiles(mainLayout, dashboardSetup.menuItems());
						}
					});
					subMenuItems.add(0, backMenuItem);
					generateTiles(mainLayout, subMenuItems);
				} else {
					if (menuItem.viewName() != null)
						UI.getCurrent().getNavigator().navigateTo(menuItem.viewName());
					else
						UI.getCurrent().getNavigator().navigateTo(dashboardSetup.dashboardViewName());

				}
			});
			int value = 1; //random.nextInt(2) + 1;
			if (colorUsedSet.size() == maxColors) {
				colorUsedSet.clear();
			}
			do {
				color = colorRandom.nextInt(maxColors) + 1;
			} while (colorUsedSet.contains(color) || color == lastColor);
			colorUsedSet.add(color);
			lastColor = color;
			//			if (value + tileCount == 5) {
			//				value = 1;
			//			}
			//			tileCount += value;
			// System.err.print(value + ",");
			panel.setStyleName("metro-tile-" + value);
			panel.addStyleName(GrapheneeTheme.STYLE_MARGIN_TOP);
			panel.addStyleName(GrapheneeTheme.STYLE_MARGIN_LEFT);
			panel.setWidth(value == 1 ? "120px" : "250px");
			panel.setHeight("120px");
			if (shouldShowColoredTiles()) {
				panel.addStyleName("color-" + color);
			} else {
				panel.addStyleName("color-default");
			}
			mainLayout.addComponent(panel);
			//			rowLayout.add(panel);
			//			if (tileCount >= maxTileCount) {
			//				mainLayout.add(rowLayout);
			//				rowLayout = new MHorizontalLayout().withMargin(false).withSpacing(true);
			//				tileCount = 0;
			//				// System.err.println();
			//			}
		}
		//		mainLayout.add(rowLayout);
	}

	@Override
	protected boolean shouldShowHeader() {
		return true;
	}

	protected boolean shouldShowColoredTiles() {
		return true;
	}

}
