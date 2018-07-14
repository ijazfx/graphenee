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
package io.graphenee.core.model.api;

import java.util.List;
import java.util.Locale;

import io.graphenee.core.model.bean.GxCityBean;
import io.graphenee.core.model.bean.GxCountryBean;
import io.graphenee.core.model.bean.GxEmailTemplateBean;
import io.graphenee.core.model.bean.GxGenderBean;
import io.graphenee.core.model.bean.GxNamespaceBean;
import io.graphenee.core.model.bean.GxSavedQueryBean;
import io.graphenee.core.model.bean.GxSecurityGroupBean;
import io.graphenee.core.model.bean.GxSecurityPolicyBean;
import io.graphenee.core.model.bean.GxStateBean;
import io.graphenee.core.model.bean.GxSupportedLocaleBean;
import io.graphenee.core.model.bean.GxTermBean;
import io.graphenee.core.model.bean.GxUserAccountBean;

public interface GxDataService {

	List<GxGenderBean> findGender();

	List<GxNamespaceBean> findNamespace();

	List<GxSupportedLocaleBean> findSupportedLocale();

	List<GxTermBean> findTermByNamespaceAndSupportedLocale(Integer page, Integer size, GxNamespaceBean namespace, GxSupportedLocaleBean supportedLocale);

	List<GxTermBean> findTermByLocale(Locale locale);

	List<GxTermBean> findTermByTermKeyAndLocale(String termKey, Locale locale);

	GxTermBean findEffectiveTermByTermKeyAndLocale(String termKey, Locale locale);

	GxTermBean save(GxTermBean entity);

	void delete(GxTermBean entity);

	GxSupportedLocaleBean save(GxSupportedLocaleBean entity);

	void delete(GxSupportedLocaleBean entity);

	GxNamespaceBean findNamespace(String namespace);

	GxNamespaceBean save(GxNamespaceBean entity);

	void delete(GxNamespaceBean entity);

	List<GxSecurityGroupBean> findSecurityGroup();

	GxSecurityGroupBean findSecurityGroup(Integer oidSecurityGroup);

	GxSecurityGroupBean save(GxSecurityGroupBean bean);

	void delete(GxSecurityGroupBean bean);

	List<GxSecurityGroupBean> findSecurityGroupByNamespace(GxNamespaceBean namespace);

	GxSecurityGroupBean findSecurityGroupByNamespaceAndGroupName(GxNamespaceBean namespace, String groupName);

	GxSecurityGroupBean findSecurityGroupByNamespaceAndGroupNameActive(GxNamespaceBean namespace, String groupName);

	List<GxUserAccountBean> findUserAccount();

	GxUserAccountBean save(GxUserAccountBean bean);

	void delete(GxUserAccountBean bean);

	List<GxSecurityPolicyBean> findSecurityPolicy();

	GxSecurityPolicyBean save(GxSecurityPolicyBean bean);

	void delete(GxSecurityPolicyBean bean);

	List<GxSecurityPolicyBean> findSecurityPolicyByNamespace(GxNamespaceBean namespace);

	List<GxUserAccountBean> findUserAccountBySecurityGroup(GxSecurityGroupBean securityGroup);

	GxGenderBean findGenderByCode(String genderCode);

	GxCountryBean createOrUpdate(GxCountryBean bean);

	GxStateBean createOrUpdate(GxStateBean bean);

	void delete(GxStateBean bean);

	void delete(GxCountryBean bean);

	List<GxCountryBean> findCountry();

	GxCountryBean findCountry(Integer oid);

	GxCountryBean findCountryByCountryName(String countryName);

	GxCountryBean findCountryByCountryAlpha3Code(String alpha3Code);

	GxCountryBean findCountryByNumericCode(Integer numericCode);

	GxCountryBean findCountryByStateName(String stateName);

	GxCountryBean findCountryByStateCode(String stateCode);

	GxCountryBean findCountryByCityName(String cityName);

	List<GxStateBean> findState();

	GxStateBean findState(Integer oid);

