package io.graphenee.vaadin.flow.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.icon.Icon;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GxMenuItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private String label;

    private Icon icon;
    private String route;
    private Class<? extends Component> componentClass;

    private List<GxMenuItem> children;

    public GxMenuItem(String label, Icon icon, String route) {
        this.label = label;
        this.icon = icon;
        this.route = route;
    }

    public GxMenuItem(String label, Icon icon, Class<? extends Component> componentClass) {
        this.label = label;
        this.icon = icon;
        this.componentClass = componentClass;
    }

    public GxMenuItem(String label, Icon icon) {
        this.label = label;
        this.icon = icon;
    }

    public GxMenuItem(String label, String route) {
        this.label = label;
        this.route = route;
    }

    public GxMenuItem add(GxMenuItem child) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(child);
        return this;
    }

    public boolean hasChildren() {
        return children != null && !children.isEmpty();
    }

    public static GxMenuItem create(String label, String route) {
        GxMenuItem mi = new GxMenuItem(label, route);
        return mi;
    }

    public static GxMenuItem create(String label, Icon icon, String route) {
        GxMenuItem mi = new GxMenuItem(label, icon, route);
        return mi;
    }

    public static GxMenuItem create(String label, Icon icon, Class<? extends Component> componentClass) {
        GxMenuItem mi = new GxMenuItem(label, icon, componentClass);
        return mi;
    }

    public static GxMenuItem create(String label, Icon icon) {
        GxMenuItem mi = new GxMenuItem(label, icon);
        return mi;
    }

}
