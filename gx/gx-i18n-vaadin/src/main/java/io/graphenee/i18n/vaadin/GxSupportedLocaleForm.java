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
package io.graphenee.i18n.vaadin;

import org.springframework.context.annotation.Scope;
import org.vaadin.viritin.fields.MCheckBox;
import org.vaadin.viritin.fields.MTextField;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.FormLayout;

import io.graphenee.core.model.bean.GxSupportedLocaleBean;
import io.graphenee.vaadin.TRAbstractForm;

@SpringComponent
@Scope("prototype")
public class GxSupportedLocaleForm extends TRAbstractForm<GxSupportedLocaleBean> {

	MTextField localeName;
	MTextField localeCode;
	MCheckBox isLeftToRight;
	MCheckBox isActive;

	@Override
	protected void addFieldsToForm(FormLayout form) {
		localeName = new MTextField("Locale Name").withRequired(true);
		localeName.setMaxLength(50);
		localeCode = new MTextField("Locale Code").withRequired(true);
		localeCode.setMaxLength(10);
		isLeftToRight = new MCheckBox("Direction LTR?");
		isActive = new MCheckBox("Is Active?");
		form.addComponents(localeName, localeCode, isLeftToRight, isActive);
	}

	@Override
	protected boolean eagerValidationEnabled() {
		return true;
	}

	@Override
	protected String formTitle() {
		return "Supported Locale";
	}

	@Override
	protected String popupHeight() {
		return "250px";
	}

	@Override
	protected String popupWidth() {
		return "500px";
	}

}
