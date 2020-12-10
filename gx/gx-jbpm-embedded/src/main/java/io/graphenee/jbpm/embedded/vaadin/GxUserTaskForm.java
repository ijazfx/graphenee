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
package io.graphenee.jbpm.embedded.vaadin;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.kie.api.task.model.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import com.vaadin.server.Page;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import io.graphenee.jbpm.embedded.GxAssignee;
import io.graphenee.jbpm.embedded.GxUserTask;
import io.graphenee.jbpm.embedded.exception.GxAssignTaskException;
import io.graphenee.jbpm.embedded.exception.GxCompleteTaskException;
import io.graphenee.jbpm.embedded.exception.GxSkipTaskException;
import io.graphenee.jbpm.embedded.vaadin.GxSelectAssigneeForm.GxAssigneeHolder;
import io.graphenee.vaadin.TRAbstractForm;
import io.graphenee.vaadin.ui.GxNotification;

public abstract class GxUserTaskForm<T> extends TRAbstractForm<T> {

	protected static final Logger L = LoggerFactory.getLogger(GxUserTaskForm.class);

	private static final long serialVersionUID = 1L;
	private GxUserTask userTask;

	private MButton approveButton;
	private MButton rejectButton;
	private MButton completeButton;
	private MButton skipButton;
	private MButton assignButton;

	public Set<GxTaskActionListener> listeners = new HashSet<>();

	@Override
	protected boolean eagerValidationEnabled() {
		return true;
	}

	@Override
	protected String formTitle() {
		if (getUserTask() != null)
			return getUserTask().getName();
		return "User Task";
	}

	public void initializeWithTask(GxUserTask userTask) {
		this.userTask = userTask;
	}

	protected String completeButtonCaption() {
		return "Complete";
	}

	protected String rejectButtonCaption() {
		return "Reject";
	}

	protected String approveButtonCaption() {
		return "Approve";
	}

	@Override
	protected void addButtonsToFooter(HorizontalLayout footer) {
		approveButton = new MButton(approveButtonCaption()).withStyleName(ValoTheme.BUTTON_FRIENDLY).withListener(event -> {
			approveTask();
		});

		rejectButton = new MButton(rejectButtonCaption()).withStyleName(ValoTheme.BUTTON_DANGER).withListener(event -> {
			rejectTask();
		});

		completeButton = new MButton(completeButtonCaption()).withStyleName(ValoTheme.BUTTON_FRIENDLY).withListener(event -> {
			completeTask();
		});

		skipButton = new MButton("Skip").withListener(event -> {
			skipTask();
		});

		assignButton = new MButton("Assign").withListener(event -> {
			assignTask();
		});

		MHorizontalLayout taskButtonsLayout = new MHorizontalLayout();
		if (

		isApprovalForm()) {
			taskButtonsLayout.addComponents(approveButton, rejectButton, skipButton, assignButton);
		} else {
			taskButtonsLayout.addComponents(completeButton, skipButton, assignButton);
		}
		footer.setWidth("100%");
		footer.addComponentAsFirst(taskButtonsLayout);
	}

	@Transactional
	private void rejectTask() {
		Map<String, Object> taskData = new HashMap<>();
		onReject(taskData, getEntity(), new GxUserTaskHandler() {

			@Override
			public void proceed() {
				try {
					getUserTask().complete(taskData);
					onPostReject(getEntity());
					notifyGxTaskActionListeners(GxTaskAction.REJECTED, getUserTask(), getEntity());
					closePopup();
				} catch (Exception ex) {
					String message = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();
					GxNotification.closable("Task Error", message, Type.ERROR_MESSAGE).show(Page.getCurrent());
					L.error(ex.getMessage(), ex);
				}
			}

			@Override
			public void cancel() {
				closePopup();
			}

			@Override
			public void error(Throwable t) {
				String message = t.getCause() != null ? t.getCause().getMessage() : t.getMessage();
				GxNotification.closable(null, message, Type.WARNING_MESSAGE).show(Page.getCurrent());
			}
		});

	}

