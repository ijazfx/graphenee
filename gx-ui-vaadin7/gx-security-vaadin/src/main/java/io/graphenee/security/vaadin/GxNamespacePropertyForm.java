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
package io.graphenee.security.vaadin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.vaadin.viritin.fields.MTextField;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.FormLayout;

import io.graphenee.core.model.api.GxDataService;
import io.graphenee.core.model.bean.GxNamespacePropertyBean;
import io.graphenee.vaadin.TRAbstractForm;

@SpringComponent
@Scope("prototype")
public class GxNamespacePropertyForm extends TRAbstractForm<GxNamespacePropertyBean> {

	private static final long serialVersionUID = 1L;

	@Autowired
	GxDataService dataService;

	MTextField propertyKey;
	MTextField propertyValue;
	MTextField propertyDefaultValue;

	@Override
	protected void addFieldsToForm(FormLayout form) {
		propertyKey = new MTextField("Key");
		propertyValue = new MTextField("Value");
		propertyDefaultValue = new MTextField("Default Value");
		form.addComponents(propertyKey, propertyValue, propertyDefaultValue);
	}

	@Override
	protected boolean eagerValidationEnabled() {
		return true;
	}

	@Override
	protected String formTitle() {
		return "Namespace Property";
	}

	@Override
	protected String popupWidth() {
		return "500px";
	}

	@Override
	protected String popupHeight() {
		return "200px";
	}

}
