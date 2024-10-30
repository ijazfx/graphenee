package io.graphenee.core.model.entity;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GxDocumentFilterBuilder {

	Set<String> mimeTypes = new HashSet<>();
	Set<String> extensions = new HashSet<>();

	private GxDocumentFilterBuilder() {
	}

	public static GxDocumentFilterBuilder builder() {
		return new GxDocumentFilterBuilder();
	}

	public GxDocumentFilterBuilder addMimeType(String... mimeType) {
		if (mimeType != null)
			mimeTypes.addAll(Stream.of(mimeType).collect(Collectors.toSet()));
		return this;
	}

	public GxDocumentFilterBuilder addExtension(String... extension) {
		if (extension != null)
			extensions.addAll(Stream.of(extension).collect(Collectors.toSet()));
		return this;
	}

	public GxDocumentFilter build() {
		GxDocumentFilter filter = new GxDocumentFilter();
		filter.mimeTypes.addAll(mimeTypes);
		filter.extensions.addAll(extensions);
		return filter;
	}

	public static GxDocumentFilter imageFilter() {
		return GxDocumentFilterBuilder.builder().addMimeType("image/").addExtension("png", "jpg", "jpeg", "svg", "gif").build();
	}

	public static GxDocumentFilter videoFilter() {
		return GxDocumentFilterBuilder.builder().addMimeType("video/").addExtension("m4a", "mp4", "mpeg", "avi", "mov").build();
	}

	public static GxDocumentFilter pdfFilter() {
		return GxDocumentFilterBuilder.builder().addMimeType("application/pdf").addExtension("pdf").build();
	}

	public static GxDocumentFilter mediaFilter() {
		GxDocumentFilter imageFilter = imageFilter();
		GxDocumentFilter videoFilter = videoFilter();
		GxDocumentFilterBuilder builder = GxDocumentFilterBuilder.builder();
		imageFilter.extensions.forEach(i -> builder.addExtension(i));
		imageFilter.mimeTypes.forEach(i -> builder.addMimeType(i));
		videoFilter.extensions.forEach(i -> builder.addExtension(i));
		videoFilter.mimeTypes.forEach(i -> builder.addMimeType(i));
		return builder.build();
	}

}