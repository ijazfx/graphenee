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
package io.graphenee.core;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Pageable;

import io.graphenee.core.enums.SmsProvider;
import io.graphenee.core.exception.RegisterDeviceFailedException;
import io.graphenee.core.exception.UnregisterDeviceFailedException;
import io.graphenee.core.model.entity.GxAccessKey;
import io.graphenee.core.model.entity.GxAuditLog;
import io.graphenee.core.model.entity.GxCity;
import io.graphenee.core.model.entity.GxCountry;
import io.graphenee.core.model.entity.GxCurrency;
import io.graphenee.core.model.entity.GxDomain;
import io.graphenee.core.model.entity.GxEmailTemplate;
import io.graphenee.core.model.entity.GxGender;
import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.core.model.entity.GxNamespaceProperty;
import io.graphenee.core.model.entity.GxRegisteredDevice;
import io.graphenee.core.model.entity.GxResource;
import io.graphenee.core.model.entity.GxSavedQuery;
import io.graphenee.core.model.entity.GxSecurityGroup;
import io.graphenee.core.model.entity.GxSecurityPolicy;
import io.graphenee.core.model.entity.GxSmsProvider;
import io.graphenee.core.model.entity.GxState;
import io.graphenee.core.model.entity.GxSupportedLocale;
import io.graphenee.core.model.entity.GxTag;
import io.graphenee.core.model.entity.GxTerm;
import io.graphenee.core.model.entity.GxTermTranslation;
import io.graphenee.core.model.entity.GxUserAccount;
import io.graphenee.security.exception.GxPermissionException;

public interface GxDataService {

	public static final String SYSTEM_NAMESPACE = "io.graphenee.system";

	/**
	 * The method returns app logo in following sequence:
	 * if domain.appLogo is set then returns domain.appLogo else
	 * domain.namespace.appLogo
	 * 
	 * @param host
	 * @return can be null
	 */
	byte[] appLogoByHost(String host);

	/**
	 * The method returns app title in following sequence:
	 * if domain.appTitle is set then returns domain.appTitle else
	 * domain.namespace.appTitle
	 * 
	 * @param host
	 * @return can be null
	 */
	String appTitleByHost(String host);

	List<GxDomain> findAllDomains();

	List<GxDomain> findDomainsByNamespace(GxNamespace namespace);

	GxDomain findDomainActiveAndVerifiedByHost(String host);

	Optional<GxNamespace> findNamespaceByHost(String host);

	GxDomain save(GxDomain domain);

	void delete(GxDomain domain);

	GxAuditLog auditEntityEventByUser(String auditEntity, Integer oidAuditEntity, String auditEvent,
			GxUserAccount userAccount);

	GxAuditLog auditEntityEventByUserWithAdditionalData(String auditEntity, Integer oidAuditEntity, String auditEvent,
			GxUserAccount userAccount, byte[] additionalData);

	GxAuditLog auditEvent(String auditEvent);

	GxAuditLog auditEventByUser(String auditEvent, GxUserAccount userAccount);

	GxAuditLog auditEventByUserWithAdditionalData(String auditEvent, GxUserAccount userAccount, byte[] additionalData);

	GxAuditLog auditEventWithAdditionalData(String auditEvent, byte[] additionalData);

	void delete(GxAccessKey entity);

	void delete(GxCity city);

	void delete(GxCountry entity);

	void delete(GxCurrency entity);

	void delete(GxEmailTemplate entity);

	void delete(GxNamespace entity);

	void delete(GxNamespaceProperty entity);

	void delete(GxRegisteredDevice entity);

	void delete(GxResource entity);

	void delete(GxSavedQuery entity);

	void delete(GxSecurityGroup entity);

	void delete(GxSecurityPolicy entity);

	void delete(GxSmsProvider entity);

	void delete(GxState entity);

	void delete(GxSupportedLocale entity);

	void delete(GxTerm entity);

	void delete(GxUserAccount entity);

	void deleteTermByTermKeyAndNamespace(String termKey, GxNamespace namespace);

	GxAccessKey findAccessKey(UUID accessKey);

	List<GxAccessKey> findAccessKey();

	List<GxAccessKey> findAccessKeyByIsActive(Boolean isActive);

	List<GxAccessKey> findAccessKeyByIsActiveAndUserAccountIsNull(Boolean isActive);

	List<GxAuditLog> findAuditLogByAuditEntity(String auditEntity);

	List<GxAuditLog> findAuditLogByAuditEntityAndOidAuditEntity(String auditEntity, Integer oidAuditEntity);

	List<GxAuditLog> findAuditLogByOidAuditEntity(Integer oidAuditEntity);

	List<GxAuditLog> findAuditLogByUser(GxUserAccount userAccount);

	List<GxCity> findCity();

	GxCity findCity(Integer oid);

	GxCity findCityByCityName(String cityName);

