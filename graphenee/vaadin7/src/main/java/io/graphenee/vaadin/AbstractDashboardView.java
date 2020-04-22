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

import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.vaadin.viritin.layouts.MPanel;

import com.google.common.eventbus.Subscribe;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import io.graphenee.vaadin.event.DashboardEvent;
import io.graphenee.vaadin.event.DashboardEvent.CloseOpenWindowsEvent;
import io.graphenee.vaadin.event.DashboardEventBus;

@SuppressWarnings("serial")
public abstract class AbstractDashboardView extends Panel implements TRView {

	public static final String VIEW_NAME = "dashboard";

	public static final String EDIT_ID = "dashboard-edit";
	public static final String TITLE_ID = "dashboard-title";

	private Label titleLabel;
	private CssLayout dashboardPanels;
	private VerticalLayout root;

	private boolean isBuilt;

	private Component sparkline;

	public AbstractDashboardView() {
		if (!isSpringView()) {
			postConstruct();
		}
	}

	protected boolean isSpringView() {
		return this.getClass().getAnnotation(SpringView.class) != null;
	}

	@PostConstruct
	private void postConstruct() {
		postInitialize();
	}

	public AbstractDashboardView build() {
		if (!isBuilt) {
			addStyleName(ValoTheme.PANEL_BORDERLESS);
			setSizeFull();
			DashboardEventBus.sessionInstance().register(this);

			root = new VerticalLayout();
			root.setSizeFull();
			root.setMargin(true);
			root.addStyleName("dashboard-view");
			setContent(root);
			Responsive.makeResponsive(root);

			root.addComponent(buildHeader());

			List<Spark> sparks = sparks();
			if (sparks != null && !sparks.isEmpty()) {
				sparkline = buildSparkline(sparks);
				root.addComponent(sparkline);
			}

			Component content = buildContent();
			root.addComponent(content);
			root.setExpandRatio(content, 1);

			// All the open sub-windows should be closed whenever the root
			// layout
			// gets clicked.
			root.addLayoutClickListener(new LayoutClickListener() {
				@Override
				public void layoutClick(final LayoutClickEvent event) {
					DashboardEventBus.sessionInstance().post(new CloseOpenWindowsEvent());
				}
			});
			postBuild();
			isBuilt = true;
		}
		return this;
	}

	private Component buildSparkline(List<Spark> sparks) {
		CssLayout sparksLayout = new CssLayout();
		sparksLayout.addStyleName("sparks");
		sparksLayout.setWidth("100%");
		Responsive.makeResponsive(sparksLayout);
		if (sparks != null) {
			sparks.forEach(spark -> {
				Component sparkComponent = spark.build().getWrappedComponent();
				sparksLayout.addComponent(sparkComponent);
			});
		}
		return sparksLayout;
	}

	protected void postBuild() {
	}

	private Component buildDashlet(final Component content) {
		final CssLayout slot = new CssLayout();
		slot.setWidth("100%");
		slot.addStyleName("dashboard-panel-slot");

		CssLayout card = new CssLayout();
		card.setWidth("100%");
		card.addStyleName(ValoTheme.LAYOUT_CARD);

		HorizontalLayout toolbar = new HorizontalLayout();
		toolbar.addStyleName("dashboard-panel-toolbar");
		toolbar.setWidth("100%");

		Label caption = new Label(content.getCaption());
		caption.addStyleName(ValoTheme.LABEL_H4);
		caption.addStyleName(ValoTheme.LABEL_COLORED);
		caption.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		content.setCaption(null);

		MenuBar tools = new MenuBar();
		tools.addStyleName(ValoTheme.MENUBAR_BORDERLESS);
		MenuItem max = tools.addItem("", FontAwesome.EXPAND, new Command() {

			@Override
			public void menuSelected(final MenuItem selectedItem) {
				if (!slot.getStyleName().contains("max")) {
					selectedItem.setIcon(FontAwesome.COMPRESS);
					toggleMaximized(slot, true);
				} else {
					slot.removeStyleName("max");
					selectedItem.setIcon(FontAwesome.EXPAND);
					toggleMaximized(slot, false);
				}
			}
		});
		max.setStyleName("icon-only");
		MenuItem root = tools.addItem("", FontAwesome.ELLIPSIS_V, null);
		root.addItem("Configure", new Command() {
			@Override
			public void menuSelected(final MenuItem selectedItem) {
				Notification.show("Not implemented in this demo");
			}
		});
		root.addSeparator();
		root.addItem("Close", new Command() {
			@Override
			public void menuSelected(final MenuItem selectedItem) {
				Notification.show("Not implemented in this demo");
			}
		});

		toolbar.addComponents(caption, tools);
		toolbar.setExpandRatio(caption, 1);
		toolbar.setComponentAlignment(caption, Alignment.MIDDLE_LEFT);

		card.addComponents(toolbar, content);
		slot.addComponent(card);
		return slot;
	}

