package io.graphenee.vaadin.flow.component;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasTheme;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;

@CssImport(value = "./styles/gx-common.css", themeFor = "vaadin-dialog-overlay")
public class GxDialog extends Dialog implements HasTheme {
	private static final long serialVersionUID = 1L;

	public GxDialog(Component... components) {
		super();
		add(components);
	}

	public void addThemeVariants(DialogVariant... dialogVariants) {
		getThemeNames().addAll(Stream.of(dialogVariants).map(DialogVariant::getVariantName).collect(Collectors.toList()));
	}

	public void removeThemeVariants(DialogVariant... dialogVariants) {
		getThemeNames().removeAll(Stream.of(dialogVariants).map(DialogVariant::getVariantName).collect(Collectors.toList()));
	}

}
