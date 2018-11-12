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

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import io.graphenee.jbpm.embedded.exception.GxTaskException;
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

	private String completeCaption = "Complete";
	private String approveCaption = "Approve";
	private String rejectCaption = "Reject";

	private String completeConfirmation = "Do you confirm your decision to complete this task?";
	private String approveConfirmation = "Do you confirm your decision to approve this task?";
	private String rejectConfirmation = "Do you confirm your decision to reject this task?";

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

	public void setCompleteCaptionAndMessage(String caption, String message) {
		this.completeCaption = caption;
		this.completeConfirmation = message;
		if (completeButton != null)
			completeButton.setCaption(caption);
	}

	public void setApproveCaptionAndMessage(String caption, String message) {
		this.approveCaption = caption;
		this.approveConfirmation = message;
		if (approveButton != null)
			approveButton.setCaption(caption);
	}

	public void setRejectCaptionAndMessage(String caption, String message) {
		this.rejectCaption = caption;
		this.rejectConfirmation = message;
		if (rejectButton != null)
			rejectButton.setCaption(caption);
	}

	@Override
	protected void addButtonsToFooter(HorizontalLayout footer) {
		approveButton = new MButton(approveCaption).withStyleName(ValoTheme.BUTTON_FRIENDLY).withListener(event -> {
			try {
				approve();
			} catch (GxCompleteTaskException ex) {
				GxNotification.closable("Unable to approve", ex.getMessage(), Type.WARNING_MESSAGE).show(Page.getCurrent());
				L.error(ex.getMessage());
			}
		});

		rejectButton = new MButton(rejectCaption).withStyleName(ValoTheme.BUTTON_DANGER).withListener(event -> {
			try {
				reject();
			} catch (GxCompleteTaskException ex) {
				GxNotification.closable("Unable to reject", ex.getMessage(), Type.WARNING_MESSAGE).show(Page.getCurrent());
				L.error(ex.getMessage());
			}
		});

		completeButton = new MButton(completeCaption).withStyleName(ValoTheme.BUTTON_FRIENDLY).withListener(event -> {
			try {
				complete();
			} catch (GxCompleteTaskException ex) {
				GxNotification.closable("Unable to complete", ex.getMessage(), Type.WARNING_MESSAGE).show(Page.getCurrent());
				L.error(ex.getMessage());
			}
		});

		skipButton = new MButton("Skip").withListener(event -> {
			try {
				skip();
			} catch (GxTaskException ex) {
				GxNotification.closable("Unable to approve", ex.getMessage(), Type.WARNING_MESSAGE).show(Page.getCurrent());
				L.error(ex.getMessage());
			}
		});

		assignButton = new MButton("Assign").withListener(event -> {
			try {
				assign(null);
			} catch (GxTaskException ex) {
				GxNotification.closable("Unable to assign", ex.getMessage(), Type.WARNING_MESSAGE).show(Page.getCurrent());
				L.error(ex.getMessage());
			}
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

	/**
	 * You should only override this method if you want to provide some sort of confirmation. Do not forget to invoke super.reject() in your implementation.
	 * @throws GxCompleteTaskException exception in case of failure
	 */
	@Transactional
	protected void reject() throws GxCompleteTaskException {
		Map<String, Object> taskData = new HashMap<>();
		if (onReject(taskData, getEntity())) {
			ConfirmDialog.show(UI.getCurrent(), "Confirmation", GxUserTaskForm.this.rejectConfirmation, "Yes", "No", dlg -> {
				if (dlg.isConfirmed()) {
					try {
						getUserTask().complete(taskData);
						onPostReject(getEntity());
						notifyGxTaskActionListeners(GxTaskAction.REJECTED, getUserTask(), getEntity());
						closePopup();
					} catch (Exception ex) {
						GxNotification.closable("Unable to reject", ex.getMessage(), Type.ERROR_MESSAGE).show(Page.getCurrent());
						L.error(ex.getMessage());
					}
				}
			});
		} else {
			closePopup();
		}
	}

	/**
	 * You should only override this method if you want to provide some sort of confirmation. Do not forget to invoke super.approve() in your implementation.
	 * @throws GxCompleteTaskException exception in case of failure
	 */
	@Transactional
	protected void approve() throws GxCompleteTaskException {
		Map<String, Object> taskData = new HashMap<>();
		if (onApprove(taskData, getEntity())) {
			ConfirmDialog.show(UI.getCurrent(), "Confirmation", GxUserTaskForm.this.approveConfirmation, "Yes", "No", dlg -> {
				if (dlg.isConfirmed()) {
					try {
						getUserTask().complete(taskData);
						onPostApprove(getEntity());
						notifyGxTaskActionListeners(GxTaskAction.APPROVED, getUserTask(), getEntity());
						closePopup();
					} catch (Exception ex) {
						GxNotification.closable("Unable to approve", ex.getMessage(), Type.ERROR_MESSAGE).show(Page.getCurrent());
						L.error(ex.getMessage());
					}
				}
			});
		} else {
			closePopup();
		}
	}

	/**
	 * You should only override this method if you want to provide some sort of confirmation. Do not forget to invoke super.assign(...) in your implementation.
	 * @param assignToUserId the user id of the user to assign the task
	 * @throws GxAssignTaskException exception in case of failure
	 */
	@Transactional
	protected void assign(String assignToUserId) throws GxAssignTaskException {
		List<GxAssignee> assignees = onAssign(getEntity());
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
							GxNotification.closable("Unable to assign", ex.getMessage(), Type.WARNING_MESSAGE).show(Page.getCurrent());
							L.error(ex.getMessage());
						}
					}
				});
			});
			assigneeForm.openInModalPopup();
		} else {
			GxNotification.closable(null, "No potential assignees are available to handle this task.", Type.WARNING_MESSAGE).show(Page.getCurrent());
		}
	}

	/**
	 * You should only override this method if you want to provide some sort of confirmation. Do not forget to invoke super.skip() in your implementation.
	 * @throws GxSkipTaskException exception in case of failure
	 */
	@Transactional
	protected void skip() throws GxSkipTaskException {
		if (onSkip(getEntity())) {
			ConfirmDialog.show(UI.getCurrent(), "Confirmation", "Do you confirm your decision to skip this task?", "Yes", "No", dlg -> {
				if (dlg.isConfirmed()) {
					try {
						getUserTask().skip();
						onPostSkip(getEntity());
						notifyGxTaskActionListeners(GxTaskAction.SKIPPED, getUserTask(), getEntity());
						closePopup();
					} catch (Exception ex) {
						GxNotification.closable("Unable to skip", ex.getMessage(), Type.ERROR_MESSAGE).show(Page.getCurrent());
						L.error(ex.getMessage());
					}
				}
			});
		} else {
			closePopup();
		}
	}

	/**
	 * You should only override this method if you want to provide some sort of confirmation. Do not forget to invoke super.complete() in your implementation.
	 * @throws GxCompleteTaskException exception in case of failure
	 */
	@Transactional
	protected void complete() throws GxCompleteTaskException {
		Map<String, Object> taskData = new HashMap<>();
		if (onComplete(taskData, getEntity())) {
			ConfirmDialog.show(UI.getCurrent(), "Confirmation", completeConfirmation, "Yes", "No", dlg -> {
				if (dlg.isConfirmed()) {
					try {
						getUserTask().complete(taskData);
						onPostComplete(getEntity());
						notifyGxTaskActionListeners(GxTaskAction.COMPLETED, getUserTask(), getEntity());
						closePopup();
					} catch (Exception ex) {
						GxNotification.closable("Unable to complete", ex.getMessage(), Type.ERROR_MESSAGE).show(Page.getCurrent());
						L.error(ex.getMessage());
					}
				}
			});
		} else {
			closePopup();
		}
	}

	@SuppressWarnings("unchecked")
	private void notifyGxTaskActionListeners(GxTaskAction taskAction, GxUserTask userTask, T entity) {
		listeners.forEach(listener -> {
			listener.onAction(taskAction, userTask, entity);
		});
	}

	@Override
	public Window openInModalPopup() {
		Window popup = super.openInModalPopup();
		approveButton.setVisible(getUserTask() != null);
		rejectButton.setVisible(getUserTask() != null);
		completeButton.setVisible(getUserTask() != null);
		skipButton.setVisible(getUserTask() != null && getUserTask().isSkipable());
		assignButton.setVisible(getUserTask() != null && isAssignable());
		completeButton.setCaption(completeCaption);
		return popup;
	}

	protected boolean onApprove(Map<String, Object> taskData, T entity) throws GxCompleteTaskException {
		return true;
	}

	protected void onPostApprove(T entity) throws GxCompleteTaskException {
	}

	protected boolean onReject(Map<String, Object> taskData, T entity) throws GxCompleteTaskException {
		return true;
	}

	protected void onPostReject(T entity) throws GxCompleteTaskException {
	}

	protected boolean onComplete(Map<String, Object> taskData, T entity) throws GxCompleteTaskException {
		return true;
	}

	protected void onPostComplete(T entity) throws GxCompleteTaskException {
	}

	protected List<GxAssignee> onAssign(T entity) throws GxAssignTaskException {
		return null;
	}

	protected void onPostAssign(GxAssignee assignee, T entity) throws GxAssignTaskException {
	}

	protected boolean onSkip(T entity) throws GxSkipTaskException {
		return true;
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

}
