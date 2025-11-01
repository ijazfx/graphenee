package io.graphenee.vaadin.flow.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.server.streams.DownloadHandler;

/**
 * An audio player component.
 */
@SuppressWarnings("serial")
@Tag("audio")
public class AudioPlayer extends Component {

	/**
	 * Creates a new instance of this component.
	 */
	public AudioPlayer() {
		getElement().setAttribute("controls", true);
	}

	/**
	 * Creates a new instance of this component.
	 * 
	 * @param src The source of the audio.
	 */
	public AudioPlayer(String src) {
		this();
		getElement().setAttribute("src", src);
	}

	/**
	 * Creates a new instance of this component.
	 * 
	 * @param downloadHandler The resource of the audio.
	 */
	public AudioPlayer(DownloadHandler downloadHandler) {
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
