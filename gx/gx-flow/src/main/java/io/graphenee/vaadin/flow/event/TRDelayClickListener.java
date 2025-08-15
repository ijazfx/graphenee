package io.graphenee.vaadin.flow.event;

import java.io.Serializable;
import java.util.concurrent.Executors;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;

/**
 * ClickListner to prevent multiple click behavior.
 * @author fijaz
 * @param <T> The component type.
 *
 */
public abstract class TRDelayClickListener<T extends Component> implements ComponentEventListener<ClickEvent<T>>, Serializable {

	private static final long serialVersionUID = 1L;

	private long sleepDuration = 1000L;
	volatile boolean isFired = false;

	@Override
	public void onComponentEvent(ClickEvent<T> event) {
		if (!isFired) {
			synchronized (this) {
				if (!isFired) {
					isFired = true;
					try {
						onClick(event);
					} finally {
						Executors.newVirtualThreadPerTaskExecutor().execute(() -> {
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

	public TRDelayClickListener<T> withSleepDuration(long sleepDuration) {
		this.sleepDuration = sleepDuration;
		return this;
	}

	public abstract void onClick(ClickEvent<T> event);

}
