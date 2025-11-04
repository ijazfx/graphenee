package io.graphenee.workshop.vaadin;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.LoadingIndicatorConfiguration;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.shared.communication.PushMode;
import com.vaadin.flow.shared.ui.Transport;

@Push(value = PushMode.AUTOMATIC, transport = Transport.WEBSOCKET_XHR)
public class AppShell implements AppShellConfigurator, VaadinServiceInitListener {

    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addUIInitListener(uiInitEvent -> {
            LoadingIndicatorConfiguration conf = uiInitEvent.getUI().getLoadingIndicatorConfiguration();

            // disable default theme -> loading indicator isn't shown
            // conf.setApplyDefaultTheme(false);

            /*
             * Delay for showing the indicator and setting the 'first' class name.
             */
            conf.setFirstDelay(5000); // 300ms is the default

            /* Delay for setting the 'second' class name */
            conf.setSecondDelay(10000); // 1500ms is the default

            /* Delay for setting the 'third' class name */
            conf.setThirdDelay(15000); // 5000ms is the default
        });
    }

}
