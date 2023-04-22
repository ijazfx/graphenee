package io.graphenee.vaadin.flow.component;

import java.util.function.Supplier;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;

public class GxCopyToClipboardButton extends Button {

	private static final long serialVersionUID = 1L;

	public GxCopyToClipboardButton(Supplier<String> contentProvider) {
		setIcon(VaadinIcon.COPY_O.create());
		addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		addClickListener(cl -> {
			String content = contentProvider.get();
			StringBuilder sb = new StringBuilder();
			sb.append("const tempTextArea = document.createElement(\"textarea\");\n");
			sb.append("tempTextArea.value = \"" + content + "\";\n");
			sb.append("document.body.appendChild(tempTextArea);\n");
			sb.append("tempTextArea.select();\n");
			sb.append("document.execCommand(\"copy\");\n");
			sb.append("document.body.removeChild(tempTextArea);");
			UI.getCurrent().getPage().executeJs(sb.toString());
			GxNotification.success(content + " copied to clipboard!");
		});
	}

}
