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
package io.graphenee.vaadin.flow.i18n;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.core.model.entity.GxTerm;
import io.graphenee.core.model.jpa.repository.GxNamespaceRepository;
import io.graphenee.core.model.jpa.repository.GxTermRepository;
import io.graphenee.vaadin.flow.base.GxAbstractEntityForm;
import io.graphenee.vaadin.flow.base.GxAbstractEntityList;

@SpringComponent
@Scope("prototype")
public class GxTermListPanel extends GxAbstractEntityList<GxTerm> {

	@Autowired
	private GxTermRepository termRepo;

	@Autowired
	private GxNamespaceRepository namespaceRepo;

	@Autowired
	private GxTermForm editorForm;

	private ComboBox<GxNamespace> namespaceComboBox;

	public GxTermListPanel() {
		super(GxTerm.class);
	}

	@Override
	protected String[] visibleProperties() {
		return new String[] { "termKey", "termSingular", "termPlural" };
	}

	@Override
	protected void decorateSearchForm(FormLayout searchForm, Binder<GxTerm> searchBinder) {
		namespaceComboBox = new ComboBox<>("Namespace");
		namespaceComboBox.setItemLabelGenerator(GxNamespace::getNamespace);
		List<GxNamespace> gxNamespaceBeans = namespaceRepo.findAll();
		namespaceComboBox.setItems(gxNamespaceBeans);

		searchForm.add(namespaceComboBox);

		searchBinder.bind(namespaceComboBox, "gxNamespace");

		searchBinder.addValueChangeListener(vcl -> {
			refresh();
		});
	}

	@Override
	protected void preEdit(GxTerm entity) {
		if (getSearchEntity().getGxNamespace() != null) {
			entity.setGxNamespace(getSearchEntity().getGxNamespace());
		}
	}

	public void initializeWithNamespace(GxNamespace namespace) {
		getSearchEntity().setGxNamespace(namespace);
		refresh();
	}

	@Override
	protected Stream<GxTerm> getData() {
		if (getSearchEntity().getGxNamespace() != null) {
			return termRepo.findByGxNamespaceOid(getSearchEntity().getGxNamespace().getOid()).stream();
		}
		return Stream.empty();
	}

	@Override
	protected GxAbstractEntityForm<GxTerm> getEntityForm(GxTerm entity) {
		return editorForm;
	}

	@Override
	protected void onSave(GxTerm entity) {
		editorForm.saveGxTermEntities();
	}

	@Override
	protected void onDelete(Collection<GxTerm> entities) {
		for (GxTerm entity : entities) {
			termRepo.deleteByTermKeyAndOidNameSpace(entity.getTermKey(), entity.getGxNamespace().getOid());
		}
	}

}
