package io.graphenee.jbpm.embedded.vaadin;

import java.util.Collection;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Window;

import io.graphenee.jbpm.embedded.GxAssignee;
import io.graphenee.jbpm.embedded.vaadin.GxSelectAssigneeForm.GxAssigneeHolder;
import io.graphenee.vaadin.TRAbstractForm;

@SuppressWarnings("serial")
public class GxSelectAssigneeForm extends TRAbstractForm<GxAssigneeHolder> {

	ComboBox assignees;
	private BeanItemContainer<GxAssignee> assigneeDataSource;

	@Override
	protected void addFieldsToForm(FormLayout form) {
		assignees = new ComboBox("Assignee");
		assigneeDataSource = new BeanItemContainer<>(GxAssignee.class);
		assignees.setContainerDataSource(assigneeDataSource);
		assignees.setItemCaptionPropertyId("fullName");
		assignees.addValueChangeListener(event -> {
			if (!isBinding())
				getEntity().setAssignee((GxAssignee) event.getProperty().getValue());
		});
		assignees.setRequired(true);
		form.addComponent(assignees);
	}

	@Override
	public Window openInModalPopup() {
		setSaveCaption("Select");
		return super.openInModalPopup();
	}

	public void initializeWithAssignees(Collection<GxAssignee> assignees) {
		assigneeDataSource.removeAllItems();
		assigneeDataSource.addAll(assignees);
	}

	@Override
	protected boolean eagerValidationEnabled() {
		return true;
	}

	@Override
	protected String formTitle() {
		return "Select Assignee...";
	}

	@Override
	protected String popupHeight() {
		return "150px";
	}

	@Override
	protected String popupWidth() {
		return "400px";
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

}
