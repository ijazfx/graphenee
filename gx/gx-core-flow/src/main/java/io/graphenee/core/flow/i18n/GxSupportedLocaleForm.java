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
package io.graphenee.core.flow.i18n;

import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;

import io.graphenee.core.model.entity.GxSupportedLocale;
import io.graphenee.vaadin.flow.GxAbstractEntityForm;

@SuppressWarnings("serial")
@SpringComponent
@Scope("prototype")
public class GxSupportedLocaleForm extends GxAbstractEntityForm<GxSupportedLocale> {

	TextField localeName;
	TextField localeCode;
	Checkbox isLeftToRight;
	Checkbox isActive;

	public GxSupportedLocaleForm() {
		super(GxSupportedLocale.class);
	}

	@Override
	protected String formTitle() {
		return "Supported Locale";
	}

	@Override
	protected void decorateForm(HasComponents entityForm) {
		localeName = new TextField("Locale Name");
		localeName.setMaxLength(50);
		localeCode = new TextField("Locale Code");
		localeCode.setMaxLength(10);
		isLeftToRight = new Checkbox("Direction LTR?");
		isActive = new Checkbox("Is Active?");
		entityForm.add(localeName, localeCode, isLeftToRight, isActive);

	}

	@Override
	protected void bindFields(Binder<GxSupportedLocale> dataBinder) {
		dataBinder.forMemberField(localeName).asRequired();
		dataBinder.forMemberField(localeCode).asRequired();
	}

	@Override
	protected String dialogHeight() {
		return "300px";
	}

}
