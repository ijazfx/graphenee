package io.graphenee.vaadin.flow.component;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.FlexLayout.FlexDirection;
import com.vaadin.flow.component.orderedlayout.FlexLayout.FlexWrap;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import io.graphenee.vaadin.flow.event.TRDelayClickListener;
import lombok.extern.slf4j.Slf4j;

@CssImport(value = "./styles/graphenee.css", themeFor = "vaadin-dialog-overlay")
@Slf4j
public class GxDialog extends Dialog {
	private static final long serialVersionUID = 1L;

	private GxDialogDelegate delegate;

	public GxDialog(Component... components) {
		this("Dialog", components);
	}

	public GxDialog(String dialogTitle, Component... components) {
		super();
		add(buildLayout(dialogTitle, components));
	}

	private Component buildLayout(String dialogTitle, Component... components) {
		HorizontalLayout dlgTitleLayout = new HorizontalLayout();
		dlgTitleLayout.addClassName("gx-form-title-layout");
		dlgTitleLayout.addClassName("draggable");
		dlgTitleLayout.setWidthFull();
		NativeLabel formTitleLabel = new NativeLabel(dialogTitle);
		formTitleLabel.addClassName("gx-form-title");
		dlgTitleLayout.add(formTitleLabel);
		addComponentAsFirst(dlgTitleLayout);
		Button dlgDismissButton = new Button("Dismiss");
		dlgDismissButton.addClickListener(new TRDelayClickListener<Button>() {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(ClickEvent<Button> event) {
				try {
					if (delegate != null) {
						delegate.onDismiss();
					}
				} catch (Exception ex) {
					log.error(ex.getMessage(), ex);
				} finally {
					close();
				}
			}
		});
		HorizontalLayout dlgFooterLayout = new HorizontalLayout(dlgDismissButton);
		dlgFooterLayout.addClassName("gx-form-footer");
		dlgFooterLayout.setWidthFull();
		FlexLayout layout = new FlexLayout();
		layout.setSizeFull();
		layout.setFlexDirection(FlexDirection.COLUMN);
		layout.setFlexWrap(FlexWrap.NOWRAP);
		layout.add(dlgTitleLayout);
		layout.add(components);
		layout.add(dlgFooterLayout);
		return layout;
	}

	public void addThemeVariants(DialogVariant... dialogVariants) {
		getThemeNames()
				.addAll(Stream.of(dialogVariants).map(DialogVariant::getVariantName).collect(Collectors.toList()));
	}

	public void removeThemeVariants(DialogVariant... dialogVariants) {
		getThemeNames()
				.removeAll(Stream.of(dialogVariants).map(DialogVariant::getVariantName).collect(Collectors.toList()));
	}

	public void setDelegate(GxDialogDelegate delegate) {
		this.delegate = delegate;
	}

	public static interface GxDialogDelegate {
		void onDismiss();
	}

}
