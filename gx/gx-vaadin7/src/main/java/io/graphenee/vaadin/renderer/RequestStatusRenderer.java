package io.graphenee.vaadin.renderer;

import java.util.Locale;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.server.Resource;
import com.vaadin.ui.renderers.ImageRenderer;

import io.graphenee.core.vaadin.enums.RequestStatus;
import io.graphenee.gx.theme.graphenee.GrapheneeTheme;

public class RequestStatusRenderer extends ImageRenderer {

	public static final StatusRepresentation STATUS_IMAGE = new StatusRepresentation();

	public RequestStatusRenderer() {
	}

	public RequestStatusRenderer(RendererClickListener listener) {
		super(listener);
	}

	public static abstract class StatusConverter implements Converter<Resource, RequestStatus> {

		@Override
		public RequestStatus convertToModel(Resource value, Class<? extends RequestStatus> targetType, Locale locale)
				throws com.vaadin.data.util.converter.Converter.ConversionException {
			if (value == pendingStateResource())
				return RequestStatus.PENDING;
			else if (value == approvedStateResource())
				return RequestStatus.APPROVED;
			return RequestStatus.REJECTED;

		}

		@Override
		public Resource convertToPresentation(RequestStatus value, Class<? extends Resource> targetType, Locale locale)
				throws com.vaadin.data.util.converter.Converter.ConversionException {
			if (value == RequestStatus.PENDING)
				return pendingStateResource();
			else if (value == RequestStatus.APPROVED)
				return approvedStateResource();
			return rejectedStateResource();
		}

		@Override
		public Class<RequestStatus> getModelType() {
			return RequestStatus.class;
		}

		@Override
		public Class<Resource> getPresentationType() {
			return Resource.class;
		}

		protected abstract Resource pendingStateResource();

		protected abstract Resource approvedStateResource();

		protected abstract Resource rejectedStateResource();

	}

	public static class StatusRepresentation extends StatusConverter {

		Resource pendingStateResource;
		Resource approvedStateResource;
		Resource rejectedStateResource;

		public StatusRepresentation() {
			pendingStateResource = GrapheneeTheme.themeResource("images/status-pending.png");
			approvedStateResource = GrapheneeTheme.themeResource("images/status-approved.png");
			rejectedStateResource = GrapheneeTheme.themeResource("images/status-rejected.png");
		}

		@Override
		protected Resource pendingStateResource() {
			return pendingStateResource;
		}

		@Override
		protected Resource approvedStateResource() {
			return approvedStateResource;
		}

		@Override
		protected Resource rejectedStateResource() {
			return rejectedStateResource;
		}

	}

}