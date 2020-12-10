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
import io.graphenee.core.model.bean.GxNamespacePropertyBean;
import io.graphenee.vaadin.AbstractEntityListPanel;
import io.graphenee.vaadin.TRAbstractForm;

@SpringComponent
@Scope("prototype")
public class GxNamespacePropertyListPanel extends AbstractEntityListPanel<GxNamespacePropertyBean> {

	private static final long serialVersionUID = 1L;

	@Autowired
	GxDataService dataService;

	@Autowired
	GxNamespacePropertyForm editorForm;

	private ComboBox namespaceComboBox;

	private GxNamespaceBean namespaceBean;

	public GxNamespacePropertyListPanel() {
		super(GxNamespacePropertyBean.class);
	}

	@Override
	protected boolean onSaveEntity(GxNamespacePropertyBean entity) {
		dataService.save(entity);
		return true;
	}

	@Override
	protected boolean onDeleteEntity(GxNamespacePropertyBean entity) {
		dataService.delete(entity);
		return true;
	}

	@Override
	protected String panelCaption() {
		return null;
	}

	@Override
	protected List<GxNamespacePropertyBean> fetchEntities() {
		if (namespaceBean != null)
			return dataService.findNamespacePropertyByNamespace(namespaceBean);
		return dataService.findNamespaceProperty();
	}

	@Override
	protected <F> List<GxNamespacePropertyBean> fetchEntities(F filter) {
		if (filter instanceof GxNamespaceBean) {
			return dataService.findNamespacePropertyByNamespace((GxNamespaceBean) filter);
		}
		return super.fetchEntities(filter);
	}

	@Override
	protected String[] visibleProperties() {
		return new String[] { "propertyKey", "propertyValue", "propertyDefaultValue" };
	}

	@Override
	protected TRAbstractForm<GxNamespacePropertyBean> editorForm() {
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
	protected void preEdit(GxNamespacePropertyBean item) {
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

}
