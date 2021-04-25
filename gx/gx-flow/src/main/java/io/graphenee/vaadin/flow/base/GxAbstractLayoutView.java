package io.graphenee.vaadin.flow.base;

import javax.annotation.PostConstruct;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.server.VaadinSession;

import io.graphenee.core.model.GxAuthenticatedUser;

@CssImport("./styles/gx-common.css")
public abstract class GxAbstractLayoutView extends Div implements BeforeEnterObserver, AfterNavigationObserver {

    private static final long serialVersionUID = 1L;

    public GxAbstractLayoutView() {
        setSizeFull();
        getElement().getStyle().set("margin", "0");
        getElement().getStyle().set("padding", "0");
        addClassName("gx-abstract-layout-view");
    }

    @PostConstruct
    private void postBuild() {
        add(getCaptionComponent());
        Component rootLayout = getLayoutComponent();
        if (rootLayout instanceof HasComponents) {
            decorateLayout((HasComponents) rootLayout);
        }

        add(rootLayout);

    }

    protected abstract Component getLayoutComponent();

    protected void decorateLayout(HasComponents rootLayout) {
    }

    protected String getCaption() {
        return null;
    }

    protected Component getCaptionComponent() {
        VerticalLayout captionComponent = new VerticalLayout(new H4(getCaption()));
        captionComponent.setMargin(false);
        captionComponent.setSpacing(false);
        // captionComponent.setPadding(true);
        return captionComponent;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (this.getClass().isAnnotationPresent(GxSecuredView.class)) {
            GxAuthenticatedUser user = VaadinSession.getCurrent().getAttribute(GxAuthenticatedUser.class);
            if (user == null) {
                event.rerouteTo("login");
            }
        }
    }

    protected boolean isLoggedIn() {
        return VaadinSession.getCurrent().getAttribute(GxAuthenticatedUser.class) != null;
    }

    @SuppressWarnings("unchecked")
    protected <T extends GxAuthenticatedUser> T loggedInUser() {
        return (T) VaadinSession.getCurrent().getAttribute(GxAuthenticatedUser.class);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
    }

}
