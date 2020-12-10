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
import org.vaadin.viritin.button.MButton;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.themes.ValoTheme;

import io.graphenee.core.model.BeanFault;
import io.graphenee.core.model.api.GxDataService;
import io.graphenee.core.model.bean.GxEmailTemplateBean;
import io.graphenee.core.model.bean.GxNamespaceBean;
import io.graphenee.vaadin.AbstractEntityListPanel;
import io.graphenee.vaadin.TRAbstractForm;

@SpringComponent
@Scope("prototype")
public class GxEmailTemplateListPanel extends AbstractEntityListPanel<GxEmailTemplateBean> {

	@Autowired
	GxDataService dataService;

	@Autowired
	GxEmailTemplateForm editorForm;

	public static final int ACTIVE = 1;
	public static final int INACTIVE = 2;

	private Integer fetchMode = ACTIVE;

	private GxNamespaceBean namespace;

	public GxEmailTemplateListPanel() {
		super(GxEmailTemplateBean.class);
	}

	@Override
	protected void addButtonsToSecondaryToolbar(AbstractOrderedLayout toolbar) {
		MButton activeButton = new MButton("Active");
		MButton inactiveButton = new MButton("Inactive");

		activeButton.addClickListener(event -> {
			activeButton.setEnabled(false);
			inactiveButton.setEnabled(true);
			fetchMode = ACTIVE;
			refresh();
		});
		inactiveButton.addClickListener(event -> {
			activeButton.setEnabled(true);
			inactiveButton.setEnabled(false);
			fetchMode = INACTIVE;
			refresh();
		});

		activeButton.setEnabled(false);
		inactiveButton.setEnabled(true);

		CssLayout activeInactiveLayout = new CssLayout(activeButton, inactiveButton);
		activeInactiveLayout.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		activeInactiveLayout.setCaption("Template Status");
		toolbar.addComponent(activeInactiveLayout);
		toolbar.setExpandRatio(activeInactiveLayout, 1);
	}

	@Override
	protected boolean onSaveEntity(GxEmailTemplateBean entity) {
		dataService.save(entity);
		return true;
	}

	@Override
	protected boolean onDeleteEntity(GxEmailTemplateBean entity) {
		dataService.delete(entity);
		return true;
	}

	@Override
	protected String panelCaption() {
		return null;
	}

	@Override
	protected <F> List<GxEmailTemplateBean> fetchEntities(F filter) {
		if (filter instanceof GxNamespaceBean) {
			namespace = (GxNamespaceBean) filter;
			switch (fetchMode) {
			case ACTIVE:
				return dataService.findEmailTemplateByNamespaceActive(namespace);
			case INACTIVE:
				return dataService.findEmailTemplateByNamespaceInactive(namespace);
			}
		}
		return super.fetchEntities(filter);
	}

	@Override
	protected List<GxEmailTemplateBean> fetchEntities() {
		switch (fetchMode) {
		case ACTIVE:
			return dataService.findEmailTemplateActive();
		case INACTIVE:
			return dataService.findEmailTemplateInactive();
		}
		return null;
	}

	@Override
	protected String[] visibleProperties() {
		return new String[] { "templateCode", "templateName", "subject" };
	}

	@Override
	protected TRAbstractForm<GxEmailTemplateBean> editorForm() {
		return editorForm;
	}

	@Override
	protected void onAddButtonClick(GxEmailTemplateBean entity) {
		if (namespace != null) {
			entity.setNamespaceBeanFault(BeanFault.beanFault(namespace.getOid(), namespace));
		}
		super.onAddButtonClick(entity);
	}

}
