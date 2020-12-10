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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.vaadin.spring.annotation.SpringComponent;

import io.graphenee.core.model.api.GxDataService;
import io.graphenee.core.model.bean.GxAccessKeyBean;
import io.graphenee.core.model.bean.GxNamespaceBean;
import io.graphenee.vaadin.AbstractEntityListPanel;
import io.graphenee.vaadin.TRAbstractForm;
import io.graphenee.vaadin.renderer.BooleanRenderer;

@SpringComponent
@Scope("prototype")
public class GxAccessKeyPanel extends AbstractEntityListPanel<GxAccessKeyBean> {

	private static final long serialVersionUID = 1L;

	@Autowired
	GxDataService dataService;
	@Autowired
	GxAccessKeyForm editorForm;

	private GxNamespaceBean namespaceBean;

	public GxAccessKeyPanel() {
		super(GxAccessKeyBean.class);
	}

	@Override
	protected boolean onSaveEntity(GxAccessKeyBean entity) {
		dataService.save(entity);
		return true;
	}

	@Override
	protected boolean onDeleteEntity(GxAccessKeyBean entity) {
		dataService.delete(entity);
		return true;
	}

	@Override
	protected String panelCaption() {
		return null;
	}

	@Override
	protected List<GxAccessKeyBean> fetchEntities() {
		return dataService.findAccessKey();
	}

	@Override
	protected String[] visibleProperties() {
		return new String[] { "accessKey", "accessKeyType", "isActive" };
	}

	@Override
	protected TRAbstractForm<GxAccessKeyBean> editorForm() {
		return editorForm;
	}

	@Override
	protected void postBuild() {
		super.postBuild();
		for (com.vaadin.ui.Grid.Column column : entityGrid().getColumns()) {
			if (column.getPropertyId().toString().matches("(isActive)")) {
				column.setRenderer(new BooleanRenderer(event -> {
					GxAccessKeyBean item = (GxAccessKeyBean) event.getItemId();
					item.setIsActive(!item.getIsActive());
					dataService.save(item);
					entityGrid().refreshRow(item);
				}), BooleanRenderer.SWITCH_CONVERTER);
			}
		}
	}

	@Override
	protected void onAddButtonClick(GxAccessKeyBean entity) {
		super.onAddButtonClick(entity);
		if (namespaceBean != null)
			editorForm.initializeWithNamespace(namespaceBean);
	}

	@Override
	protected void preEdit(GxAccessKeyBean item) {
		if (namespaceBean != null)
			editorForm.initializeWithNamespace(namespaceBean);
	}

	@Override
	protected boolean shouldShowDeleteConfirmation() {
		return true;
	}

	public void initializeWithNamespace(GxNamespaceBean namespaceBean) {
		this.namespaceBean = namespaceBean;
	}

	@Override
	protected boolean isGridCellFilterEnabled() {
		return true;
	}

}
