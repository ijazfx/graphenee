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
package io.graphenee.core.flow.i18n;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.vaadin.flow.spring.annotation.SpringComponent;

import io.graphenee.core.model.entity.GxSupportedLocale;
import io.graphenee.core.model.entity.GxTerm;
import io.graphenee.core.model.jpa.repository.GxSupportedLocaleRepository;
import io.graphenee.core.model.jpa.repository.GxTermRepository;
import io.graphenee.vaadin.flow.GxAbstractEntityForm;
import io.graphenee.vaadin.flow.GxAbstractEntityList;

@SuppressWarnings("serial")
@SpringComponent
@Scope("prototype")
public class GxTermTablePanel extends GxAbstractEntityList<GxTerm> {

	private GxTerm selectedTerm;

	@Autowired
	GxSupportedLocaleRepository localeRepo;

	@Autowired
	GxTermRepository termRepo;

	private List<GxSupportedLocale> availableLocales;

	public List<GxTerm> availableTerms;

	Map<String, GxTerm> terms;

	public GxTermTablePanel() {
		super(GxTerm.class);
	}

	@Override
	protected String[] visibleProperties() {
		return new String[] { "language", "termSingular", "termPlural" };
	}

	private List<GxTerm> findAvailableLocalesAndTerms() {
		availableLocales = localeRepo.findAll();
		availableTerms = termRepo.findByNamespaceAndTermKey(selectedTerm.getNamespace(), selectedTerm.getTermKey());
		terms = new HashMap<>();
		availableTerms.forEach(term -> {
			terms.put(term.getSupportedLocale().getLocaleCode(), term);
		});

		for (GxSupportedLocale locale : availableLocales) {
			if (!terms.containsKey(locale.getLocaleCode())) {
				GxTerm term = new GxTerm();
				term.setTermKey(selectedTerm.getTermKey());
				term.setNamespace(selectedTerm.getNamespace());
				term.setSupportedLocale(locale);
				terms.put(locale.getLocaleCode(), term);
			}
		}

		availableTerms = new ArrayList<>(terms.values());
		availableTerms.sort((a, b) -> a.getLanguage().compareToIgnoreCase(b.getLanguage()));
		return availableTerms;
	}

	@Override
	protected void postBuild() {
		hideToolbar();
	}

	public void initializeWithEntity(GxTerm term) {
		this.selectedTerm = term;
		refresh();
	}

	@Override
	protected Stream<GxTerm> getData() {
		return findAvailableLocalesAndTerms().stream();
	}

	@Override
	protected GxAbstractEntityForm<GxTerm> getEntityForm(GxTerm entity) {
		return null;
	}

	@Override
	protected boolean isGridInlineEditingEnabled() {
		return true;
	}

	@Override
	protected void onSave(GxTerm entity) {

	}

	@Override
	protected void onDelete(Collection<GxTerm> entities) {

	}

}
