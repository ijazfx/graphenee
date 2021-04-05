package io.graphenee.vaadin.flow.base;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Label;

@CssImport("./styles/gx-common.css")
public class GxLabel {

    public static Label infoLabel(String text) {
        Label label = new Label(text);
        label.addClassName("gx-info-color");
        return label;
    }

    public static Label warningLabel(String text) {
        Label label = new Label(text);
        label.addClassName("gx-warning-color");
        return label;
    }

    public static Label successLabel(String text) {
        Label label = new Label(text);
        label.addClassName("gx-success-color");
        return label;
    }

}
