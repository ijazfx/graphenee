package io.graphenee.vaadin.flow.base;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.router.AfterNavigationEvent;

import org.springframework.beans.factory.annotation.Autowired;

@GxSecuredView(GxRegisteredDeviceListView.VIEW_NAME)
public class GxRegisteredDeviceListView extends GxVerticalLayoutView {
    private static final long serialVersionUID = 1L;

    public static final String VIEW_NAME = "registered-device";

    @Autowired
    private GxRegisteredDeviceList list;

    @Override
    protected void decorateLayout(HasComponents rootLayout) {
        rootLayout.add(list);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        list.refresh();
    }

    @Override
    protected String getCaption() {
        return "Registered Device";
    }
}
