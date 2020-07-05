package io.graphenee.vaadin.component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

import org.vaadin.viritin.button.MButton;

import com.vaadin.annotations.JavaScript;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.JavaScriptFunction;
import com.vaadin.ui.themes.ValoTheme;

import elemental.json.JsonArray;

@SuppressWarnings("serial")
@JavaScript({ "record-audio.js" })
public class RecordAudioComponent extends HorizontalLayout {

	private RecordAudioDelegate delegate;

	private int status = 0;

	public RecordAudioComponent() {
		this(FontAwesome.MICROPHONE, FontAwesome.STOP, FontAwesome.PLAY, FontAwesome.TRASH);
	}

	public RecordAudioComponent(Resource recordIcon, Resource stopIcon, Resource playIcon, Resource deleteIcon) {
		MButton recordButton = new MButton().withIcon(recordIcon).withStyleName(ValoTheme.BUTTON_ICON_ONLY);
		MButton deleteButton = new MButton().withIcon(deleteIcon).withStyleName(ValoTheme.BUTTON_ICON_ONLY).withVisible(false);

		final FileOutputStream[] tempFos = new FileOutputStream[1];

		recordButton.addClickListener(new ClickListener() {

			private File audioFile;

			@Override
			public void buttonClick(ClickEvent event) {
				switch (status) {
				case 0:
					try {
						audioFile = File.createTempFile("recording", "webm");
						tempFos[0] = new FileOutputStream(audioFile);
					} catch (IOException e) {
						e.printStackTrace();
					}
					// record audio
					com.vaadin.ui.JavaScript.eval("gxStartRecording()");
					status = 1;
					recordButton.setIcon(stopIcon);
				break;
				case 1:
					// stop recording
					com.vaadin.ui.JavaScript.eval("gxStopRecording()");
					status = 2;
					recordButton.setIcon(playIcon);
					if (delegate != null)
						delegate.onAudioAvailable(audioFile);
				break;
				case 2:
					// play audio
					com.vaadin.ui.JavaScript.eval("gxPlayAudio()");
					status = 3;
					recordButton.setIcon(stopIcon);
				break;
				case 3:
					// play audio
					com.vaadin.ui.JavaScript.eval("gxStopAudio()");
					status = 2;
					recordButton.setIcon(playIcon);
				break;
				}
				deleteButton.setVisible(status >= 2);
			}
		});

		deleteButton.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				// delete audio
				com.vaadin.ui.JavaScript.eval("gxDeleteAudio()");
				status = 0;
				recordButton.setIcon(recordIcon);
				deleteButton.setVisible(false);
			}
		});

		CssLayout buttonLayout = new CssLayout();
		buttonLayout.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

		buttonLayout.addComponents(recordButton, deleteButton);

		addComponents(buttonLayout);

		com.vaadin.ui.JavaScript.getCurrent().addFunction("io.graphenee.vaadin.component.record_audio.uploadFile", new JavaScriptFunction() {

			@Override
			public void call(JsonArray arguments) {
				//				String content = arguments.getString(0);
				//				String[] parts = content.split("base64,");
				//				if (tempFos[0] != null) {
				//					try {
				//						byte[] decoded = Base64.getDecoder().decode(parts[1].getBytes());
				//						tempFos[0].write(decoded);
				//					} catch (Exception e) {
				//						e.printStackTrace();
				//					}
				//				}
			}
		});

		com.vaadin.ui.JavaScript.getCurrent().addFunction("io.graphenee.vaadin.component.record_audio.uploadChunk", new JavaScriptFunction() {

			@Override
			public void call(JsonArray arguments) {
				String content = arguments.getString(0);
				String[] parts = content.split("base64,");
				if (tempFos[0] != null) {
					try {
						byte[] decoded = Base64.getDecoder().decode(parts[1].getBytes());
						tempFos[0].write(decoded);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	public RecordAudioDelegate getDelegate() {
		return delegate;
	}

	public void setDelegate(RecordAudioDelegate delegate) {
		this.delegate = delegate;
	}

	public static interface RecordAudioDelegate {
		void onAudioAvailable(File audioFile);
	}

}