	List<GxCity> findCityByCountry(GxCountry country);

	List<GxCity> findCityByCountryNumericCode(Integer numericCode);

	List<GxCity> findCityByState(GxState state);

	List<GxCity> findCityByStateCode(String stateCode);

	List<GxCountry> findCountry();

	GxCountry findCountry(Integer oid);

	GxCountry findCountryByCityName(String cityName);

	GxCountry findCountryByCountryAlpha3Code(String alpha3Code);

	GxCountry findCountryByCountryName(String countryName);

	GxCountry findCountryByNumericCode(Integer numericCode);

	GxCountry findCountryByStateCode(String stateCode);

	GxCountry findCountryByStateName(String stateName);

	List<GxCurrency> findCurrency();

	GxCurrency findCurrency(Integer oid);

	List<GxCurrency> findCurrencyActive();

	GxCurrency findCurrencyByCurrencyAlpha3Code(String alpha3Code);

	GxCurrency findCurrencyByCurrencyNumericCode(Integer numericCode);

	List<GxEmailTemplate> findEmailTemplate();

	GxEmailTemplate findEmailTemplate(Integer oid);

	List<GxEmailTemplate> findEmailTemplateActive();

	List<GxEmailTemplate> findEmailTemplateByNamespace(GxNamespace namespace);

	List<GxEmailTemplate> findEmailTemplateByNamespaceActive(GxNamespace namespace);

	List<GxEmailTemplate> findEmailTemplateByNamespaceInactive(GxNamespace namespace);

	GxEmailTemplate findEmailTemplateByTemplateCodeActive(String templateCode);

	GxEmailTemplate findEmailTemplateByTemplateCodeAndNamespaceActive(String templateCode, GxNamespace namespace);

	GxEmailTemplate findEmailTemplateByTemplateNameActive(String templateName);

	GxEmailTemplate findEmailTemplateByTemplateNameAndNamespaceActive(String templateName, GxNamespace namespace);

	List<GxEmailTemplate> findEmailTemplateInactive();

	List<GxGender> findGender();

	GxGender findGenderByCode(String genderCode);

	List<GxNamespace> findNamespace();

	GxNamespace findNamespace(Integer oidNamespace);

	GxNamespace findNamespace(String namespace);

	List<GxNamespaceProperty> findNamespaceProperty();

	GxNamespaceProperty findNamespaceProperty(Integer oidNamespaceProperty);

	List<GxNamespaceProperty> findNamespacePropertyByNamespace(GxNamespace namespace);

	GxNamespaceProperty findNamespacePropertyByNamespaceAndPropertyKey(GxNamespace namespace, String propertyKey);

	GxNamespace findOrCreateNamespace(String namespace);

	GxSecurityGroup findOrCreateSecurityGroup(String groupName, GxNamespace namespace);

	GxSecurityPolicy findOrCreateSecurityPolicy(String policyName, GxNamespace namespace);

	Long countRegisterDevice(GxRegisteredDevice se);

	List<GxRegisteredDevice> findRegisteredDevice(GxRegisteredDevice se, Pageable page);

	List<GxRegisteredDevice> findRegisteredDeviceByNamespace(GxNamespace entity);

	GxResource findResourceByResourceNameAndNamespace(String resourceName, GxNamespace namespace);

	List<GxResource> findResourceByNamespace(GxNamespace namespace);

	List<GxSavedQuery> findSavedQuery();

	GxSavedQuery findSavedQuery(Integer oid);

	List<GxSavedQuery> findSavedQueryByUsername(String username);

	List<GxSecurityGroup> findSecurityGroup();

	GxSecurityGroup findSecurityGroup(Integer oidSecurityGroup);

	List<GxSecurityGroup> findSecurityGroupActive();

	List<GxSecurityGroup> findSecurityGroupByNamespace(GxNamespace namespace);

	List<GxSecurityGroup> findSecurityGroupByNamespaceActive(GxNamespace namespace);

	GxSecurityGroup findSecurityGroupByNamespaceAndGroupName(GxNamespace namespace, String groupName);

	GxSecurityGroup findSecurityGroupByNamespaceAndGroupNameActive(GxNamespace namespace, String groupName);

	List<GxSecurityGroup> findSecurityGroupByNamespaceInactive(GxNamespace namespace);

	List<GxSecurityGroup> findSecurityGroupInactive();

	List<GxSecurityPolicy> findSecurityPolicy();

	List<GxSecurityPolicy> findSecurityPolicyActive();

	List<GxSecurityPolicy> findSecurityPolicyByNamespace(GxNamespace namespace);

	List<GxSecurityPolicy> findSecurityPolicyByNamespaceActive(GxNamespace namespace);

	List<GxSecurityPolicy> findSecurityPolicyByNamespaceInactive(GxNamespace namespace);

	List<GxSecurityPolicy> findSecurityPolicyInactive();

