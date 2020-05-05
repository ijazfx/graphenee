package io.graphenee.vaadin.renderer;

import java.util.Locale;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.server.Resource;
import com.vaadin.ui.renderers.ImageRenderer;

import io.graphenee.core.enums.RequestStatus;
import io.graphenee.gx.theme.graphenee.GrapheneeTheme;

/**
 * @author Ahmad Shafique
 */
public class RequestStatusRenderer extends ImageRenderer {

	public static final RequestStatusConverter REQUEST_STATUS_CONVERTER = new RequestStatusConverter();

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
			else if (value == objectionStateResource())
				return RequestStatus.OBJECTION;
			return RequestStatus.REJECTED;

		}

		@Override
		public Resource convertToPresentation(RequestStatus value, Class<? extends Resource> targetType, Locale locale)
				throws com.vaadin.data.util.converter.Converter.ConversionException {
			if (value == RequestStatus.PENDING)
				return pendingStateResource();
			else if (value == RequestStatus.APPROVED)
				return approvedStateResource();
			else if (value == RequestStatus.OBJECTION)
				return objectionStateResource();
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

		protected abstract Resource objectionStateResource();

	}

	public static class RequestStatusConverter extends StatusConverter {

		Resource pendingStateResource;
		Resource approvedStateResource;
		Resource rejectedStateResource;
		Resource objectionStateResource;

		public RequestStatusConverter() {
			pendingStateResource = GrapheneeTheme.themeResource("images/status-pending.png");
			approvedStateResource = GrapheneeTheme.themeResource("images/status-approved.png");
			rejectedStateResource = GrapheneeTheme.themeResource("images/status-rejected.png");
			objectionStateResource = GrapheneeTheme.themeResource("images/status-objection.png");
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

		@Override
		protected Resource objectionStateResource() {
			return objectionStateResource;
		}

	}

}