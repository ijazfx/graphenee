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

import org.vaadin.viritin.fields.MTextField;

import com.vaadin.ui.FormLayout;

import io.graphenee.core.model.bean.GxSavedQueryBean;
import io.graphenee.vaadin.TRAbstractForm;

@SuppressWarnings("serial")
public class GxSaveQueryForm extends TRAbstractForm<GxSavedQueryBean> {

	MTextField queryName;

	@Override
	protected boolean eagerValidationEnabled() {
		return true;
	}

	@Override
	protected String formTitle() {
		return "Save Query Form";
	}

	@Override
	protected void addFieldsToForm(FormLayout form) {
		queryName = new MTextField("Query Name").withRequired(true);
		form.addComponent(queryName);
	}

	@Override
	protected String popupHeight() {
		return "150px";
	}

	@Override
	protected String popupWidth() {
		return "400px";
	}

}