	List<GxSmsProvider> findSmsProvider();

	GxSmsProvider findSmsProvider(Integer oid);

	List<GxSmsProvider> findSmsProviderActive();

	GxSmsProvider findSmsProviderByProvider(SmsProvider smsProvider);

	GxSmsProvider findSmsProviderByProviderName(String providerName);

	GxSmsProvider findSmsProviderPrimary();

	List<GxState> findState();

	GxState findState(Integer oid);

	GxState findStateByCityName(String cityName);

	List<GxState> findStateByCountry(GxCountry country);

	List<GxState> findStateByCountryCountryName(String countryName);

	List<GxState> findStateByCountryNumericCode(Integer numeriCode);

	GxState findStateByStateCode(String stateCode);

	GxState findStateByStateName(String stateName);

	List<GxSupportedLocale> findSupportedLocale();

	List<GxTerm> findTermByNamespace(Integer page, Integer size, GxNamespace namespace);

	List<GxTerm> findTermByTermKey(String termKey);

	List<GxTermTranslation> findTermTranslationByLocale(Locale locale);

	GxTermTranslation findEffectiveTermTranslationByTermKeyAndLocale(String termKey, Locale locale);

	List<GxUserAccount> findUserAccount();

	GxUserAccount findUserAccount(Integer oidUserAccount);

	List<GxUserAccount> findUserAccountActive();

	List<GxUserAccount> findUserAccountByNamespace(GxNamespace namespace);

	List<GxUserAccount> findUserAccountBySecurityGroup(GxSecurityGroup securityGroup);

	List<GxUserAccount> findUserAccountBySecurityGroupActive(GxSecurityGroup securityGroup);

	List<GxUserAccount> findUserAccountBySecurityGroupInactive(GxSecurityGroup securityGroup);

	GxUserAccount findUserAccountByUsername(String username);

	GxUserAccount findUserAccountByUsernameAndNamespace(String username, GxNamespace namespace);

	GxUserAccount findUserAccountByUsernameAndPassword(String username, String password);

	GxUserAccount findUserAccountByUsernamePasswordAndNamespace(String username, String password,
			GxNamespace namespace);

	List<GxUserAccount> findUserAccountInactive();

	void log(GxNamespace namespace, String accessKey, String resourceName, Timestamp timeStamp, Integer accessType,
			Boolean isSuccess);

	void markAsPrimary(GxSmsProvider entity);

	GxRegisteredDevice registerDevice(String namespace, String uniqueId, String systemName, String brand,
			boolean isTablet, String ownerId) throws RegisterDeviceFailedException;

	void updateAwsDeviceArn(String namespace, String deviceToken, String awsDeviceArn);

	void updateAwsDeviceArn(String namespace, Map<String, String> deviceTokenArnMap);

	GxAccessKey save(GxAccessKey entity);

	GxAuditLog save(GxAuditLog entity);

	GxCity save(GxCity city);

	GxCountry save(GxCountry entity);

	GxCurrency save(GxCurrency entity);

	GxEmailTemplate save(GxEmailTemplate entity);

	GxNamespace save(GxNamespace entity);

	GxNamespaceProperty save(GxNamespaceProperty entity);

	GxRegisteredDevice save(GxRegisteredDevice entity);

	GxResource save(GxResource entity);

	GxSavedQuery save(GxSavedQuery entity);

	GxSecurityGroup save(GxSecurityGroup entity);

	GxSecurityPolicy save(GxSecurityPolicy entity);

	GxSmsProvider save(GxSmsProvider entity);

	GxState save(GxState entity);

	GxSupportedLocale save(GxSupportedLocale entity);

	GxTerm save(GxTerm entity);

	GxUserAccount save(GxUserAccount entity);

	GxNamespace systemNamespace();

	void unregisterDevice(String namespace, String uniqueId) throws UnregisterDeviceFailedException;

	void access(GxNamespace namespace, String accessKey, String resourceName, Timestamp timeStamp)
			throws GxPermissionException;

	void checkIn(GxNamespace namespace, String accessKey, String resourceName, Timestamp timeStamp)
			throws GxPermissionException;

	void checkOut(GxNamespace namespace, String accessKey, String resourceName, Timestamp timeStamp)
			throws GxPermissionException;

	boolean canAccessResource(GxNamespace namespace, String accessKey, String resourceName, Timestamp timeStamp)
			throws GxPermissionException;

	List<GxAccessKey> findAccessKeyByUserAccount(GxUserAccount user);

	List<GxAccessKey> findAccessKeyBySecurityGroup(GxSecurityGroup group);

	List<GxAccessKey> findAccessKeyBySecurityPolicy(GxSecurityPolicy policy);

	List<Principal> findPrincipalActiveByNamespace(GxNamespace namespace);

	List<GxTag> findTagByNamespace(GxNamespace namespace);

}
