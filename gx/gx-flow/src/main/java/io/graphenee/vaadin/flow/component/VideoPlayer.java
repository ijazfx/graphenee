package io.graphenee.vaadin.flow.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.server.StreamResource;

import lombok.Setter;

@Tag("video")
public class VideoPlayer extends Component implements HasSize {

    @Setter
    private StreamResource resource;

    public VideoPlayer() {
        setSizeFull();
        getElement().setAttribute("controls", true);
    }

    public VideoPlayer(String src) {
        this();
        getElement().setAttribute("src", src);
    }

    public VideoPlayer(StreamResource resource) {
        this();
        this.resource = resource;
        getElement().setAttribute("src", resource);
    }

}
