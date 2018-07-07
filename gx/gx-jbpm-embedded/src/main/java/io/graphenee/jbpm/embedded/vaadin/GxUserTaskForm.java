package io.graphenee.jbpm.embedded.vaadin;

import java.util.ArrayList;
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
	}

	public void setApproveCaptionAndMessage(String caption, String message) {
		this.approveCaption = caption;
		this.approveConfirmation = message;
	}

	public void setRejectCaptionAndMessage(String caption, String message) {
		this.rejectCaption = caption;
		this.rejectConfirmation = message;
	}

	@Override
	protected void addButtonsToFooter(HorizontalLayout footer) {
		approveButton = new MButton(approveCaption).withStyleName(ValoTheme.BUTTON_FRIENDLY).withListener(event -> {
			ConfirmDialog.show(UI.getCurrent(), "Confirmation", GxUserTaskForm.this.approveConfirmation, "Yes", "No", dlg -> {
				if (dlg.isConfirmed()) {
					try {
						approve();
					} catch (GxTaskException ex) {
						GxNotification.closable("Unable to approve", ex.getMessage(), Type.WARNING_MESSAGE).show(Page.getCurrent());
						L.error(ex.getMessage());
					} catch (Exception ex) {
						GxNotification.closable("Unable to approve", ex.getMessage(), Type.ERROR_MESSAGE).show(Page.getCurrent());
						L.error(ex.getMessage());
					}
				}
			});

		});

		rejectButton = new MButton(rejectCaption).withStyleName(ValoTheme.BUTTON_DANGER).withListener(event -> {
			ConfirmDialog.show(UI.getCurrent(), "Confirmation", GxUserTaskForm.this.rejectConfirmation, "Yes", "No", dlg -> {
				if (dlg.isConfirmed()) {
					try {
						reject();
					} catch (GxTaskException ex) {
						GxNotification.closable("Unable to reject", ex.getMessage(), Type.WARNING_MESSAGE).show(Page.getCurrent());
						L.error(ex.getMessage());
					} catch (Exception ex) {
						GxNotification.closable("Unable to reject", ex.getMessage(), Type.ERROR_MESSAGE).show(Page.getCurrent());
						L.error(ex.getMessage());
					}
				}
			});
		});

		completeButton = new MButton(completeCaption).withStyleName(ValoTheme.BUTTON_FRIENDLY).withListener(event -> {
			ConfirmDialog.show(UI.getCurrent(), "Confirmation", completeConfirmation, "Yes", "No", dlg -> {
				if (dlg.isConfirmed()) {
					try {
						complete();
					} catch (GxTaskException ex) {
						GxNotification.closable("Unable to complete", ex.getMessage(), Type.WARNING_MESSAGE).show(Page.getCurrent());
						L.error(ex.getMessage());
					} catch (Exception ex) {
						GxNotification.closable("Unable to complete", ex.getMessage(), Type.ERROR_MESSAGE).show(Page.getCurrent());
						L.error(ex.getMessage());
					}
				}
			});
		});

		skipButton = new MButton("Skip").withListener(event -> {
			ConfirmDialog.show(UI.getCurrent(), "Confirmation", "Do you confirm your decision to skip this task?", "Yes", "No", dlg -> {
				if (dlg.isConfirmed()) {
					try {
						skip();
					} catch (GxTaskException ex) {
						GxNotification.closable("Unable to skip", ex.getMessage(), Type.WARNING_MESSAGE).show(Page.getCurrent());
						L.error(ex.getMessage());
					} catch (Exception ex) {
						GxNotification.closable("Unable to skip", ex.getMessage(), Type.ERROR_MESSAGE).show(Page.getCurrent());
						L.error(ex.getMessage());
					}
				}
			});
		});

		assignButton = new MButton("Assign").withListener(event -> {
			ConfirmDialog.show(UI.getCurrent(), "Confirmation", "Do you confirm your decision to assign this task to someone else?", "Yes", "No", dlg -> {
				if (dlg.isConfirmed()) {
					try {
						assign(null);
					} catch (GxTaskException ex) {
						GxNotification.closable("Unable to assign", ex.getMessage(), Type.WARNING_MESSAGE).show(Page.getCurrent());
						L.error(ex.getMessage());
					} catch (Exception ex) {
						GxNotification.closable("Unable to assign", ex.getMessage(), Type.ERROR_MESSAGE).show(Page.getCurrent());
						L.error(ex.getMessage());
					}
				}
			});

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

	private void reject() throws GxCompleteTaskException {
		Map<String, Object> taskData = new HashMap<>();
		onReject(taskData, getEntity());
		getUserTask().complete(taskData);
		onPostReject(getEntity());
		notifyGxTaskActionListeners(GxTaskAction.REJECTED, getUserTask(), getEntity());
		closePopup();
	}

	private void approve() throws GxCompleteTaskException {
		Map<String, Object> taskData = new HashMap<>();
		onApprove(taskData, getEntity());
		getUserTask().complete(taskData);
		onPostApprove(getEntity());
		notifyGxTaskActionListeners(GxTaskAction.APPROVED, getUserTask(), getEntity());
		closePopup();
	}

	@Transactional
	private void assign(String assignToUserId) throws GxAssignTaskException {
		List<GxAssignee> assignees = new ArrayList<>();
		onAssign(assignees, getEntity());
		if (assignees.isEmpty()) {
			throw new GxAssignTaskException("No assignees are available to assign this task");
		} else {
			GxSelectAssigneeForm assigneeForm = new GxSelectAssigneeForm();
			assigneeForm.setEntity(GxAssigneeHolder.class, new GxAssigneeHolder());
			assigneeForm.initializeWithAssignees(assignees);
			assigneeForm.setSavedHandler(holder -> {
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
			});
			assigneeForm.openInModalPopup();
		}
	}

	@Transactional
	private void skip() throws GxSkipTaskException {
		onSkip(getEntity());
		getUserTask().skip();
		onPostSkip(getEntity());
		notifyGxTaskActionListeners(GxTaskAction.SKIPPED, getUserTask(), getEntity());
		closePopup();
	}

	@Transactional
	private void complete() throws GxCompleteTaskException {
		Map<String, Object> taskData = new HashMap<>();
		onComplete(taskData, getEntity());
		getUserTask().complete(taskData);
		onPostComplete(getEntity());
		notifyGxTaskActionListeners(GxTaskAction.COMPLETED, getUserTask(), getEntity());
		closePopup();
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

	protected void onApprove(Map<String, Object> taskData, T entity) throws GxCompleteTaskException {
	}

	protected void onPostApprove(T entity) throws GxCompleteTaskException {
	}

	protected void onReject(Map<String, Object> taskData, T entity) throws GxCompleteTaskException {
	}

	protected void onPostReject(T entity) throws GxCompleteTaskException {
	}

	protected void onComplete(Map<String, Object> taskData, T entity) throws GxCompleteTaskException {
	}

	protected void onPostComplete(T entity) throws GxCompleteTaskException {
	}

	protected void onAssign(List<GxAssignee> assignee, T entity) throws GxAssignTaskException {
	}

	protected void onPostAssign(GxAssignee assignee, T entity) throws GxAssignTaskException {
	}

	protected void onSkip(T entity) throws GxSkipTaskException {
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
		APPROVED, REJECTED, COMPLETED, ASSIGNED, SKIPPED
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
