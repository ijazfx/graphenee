package io.graphenee.vaadin.flow;

import java.util.List;
import java.util.Locale;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.server.VaadinSession;

import io.graphenee.vaadin.flow.utils.GxTranslationUtils;

public class GxLanguageBar extends FlexLayout {

    public GxLanguageBar() {
        List<Locale> locales = GxTranslationUtils.getProvidedLocales();
        if (locales.size() > 1) {
            HorizontalLayout layout = new HorizontalLayout();
            layout.addClassName("gx-lang-selection-bar");
            locales.forEach(l -> {
                Button langButton = new Button(l.getDisplayLanguage(l));
                langButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
                langButton.addClassName("gx-lang-button");
                // langButton.getStyle().set("font-size",
                // "var(--lumo-font-size-m)").set("margin", "0");
                // langButton.getStyle().set("color", "var(--lumo-base-color)");
                // langButton.addThemeVariants(ButtonVariant.LUMO_ICON);
                langButton.addClickListener(cl -> {
                    cl.getSource().getParent().get().getChildren().forEach(c -> {
                        c.removeClassName("gx-lang-selected");
                        layout.remove(cl.getSource());
                        layout.addComponentAsFirst(cl.getSource());
                    });
                    cl.getSource().addClassName("gx-lang-selected");
                    VaadinSession.getCurrent().setLocale(l);
                    getUI().ifPresent(ui -> ui.setLocale(l));
                });
                Locale selectedLocale = VaadinSession.getCurrent().getLocale();
                if (l.equals(selectedLocale)) {
                    langButton.addClassName("gx-lang-selected");
                    layout.addComponentAsFirst(langButton);
                } else {
                    layout.add(langButton);
                }
            });
            add(layout);
        }
    }

}
