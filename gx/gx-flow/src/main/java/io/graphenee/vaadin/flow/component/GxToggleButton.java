package io.graphenee.vaadin.flow.component;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.shared.Registration;

import io.graphenee.util.callback.TRParamCallback;

@SuppressWarnings("serial")
public class GxToggleButton extends Button {

	private Component trueStateIcon;
	private Component falseStateIcon;
	private Boolean state;
	private TRParamCallback<Button> trueStateCallback;
	private TRParamCallback<Button> falseStateCallback;

	public GxToggleButton(Component trueStateIcon, Component falseStateIcon, Boolean initialState) {
		this.trueStateIcon = trueStateIcon;
		this.falseStateIcon = falseStateIcon;
		updateState(initialState);
		addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY_INLINE);
		addClickListener(cl -> {
			try {
				if (state) {
					if (falseStateCallback != null) {
						falseStateCallback.execute(cl.getSource());
						updateState(!state);
					}
				} else {
					if (trueStateCallback != null) {
						trueStateCallback.execute(cl.getSource());
						updateState(!state);
					}
				}
			} catch (Exception ex) {
				throw new RuntimeException(ex.getMessage(), ex);
			}
		});
	}

	private void updateState(Boolean state) {
		this.state = state;
		setIcon(state ? trueStateIcon : falseStateIcon);
	}

	@Override
	final public Registration addClickListener(ComponentEventListener<ClickEvent<Button>> listener) {
		return super.addClickListener(listener);
	}

	public void setTrueStateCallback(TRParamCallback<Button> trueStateCallback) {
		this.trueStateCallback = trueStateCallback;
	}

	public void setFalseStateCallback(TRParamCallback<Button> falseStateCallback) {
		this.falseStateCallback = falseStateCallback;
	}

}
