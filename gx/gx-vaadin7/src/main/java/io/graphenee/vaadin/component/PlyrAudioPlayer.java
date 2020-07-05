package io.graphenee.vaadin.component;

import org.vaadin.viritin.label.MLabel;

import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.StyleSheet;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.HorizontalLayout;

@SuppressWarnings("serial")
@JavaScript({ "https://cdn.plyr.io/3.6.2/plyr.polyfilled.js", "plyr.js" })
@StyleSheet({ "https://cdn.plyr.io/3.6.2/plyr.css" })
public class PlyrAudioPlayer extends HorizontalLayout {

	public PlyrAudioPlayer() {
		MLabel html = new MLabel().withContentMode(ContentMode.HTML);

		StringBuilder sb = new StringBuilder();
		sb.append("<audio id=\"player\" controls>");
		sb.append("</audio>");

		html.setValue(sb.toString());
		addComponent(html);

		addAttachListener(new AttachListener() {

			@Override
			public void attach(AttachEvent event) {
				System.err.println(event.getConnector());
				com.vaadin.ui.JavaScript.eval("gxPlyrInit('player');");
			}
		});

	}

	public void setAudioUrl(String streamUrl) {
		System.err.println(streamUrl);
		com.vaadin.ui.JavaScript.eval("gxPlyrSetAudioUrl('player', '" + streamUrl + "')");
	}

}
