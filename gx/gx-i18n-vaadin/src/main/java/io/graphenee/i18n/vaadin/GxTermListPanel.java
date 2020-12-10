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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.ComboBox;

import io.graphenee.core.model.BeanFault;
import io.graphenee.core.model.api.GxDataService;
import io.graphenee.core.model.bean.GxNamespaceBean;
import io.graphenee.core.model.bean.GxSupportedLocaleBean;
import io.graphenee.core.model.bean.GxTermBean;
import io.graphenee.i18n.api.LocalizerService;
import io.graphenee.vaadin.AbstractEntityListPanel;
import io.graphenee.vaadin.TRAbstractForm;

@SpringComponent
@Scope("prototype")
public class GxTermListPanel extends AbstractEntityListPanel<GxTermBean> {

	@Autowired
	GxDataService dataService;

	@Autowired
	GxTermForm editorForm;

	@Autowired
	LocalizerService localizer;

	private GxNamespaceBean selectedNamespace;
	private GxSupportedLocaleBean selectedSupportedLocale;

	private ComboBox namespaceComboBox;

	public GxTermListPanel() {
		super(GxTermBean.class);
	}

	@Override
	protected boolean onSaveEntity(GxTermBean entity) {
		return true;
	}

	@Override
	protected boolean onDeleteEntity(GxTermBean entity) {
		dataService.deleteTermByTermKeyAndOidNameSpace(entity.getTermKey(), entity.getNamespaceFault().getOid());
		return true;
	}

	@Override
	protected String panelCaption() {
		return null;
	}

	@Override
	protected List<GxTermBean> fetchEntities() {
		return dataService.findDistinctTermByNamespaceAndSupportedLocale(selectedNamespace, selectedSupportedLocale);
	}

	@Override
	protected String[] visibleProperties() {
		return new String[] { "termKey", "termSingular", "termPlural" };
	}

	@Override
	protected TRAbstractForm<GxTermBean> editorForm() {
		return editorForm;
	}

	@Override
	protected void addButtonsToToolbar(AbstractOrderedLayout toolbar) {
		namespaceComboBox = new ComboBox("Namespace");
		List<GxNamespaceBean> gxNamespaceBeans = dataService.findNamespace();
		namespaceComboBox.addItems(gxNamespaceBeans);
		namespaceComboBox.setValue(gxNamespaceBeans.get(0));
		selectedNamespace = (GxNamespaceBean) namespaceComboBox.getValue();
		namespaceComboBox.addValueChangeListener(event -> {
			selectedNamespace = (GxNamespaceBean) event.getProperty().getValue();
			refresh();
		});

		ComboBox supportedLocaleComboBox = new ComboBox("Supported Locale");
		supportedLocaleComboBox.addItems(dataService.findSupportedLocale());
		supportedLocaleComboBox.addValueChangeListener(event -> {
			selectedSupportedLocale = (GxSupportedLocaleBean) event.getProperty().getValue();
			refresh();
		});

		toolbar.addComponents(namespaceComboBox, supportedLocaleComboBox);
		super.addButtonsToToolbar(toolbar);
	}

	@Override
	protected void onAddButtonClick(GxTermBean entity) {
		if (selectedNamespace != null) {
			entity.setNamespaceFault(BeanFault.beanFault(selectedNamespace.getOid(), selectedNamespace));
		} else {
			entity.setNamespaceFault(null);
		}
		if (selectedSupportedLocale != null) {
			entity.setSupportedLocaleFault(BeanFault.beanFault(selectedSupportedLocale.getOid(), selectedSupportedLocale));
		} else {
			entity.setSupportedLocaleFault(null);
		}
		super.onAddButtonClick(entity);
	}

	public void initializeWithNamespace(GxNamespaceBean namespaceBean) {
		this.selectedNamespace = namespaceBean;
		namespaceComboBox.setVisible(namespaceBean == null);
	}

	@Override
	protected boolean isGridCellFilterEnabled() {
		return true;
	}

}
