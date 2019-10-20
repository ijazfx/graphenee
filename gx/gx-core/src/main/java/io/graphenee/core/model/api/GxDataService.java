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

import java.sql.Timestamp;
import java.util.List;
import java.util.Locale;

import io.graphenee.core.enums.SmsProvider;
import io.graphenee.core.exception.RegisterDeviceFailedException;
import io.graphenee.core.exception.UnregisterDeviceFailedException;
import io.graphenee.core.model.bean.GxAccessKeyBean;
import io.graphenee.core.model.bean.GxAuditLogBean;
import io.graphenee.core.model.bean.GxCityBean;
import io.graphenee.core.model.bean.GxCountryBean;
import io.graphenee.core.model.bean.GxCurrencyBean;
import io.graphenee.core.model.bean.GxEmailTemplateBean;
import io.graphenee.core.model.bean.GxGenderBean;
import io.graphenee.core.model.bean.GxNamespaceBean;
import io.graphenee.core.model.bean.GxNamespacePropertyBean;
import io.graphenee.core.model.bean.GxRegisteredDeviceBean;
import io.graphenee.core.model.bean.GxResourceBean;
import io.graphenee.core.model.bean.GxSavedQueryBean;
import io.graphenee.core.model.bean.GxSecurityGroupBean;
import io.graphenee.core.model.bean.GxSecurityPolicyBean;
import io.graphenee.core.model.bean.GxSmsProviderBean;
import io.graphenee.core.model.bean.GxStateBean;
import io.graphenee.core.model.bean.GxSupportedLocaleBean;
import io.graphenee.core.model.bean.GxTermBean;
import io.graphenee.core.model.bean.GxUserAccountBean;
import io.graphenee.core.model.entity.GxSmsProvider;

public interface GxDataService {

	public static final String CORE_NAMESPACE = "io.graphenee.core";

	public static final String SYSTEM_NAMESPACE = "io.graphenee.system";

	List<GxGenderBean> findGender();

	List<GxNamespaceBean> findNamespace();

	GxNamespaceBean findNamespace(Integer oidNamespace);

	GxNamespaceBean findSystemNamespace();

	List<GxSupportedLocaleBean> findSupportedLocale();

	List<GxTermBean> findTermByNamespaceAndSupportedLocale(Integer page, Integer size, GxNamespaceBean namespace, GxSupportedLocaleBean supportedLocale);

	List<GxTermBean> findDistinctTermByNamespaceAndSupportedLocale(GxNamespaceBean namespace, GxSupportedLocaleBean supportedLocale);

	List<GxTermBean> findTermByLocale(Locale locale);

	List<GxTermBean> findTermByTermKey(String termKey);

	List<GxTermBean> findTermByTermKeyAndLocale(String termKey, Locale locale);

	GxTermBean findEffectiveTermByTermKeyAndLocale(String termKey, Locale locale);

	GxTermBean save(GxTermBean entity);

	void deleteTermByTermKeyAndOidNameSpace(String termKey, Integer oidNamespace);

	void delete(GxTermBean entity);

	GxSupportedLocaleBean save(GxSupportedLocaleBean entity);

	void delete(GxSupportedLocaleBean entity);

	GxNamespaceBean findNamespace(String namespace);

	GxNamespaceBean save(GxNamespaceBean entity);

	void delete(GxNamespaceBean entity);

	List<GxNamespacePropertyBean> findNamespaceProperty();

	GxNamespacePropertyBean findNamespaceProperty(Integer oidNamespaceProperty);

	GxNamespacePropertyBean save(GxNamespacePropertyBean bean);

	void delete(GxNamespacePropertyBean bean);

	List<GxNamespacePropertyBean> findNamespacePropertyByNamespace(GxNamespaceBean namespace);

	GxNamespacePropertyBean findNamespacePropertyByNamespaceAndPropertyKey(GxNamespaceBean namespace, String propertyKey);

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

	List<GxUserAccountBean> findUserAccountByNamespace(GxNamespaceBean namespace);

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

	GxCurrencyBean createOrUpdate(GxCurrencyBean bean);

	void delete(GxCurrencyBean bean);

	List<GxCurrencyBean> findCurrency();

	List<GxCurrencyBean> findCurrencyActive();

	GxCurrencyBean findCurrency(Integer oid);

	GxCurrencyBean findCurrencyByCurrencyNumericCode(Integer numericCode);

	GxCurrencyBean findCurrencyByCurrencyAlpha3Code(String alpha3Code);

	GxSavedQueryBean findSavedQuery(Integer oid);

	List<GxSavedQueryBean> findSavedQueryByUsername(String username);

	void delete(GxSavedQueryBean bean);

