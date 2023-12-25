package io.graphenee.vaadin.flow;

import org.springframework.stereotype.Service;

import com.google.common.eventbus.EventBus;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;

import lombok.Getter;

@Service
@VaadinSessionScope
public class GxEventBus extends EventBus {

	public GxEventBus() {
		super("gx-event-bus");
	}

	public static enum TargetArea {
		MAIN,
		END_DRAWER,
		MINIMIZED_BAR
	}

	@Getter
	public static class ShowComponentEvent {

		private Component component;
		private String width;
		private TargetArea area;

		public ShowComponentEvent(Component c, TargetArea area) {
			this(c, area, "100%");
		}

		public ShowComponentEvent(Component c, TargetArea area, String width) {
			this.component = c;
			this.area = area;
			this.width = width;
		}

	}

	@Getter
	public static class RemoveComponentEvent {

		private Component component;

		public RemoveComponentEvent(Component c) {
			this.component = c;

		}

	}

	@Getter
	public static class ResizeComponentEvent {

		private Component component;
		private String width;

		public ResizeComponentEvent(Component c, String width) {
			this.component = c;
			this.width = width;

		}

	}

	@Getter
	public static class TagComponentEvent {

		private Component component;
		private String width;
		private String title;

		public TagComponentEvent(Component c, String width, String title) {
			this.component = c;
			this.width = width;
			this.title = title;

		}

	}

}
