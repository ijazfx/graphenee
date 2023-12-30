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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Sort;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;

import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.core.model.entity.GxTerm;
import io.graphenee.core.model.jpa.repository.GxNamespaceRepository;
import io.graphenee.core.model.jpa.repository.GxTermRepository;
import io.graphenee.vaadin.flow.base.GxAbstractEntityForm;
import io.graphenee.vaadin.flow.base.GxAbstractEntityList;
import io.graphenee.vaadin.flow.base.GxFormLayout;

@SuppressWarnings("serial")
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
		return new String[] { "supportedLocale", "termKey", "termSingular", "termPlural" };
	}

	@Override
	protected void decorateSearchForm(GxFormLayout searchForm, Binder<GxTerm> searchBinder) {
		namespaceComboBox = new ComboBox<>("Namespace");
		namespaceComboBox.setItemLabelGenerator(GxNamespace::getNamespace);
		List<GxNamespace> namespaceBeans = namespaceRepo.findAll();
		namespaceComboBox.setItems(namespaceBeans);

		searchForm.add(namespaceComboBox);

		searchBinder.bind(namespaceComboBox, "namespace");

		searchBinder.addValueChangeListener(vcl -> {
			refresh();
		});
	}

	@Override
	protected void decorateColumn(String propertyName, Column<GxTerm> column) {
		if (propertyName.matches("supportedLocale")) {
			column.setHeader("Language");
		}
	}

	@Override
	protected void preEdit(GxTerm entity) {
		if (getSearchEntity().getNamespace() != null) {
			entity.setNamespace(getSearchEntity().getNamespace());
		}
	}

	public void initializeWithNamespace(GxNamespace namespace) {
		getSearchEntity().setNamespace(namespace);
		refresh();
	}

	@Override
	protected Stream<GxTerm> getData() {
		if (getSearchEntity().getNamespace() != null) {
			return termRepo.findByNamespace(getSearchEntity().getNamespace(), Sort.by("termKey")).stream();
		}
		return Stream.empty();
	}

	@Override
	protected GxAbstractEntityForm<GxTerm> getEntityForm(GxTerm entity) {
		return editorForm;
	}

	@Override
	protected void onSave(GxTerm entity) {
		termRepo.save(entity);
	}

	@Override
	protected void onDelete(Collection<GxTerm> entities) {
		for (GxTerm entity : entities) {
			termRepo.deleteByTermKeyAndNamespace(entity.getTermKey(), entity.getNamespace());
		}
	}

}
