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
package io.graphenee.vaadin.flow.sms;

import java.util.Collection;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.spring.annotation.SpringComponent;

import io.graphenee.core.enums.SmsProvider;
import io.graphenee.core.model.api.GxDataService;
import io.graphenee.core.model.bean.GxSmsProviderBean;
import io.graphenee.vaadin.flow.base.GxAbstractEntityForm;
import io.graphenee.vaadin.flow.base.GxAbstractEntityList;

@SpringComponent
@Scope("prototype")
public class GxSmsProviderListPanel extends GxAbstractEntityList<GxSmsProviderBean> {

	@Autowired
	GxDataService dataService;

	GxAbstractEntityForm<GxSmsProviderBean> editorForm;

	public GxSmsProviderListPanel() {
		super(GxSmsProviderBean.class);
	}

	@Override
	protected String[] visibleProperties() {
		return new String[] { "providerName", "isPrimary" };
	}

	@Override
	protected void preEdit(GxSmsProviderBean item) {
		if (item.getProviderName().equals(SmsProvider.AWS.getProviderName()))
			editorForm = new GxAwsSmsProviderForm();
		else if (item.getProviderName().equals(SmsProvider.TWILIO.getProviderName()))
			editorForm = new GxTwilioSmsProviderForm();
		else if (item.getProviderName().equals(SmsProvider.EOCEAN.getProviderName()))
			editorForm = new GxEoceanSmsProviderForm();
		else
			editorForm = null;
	}

	@Override
	protected void onGridItemClicked(ItemClickEvent<GxSmsProviderBean> icl) {
		String propertyId = icl.getColumn().getId().orElse(null);
		if (propertyId != null && !propertyId.equals("isPrimary"))
			super.onGridItemClicked(icl);
	}

	@Override
	protected Stream<GxSmsProviderBean> getData() {
		return dataService.findSmsProvider().stream();
	}

	@Override
	protected GxAbstractEntityForm<GxSmsProviderBean> getEntityForm(GxSmsProviderBean entity) {
		return editorForm;
	}

	@Override
	protected void onSave(GxSmsProviderBean entity) {
		dataService.createOrUpdate(entity);
	}

	@Override
	protected void onDelete(Collection<GxSmsProviderBean> entities) {
	}

}
