package io.graphenee.vaadin.flow.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.server.StreamResource;

import lombok.Setter;

/**
 * An audio player component.
 */
@SuppressWarnings("serial")
@Tag("audio")
public class AudioPlayer extends Component {

	@Setter
	private StreamResource resource;

	/**
	 * Creates a new instance of this component.
	 */
	public AudioPlayer() {
		getElement().setAttribute("controls", true);
	}

	/**
	 * Creates a new instance of this component.
	 * @param src The source of the audio.
	 */
	public AudioPlayer(String src) {
		this();
		getElement().setAttribute("src", src);
	}

	/**
	 * Creates a new instance of this component.
	 * @param resource The resource of the audio.
	 */
	public AudioPlayer(StreamResource resource) {
		this();
		this.resource = resource;
		getElement().setAttribute("src", resource);
	}

}
