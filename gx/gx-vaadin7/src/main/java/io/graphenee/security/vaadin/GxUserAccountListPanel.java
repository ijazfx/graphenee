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

import java.sql.Timestamp;
import java.util.List;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.ComboBox;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import io.graphenee.core.model.BeanFault;
import io.graphenee.core.model.api.GxDataService;
import io.graphenee.core.model.bean.GxNamespaceBean;
import io.graphenee.core.model.bean.GxUserAccountBean;
import io.graphenee.vaadin.AbstractEntityListPanel;
import io.graphenee.vaadin.TRAbstractForm;
import io.graphenee.vaadin.renderer.BooleanRenderer;

@SpringComponent
@Scope("prototype")
public class GxUserAccountListPanel extends AbstractEntityListPanel<GxUserAccountBean> {

	private static final long serialVersionUID = 1L;

	@Autowired
	GxDataService dataService;

	@Autowired
	GxUserAccountForm editorForm;

	private ComboBox namespaceComboBox;

	private GxNamespaceBean namespaceBean;

	public GxUserAccountListPanel() {
		super(GxUserAccountBean.class);
	}

	@Override
	protected boolean onSaveEntity(GxUserAccountBean entity) {
		if (entity.getOid() == null)
			entity.setAccountActivationDate(new Timestamp(System.currentTimeMillis()));
		dataService.save(entity);
		return true;
	}

	@Override
	protected boolean onDeleteEntity(GxUserAccountBean entity) {
		dataService.delete(entity);
		return true;
	}

	@Override
	protected String panelCaption() {
		return null;
	}

	@Override
	protected List<GxUserAccountBean> fetchEntities() {
		if (namespaceBean != null)
			return dataService.findUserAccountByNamespace(namespaceBean);
		return dataService.findUserAccount();
	}

	@Override
	protected <F> List<GxUserAccountBean> fetchEntities(F filter) {
		if (filter instanceof GxNamespaceBean) {
			return dataService.findUserAccountByNamespace((GxNamespaceBean) filter);
		}
		return super.fetchEntities(filter);
	}

	@Override
	protected String[] visibleProperties() {
		return new String[] { "username", "firstName", "lastName", "isActive", "isLocked", "isPasswordChangeRequired" };
	}

	@Override
	protected TRAbstractForm<GxUserAccountBean> editorForm() {
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
	protected void postBuild() {
		super.postBuild();
		for (com.vaadin.ui.Grid.Column column : entityGrid().getColumns()) {
			if (column.getPropertyId().toString().matches("(isActive|isLocked|isPasswordChangeRequired)")) {
				column.setRenderer(new BooleanRenderer(event -> {
					onGridItemClicked((GxUserAccountBean) event.getItemId(), column.getPropertyId().toString());
				}), BooleanRenderer.SWITCH_CONVERTER);
			}
		}
	}

	@Override
	protected void onGridItemClicked(GxUserAccountBean item, String propertyId) {
		if (propertyId.equals("isActive")) {
			item.setIsActive(!item.getIsActive());
			dataService.save(item);
			entityGrid().refreshRow(item);
			return;
		}
		if (propertyId.equals("isLocked")) {
			item.setIsLocked(!item.getIsLocked());
			dataService.save(item);
			entityGrid().refreshRow(item);
			return;
		}
		if (propertyId.equals("isPasswordChangeRequired")) {
			item.setIsPasswordChangeRequired(!item.getIsPasswordChangeRequired());
			dataService.save(item);
			entityGrid().refreshRow(item);
			return;
		}
		super.onGridItemClicked(item, propertyId);
	}

	@Override
	protected void preEdit(GxUserAccountBean item) {
		if (item.getOid() == null) {
			GxNamespaceBean selectedNamespaceBean = namespaceBean != null ? namespaceBean : (GxNamespaceBean) namespaceComboBox.getValue();
			if (selectedNamespaceBean != null) {
				item.setNamespaceFault(BeanFault.beanFault(selectedNamespaceBean.getOid(), selectedNamespaceBean));
			}
		}
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
