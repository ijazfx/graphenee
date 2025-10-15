package io.graphenee.vaadin.flow;

import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;

import io.graphenee.vaadin.flow.GxForbiddenView.ForbiddenException;
import jakarta.servlet.http.HttpServletResponse;

public class GxForbiddenView extends VerticalLayout implements HasErrorParameter<ForbiddenException> {

	private static final long serialVersionUID = 1L;

	private final NativeLabel error = new NativeLabel();

	public GxForbiddenView() {
		add(error);
	}

	@Override
	public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<ForbiddenException> parameter) {
		error.setText("Access is denied!");
		return HttpServletResponse.SC_FORBIDDEN;
	}

	public static class ForbiddenException extends RuntimeException {

	}

}
