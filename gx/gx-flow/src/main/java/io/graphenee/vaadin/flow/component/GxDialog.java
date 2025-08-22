package io.graphenee.vaadin.flow.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;

import lombok.extern.slf4j.Slf4j;

@CssImport(value = "./styles/graphenee.css", themeFor = "vaadin-dialog-overlay")
@Slf4j
public class GxDialog extends Dialog {
	private static final long serialVersionUID = 1L;

	public GxDialog(Component... components) {
		super();
		add(components);
	}

}
