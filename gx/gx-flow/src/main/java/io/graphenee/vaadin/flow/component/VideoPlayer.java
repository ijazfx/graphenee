package io.graphenee.vaadin.flow.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.server.streams.DownloadHandler;

@SuppressWarnings("serial")
@Tag("video")
public class VideoPlayer extends Component implements HasSize {

	public VideoPlayer() {
		setSizeFull();
		getElement().setAttribute("controls", true);
	}

	public VideoPlayer(String src) {
		this();
		getElement().setAttribute("src", src);
	}

	public VideoPlayer(DownloadHandler downloadHandler) {
		this();
		getElement().setAttribute("src", downloadHandler);
	}

	public void setSrc(String src) {
		getElement().setAttribute("src", src);
	}

	public void setSrc(DownloadHandler downloadHandler) {
		getElement().setAttribute("src", downloadHandler);
	}

}
