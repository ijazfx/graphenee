package io.graphenee.vaadin.event;

import java.io.Serializable;
import java.util.concurrent.Executors;

import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.event.MouseEvents.ClickListener;

/**
 * ClickListner to prevent multiple click behavior.
 * @author fijaz
 *
 */
public abstract class TRClickListener implements ClickListener, Serializable {

	private static final long serialVersionUID = 1L;

	private long sleepDuration = 1000L;
	volatile boolean isFired = false;

	@Override
	public final void click(ClickEvent event) {
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

	public TRClickListener withSleepDuration(long sleepDuration) {
		this.sleepDuration = sleepDuration;
		return this;
	}

	public abstract void onClick(ClickEvent event);

}
