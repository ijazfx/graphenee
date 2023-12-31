package io.graphenee.jbpm.embedded.flow;

import java.util.Collection;
import java.util.List;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;

import io.graphenee.jbpm.embedded.GxAssignee;
import io.graphenee.jbpm.embedded.flow.GxSelectAssigneeForm.GxAssigneeHolder;
import io.graphenee.vaadin.flow.GxAbstractEntityForm;

@SuppressWarnings("serial")
public class GxSelectAssigneeForm extends GxAbstractEntityForm<GxAssigneeHolder> {

	public GxSelectAssigneeForm() {
		super(GxAssigneeHolder.class);
	}

	ComboBox<GxAssignee> assignee;
	private List<GxAssignee> assigneeList;

	@Override
	protected void customizeSaveButton(Button saveButton) {
		saveButton.setText("Assign");
	}

	public void initializeWithAssignees(Collection<GxAssignee> assignees) {
		assigneeList.clear();
		assigneeList.addAll(assignees);
	}

	@Override
	protected String formTitle() {
		return "Assign Task";
	}

	public static class GxAssigneeHolder {
		GxAssignee assignee;

		public GxAssignee getAssignee() {
			return assignee;
		}

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
