/*******************************************************************************
 * Copyright (c) 2016, 2018 Farrukh Ijaz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
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
		setPrimaryStyleName("gx-boolean-button");
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

	public void setButtonState(boolean state) {
		currentState = state;
		updateImage();
	}

	private void updateImage() {
		if (currentState) {
			setSource(trueStateResource());
			//setCaption("True");
			markAsDirty();
		} else {
			setSource(falseStateResource());
			//setCaption("False");
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
