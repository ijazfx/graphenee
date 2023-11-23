package io.graphenee.workshop.vaadin;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.NotFoundException;

import io.graphenee.core.model.GxAuthenticatedUser;
import io.graphenee.vaadin.flow.utils.DashboardUtils;
import jakarta.servlet.http.HttpServletResponse;

public class NotFoundView extends Div implements HasErrorParameter<NotFoundException> {

    private static final long serialVersionUID = 1L;

    private final Label error = new Label();

    public NotFoundView() {
        add(error);
    }

    @Override
    public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<NotFoundException> parameter) {
        GxAuthenticatedUser user = DashboardUtils.getLoggedInUser();
        if (user == null || event.getLocation().getPath().startsWith("login")) {
            event.rerouteTo("login", event.getLocation().getPath());
            return HttpServletResponse.SC_FOUND;
        }
        error.setText(parameter.getCustomMessage());
        return HttpServletResponse.SC_NOT_FOUND;
    }

}
