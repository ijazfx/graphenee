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
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.ComboBox;

import io.graphenee.core.model.BeanFault;
import io.graphenee.core.model.api.GxDataService;
import io.graphenee.core.model.bean.GxNamespaceBean;
import io.graphenee.core.model.bean.GxSecurityPolicyBean;
import io.graphenee.vaadin.AbstractEntityListPanel;
import io.graphenee.vaadin.TRAbstractForm;
import io.graphenee.vaadin.renderer.BooleanRenderer;

@SpringComponent
@Scope("prototype")
public class GxSecurityPolicyListPanel extends AbstractEntityListPanel<GxSecurityPolicyBean> {

	private static final long serialVersionUID = 1L;

	@Autowired
	GxDataService dataService;

	@Autowired
	GxSecurityPolicyForm editorForm;

	private ComboBox namespaceComboBox;

	private GxNamespaceBean namespaceBean;

	public GxSecurityPolicyListPanel() {
		super(GxSecurityPolicyBean.class);
	}

	@Override
	protected boolean onSaveEntity(GxSecurityPolicyBean entity) {
		dataService.save(entity);
		return true;
	}

	@Override
	protected boolean onDeleteEntity(GxSecurityPolicyBean entity) {
		dataService.delete(entity);
		return true;
	}

	@Override
	protected String panelCaption() {
		return null;
	}

	@Override
	protected List<GxSecurityPolicyBean> fetchEntities() {
		if (namespaceBean != null)
			return dataService.findSecurityPolicyByNamespace(namespaceBean);
		return dataService.findSecurityPolicy();
	}

	@Override
	protected <F> List<GxSecurityPolicyBean> fetchEntities(F filter) {
		if (filter instanceof GxNamespaceBean) {
			return dataService.findSecurityPolicyByNamespace((GxNamespaceBean) filter);
		}
		return super.fetchEntities(filter);
	}

	@Override
	protected String[] visibleProperties() {
		return new String[] { "securityPolicyName", "securityPolicyDescription", "priority", "isActive" };
	}

	@Override
	protected TRAbstractForm<GxSecurityPolicyBean> editorForm() {
		return editorForm;
	}

	@Override
	protected void addButtonsToSecondaryToolbar(AbstractOrderedLayout toolbar) {
		namespaceComboBox = new ComboBox("Namespace");
		namespaceComboBox.setTextInputAllowed(false);
		namespaceComboBox.addItems(dataService.findNamespace());
		namespaceComboBox.addValueChangeListener(event -> {
			refresh(event.getProperty().getValue());
		});
		toolbar.addComponent(namespaceComboBox);
	}

	@Override
	protected void preEdit(GxSecurityPolicyBean item) {
		if (item.getOid() == null) {
			GxNamespaceBean selectedNamespaceBean = namespaceBean != null ? namespaceBean : (GxNamespaceBean) namespaceComboBox.getValue();
			if (selectedNamespaceBean != null) {
				item.setNamespaceFault(BeanFault.beanFault(selectedNamespaceBean.getOid(), selectedNamespaceBean));
			}
		}
	}

	@Override
	protected void postBuild() {
		super.postBuild();
		for (com.vaadin.ui.Grid.Column column : entityGrid().getColumns()) {
			if (column.getPropertyId().toString().matches("(isActive)")) {
				column.setRenderer(new BooleanRenderer(event -> {
					onGridItemClicked((GxSecurityPolicyBean) event.getItemId(), column.getPropertyId().toString());
				}), BooleanRenderer.SWITCH_CONVERTER);
			}
		}
	}

	@Override
	protected void onGridItemClicked(GxSecurityPolicyBean item, String propertyId) {
		if (propertyId.equals("isActive")) {
			item.setIsActive(!item.getIsActive());
			dataService.save(item);
			entityGrid().refreshRow(item);
			return;
		}
		super.onGridItemClicked(item, propertyId);
	}

	public void initializeWithNamespace(GxNamespaceBean namespaceBean) {
		this.namespaceBean = namespaceBean;
		namespaceComboBox.setVisible(namespaceBean == null);
	}

	@Override
	protected boolean isGridCellFilterEnabled() {
		return true;
	}

}
