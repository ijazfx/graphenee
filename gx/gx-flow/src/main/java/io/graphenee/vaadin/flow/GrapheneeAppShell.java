package io.graphenee.vaadin.flow;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;

/**
 * The main application shell.
 */
@Push
@Viewport("width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes")
@CssImport(value = "./styles/graphenee.css")
@JsModule("./styles/graphenee.js")
public final class GrapheneeAppShell implements AppShellConfigurator {
    private static final long serialVersionUID = 1L;
}
