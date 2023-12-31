package io.graphenee.vaadin.flow;

import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.NotFoundException;

import jakarta.servlet.http.HttpServletResponse;

public class GxNotFoundView extends VerticalLayout implements HasErrorParameter<NotFoundException> {

	private static final long serialVersionUID = 1L;

	private final NativeLabel error = new NativeLabel();

	public GxNotFoundView() {
		add(error);
	}

	@Override
	public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<NotFoundException> parameter) {
		error.setText(parameter.getCustomMessage());
		return HttpServletResponse.SC_NOT_FOUND;
	}

}
