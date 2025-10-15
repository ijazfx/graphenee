package io.graphenee.jbpm.embedded.flow;

import java.util.Collection;
import java.util.List;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;

import io.graphenee.jbpm.embedded.GxAssignee;
import io.graphenee.jbpm.embedded.flow.GxSelectAssigneeForm.GxAssigneeHolder;
import io.graphenee.vaadin.flow.GxAbstractEntityForm;

/**
 * A form for selecting an assignee.
 */
public class GxSelectAssigneeForm extends GxAbstractEntityForm<GxAssigneeHolder> {

	/**
	 * Creates a new instance of this form.
	 */
	public GxSelectAssigneeForm() {
		super(GxAssigneeHolder.class);
	}

	ComboBox<GxAssignee> assignee;
	private List<GxAssignee> assigneeList;

	@Override
	protected void customizeSaveButton(Button saveButton) {
		saveButton.setText("Assign");
	}

	/**
	 * Initializes the form with a collection of assignees.
	 * @param assignees The assignees.
	 */
	public void initializeWithAssignees(Collection<GxAssignee> assignees) {
		assigneeList.clear();
		assigneeList.addAll(assignees);
	}

	@Override
	protected String formTitle() {
		return "Assign Task";
	}

	/**
	 * A holder for an assignee.
	 */
	public static class GxAssigneeHolder {
		/**
		 * Creates a new instance of this holder.
		 */
		public GxAssigneeHolder() {
			// a default constructor
		}
		GxAssignee assignee;

		/**
		 * Gets the assignee.
		 * @return The assignee.
		 */
		public GxAssignee getAssignee() {
			return assignee;
		}

		/**
		 * Sets the assignee.
		 * @param assignee The assignee.
		 */
		public void setAssignee(GxAssignee assignee) {
			this.assignee = assignee;
		}
	}

	@Override
	protected void decorateForm(HasComponents form) {
		assignee = new ComboBox<>("Assign to");
		assignee.setItems(assigneeList);
		assignee.setItemLabelGenerator(GxAssignee::getFullName);
		assignee.addValueChangeListener(event -> {
			if (isEntityBound()) {
				assignee.setValue(getEntity().getAssignee());
			}
		});
		assignee.setRequired(true);
		form.add(assignee);
	}

}
