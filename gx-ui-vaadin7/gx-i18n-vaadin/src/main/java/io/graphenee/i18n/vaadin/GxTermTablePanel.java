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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.vaadin.viritin.fields.MTextField;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Field;

import io.graphenee.core.model.BeanFault;
import io.graphenee.core.model.api.GxDataService;
import io.graphenee.core.model.bean.GxSupportedLocaleBean;
import io.graphenee.core.model.bean.GxTermBean;
import io.graphenee.core.model.jpa.repository.GxSupportedLocaleRepository;
import io.graphenee.vaadin.AbstractEntityTablePanel;
import io.graphenee.vaadin.TRAbstractForm;

@SuppressWarnings("serial")
@SpringComponent
@Scope("prototype")
public class GxTermTablePanel extends AbstractEntityTablePanel<GxTermBean> {

	private GxTermBean gxTermBean;

	@Autowired
	GxDataService dataService;

	@Autowired
	GxSupportedLocaleRepository supportedLocaleRepo;

	private List<GxSupportedLocaleBean> availableLocales;

	public List<GxTermBean> availableTerms;

	Map<GxSupportedLocaleBean, GxTermBean> terms;

	public GxTermTablePanel() {
		super(GxTermBean.class);
	}

	@Override
	protected boolean onSaveEntity(GxTermBean entity) {
		return false;
	}

	@Override
	protected boolean onDeleteEntity(GxTermBean entity) {
		return false;
	}

	@Override
	protected String panelCaption() {
		return null;
	}

	@Override
	protected List<GxTermBean> fetchEntities() {
		return findAvailableLocalesAndTerms();
	}

	@Override
	protected String[] visibleProperties() {
		return new String[] { "language", "termSingular", "termPlural" };
	}

	@Override
	protected TRAbstractForm<GxTermBean> editorForm() {
		return null;
	}

	@Override
	protected boolean isTableEditable() {
		return true;
	}

	private List<GxTermBean> findAvailableLocalesAndTerms() {
		availableLocales = dataService.findSupportedLocale();
		availableTerms = dataService.findTermByTermKey(gxTermBean.getTermKey());
		terms = new HashMap<>();
		availableTerms.forEach(term -> {
			terms.put(term.getSupportedLocaleFault().getBean(), term);
		});

		for (GxSupportedLocaleBean locale : availableLocales) {
			if (!terms.containsKey(locale)) {
				GxTermBean term = new GxTermBean();
				term.setTermKey(gxTermBean.getTermKey());
				term.setNamespaceFault(BeanFault.beanFault(gxTermBean.getNamespaceFault().getOid(), gxTermBean.getNamespaceFault().getBean()));
				term.setSupportedLocaleFault(BeanFault.beanFault(locale.getOid(), locale));
				terms.put(locale, term);
			}
		}

		availableTerms = new ArrayList<>(terms.values());
		return availableTerms;
	}

	@Override
	protected Field<?> propertyField(GxTermBean itemId, String propertyId) {
		if (propertyId.matches("(termSingular)")) {
			MTextField termSingular = new MTextField().withFullWidth();
			termSingular.addFocusListener(event -> {
				termSingular.selectAll();
			});
			return termSingular;
		}
		if (propertyId.matches("(termPlural)")) {
			MTextField termPlural = new MTextField().withFullWidth();
			termPlural.addFocusListener(event -> {
				termPlural.selectAll();
			});
			return termPlural;
		}
		return super.propertyField(itemId, propertyId);
	}

	@Override
	protected void postBuild() {
		hideToolbar();
		entityTable().setHeight("250px");
	}

	public void initializeWithEntity(GxTermBean gxTermBean) {
		this.gxTermBean = gxTermBean;
	}

}
