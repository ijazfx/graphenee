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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.ComboBox;

import io.graphenee.core.model.api.GxDataService;
import io.graphenee.core.model.bean.GxSecurityPolicyBean;
import io.graphenee.core.model.bean.GxSecurityPolicyDocumentBean;
import io.graphenee.vaadin.AbstractEntityListPanel;
import io.graphenee.vaadin.TRAbstractForm;
import io.graphenee.vaadin.renderer.BooleanRenderer;

@SpringComponent
@Scope("prototype")
public class GxSecurityPolicyDocumentListPanel extends AbstractEntityListPanel<GxSecurityPolicyDocumentBean> {

	@Autowired
	GxDataService dataService;

	@Autowired
	GxSecurityPolicyDocumentForm editorForm;

	private ComboBox namespaceComboBox;

	private GxSecurityPolicyBean securityPolicyBean;

	public GxSecurityPolicyDocumentListPanel() {
		super(GxSecurityPolicyDocumentBean.class);
	}

	@Override
	protected boolean onSaveEntity(GxSecurityPolicyDocumentBean entity) {
		if (entity.getOid() == null) {
			securityPolicyBean.getSecurityPolicyDocumentCollectionFault().add(entity);
		} else {
			securityPolicyBean.getSecurityPolicyDocumentCollectionFault().update(entity);
		}
		return true;
	}

	@Override
	protected boolean onDeleteEntity(GxSecurityPolicyDocumentBean entity) {
		securityPolicyBean.getSecurityPolicyDocumentCollectionFault().remove(entity);
		return true;
	}

	@Override
	protected String panelCaption() {
		return "Security Policy Documents";
	}

	@Override
	protected List<GxSecurityPolicyDocumentBean> fetchEntities() {
		return new ArrayList<>(securityPolicyBean.getSecurityPolicyDocumentCollectionFault().getBeans());
	}

	@Override
	protected <F> List<GxSecurityPolicyDocumentBean> fetchEntities(F filter) {
		return Collections.emptyList();
	}

	@Override
	protected String[] visibleProperties() {
		return new String[] { "documentJson", "isDefault" };
	}

	@Override
	protected TRAbstractForm<GxSecurityPolicyDocumentBean> editorForm() {
		return editorForm;
	}

	@Override
	protected void addButtonsToToolbar(AbstractOrderedLayout toolbar) {
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
			if (column.getPropertyId().toString().matches("(isDefault)")) {
				column.setRenderer(new BooleanRenderer(), BooleanRenderer.CHECK_BOX_CONVERTER);
			}
		}
	}

	public void initializeWithEntity(GxSecurityPolicyBean securityPolicyBean) {
		this.securityPolicyBean = securityPolicyBean;

	}

}