	private Component buildHeader() {
		HorizontalLayout header = new HorizontalLayout();
		header.addStyleName("viewheader");
		header.setSpacing(true);

		titleLabel = new Label(dashboardTitle());
		titleLabel.setId(TITLE_ID);
		titleLabel.setSizeUndefined();
		titleLabel.addStyleName(ValoTheme.LABEL_H1);
		titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		header.addComponent(titleLabel);

		HorizontalLayout tools = new HorizontalLayout();
		tools.setSpacing(true);
		tools.addStyleName("toolbar");
		header.addComponent(tools);

		return header;
	}

	protected void postInitialize() {
	}

	protected abstract String dashboardTitle();

	private Component buildContent() {
		dashboardPanels = new CssLayout();
		dashboardPanels.addStyleName("dashboard-panels");
		Responsive.makeResponsive(dashboardPanels);
		if (dashlets() != null) {
			dashlets().forEach(dashlet -> {
				dashboardPanels.addComponent(dashlet.getWrappedComponent());
			});
		}
		return dashboardPanels;
	}

	@Override
	public void enter(final ViewChangeEvent event) {
		build();
	}

	private void toggleMaximized(final Component panel, final boolean maximized) {
		for (Iterator<Component> it = root.iterator(); it.hasNext();) {
			it.next().setVisible(!maximized);
		}
		dashboardPanels.setVisible(true);

		for (Iterator<Component> it = dashboardPanels.iterator(); it.hasNext();) {
			Component c = it.next();
			c.setVisible(!maximized);
		}

		if (maximized) {
			panel.setVisible(true);
			panel.addStyleName("max");
		} else {
			panel.removeStyleName("max");
		}
	}

	public static class Dashlet {

		private Component wrappedComponent;
		private MPanel contentPanel;
		private MPanel maximizedContentPanel;
		private String[] styles;
		private String title;
		private boolean paddingEnabled;

		public Dashlet(final Component content) {
			this(content, new String[0]);
		}

		public Dashlet(final Component content, String... styles) {
			this.contentPanel = new MPanel(content).withCaption(content.getCaption()).withStyleName(ValoTheme.PANEL_BORDERLESS).withFullWidth().withFullHeight();
			this.styles = styles;
		}

		public Dashlet(final Component content, final Component maximizedContent) {
			this(content, maximizedContent, new String[0]);
		}

		public Dashlet(final Component content, final Component maximizedContent, String... styles) {
			this.contentPanel = new MPanel(content).withCaption(content.getCaption()).withStyleName(ValoTheme.PANEL_BORDERLESS).withFullWidth().withFullHeight();
			this.maximizedContentPanel = new MPanel(maximizedContent).withCaption(maximizedContent.getCaption()).withStyleName(ValoTheme.PANEL_BORDERLESS).withFullWidth()
					.withFullHeight();
			maximizedContentPanel.setVisible(false);
			this.styles = styles;
		}

		public Dashlet withTitle(String title) {
			this.title = title;
			return this;
		}

		public Dashlet withStyle(String... styles) {
			this.styles = styles;
			return this;
		}

		public Dashlet withPadding(boolean paddingEnabled) {
			this.paddingEnabled = paddingEnabled;
			return this;
		}

