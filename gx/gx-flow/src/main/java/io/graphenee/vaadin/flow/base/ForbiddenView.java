package io.graphenee.vaadin.flow.base;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;

import io.graphenee.vaadin.flow.base.ForbiddenView.ForbiddenException;
import jakarta.servlet.http.HttpServletResponse;

public class ForbiddenView extends VerticalLayout implements HasErrorParameter<ForbiddenException> {

	private static final long serialVersionUID = 1L;

	private final Label error = new Label();

	public ForbiddenView() {
		add(error);
	}

	@Override
	public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<ForbiddenException> parameter) {
		error.setText(parameter.getCustomMessage());
		return HttpServletResponse.SC_FORBIDDEN;
	}

	public static class ForbiddenException extends RuntimeException {

	}

}
