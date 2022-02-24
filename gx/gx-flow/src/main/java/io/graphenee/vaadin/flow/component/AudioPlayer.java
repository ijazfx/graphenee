package io.graphenee.vaadin.flow.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.server.StreamResource;

import lombok.Setter;

@Tag("audio")
public class AudioPlayer extends Component {

    @Setter
    private StreamResource resource;

    public AudioPlayer() {
        getElement().setAttribute("controls", true);
    }

    public AudioPlayer(String src) {
        this();
        getElement().setAttribute("src", src);
    }

    public AudioPlayer(StreamResource resource) {
        this();
        this.resource = resource;
        getElement().setAttribute("src", resource);
    }

}
