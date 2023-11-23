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
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.vaadin.flow.spring.annotation.SpringComponent;

import io.graphenee.core.model.entity.GxSupportedLocale;
import io.graphenee.core.model.jpa.repository.GxSupportedLocaleRepository;
import io.graphenee.vaadin.flow.base.GxAbstractEntityForm;
import io.graphenee.vaadin.flow.base.GxAbstractEntityList;

@SpringComponent
@Scope("prototype")
public class GxSupportedLocaleListPanel extends GxAbstractEntityList<GxSupportedLocale> {

	@Autowired
	GxSupportedLocaleRepository repo;

	@Autowired
	GxSupportedLocaleForm editorForm;

	public GxSupportedLocaleListPanel() {
		super(GxSupportedLocale.class);
	}

	@Override
	protected String[] visibleProperties() {
		return new String[] { "localeName", "localeCode" };
	}

	@Override
	protected Stream<GxSupportedLocale> getData() {
		return repo.findAll().stream();
	}

	@Override
	protected GxAbstractEntityForm<GxSupportedLocale> getEntityForm(GxSupportedLocale entity) {
		return editorForm;
	}

	@Override
	protected void onSave(GxSupportedLocale entity) {
		repo.save(entity);
	}

	@Override
	protected void onDelete(Collection<GxSupportedLocale> entities) {
		repo.deleteInBatch(entities);
	}

}
