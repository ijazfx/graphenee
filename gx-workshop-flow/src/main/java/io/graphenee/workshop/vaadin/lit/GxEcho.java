package io.graphenee.workshop.vaadin.lit;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;

@Tag("gx-echo")
@JsModule("./gx-echo/gx-echo.js")
public class GxEcho extends Component {
	private static final long serialVersionUID = 1L;

	public GxEcho() {
		getElement().setProperty("message", "Hello from Farrukh!");
	}

	public void setMessage(String message) {
		getElement().setProperty("message", message);
	}

}
