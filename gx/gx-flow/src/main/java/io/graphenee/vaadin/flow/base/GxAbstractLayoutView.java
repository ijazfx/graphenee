package io.graphenee.vaadin.flow.base;

import javax.annotation.PostConstruct;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
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
        H3 lblCaption = new H3(getCaption());
        lblCaption.getElement().getStyle().set("padding", "0");
        lblCaption.getElement().getStyle().set("font-size", "var(--lumo-font-size-xl");
        lblCaption.getElement().getStyle().set("color", "var(--lumo-primary-color");
        lblCaption.getElement().getStyle().set("margin-bottom", "0.4em");
        lblCaption.getElement().getStyle().set("margin-left", "0.85em");
        lblCaption.getElement().getStyle().set("margin-top", "1em");
        lblCaption.getElement().getStyle().set("border-bottom", "0.2em solid var(--lumo-primary-color)");
        lblCaption.getElement().getStyle().set("display", "inline-block");
        return lblCaption;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (this.getClass().isAnnotationPresent(GxSecuredView.class)) {
            GxSecuredView annotation = this.getClass().getAnnotation(GxSecuredView.class);
            String route = null;
            if (annotation.value() != null) {
                route = annotation.value();
            } else if (this.getClass().isAnnotationPresent(Route.class)) {
                Route routeAnnotation = this.getClass().getAnnotation(Route.class);
                route = routeAnnotation.value();
            }
            GxAuthenticatedUser user = VaadinSession.getCurrent().getAttribute(GxAuthenticatedUser.class);
            if (user == null) {
                event.rerouteTo("login", route);
            } else if (!route.equals("") && !user.canDoAction(route, "view")) {
                event.rerouteTo("");
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
