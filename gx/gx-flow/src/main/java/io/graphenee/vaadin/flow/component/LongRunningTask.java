package io.graphenee.vaadin.flow.component;

import java.util.concurrent.Executors;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;

import io.graphenee.util.callback.TRBiParamCallback;
import io.graphenee.util.callback.TRParamCallback;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LongRunningTask {

	private String progressMessage;
	private String successMessage;
	private String errorMessage;

	private TRParamCallback<UI> taskCallback;
	private TRParamCallback<UI> doneCallback;
	private TRBiParamCallback<UI, Exception> errorCallback;
	private UI ui;

	private LongRunningTask(UI ui, TRParamCallback<UI> taskCallback) {
		this.ui = ui;
		this.taskCallback = taskCallback;
	}

	public static LongRunningTask newTask(UI ui, TRParamCallback<UI> taskCallback) {
		return new LongRunningTask(ui, taskCallback);
	}

	public LongRunningTask withProgressMessage(String progressMessage) {
		this.progressMessage = progressMessage;
		return this;
	}

	public LongRunningTask withSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
		return this;
	}

	public LongRunningTask withErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
		return this;
	}

	public LongRunningTask withDoneCallback(TRParamCallback<UI> actionCallback) {
		this.doneCallback = actionCallback;
		return this;
	}

	public LongRunningTask withErrorCallback(TRBiParamCallback<UI, Exception> errorCallback) {
		this.errorCallback = errorCallback;
		return this;
	}

	public void start() {
		Notification notification = new Notification();
		notification.setPosition(Position.BOTTOM_END);

		ProgressBar progressBar = new ProgressBar();
		progressBar.setIndeterminate(true);

		VerticalLayout notificationLoadingLayout = new VerticalLayout(
				new Text(progressMessage != null ? progressMessage : "Task is in progress..."), progressBar);
		notification.add(notificationLoadingLayout);

		notification.open();
		ui.push();
		Executors.newVirtualThreadPerTaskExecutor().execute(() -> {
			try {
				taskCallback.execute(ui);
				ui.access(() -> {
					notification.remove(notificationLoadingLayout);
					notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
					notification.setDuration(7000);
					notification
							.setText(successMessage != null ? successMessage : "Task has been completed successfully!");
					if (doneCallback != null) {
						doneCallback.execute(ui);
					} else {
						ui.push();
					}
				});
			} catch (Exception ex) {
				Exception cause = cause(ex);
				log.error("Task execution failed:", ex);
				if (errorCallback != null) {
					errorCallback.execute(ui, cause);
				}
				ui.access(() -> {
					notification.remove(notificationLoadingLayout);
					notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
					notification.setDuration(0);
					errorMessage = errorMessage == null
							? (cause != null && cause.getMessage() != null ? cause.getMessage()
									: "Task stopped with error!")
							: errorMessage;
					notification.setText(errorMessage);
					ui.push();
				});
			}
		});
	}

	private Exception cause(Exception t) {
		if (t instanceof NullPointerException) {
			if (t.getCause() instanceof Exception)
				return (Exception) t.getCause();
		}
		return t;
	}

}
