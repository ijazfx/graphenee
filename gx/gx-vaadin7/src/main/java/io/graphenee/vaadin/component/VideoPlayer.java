package io.graphenee.vaadin.component;

import java.util.UUID;

import com.vaadin.server.Page;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;

import org.vaadin.viritin.label.MLabel;

import io.graphenee.gx.theme.graphenee.GrapheneeTheme;

@SuppressWarnings("serial")
// @JavaScript({ "https://cdn.plyr.io/3.6.2/plyr.polyfilled.js" })
// @StyleSheet({ "https://cdn.plyr.io/3.6.2/plyr.css" })
public class VideoPlayer extends VerticalLayout {
	
	String playerId = "player_" + UUID.randomUUID().toString().replace("-", "");
	
	public VideoPlayer() {
		MLabel html = new MLabel().withContentMode(ContentMode.HTML).withStyleName(GrapheneeTheme.STYLE_DISPLAY_INLINE);

		int wndHeight = Page.getCurrent().getBrowserWindowHeight();
		int wndWidth = Page.getCurrent().getBrowserWindowWidth();

		if(wndHeight > wndWidth)
			wndHeight /= 1.5;
		else
			wndWidth *= 0.67;

		int videoHeight = (int) (wndHeight * 0.9);
		int videoWidth = (int) (wndWidth * 0.9);

		StringBuilder sb = new StringBuilder();
		sb.append("<video width=\"" + videoWidth + "\" height=\"" + videoHeight + "\" id=\"" + playerId + "\" playsinline controls>");
		sb.append("</video>");

		html.setValue(sb.toString());
		addComponent(html);
		setComponentAlignment(html, Alignment.MIDDLE_CENTER);

		// addAttachListener(new AttachListener() {

		// 	@Override
		// 	public void attach(AttachEvent event) {
		// 		com.vaadin.ui.JavaScript.eval("new Plyr(document.getElementById('" + playerId + "'));");
		// 	}
		// });

	}

	public void setUrl(String streamUrl, String mimeType) {
		StringBuilder sb = new StringBuilder();
		sb.append("document.getElementById('" + playerId + "').src = '" + streamUrl + "';");
		sb.append("document.getElementById('" + playerId + "').type = '" + mimeType + "';");
		com.vaadin.ui.JavaScript.eval(sb.toString());
	}

}