		public Dashlet build() {
			final CssLayout slot = new CssLayout();
			slot.setWidth("100%");
			slot.setStyleName("dashboard-panel-slot");

			CssLayout card = new CssLayout();
			card.setWidth("100%");
			card.setStyleName("dashboard-panel");
			card.addStyleName(ValoTheme.LAYOUT_CARD);

			HorizontalLayout toolbar = new HorizontalLayout();
			toolbar.addStyleName("dashboard-panel-toolbar");
			toolbar.setWidth("100%");

			String caption = title != null ? title : contentPanel.getCaption();

			Label captionLabel = new Label(caption);
			captionLabel.addStyleName(ValoTheme.LABEL_H4);
			captionLabel.addStyleName(ValoTheme.LABEL_COLORED);
			captionLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
			// content.setCaption(null);

			MenuBar tools = new MenuBar();
			tools.addStyleName(ValoTheme.MENUBAR_BORDERLESS);
			MenuItem max = tools.addItem("", FontAwesome.EXPAND, new Command() {

				@Override
				public void menuSelected(final MenuItem selectedItem) {
					if (!slot.getStyleName().contains("max")) {
						selectedItem.setIcon(FontAwesome.COMPRESS);
						toggleMaximized(slot, true);
					} else {
						slot.removeStyleName("max");
						selectedItem.setIcon(FontAwesome.EXPAND);
						toggleMaximized(slot, false);
					}
				}
			});
			max.setStyleName("icon-only");
			MenuItem root = tools.addItem("", FontAwesome.ELLIPSIS_V, null);
			buildMenuItems(root);
			if (!root.hasChildren()) {
				tools.removeItem(root);
			}

			toolbar.addComponents(captionLabel, tools);
			toolbar.setExpandRatio(captionLabel, 1);
			toolbar.setComponentAlignment(captionLabel, Alignment.MIDDLE_LEFT);

			card.addComponent(toolbar);
			if (contentPanel != null) {
				if (paddingEnabled) {
					contentPanel.addStyleName("dashlet-content");
				} else {
					contentPanel.addStyleName("dashlet-content-nopadding");
				}
				card.addComponent(contentPanel);
			}
			if (maximizedContentPanel != null) {
				if (paddingEnabled) {
					maximizedContentPanel.addStyleName("dashlet-content");
				} else {
					maximizedContentPanel.addStyleName("dashlet-content-nopadding");
				}
				maximizedContentPanel.addStyleName("dashlet-content");
				card.addComponent(maximizedContentPanel);
			}
			slot.addComponent(card);
			wrappedComponent = slot;
			if (styles != null) {
				for (String style : styles) {
					wrappedComponent.addStyleName(style);
				}
			}
			return this;
		}

		private void toggleMaximized(final Component panel, final boolean maximized) {
			if (contentPanel != null && maximizedContentPanel != null) {
				contentPanel.setVisible(!maximized);
				maximizedContentPanel.setVisible(maximized);
			}
			for (Iterator<Component> it = panel.getParent().iterator(); it.hasNext();) {
				it.next().setVisible(!maximized);
			}

			panel.getParent().setVisible(true);

			for (Iterator<Component> it = panel.getParent().iterator(); it.hasNext();) {
				Component c = it.next();
				c.setVisible(!maximized);
			}

			if (maximized) {
				panel.setVisible(true);
				panel.addStyleName("max");
				DashboardEventBus.sessionInstance().post(new DashboardEvent.DashletMaximized(this));
			} else {
				panel.removeStyleName("max");
				DashboardEventBus.sessionInstance().post(new DashboardEvent.DashletMinimized(this));
			}
		}

		protected void buildMenuItems(MenuItem root) {
			// root.addItem("Configure", new Command() {
			// @Override
			// public void menuSelected(final MenuItem selectedItem) {
			// Notification.show("Not implemented in this demo");
			// }
			// });
			// root.addSeparator();
			// root.addItem("Close", new Command() {
			// @Override
			// public void menuSelected(final MenuItem selectedItem) {
			// Notification.show("Not implemented in this demo");
			// }
			// });
		}

		public Component getWrappedComponent() {
			if (wrappedComponent == null) {
				build();
			}
			return wrappedComponent;
		}

	}

	public static class Spark {

		private Component wrappedComponent;
		private Component content;
		private String[] styles;
		private String title;

		public Spark(final Component content) {
			this(content, new String[0]);
		}

		public Spark(final Component content, String... styles) {
			this.content = content;
			this.styles = styles;
		}

		public Spark withStyle(String... styles) {
			this.styles = styles;
			return this;
		}

		public Spark build() {
			final CssLayout card = new CssLayout();
			card.setWidthUndefined();
			card.setStyleName("spark");
			if (styles != null) {
				for (String style : styles) {
					card.addStyleName(style);
				}
			}
			card.addComponent(content);
			this.wrappedComponent = card;
			return this;
		}

		public Component getWrappedComponent() {
			if (wrappedComponent == null) {
				build();
			}
			return wrappedComponent;
		}

	}

	protected abstract List<Dashlet> dashlets();

	protected List<Spark> sparks() {
		return null;
	}

	@Subscribe
	public void onDashletMinimized(DashboardEvent.DashletMinimized event) {
		if (sparkline != null) {
			sparkline.setVisible(true);
		}
	}

	@Subscribe
	public void onDashletMaximized(DashboardEvent.DashletMaximized event) {
		if (sparkline != null) {
			sparkline.setVisible(false);
		}
	}

}
