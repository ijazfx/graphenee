package io.graphenee.workshop.vaadin;

import java.util.UUID;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import io.graphenee.vaadin.flow.GxSecuredView;
import io.graphenee.vaadin.flow.GxVerticalLayoutView;
import io.graphenee.vaadin.flow.component.GxCopyToClipboardWrapper;
import io.graphenee.workshop.vaadin.lit.GxEcho;

@GxSecuredView
@Route(value = "playground", layout = MainLayout.class)
public class PlaygroundView extends GxVerticalLayoutView {

	private static final long serialVersionUID = 1L;

	@Override
	protected void decorateLayout(HasComponents rootLayout) {
		NativeLabel label1 = new NativeLabel(UUID.randomUUID().toString());
		rootLayout.add(new GxCopyToClipboardWrapper(label1));
		TextField label2 = new TextField("UUID");
		label2.setValue(UUID.randomUUID().toString());
		rootLayout.add(new GxCopyToClipboardWrapper(label2));

		GxEcho echo = new GxEcho();

		TextField message = new TextField("Message");
		message.addValueChangeListener(vcl -> {
			echo.setMessage(vcl.getValue());
		});

		rootLayout.add(message, echo);

	}

}
