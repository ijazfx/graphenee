package io.graphenee.vaadin.event;

import java.io.Serializable;
import java.util.concurrent.Executors;

import com.vaadin.event.ItemClickEvent;

/**
 * ItemClickListner to prevent multiple click behavior.
 * @author fijaz
 *
 */
public abstract class TRItemClickListener implements ItemClickEvent.ItemClickListener, Serializable {

	private static final long serialVersionUID = 1L;

	private long sleepDuration = 1000L;
	volatile boolean isFired = false;

	@Override
	public void itemClick(ItemClickEvent event) {
		if (!isFired) {
			synchronized (this) {
				if (!isFired) {
					isFired = true;
					try {
						onItemClick(event);
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

	public TRItemClickListener withSleepDuration(long sleepDuration) {
		this.sleepDuration = sleepDuration;
		return this;
	}

	public abstract void onItemClick(ItemClickEvent event);

}
