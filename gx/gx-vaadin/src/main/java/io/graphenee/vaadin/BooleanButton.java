package io.graphenee.vaadin;

import com.vaadin.server.Resource;
import com.vaadin.ui.Image;

import io.graphenee.core.callback.TRParamCallback;
import io.graphenee.gx.theme.graphenee.GrapheneeTheme;

@SuppressWarnings("serial")
public abstract class BooleanButton extends Image {

	private boolean currentState = false;

	private TRParamCallback<Boolean> onStateChangeCallback;

	public BooleanButton() {
		this(false);
	}

	public BooleanButton(boolean initialState) {
		currentState = initialState;
		updateImage();
		addClickListener(event -> {
			try {
				if (onStateChangeCallback != null)
					onStateChangeCallback.execute(!currentState);
				currentState = !currentState;
				updateImage();
			} catch (Exception ex) {
				throw ex;
			}
		});
	}

	private void updateImage() {
		if (currentState) {
			setSource(trueStateResource());
			markAsDirty();
		} else {
			setSource(falseStateResource());
			markAsDirty();
		}
	}

	protected abstract Resource trueStateResource();

	protected abstract Resource falseStateResource();

	public void setOnStateChangeCallback(TRParamCallback<Boolean> onStateChangeCallback) {
		this.onStateChangeCallback = onStateChangeCallback;
	}

	public static class ToggleButton extends BooleanButton {

		public ToggleButton() {
			super(false);
		}

		public ToggleButton(boolean initialState) {
			super(initialState);
		}

		@Override
		protected Resource trueStateResource() {
			return GrapheneeTheme.themeResource("images/toggle-on.png");
		}

		@Override
		protected Resource falseStateResource() {
			return GrapheneeTheme.themeResource("images/toggle-off.png");
		}

	}

	public static class CheckBoxButton extends BooleanButton {

		public CheckBoxButton() {
			super(false);
		}

		public CheckBoxButton(boolean initialState) {
			super(initialState);
		}

		@Override
		protected Resource trueStateResource() {
			return GrapheneeTheme.themeResource("images/checked_checkbox.png");
		}

		@Override
		protected Resource falseStateResource() {
			return GrapheneeTheme.themeResource("images/unchecked_checkbox.png");
		}

	}

}
