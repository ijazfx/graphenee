package io.graphenee.vaadin.event;

import java.io.Serializable;
import java.util.concurrent.Executors;

import com.vaadin.ui.Button;

/**
 * ClickListner to prevent multiple click behavior.
 * @author fijaz
 *
 */
public abstract class TRButtonClickListener implements Button.ClickListener, Serializable {

	private static final long serialVersionUID = 1L;

	private long sleepDuration = 1000L;
	private volatile boolean isFired = false;

	@Override
	public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
		if (!isFired) {
			synchronized (this) {
				if (!isFired) {
					isFired = true;
					try {
						onButtonClick(event);
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

	public TRButtonClickListener withSleepDuration(long sleepDuration) {
		this.sleepDuration = sleepDuration;
		return this;
	}

	public abstract void onButtonClick(com.vaadin.ui.Button.ClickEvent event);
}
