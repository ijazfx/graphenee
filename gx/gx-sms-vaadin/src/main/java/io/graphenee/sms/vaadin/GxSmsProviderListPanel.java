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
package io.graphenee.sms.vaadin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Grid.Column;

import io.graphenee.core.enums.SmsProvider;
import io.graphenee.core.model.api.GxDataService;
import io.graphenee.core.model.bean.GxSmsProviderBean;
import io.graphenee.vaadin.AbstractEntityListPanel;
import io.graphenee.vaadin.TRAbstractForm;
import io.graphenee.vaadin.renderer.BooleanRenderer;

@SpringComponent
@Scope("prototype")
public class GxSmsProviderListPanel extends AbstractEntityListPanel<GxSmsProviderBean> {

	@Autowired
	GxDataService dataService;

	TRAbstractForm<GxSmsProviderBean> editorForm;

	public GxSmsProviderListPanel() {
		super(GxSmsProviderBean.class);
	}

	@Override
	protected boolean onSaveEntity(GxSmsProviderBean entity) {
		dataService.createOrUpdate(entity);
		return true;
	}

	@Override
	protected boolean onDeleteEntity(GxSmsProviderBean entity) {
		return false;
	}

	@Override
	protected String panelCaption() {
		return null;
	}

	@Override
	protected List<GxSmsProviderBean> fetchEntities() {
		return dataService.findSmsProvider();
	}

	@Override
	protected String[] visibleProperties() {
		return new String[] { "providerName", "isPrimary" };
	}

	@Override
	protected TRAbstractForm<GxSmsProviderBean> editorForm() {
		return editorForm;
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
	protected void applyRendererForColumn(Column column) {
		if (column.getPropertyId().toString().equals("isPrimary")) {
			column.setRenderer(new BooleanRenderer(event -> {
				GxSmsProviderBean item = (GxSmsProviderBean) event.getItemId();
				if (!item.getIsPrimary()) {
					entityGrid().getContainerDataSource().getItemIds().forEach(row -> {
						GxSmsProviderBean bean = (GxSmsProviderBean) row;
						bean.setIsPrimary(false);
					});
				}
				item.setIsPrimary(!item.getIsPrimary());
				if (item.getIsPrimary())
					dataService.markAsPrimary(item);
				else
					dataService.createOrUpdate(item);
				entityGrid().refreshAllRows();
			}), BooleanRenderer.SWITCH_CONVERTER);
		} else
			super.applyRendererForColumn(column);
	}

	@Override
	protected void onGridItemClicked(GxSmsProviderBean item, String propertyId) {
		if (!propertyId.equals("isPrimary"))
			super.onGridItemClicked(item, propertyId);
	}

}
