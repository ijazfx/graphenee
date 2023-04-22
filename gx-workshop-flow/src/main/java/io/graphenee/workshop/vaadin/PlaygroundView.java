package io.graphenee.workshop.vaadin;

import java.util.UUID;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import io.graphenee.vaadin.flow.base.GxSecuredView;
import io.graphenee.vaadin.flow.base.GxVerticalLayoutView;
import io.graphenee.vaadin.flow.component.GxCopyToClipboardWrapper;

@GxSecuredView
@Route(value = "playground", layout = MainLayout.class)
public class PlaygroundView extends GxVerticalLayoutView {

	private static final long serialVersionUID = 1L;

	@Override
	protected void decorateLayout(HasComponents rootLayout) {
		Label label1 = new Label(UUID.randomUUID().toString());
		rootLayout.add(new GxCopyToClipboardWrapper(label1));
		TextField label2 = new TextField("UUID");
		label2.setValue(UUID.randomUUID().toString());
		rootLayout.add(new GxCopyToClipboardWrapper(label2));
	}

}
