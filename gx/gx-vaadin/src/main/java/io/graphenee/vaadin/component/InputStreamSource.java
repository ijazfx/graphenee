package io.graphenee.vaadin.component;

import java.io.InputStream;

import com.vaadin.server.StreamResource.StreamSource;

public class InputStreamSource implements StreamSource {

	private InputStream inputStream;

	public InputStreamSource(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	@Override
	public InputStream getStream() {
		return inputStream;
	}

}