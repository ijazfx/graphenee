package io.graphenee.vaadin.flow;

import java.util.Locale;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.i18n.I18NProvider;
import com.vaadin.flow.server.VaadinSession;

@Component
@Scope("prototype")
public class GxLanguageBar extends FlexLayout {

    public GxLanguageBar(I18NProvider i18nProvider) {
        HorizontalLayout layout = new HorizontalLayout();
        layout.addClassName("gx-lang-selection-bar");
        i18nProvider.getProvidedLocales().forEach(l -> {
            Button langButton = new Button(l.getLanguage());
            langButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
            langButton.addClassName("gx-lang-button");
            // langButton.getStyle().set("font-size",
            // "var(--lumo-font-size-m)").set("margin", "0");
            // langButton.getStyle().set("color", "var(--lumo-base-color)");
            langButton.addThemeVariants(ButtonVariant.LUMO_ICON);
            langButton.addClickListener(cl -> {
                cl.getSource().getParent().get().getChildren().forEach(c -> {
                    c.removeClassName("gx-lang-selected");
                });
                cl.getSource().addClassName("gx-lang-selected");

                VaadinSession.getCurrent().setLocale(l);
            });
            Locale selectedLocale = VaadinSession.getCurrent().getLocale();
            if (l.equals(selectedLocale)) {
                langButton.addClassName("gx-lang-selected");
            }
            layout.add(langButton);
        });
        add(layout);
    }

}
