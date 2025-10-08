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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.data.binder.PropertyDefinition;
import com.vaadin.flow.spring.annotation.SpringComponent;

import io.graphenee.core.model.entity.GxSupportedLocale;
import io.graphenee.core.model.entity.GxTerm;
import io.graphenee.core.model.entity.GxTermTranslation;
import io.graphenee.core.model.jpa.repository.GxSupportedLocaleRepository;
import io.graphenee.vaadin.flow.GxAbstractEntityForm;
import io.graphenee.vaadin.flow.GxAbstractEntityList;

@SuppressWarnings("serial")
@SpringComponent
@Scope("prototype")
public class GxTermTablePanel extends GxAbstractEntityList<GxTermTranslation> {

	private GxTerm selectedTerm;

	@Autowired
	GxSupportedLocaleRepository localeRepo;

	Map<GxSupportedLocale, GxTermTranslation> terms;

	public GxTermTablePanel() {
		super(GxTermTranslation.class);
	}

	@Override
	protected String[] visibleProperties() {
		return new String[] { "language", "termSingular", "termPlural" };
	}

	@Override
	protected void postBuild() {
		hideToolbar();
	}

	public void initializeWithEntity(GxTerm term) {
		this.selectedTerm = term;
		terms = new HashMap<>();
		refresh();
	}

	@Override
	protected Stream<GxTermTranslation> getData() {
		selectedTerm.getTranslations().forEach(t -> {
			terms.put(t.getSupportedLocale(), t);
		});
		localeRepo.findAll().forEach(l -> {
			if (!terms.containsKey(l)) {
				GxTermTranslation newTranslation = new GxTermTranslation();
				newTranslation.setSupportedLocale(l);
				selectedTerm.addToTranslations(newTranslation);
				terms.put(l, newTranslation);
			}
		});
		return terms.values().stream().sorted();
	}

	@Override
	protected GxAbstractEntityForm<GxTermTranslation> getEntityForm(GxTermTranslation entity) {
		return null;
	}

	@Override
	protected boolean isGridInlineEditingEnabled() {
		return true;
	}

	@Override
	protected void onSave(GxTermTranslation entity) {
	}

	@Override
	protected void onDelete(Collection<GxTermTranslation> entities) {
		entities.stream().forEach(t -> {
			t.setTermSingular(null);
			t.setTermPlural(null);
		});
	}

	@Override
	protected AbstractField<?, ?> inlineEditorForProperty(String propertyName,
			PropertyDefinition<GxTermTranslation, Object> propertyDefinition) {
		if (propertyName.matches("(termSingular|termPlural)")) {
			AbstractField<?, ?> tf = super.inlineEditorForProperty(propertyName, propertyDefinition);
			if(tf != null) {
				tf.getStyle().set("director", "rtl");
			}
			return tf;
		}
		return super.inlineEditorForProperty(propertyName, propertyDefinition);
	}

	@Override
	protected String dialogHeight() {
		return "37.5rem";
	}

	@Override
	protected String dialogWidth() {
		return "50rem";
	}

}
