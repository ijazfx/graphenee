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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.vaadin.spring.annotation.SpringComponent;

import io.graphenee.core.model.api.GxDataService;
import io.graphenee.core.model.bean.GxStateBean;
import io.graphenee.vaadin.AbstractEntityListPanel;
import io.graphenee.vaadin.TRAbstractForm;

@SuppressWarnings("serial")
@SpringComponent
@Scope("prototype")
public class GxStateListPanel extends AbstractEntityListPanel<GxStateBean> {

	@Autowired
	GxDataService dataService;

	@Autowired
	GxStateForm editorForm;

	public GxStateListPanel() {
		super(GxStateBean.class);
	}

	@Override
	protected boolean onSaveEntity(GxStateBean entity) {
		dataService.createOrUpdate(entity);
		return true;
	}

	@Override
	protected boolean onDeleteEntity(GxStateBean entity) {
		dataService.delete(entity);
		return true;
	}

	@Override
	protected String panelCaption() {
		return "Countries";
	}

	@Override
	protected List<GxStateBean> fetchEntities() {
		return dataService.findState();
	}

	@Override
	protected String[] visibleProperties() {
		return new String[] { "stateName", "stateCode", "countryName" };
	}

	@Override
	protected TRAbstractForm<GxStateBean> editorForm() {
		return editorForm;
	}

	@Override
	protected boolean isGridCellFilterEnabled() {
		return true;
	}
}