	protected void onReject(Map<String, Object> taskData, T entity, GxUserTaskHandler handler) {
	}

	@Transactional
	private void approveTask() {
		Map<String, Object> taskData = new HashMap<>();
		onApprove(taskData, getEntity(), new GxUserTaskHandler() {

			@Override
			public void proceed() {
				try {
					getUserTask().complete(taskData);
					onPostApprove(getEntity());
					notifyGxTaskActionListeners(GxTaskAction.APPROVED, getUserTask(), getEntity());
					closePopup();
				} catch (Exception ex) {
					String message = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();
					GxNotification.closable("Task Error", message, Type.ERROR_MESSAGE).show(Page.getCurrent());
					L.error(ex.getMessage(), ex);
				}
			}

			@Override
			public void cancel() {
				closePopup();
			}

			@Override
			public void error(Throwable t) {
				String message = t.getCause() != null ? t.getCause().getMessage() : t.getMessage();
				GxNotification.closable(null, message, Type.WARNING_MESSAGE).show(Page.getCurrent());
			}
		});
	}

	protected void onApprove(Map<String, Object> taskData, T entity, GxUserTaskHandler handler) {
	}

	@Transactional
	private void assignTask() {
		onAssign(getEntity(), new GxUserTaskAssigner() {

			@Override
			public void assign(Collection<GxAssignee> assignees) {
				if (assignees != null && !assignees.isEmpty()) {
					GxSelectAssigneeForm assigneeForm = new GxSelectAssigneeForm();
					assigneeForm.setEntity(GxAssigneeHolder.class, new GxAssigneeHolder());
					assigneeForm.initializeWithAssignees(assignees);
					assigneeForm.setSavedHandler(holder -> {
						ConfirmDialog.show(UI.getCurrent(), "Confirmation", "Are you sure to assign the task to " + holder.getAssignee() + "?", "Yes", "No", dlg -> {
							if (dlg.isConfirmed()) {
								GxAssignee assignee = holder.getAssignee();
								try {
									getUserTask().assign(assignee.getUsername());
									GxUserTaskForm.this.onPostAssign(assignee, getEntity());
									notifyGxTaskActionListeners(GxTaskAction.ASSIGNED, getUserTask(), getEntity());
									assigneeForm.closePopup();
									closePopup();
								} catch (GxAssignTaskException ex) {
									GxNotification.closable("Task Error", ex.getMessage(), Type.WARNING_MESSAGE).show(Page.getCurrent());
									L.error(ex.getMessage(), ex);
								}
							}
						});
					});
					assigneeForm.openInModalPopup();
				} else {
					GxNotification.closable(null, "No potential assignees are available to handle this task.", Type.WARNING_MESSAGE).show(Page.getCurrent());
				}
			}

			@Override
			public void cancel() {
				closePopup();
			}

			@Override
			public void error(Throwable t) {
				String message = t.getCause() != null ? t.getCause().getMessage() : t.getMessage();
				GxNotification.closable(null, message, Type.WARNING_MESSAGE).show(Page.getCurrent());
			}

		});

	}

	protected void onAssign(T entity, GxUserTaskAssigner assigner) {
	}

	@Transactional
	private void skipTask() {
		onSkip(getEntity(), new GxUserTaskSkipper() {

			@Override
			public void skip() {
				try {
					getUserTask().skip();
					onPostSkip(getEntity());
					notifyGxTaskActionListeners(GxTaskAction.SKIPPED, getUserTask(), getEntity());
					closePopup();
				} catch (Exception ex) {
					String message = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();
					GxNotification.closable("Task Error", message, Type.ERROR_MESSAGE).show(Page.getCurrent());
					L.error(ex.getMessage(), ex);
				}
			}

			@Override
			public void cancel() {
				closePopup();
			}

			@Override
			public void error(Throwable t) {
				String message = t.getCause() != null ? t.getCause().getMessage() : t.getMessage();
				GxNotification.closable(null, message, Type.WARNING_MESSAGE).show(Page.getCurrent());
			}

		});
	}

