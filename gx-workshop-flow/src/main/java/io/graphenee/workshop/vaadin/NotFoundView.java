package io.graphenee.workshop.vaadin;

import javax.servlet.http.HttpServletResponse;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.NotFoundException;


public class NotFoundView extends Div implements HasErrorParameter<NotFoundException> {

    private static final long serialVersionUID = 1L;
    
    private final Label error = new Label();

    public NotFoundView() {
        add(error);
    }

    @Override
    public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<NotFoundException> parameter) {
        error.setText("Cannot find URL: " + event.getLocation().getPath());
        return HttpServletResponse.SC_NOT_FOUND;
    }
    
}
