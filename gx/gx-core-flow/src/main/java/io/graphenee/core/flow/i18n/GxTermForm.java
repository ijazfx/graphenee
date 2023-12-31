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

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;

import io.graphenee.core.model.entity.GxSupportedLocale;
import io.graphenee.core.model.entity.GxTerm;
import io.graphenee.core.model.jpa.repository.GxNamespaceRepository;
import io.graphenee.core.model.jpa.repository.GxSupportedLocaleRepository;
import io.graphenee.core.model.jpa.repository.GxTermRepository;
import io.graphenee.i18n.LocalizerService;
import io.graphenee.vaadin.flow.GxAbstractEntityForm;

@SpringComponent
@Scope("prototype")
public class GxTermForm extends GxAbstractEntityForm<GxTerm> {

	private static final long serialVersionUID = 1L;

	public GxTermForm() {
		super(GxTerm.class);
	}

	@Autowired
	GxNamespaceRepository namespaceRepo;

	@Autowired
	GxSupportedLocaleRepository localeRepo;

	@Autowired
	GxTermRepository termRepo;

	@Autowired
	GxTermTablePanel termTablePanel;

	@Autowired
	LocalizerService localizer;

	TextField termKey;

	Checkbox isActive;

	Map<GxSupportedLocale, GxTerm> terms;

	@Override
	protected void decorateForm(HasComponents entityForm) {
		termKey = new TextField("Term Key");
		isActive = new Checkbox("Is Active?", true);

		termTablePanel.initializeWithEntity(getEntity());
		termTablePanel.setHeight("480px");

		entityForm.add(termKey, isActive, termTablePanel);
		setColspan(termTablePanel, 2);
	}

	@Override
	protected void bindFields(Binder<GxTerm> dataBinder) {
		dataBinder.forMemberField(termKey).asRequired();
	}

	@Override
	protected void postBinding(GxTerm entity) {
		termTablePanel.initializeWithEntity(entity);
	}

	public void saveGxTermEntities() {
		termTablePanel.availableTerms.forEach(term -> {
			term.setTermKey(termKey.getValue());
			term.setNamespace(getEntity().getNamespace());
			term.setIsActive(isActive.getValue());
			termRepo.save(term);
			localizer.invalidateTerm(term.getTermKey());
		});
	}

	@Override
	protected String formTitle() {
		if (getEntity().getOid() == null) {
			return null;
		}
		return getEntity().getTermKey();
	}

	@Override
	protected String dialogHeight() {
		return "650px";
	}

	@Override
	protected String dialogWidth() {
		return "650px";
	}
}
