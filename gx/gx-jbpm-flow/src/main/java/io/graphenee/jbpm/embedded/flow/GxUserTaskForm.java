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
import io.graphenee.vaadin.flow.GxAbstractEntityForm;
import io.graphenee.vaadin.flow.component.DialogFactory;
import io.graphenee.vaadin.flow.component.GxNotification;

/**
 * An abstract form for user tasks.
 *
 * @param <T> The entity type.
 */
public abstract class GxUserTaskForm<T> extends GxAbstractEntityForm<T> {

	/**
	 * Creates a new instance of this form.
	 * @param entityClass The entity class.
	 */
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

	/**
	 * The listeners for this form.
	 */
	public Set<GxTaskActionListener<T>> listeners = new HashSet<>();

	@Override
	protected String formTitle() {
		if (getUserTask() != null)
			return getUserTask().getName();
		return "User Task";
	}

	/**
	 * Initializes the form with a user task.
	 * @param userTask The user task.
	 */
	public void initializeWithTask(GxUserTask userTask) {
		this.userTask = userTask;
	}

	/**
	 * Gets the caption for the complete button.
	 * @return The caption for the complete button.
	 */
	protected String completeButtonCaption() {
		return "Complete";
	}

	/**
	 * Gets the caption for the reject button.
	 * @return The caption for the reject button.
	 */
	protected String rejectButtonCaption() {
		return "Reject";
	}

	/**
	 * Gets the caption for the approve button.
	 * @return The caption for the approve button.
	 */
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
		completeButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

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
					GxNotification.error(message);
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
				GxNotification.error(message);
			}
		});

	}

	/**
	 * Called when the task is rejected.
	 * @param taskData The task data.
	 * @param entity The entity.
	 * @param handler The handler.
	 */
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
					GxNotification.error(message);
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
				GxNotification.error(message);
			}
		});
	}

	/**
	 * Called when the task is approved.
	 * @param taskData The task data.
	 * @param entity The entity.
	 * @param handler The handler.
	 */
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
							DialogFactory.questionDialog("Confirmation", msg, dlg -> {
								GxAssignee assignee = holder.getAssignee();
								try {
									getUserTask().assign(assignee.getUsername());
									GxUserTaskForm.this.onPostAssign(assignee, getEntity());
									notifyGxTaskActionListeners(GxTaskAction.ASSIGNED, getUserTask(), getEntity());
									assigneeForm.closeDialog();
									closeDialog();
								} catch (GxAssignTaskException ex) {
									GxNotification.error(ex.getMessage());
									L.error(ex.getMessage(), ex);
								}
							}).open();
						}

					});
					assigneeForm.showInDialog(new GxAssigneeHolder());
				} else {
					GxNotification.primary("No potential assignees are available to handle this task.");
				}
			}

			@Override
			public void cancel() {
				closeDialog();
			}

			@Override
			public void error(Throwable t) {
				String message = t.getCause() != null ? t.getCause().getMessage() : t.getMessage();
				GxNotification.error(message);
			}

		});

	}

	/**
	 * Called when the task is assigned.
	 * @param entity The entity.
	 * @param assigner The assigner.
	 */
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
					GxNotification.error(message);
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
				GxNotification.error(message);
			}

		});
	}

	/**
	 * Called when the task is skipped.
	 * @param entity The entity.
	 * @param skipper The skipper.
	 */
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
					GxNotification.error(message);
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
				GxNotification.error(message);
			}
		});
	}

	/**
	 * Called when the task is completed.
	 * @param taskData The task data.
	 * @param entity The entity.
	 * @param handler The handler.
	 */
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

	/**
	 * Called after the task is approved.
	 * @param entity The entity.
	 * @throws GxCompleteTaskException If the task cannot be completed.
	 */
	protected void onPostApprove(T entity) throws GxCompleteTaskException {
	}

	/**
	 * Called after the task is rejected.
	 * @param entity The entity.
	 * @throws GxCompleteTaskException If the task cannot be completed.
	 */
	protected void onPostReject(T entity) throws GxCompleteTaskException {
	}

	/**
	 * Called after the task is completed.
	 * @param entity The entity.
	 * @throws GxCompleteTaskException If the task cannot be completed.
	 */
	protected void onPostComplete(T entity) throws GxCompleteTaskException {
	}

	/**
	 * Called after the task is assigned.
	 * @param assignee The assignee.
	 * @param entity The entity.
	 * @throws GxAssignTaskException If the task cannot be assigned.
	 */
	protected void onPostAssign(GxAssignee assignee, T entity) throws GxAssignTaskException {
	}

	/**
	 * Called after the task is skipped.
	 * @param entity The entity.
	 * @throws GxSkipTaskException If the task cannot be skipped.
	 */
	protected void onPostSkip(T entity) throws GxSkipTaskException {
	}

	/**
	 * Adds a task action listener.
	 * @param listener The listener to add.
	 */
	public void addTaskActionListener(GxTaskActionListener<T> listener) {
		listeners.add(listener);
	}

	/**
	 * An interface for task action listeners.
	 *
	 * @param <T> The entity type.
	 */
	public static interface GxTaskActionListener<T> {
		/**
		 * Called when an action is performed on a task.
		 * @param action The action.
		 * @param userTask The user task.
		 * @param entity The entity.
		 */
		void onAction(GxTaskAction action, GxUserTask userTask, T entity);
	}

	/**
	 * An enum that represents the actions that can be performed on a task.
	 */
	public static enum GxTaskAction {
		/**
		 * The task was approved.
		 */
		APPROVED,
		/**
		 * The task was rejected.
		 */
		REJECTED,
		/**
		 * The task was completed.
		 */
		COMPLETED,
		/**
		 * The task was assigned.
		 */
		ASSIGNED,
		/**
		 * The task was skipped.
		 */
		SKIPPED
	}

	/**
	 * Checks if the form is an approval form.
	 * @return True if the form is an approval form, false otherwise.
	 */
	protected abstract boolean isApprovalForm();

	/**
	 * Checks if the task is assignable.
	 * @return True if the task is assignable, false otherwise.
	 */
	protected abstract boolean isAssignable();

	/**
	 * Gets the user task.
	 * @return The user task.
	 */
	public GxUserTask getUserTask() {
		assert userTask != null;
		return userTask;
	}

	/**
	 * An interface for user task handlers.
	 */
	public static interface GxUserTaskHandler {
		/**
		 * Proceeds with the task.
		 */
		void proceed();

		/**
		 * Cancels the task.
		 */
		void cancel();

		/**
		 * Called when an error occurs.
		 * @param t The error.
		 */
		void error(Throwable t);
	}

	/**
	 * An interface for user task assigners.
	 */
	public static interface GxUserTaskAssigner {
		/**
		 * Assigns the task.
		 * @param assignees The assignees.
		 */
		void assign(Collection<GxAssignee> assignees);

		/**
		 * Cancels the assignment.
		 */
		void cancel();

		/**
		 * Called when an error occurs.
		 * @param t The error.
		 */
		void error(Throwable t);
	}

	/**
	 * An interface for user task skippers.
	 */
	public static interface GxUserTaskSkipper {
		/**
		 * Skips the task.
		 */
		void skip();

		/**
		 * Cancels the skip.
		 */
		void cancel();

		/**
		 * Called when an error occurs.
		 * @param t The error.
		 */
		void error(Throwable t);
	}

}
