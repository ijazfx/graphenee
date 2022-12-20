package io.graphenee.jbpm.embedded.flow;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.kie.api.task.model.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import io.graphenee.jbpm.embedded.GxAssignee;
import io.graphenee.jbpm.embedded.GxUserTask;
import io.graphenee.jbpm.embedded.exception.GxAssignTaskException;
import io.graphenee.jbpm.embedded.exception.GxCompleteTaskException;
import io.graphenee.jbpm.embedded.exception.GxSkipTaskException;
import io.graphenee.jbpm.embedded.flow.GxSelectAssigneeForm.GxAssigneeHolder;
import io.graphenee.vaadin.flow.GxFlowNotification;
import io.graphenee.vaadin.flow.base.GxAbstractEntityForm;

public abstract class GxUserTaskForm<T> extends GxAbstractEntityForm<T> {

	public GxUserTaskForm(Class<T> entityClass) {
		super(entityClass);
	}

	protected static final Logger L = LoggerFactory.getLogger(GxUserTaskForm.class);

	private static final long serialVersionUID = 1L;
	private GxUserTask userTask;

	private Button approveButton;
	private Button rejectButton;
	private Button completeButton;
	private Button skipButton;
	private Button assignButton;

	public Set<GxTaskActionListener<T>> listeners = new HashSet<>();

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
	protected void decorateToolbar(HasComponents footer) {
		approveButton = new Button(approveButtonCaption(), event -> {
			approveTask();
		});
		approveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		rejectButton = new Button(rejectButtonCaption(), event -> {
			rejectTask();
		});
		rejectButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

		completeButton = new Button(completeButtonCaption(), event -> {
			completeTask();
		});
		completeButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);

		skipButton = new Button("Skip", event -> {
			skipTask();
		});

		assignButton = new Button("Assign", event -> {
			assignTask();
		});

		HorizontalLayout taskButtonsLayout = new HorizontalLayout();
		if (

		isApprovalForm()) {
			taskButtonsLayout.add(approveButton, rejectButton, skipButton, assignButton);
		} else {
			taskButtonsLayout.add(completeButton, skipButton, assignButton);
		}
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
					closeDialog();
				} catch (Exception ex) {
					String message = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();
					GxFlowNotification.alert(message).open();
					L.error(ex.getMessage(), ex);
				}
			}

			@Override
			public void cancel() {
				closeDialog();
			}

			@Override
			public void error(Throwable t) {
				String message = t.getCause() != null ? t.getCause().getMessage() : t.getMessage();
				GxFlowNotification.alert(message).open();
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
					closeDialog();
				} catch (Exception ex) {
					String message = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();
					GxFlowNotification.alert(message).open();
					L.error(ex.getMessage(), ex);
				}
			}

			@Override
			public void cancel() {
				closeDialog();
			}

			@Override
			public void error(Throwable t) {
				String message = t.getCause() != null ? t.getCause().getMessage() : t.getMessage();
				GxFlowNotification.alert(message).open();
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
					assigneeForm.initializeWithAssignees(assignees);
					assigneeForm.setDelegate(new EntityFormDelegate<GxSelectAssigneeForm.GxAssigneeHolder>() {

						@Override
						public void onSave(GxAssigneeHolder holder) {
							String msg = "Are you sure to assign the task to " + holder.getAssignee() + "?";
							org.claspina.confirmdialog.ConfirmDialog dlg = org.claspina.confirmdialog.ConfirmDialog.createQuestion();
							dlg.withMessage(msg).withYesButton(() -> {
								GxAssignee assignee = holder.getAssignee();
								try {
									getUserTask().assign(assignee.getUsername());
									GxUserTaskForm.this.onPostAssign(assignee, getEntity());
									notifyGxTaskActionListeners(GxTaskAction.ASSIGNED, getUserTask(), getEntity());
									assigneeForm.closeDialog();
									closeDialog();
								} catch (GxAssignTaskException ex) {
									GxFlowNotification.alert(ex.getMessage()).open();
									L.error(ex.getMessage(), ex);
								}
							});
						}

					});
					assigneeForm.showInDialog(new GxAssigneeHolder());
				} else {
					GxFlowNotification.info("No potential assignees are available to handle this task.").open();
				}
			}

			@Override
			public void cancel() {
				closeDialog();
			}

			@Override
			public void error(Throwable t) {
				String message = t.getCause() != null ? t.getCause().getMessage() : t.getMessage();
				GxFlowNotification.alert(message).open();
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
					closeDialog();
				} catch (Exception ex) {
					String message = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();
					GxFlowNotification.alert(message).open();
					L.error(ex.getMessage(), ex);
				}
			}

			@Override
			public void cancel() {
				closeDialog();
			}

			@Override
			public void error(Throwable t) {
				String message = t.getCause() != null ? t.getCause().getMessage() : t.getMessage();
				GxFlowNotification.alert(message).open();
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
					closeDialog();
				} catch (Exception ex) {
					String message = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();
					GxFlowNotification.alert(message).open();
					L.error(ex.getMessage(), ex);
				}
			}

			@Override
			public void cancel() {
				closeDialog();
			}

			@Override
			public void error(Throwable t) {
				String message = t.getCause() != null ? t.getCause().getMessage() : t.getMessage();
				GxFlowNotification.alert(message).open();
			}
		});
	}

	protected void onComplete(Map<String, Object> taskData, T entity, GxUserTaskHandler handler) {
	}

	private void notifyGxTaskActionListeners(GxTaskAction taskAction, GxUserTask userTask, T entity) {
		listeners.forEach(listener -> {
			listener.onAction(taskAction, userTask, entity);
		});
	}

	@Override
	protected void preBinding(T entity) {
		GxUserTask task = getUserTask();
		boolean valid = task != null
				&& (task.getStatus() == Status.Ready || task.getStatus() == Status.Reserved || task.getStatus() == Status.InProgress || task.getStatus() == Status.Created);
		approveButton.setText(approveButtonCaption());
		approveButton.setVisible(valid);
		rejectButton.setText(rejectButtonCaption());
		rejectButton.setVisible(valid);
		completeButton.setText(completeButtonCaption());
		completeButton.setVisible(valid);
		skipButton.setVisible(valid && getUserTask().isSkipable());
		assignButton.setVisible(valid && isAssignable());
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
