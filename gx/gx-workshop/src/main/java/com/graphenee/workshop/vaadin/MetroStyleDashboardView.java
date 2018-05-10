package com.graphenee.workshop.vaadin;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.navigator.MView;

import com.graphenee.vaadin.AbstractDashboardSetup;
import com.graphenee.vaadin.MetroStyleDashboardPanel;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;

@SuppressWarnings("serial")
@SpringView(name = MetroStyleDashboardView.VIEW_NAME)
public class MetroStyleDashboardView extends MetroStyleDashboardPanel implements MView {

	public static final String VIEW_NAME = "metro";

	@Autowired
	AbstractDashboardSetup dashboardSetup;

	public MetroStyleDashboardView(AbstractDashboardSetup dashboardSetup) {
		super(dashboardSetup);
	}

	@Override
	public void enter(ViewChangeEvent event) {

	}

	@Override
	public boolean beforeViewChange(ViewChangeEvent event) {
		return true;
	}

	@Override
	public void afterViewChange(ViewChangeEvent event) {
	}

}
