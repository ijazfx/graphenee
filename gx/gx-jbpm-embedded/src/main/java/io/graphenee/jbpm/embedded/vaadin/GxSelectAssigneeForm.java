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
		return "180px";
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