	protected void onSkip(T entity, GxUserTaskSkipper skipper) {
	}

	@Transactional
	private void completeTask() {
		Map<String, Object> taskData = new HashMap<>();
		onComplete(taskData, getEntity(), new GxUserTaskHandler() {

			@Override
			public void proceed() {
				try {
					getUserTask().complete(taskData);
					onPostComplete(getEntity());
					notifyGxTaskActionListeners(GxTaskAction.COMPLETED, getUserTask(), getEntity());
					closePopup();
				} catch (Exception ex) {
					String message = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();
					GxNotification.closable("Task Error", message, Type.ERROR_MESSAGE).show(Page.getCurrent());
					L.error(ex.getMessage(), ex);
				}
			}

			@Override
			public void cancel() {
				closePopup();
			}

			@Override
			public void error(Throwable t) {
				String message = t.getCause() != null ? t.getCause().getMessage() : t.getMessage();
				GxNotification.closable(null, message, Type.WARNING_MESSAGE).show(Page.getCurrent());
			}
		});
	}

	protected void onComplete(Map<String, Object> taskData, T entity, GxUserTaskHandler handler) {
	}

	@SuppressWarnings("unchecked")
	private void notifyGxTaskActionListeners(GxTaskAction taskAction, GxUserTask userTask, T entity) {
		listeners.forEach(listener -> {
			listener.onAction(taskAction, userTask, entity);
		});
	}

	@Override
	public Window openInModalPopup() {
		GxUserTask task = getUserTask();
		boolean valid = task != null
				&& (task.getStatus() == Status.Ready || task.getStatus() == Status.Reserved || task.getStatus() == Status.InProgress || task.getStatus() == Status.Created);
		Window popup = super.openInModalPopup();
		approveButton.setCaption(approveButtonCaption());
		approveButton.setVisible(valid);
		rejectButton.setCaption(rejectButtonCaption());
		rejectButton.setVisible(valid);
		completeButton.setCaption(completeButtonCaption());
		completeButton.setVisible(valid);
		skipButton.setVisible(valid && getUserTask().isSkipable());
		assignButton.setVisible(valid && isAssignable());
		return popup;
	}

	protected void onPostApprove(T entity) throws GxCompleteTaskException {
	}

	protected void onPostReject(T entity) throws GxCompleteTaskException {
	}

	protected void onPostComplete(T entity) throws GxCompleteTaskException {
	}

	protected void onPostAssign(GxAssignee assignee, T entity) throws GxAssignTaskException {
	}

	protected void onPostSkip(T entity) throws GxSkipTaskException {
	}

	public void addTaskActionListener(GxTaskActionListener<T> listener) {
		listeners.add(listener);
	}

	public static interface GxTaskActionListener<T> {
		void onAction(GxTaskAction action, GxUserTask userTask, T entity);
	}

	public static enum GxTaskAction {
		APPROVED,
		REJECTED,
		COMPLETED,
		ASSIGNED,
		SKIPPED
	}

	protected abstract boolean isApprovalForm();

	protected abstract boolean isAssignable();

	public GxUserTask getUserTask() {
		assert userTask != null;
		return userTask;
	}

	@Override
	protected String popupHeight() {
		return "350px";
	}

	@Override
	protected String popupWidth() {
		return "450px";
	}

	@Override
	protected void adjustSaveButtonState() {
		super.adjustSaveButtonState();
		boolean valid = isValid();
		approveButton.setEnabled(valid);
		rejectButton.setEnabled(valid);
		completeButton.setEnabled(valid);
	}

	public static interface GxUserTaskHandler {
		void proceed();

		void cancel();

		void error(Throwable t);
	}

	public static interface GxUserTaskAssigner {
		void assign(Collection<GxAssignee> assignees);

		void cancel();

		void error(Throwable t);
	}

	public static interface GxUserTaskSkipper {
		void skip();

		void cancel();

		void error(Throwable t);
	}

}
