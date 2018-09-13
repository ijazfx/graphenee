package io.graphenee.jbpm.embedded.vaadin;

import java.util.Collection;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;

import io.graphenee.jbpm.embedded.GxAssignee;
import io.graphenee.jbpm.embedded.vaadin.GxSelectAssigneeForm.GxAssigneeHolder;
import io.graphenee.vaadin.TRAbstractForm;

@SuppressWarnings("serial")
public class GxSelectAssigneeForm extends TRAbstractForm<GxAssigneeHolder> {

	ComboBox assignee;
	private BeanItemContainer<GxAssignee> assigneeDataSource;

	@Override
	protected void addFieldsToForm(FormLayout form) {
		assignee = new ComboBox("Assign to");
		assigneeDataSource = new BeanItemContainer<>(GxAssignee.class);
		assignee.setContainerDataSource(assigneeDataSource);
		assignee.setItemCaptionPropertyId("fullName");
		assignee.addValueChangeListener(event -> {
			if (!isBinding())
				getEntity().setAssignee((GxAssignee) event.getProperty().getValue());
		});
		assignee.setRequired(true);
		form.addComponent(assignee);
	}

	@Override
	protected void postBinding(GxAssigneeHolder entity) {
		super.postBinding(entity);
		getSaveButton().setCaption("Assign");
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
		return "Assign Task";
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
