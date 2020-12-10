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
package io.graphenee.core.vaadin;

import org.springframework.context.annotation.Scope;
import org.vaadin.viritin.fields.MCheckBox;
import org.vaadin.viritin.fields.MTextField;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.FormLayout;

import io.graphenee.core.model.bean.GxResourceBean;
import io.graphenee.vaadin.TRAbstractForm;

@SpringComponent
@Scope("prototype")
public class GxResourceForm extends TRAbstractForm<GxResourceBean> {

	MTextField resourceName, resourceDescription;
	MCheckBox isActive;

	@Override
	protected void addFieldsToForm(FormLayout form) {
		resourceName = new MTextField("Resource Name");
		resourceName.setRequired(true);
		resourceDescription = new MTextField("Resource Description");
		isActive = new MCheckBox("Is Active?", true);
		form.addComponents(resourceName, resourceDescription, isActive);
	}

	@Override
	protected boolean eagerValidationEnabled() {
		return true;
	}

	@Override
	protected String formTitle() {
		return "Resource Form";
	}

	@Override
	protected String popupHeight() {
		return "300px";
	}

	@Override
	protected String popupWidth() {
		return "500px";
	}

}