	GxStateBean findStateByStateCode(String stateCode);

	GxStateBean findStateByStateName(String stateName);

	GxStateBean findStateByCityName(String cityName);

	List<GxStateBean> findStateByCountry(Integer oidCountry);

	List<GxStateBean> findStateByCountryNumericCode(Integer numeriCode);

	List<GxStateBean> findStateByCountryCountryName(String countryName);

	List<GxCityBean> findCity();

	GxCityBean createOrUpdate(GxCityBean cityBean);

	void delete(GxCityBean cityBean);

	GxCityBean findCity(Integer oid);

	GxCityBean findCityByCityName(String cityName);

	List<GxCityBean> findCityByCountry(Integer oidCountry);

	List<GxCityBean> findCityByState(Integer oidState);

	List<GxCityBean> findCityByCountryNumericCode(Integer numericCode);

	List<GxCityBean> findCityByStateCode(String stateCode);

	GxSavedQueryBean findSavedQuery(Integer oid);

	List<GxSavedQueryBean> findSavedQueryByUsername(String username);

	void delete(GxSavedQueryBean bean);

	List<GxSavedQueryBean> findSavedQuery();

	GxSavedQueryBean save(GxSavedQueryBean bean);

	GxEmailTemplateBean findEmailTemplateByTemplateNameActive(String templateName);

	GxEmailTemplateBean findEmailTemplateByTemplateNameAndNamespaceActive(String templateName, GxNamespaceBean namespace);

	List<GxEmailTemplateBean> findEmailTemplate();

	List<GxEmailTemplateBean> findEmailTemplateByNamespace(GxNamespaceBean namespace);

	GxEmailTemplateBean save(GxEmailTemplateBean bean);

	void delete(GxEmailTemplateBean bean);

	GxNamespaceBean findOrCreateNamespace(String namespace);

	GxEmailTemplateBean findEmailTemplate(Integer oid);

	List<GxEmailTemplateBean> findEmailTemplateByNamespaceActive(GxNamespaceBean namespace);

	List<GxEmailTemplateBean> findEmailTemplateByNamespaceInactive(GxNamespaceBean namespace);

	List<GxEmailTemplateBean> findEmailTemplateActive();

	List<GxEmailTemplateBean> findEmailTemplateInactive();

	List<GxSecurityGroupBean> findSecurityGroupActive();

	List<GxSecurityGroupBean> findSecurityGroupInactive();

	List<GxSecurityGroupBean> findSecurityGroupByNamespaceActive(GxNamespaceBean namespace);

	List<GxSecurityGroupBean> findSecurityGroupByNamespaceInactive(GxNamespaceBean namespace);

	List<GxSecurityPolicyBean> findSecurityPolicyActive();

	List<GxSecurityPolicyBean> findSecurityPolicyInactive();

	List<GxSecurityPolicyBean> findSecurityPolicyByNamespaceActive(GxNamespaceBean namespace);

	List<GxSecurityPolicyBean> findSecurityPolicyByNamespaceInactive(GxNamespaceBean namespace);

	List<GxUserAccountBean> findUserAccountActive();

	List<GxUserAccountBean> findUserAccountInactive();

	List<GxUserAccountBean> findUserAccountBySecurityGroupActive(GxSecurityGroupBean securityGroup);

	List<GxUserAccountBean> findUserAccountBySecurityGroupInactive(GxSecurityGroupBean securityGroup);

	GxUserAccountBean findUserAccount(Integer oidUserAccount);

	GxUserAccountBean findUserAccountByUsername(String username);

	GxUserAccountBean findUserAccountByUsernameAndPassword(String username, String password);

	GxSecurityGroupBean findOrCreateSecurityGroup(String groupName, GxNamespaceBean namespaceBean);

	GxSecurityGroupBean createOrUpdate(GxSecurityGroupBean bean);

	GxSecurityPolicyBean findOrCreateSecurityPolicy(String policyName, GxNamespaceBean namespaceBean);

	GxSecurityPolicyBean createOrUpdate(GxSecurityPolicyBean bean);

}
