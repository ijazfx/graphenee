
package io.graphenee.vaadin.flow.component;

import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executors;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.dom.DomEvent;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.shared.Registration;

import io.graphenee.util.TRCalendarUtil;
import io.graphenee.util.callback.TRVoidCallback;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class GxDownloadButton extends Button {
	private static final long serialVersionUID = 1L;

	@Setter
	@Getter
	private String defaultFileName = TRCalendarUtil.getCurrentTimeStamp() + "-download";

	@Setter
	@Getter
	private InputStreamFactory inputStreamFactory;

	@Setter
	@Getter
	private boolean iconVisible = true;

	@Setter
	private TRVoidCallback beforeDownloadCallback;

	private Anchor anchor;

	public GxDownloadButton(String text) {
		super(text);

		if (isIconVisible())
			setIcon(VaadinIcon.FILE_ZIP.create());

		setDisableOnClick(true);
		addThemeVariants(ButtonVariant.LUMO_ICON);

		addClickListener(event -> {
			Button button = event.getSource();
			button.setText("Preparing Download...");
			getParent().ifPresent(component -> {
				Objects.requireNonNull(defaultFileName, "File name  must not be null");
				Objects.requireNonNull(inputStreamFactory, "Stream resource must not be null");

				if (anchor == null) {
					anchor = new Anchor();
					Element anchorElement = anchor.getElement();
					anchorElement.setAttribute("download", true);
					anchorElement.getStyle().set("display", "none");
					component.getElement().appendChild(anchor.getElement());

					anchorElement.addEventListener("click", listener -> fireEvent(new DownloadStartEvent(this, true, listener)));
				}

				if (beforeDownloadCallback != null) {
					beforeDownloadCallback.execute();
				}
				Optional<UI> optionalUI = getUI();
				Executors.newVirtualThreadPerTaskExecutor().execute(() -> {
					try {
						InputStream is = getInputStreamFactory().createInputStream();
						StreamResource streamResource = new StreamResource(defaultFileName, () -> is);
						streamResource.setCacheTime(0);
						optionalUI.ifPresent(ui -> ui.access(() -> {
							anchor.setHref(streamResource);
							anchor.getElement().callJsFunction("click");
							ui.push();
						}));
					} catch (Exception e) {
						e.printStackTrace();
						throw new RuntimeException(e);
					}
				});
			});
		});

		addDownloadStartListener(listener -> {
			GxDownloadButton button = listener.getSource();
			button.setText(text);
			button.setEnabled(true);
		});
	}

	@Override
	protected void onDetach(DetachEvent detachEvent) {
		if (anchor != null) {
			getParent().map(Component::getElement).ifPresent(parentElement -> {
				Element anchorElement = anchor.getElement();
				if (anchorElement != null && parentElement.getChildren().anyMatch(anchorElement::equals)) {
					parentElement.removeChild(anchorElement);
				}
			});
		}
	}

	public Registration addDownloadStartListener(ComponentEventListener<DownloadStartEvent> listener) {
		Registration registration = addListener(DownloadStartEvent.class, listener);
		return registration;
	}

	@SuppressWarnings("serial")
	public static class DownloadStartEvent extends ComponentEvent<GxDownloadButton> {

		private final DomEvent clientSideEvent;

		public DownloadStartEvent(GxDownloadButton source, boolean fromClient, DomEvent clientSideEvent) {
			super(source, fromClient);
			this.clientSideEvent = clientSideEvent;
		}

		public DomEvent getClientSideEvent() {
			return clientSideEvent;
		}
	}

}