	List<GxSavedQueryBean> findSavedQuery();

	GxSavedQueryBean save(GxSavedQueryBean bean);

	GxEmailTemplateBean findEmailTemplateByTemplateNameActive(String templateName);

	GxEmailTemplateBean findEmailTemplateByTemplateNameAndNamespaceActive(String templateName, GxNamespaceBean namespace);

	GxEmailTemplateBean findEmailTemplateByTemplateCodeActive(String templateCode);

	GxEmailTemplateBean findEmailTemplateByTemplateCodeAndNamespaceActive(String templateCode, GxNamespaceBean namespace);

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

	GxUserAccountBean findUserAccountByUsernameAndNamespace(String username, GxNamespaceBean namespaceBean);

	GxUserAccountBean findUserAccountByUsernameAndPassword(String username, String password);

	GxSecurityGroupBean findOrCreateSecurityGroup(String groupName, GxNamespaceBean namespaceBean);

	GxSecurityGroupBean createOrUpdate(GxSecurityGroupBean bean);

	GxSecurityPolicyBean findOrCreateSecurityPolicy(String policyName, GxNamespaceBean namespaceBean);

	GxSecurityPolicyBean createOrUpdate(GxSecurityPolicyBean bean);

	GxAuditLogBean createOrUpdate(GxAuditLogBean bean);

	GxAuditLogBean auditEvent(String auditEvent);

	GxAuditLogBean auditEventWithAdditionalData(String auditEvent, byte[] additionalData);

	GxAuditLogBean auditEventByUser(String auditEvent, GxUserAccountBean userAccountBean);

	GxAuditLogBean auditEventByUserWithAdditionalData(String auditEvent, GxUserAccountBean userAccountBean, byte[] additionalData);

	GxAuditLogBean auditEntityEventByUser(String auditEntity, Integer oidAuditEntity, String auditEvent, GxUserAccountBean userAccountBean);

	GxAuditLogBean auditEntityEventByUserWithAdditionalData(String auditEntity, Integer oidAuditEntity, String auditEvent, GxUserAccountBean userAccountBean,
			byte[] additionalData);

	List<GxAuditLogBean> findAuditLogByAuditEntityAndOidAuditEntity(String auditEntity, Integer oidAuditEntity);

	List<GxAuditLogBean> findAuditLogByAuditEntity(String auditEntity);

	List<GxAuditLogBean> findAuditLogByUser(GxUserAccountBean userAccountBean);

	void log(GxNamespaceBean gxNamespaceBean, String accessKey, String resourceName, Timestamp timeStamp, Integer accessType, Boolean isSuccess);

	List<GxAccessKeyBean> findAccessKey();

	GxAccessKeyBean save(GxAccessKeyBean bean);

	void delete(GxAccessKeyBean bean);

	List<GxAccessKeyBean> findAccessKeyByIsActive(Boolean isActive);

	List<GxAccessKeyBean> findAccessKeyByIsActiveAndGxUserAccountIsNull(Boolean isActive);

	GxResourceBean createOrUpdate(GxResourceBean bean);

	List<GxResourceBean> findResourceByNamespace(GxNamespaceBean gxNamespaceBean);

	void delete(GxResourceBean bean);

	GxSmsProviderBean createOrUpdate(GxSmsProviderBean bean);

	void markAsPrimary(GxSmsProviderBean bean);

	void delete(GxSmsProvider bean);

	List<GxSmsProviderBean> findSmsProvider();

	GxSmsProviderBean findSmsProvider(Integer oid);

	List<GxSmsProviderBean> findSmsProviderActive();

	GxSmsProviderBean findSmsProviderPrimary();

	GxSmsProviderBean findSmsProviderByProvider(SmsProvider smsProvider);

	GxSmsProviderBean findSmsProviderByProviderName(String providerName);

	List<GxAuditLogBean> findAuditLogByOidAuditEntity(Integer oidAuditEntity);

	GxRegisteredDeviceBean createOrUpdate(GxRegisteredDeviceBean bean);

	List<GxRegisteredDeviceBean> findRegisteredDevice();

	List<GxRegisteredDeviceBean> findRegisteredDeviceByNamespace(GxNamespaceBean bean);

	void delete(GxRegisteredDeviceBean bean);

	GxRegisteredDeviceBean registerDevice(String namespace, String uniqueId, String systemName, String brand, boolean isTablet, String ownerId)
			throws RegisterDeviceFailedException;

	void unregisterDevice(String namespace, String uniqueId) throws UnregisterDeviceFailedException;

	GxUserAccountBean findUserAccountByUsernamePasswordAndNamespace(String username, String password, GxNamespaceBean namespace);

}
