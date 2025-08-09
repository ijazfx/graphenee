package io.graphenee.vaadin.flow.event;

import java.io.Serializable;
import java.util.concurrent.Executors;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;

/**
 * ClickListner to prevent multiple click behavior.
 * @author fijaz
 * @param <T> The component event type.
 *
 */
public abstract class TRDelayEventListener<T extends ComponentEvent<?>> implements ComponentEventListener<T>, Serializable {

	private static final long serialVersionUID = 1L;

	private long sleepDuration = 1000L;
	volatile boolean isFired = false;

	@Override
	public void onComponentEvent(T event) {
		if (!isFired) {
			synchronized (this) {
				if (!isFired) {
					isFired = true;
					try {
						onClick(event);
					} finally {
						Executors.newSingleThreadExecutor().execute(() -> {
							try {
								Thread.sleep(sleepDuration);
							} catch (InterruptedException e) {
							} finally {
								isFired = false;
							}
						});
					}
				}
			}
		}
	}

	public TRDelayEventListener<T> withSleepDuration(long sleepDuration) {
		this.sleepDuration = sleepDuration;
		return this;
	}

	public abstract void onClick(T event);

}
