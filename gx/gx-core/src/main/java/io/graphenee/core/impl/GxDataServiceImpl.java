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
package io.graphenee.core.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import io.graphenee.core.GxDataService;
import io.graphenee.core.enums.AccessTypeStatus;
import io.graphenee.core.enums.SmsProvider;
import io.graphenee.core.exception.RegisterDeviceFailedException;
import io.graphenee.core.exception.UnregisterDeviceFailedException;
import io.graphenee.core.model.entity.GxAccessKey;
import io.graphenee.core.model.entity.GxAccessLog;
import io.graphenee.core.model.entity.GxAuditLog;
import io.graphenee.core.model.entity.GxCity;
import io.graphenee.core.model.entity.GxCountry;
import io.graphenee.core.model.entity.GxCurrency;
import io.graphenee.core.model.entity.GxEmailTemplate;
import io.graphenee.core.model.entity.GxGender;
import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.core.model.entity.GxNamespaceProperty;
import io.graphenee.core.model.entity.GxRegisteredDevice;
import io.graphenee.core.model.entity.GxResource;
import io.graphenee.core.model.entity.GxSavedQuery;
import io.graphenee.core.model.entity.GxSecurityGroup;
import io.graphenee.core.model.entity.GxSecurityPolicy;
import io.graphenee.core.model.entity.GxSecurityPolicyDocument;
import io.graphenee.core.model.entity.GxSmsProvider;
import io.graphenee.core.model.entity.GxState;
import io.graphenee.core.model.entity.GxSupportedLocale;
import io.graphenee.core.model.entity.GxTerm;
import io.graphenee.core.model.entity.GxUserAccount;
import io.graphenee.core.model.jpa.repository.GxAccessKeyRepository;
import io.graphenee.core.model.jpa.repository.GxAccessLogRepository;
import io.graphenee.core.model.jpa.repository.GxAuditLogRepository;
import io.graphenee.core.model.jpa.repository.GxCityRepository;
import io.graphenee.core.model.jpa.repository.GxCountryRepository;
import io.graphenee.core.model.jpa.repository.GxCurrencyRepository;
import io.graphenee.core.model.jpa.repository.GxEmailTemplateRepository;
import io.graphenee.core.model.jpa.repository.GxGenderRepository;
import io.graphenee.core.model.jpa.repository.GxNamespacePropertyRepository;
import io.graphenee.core.model.jpa.repository.GxNamespaceRepository;
import io.graphenee.core.model.jpa.repository.GxPasswordHistoryRepository;
import io.graphenee.core.model.jpa.repository.GxRegisteredDeviceRepository;
import io.graphenee.core.model.jpa.repository.GxResourceRepository;
import io.graphenee.core.model.jpa.repository.GxSavedQueryRepository;
import io.graphenee.core.model.jpa.repository.GxSecurityGroupRepository;
import io.graphenee.core.model.jpa.repository.GxSecurityPolicyDocumentRepository;
import io.graphenee.core.model.jpa.repository.GxSecurityPolicyRepository;
import io.graphenee.core.model.jpa.repository.GxSmsProviderRepository;
import io.graphenee.core.model.jpa.repository.GxStateRepository;
import io.graphenee.core.model.jpa.repository.GxSupportedLocaleRepository;
import io.graphenee.core.model.jpa.repository.GxTermRepository;
import io.graphenee.core.model.jpa.repository.GxUserAccountRepository;
import io.graphenee.security.exception.GxPermissionException;
import io.graphenee.util.CryptoUtil;
import io.graphenee.util.JpaSpecificationBuilder;
import io.graphenee.util.TRCalendarUtil;
import jakarta.annotation.PostConstruct;

@Service
@DependsOn({ "flyway", "flywayInitializer" })
@Transactional
public class GxDataServiceImpl implements GxDataService {

	@Autowired
	GxGenderRepository genderRepo;

	@Autowired
	GxSupportedLocaleRepository supportedLocaleRepo;

	@Autowired
	GxTermRepository termRepo;

	@Autowired
	GxNamespaceRepository namespaceRepo;

	@Autowired
	GxNamespacePropertyRepository namespacePropertyRepo;

	@Autowired
	GxSecurityGroupRepository securityGroupRepo;

	@Autowired
	GxUserAccountRepository userAccountRepo;

	@Autowired
	GxSecurityPolicyRepository securityPolicyRepo;

	@Autowired
	GxSecurityPolicyDocumentRepository securityPolicyDocumentRepo;

	@Autowired
	GxCountryRepository countryRepo;

	@Autowired
	GxCurrencyRepository currencyRepo;

	@Autowired
	GxStateRepository stateRepo;

	@Autowired
	GxCityRepository cityRepo;

	@Autowired
	GxSavedQueryRepository savedQueryRepo;

	@Autowired
	GxEmailTemplateRepository emailTemplateRepo;

	@Autowired
	GxAuditLogRepository auditLogRepo;

	@Autowired
	PlatformTransactionManager transactionManager;

	@Autowired
	GxAccessLogRepository accessLogRepo;

	@Autowired
	GxResourceRepository resourceRepo;

	@Autowired
	GxAccessKeyRepository accessKeyRepo;

	@Autowired
	GxSmsProviderRepository smsProviderRepo;

	@Autowired
	GxRegisteredDeviceRepository registeredDeviceRepo;

	@Autowired
	GxPasswordHistoryRepository passwordHistoryRepo;

	@Override
	public void access(GxNamespace namespace, String accessKey, String resourceName, Timestamp timeStamp) throws GxPermissionException {
		if (canAccessResource(namespace, accessKey, resourceName, timeStamp)) {
			log(namespace, accessKey, resourceName, timeStamp, AccessTypeStatus.ACCESS.statusCode(), true);
		} else {
			log(namespace, accessKey, resourceName, timeStamp, AccessTypeStatus.ACCESS.statusCode(), false);
			throw new GxPermissionException("Access denied");
		}
	}

	@Override
	public void checkIn(GxNamespace namespace, String accessKey, String resourceName, Timestamp timeStamp) throws GxPermissionException {
		if (canAccessResource(namespace, accessKey, resourceName, timeStamp)) {
			log(namespace, accessKey, resourceName, timeStamp, AccessTypeStatus.CHECKIN.statusCode(), true);
		} else {
			log(namespace, accessKey, resourceName, timeStamp, AccessTypeStatus.CHECKIN.statusCode(), false);
			throw new GxPermissionException("Check-in denied");
		}
	}

	@Override
	public void checkOut(GxNamespace namespace, String accessKey, String resourceName, Timestamp timeStamp) throws GxPermissionException {
		if (canAccessResource(namespace, accessKey, resourceName, timeStamp)) {
			log(namespace, accessKey, resourceName, timeStamp, AccessTypeStatus.CHECKOUT.statusCode(), true);
		} else {
			log(namespace, accessKey, resourceName, timeStamp, AccessTypeStatus.CHECKOUT.statusCode(), false);
			throw new GxPermissionException("Check-out denied");
		}
	}

	@Override
	public boolean canAccessResource(GxNamespace namespace, String accessKey, String resourceName, Timestamp timeStamp) throws GxPermissionException {
		UUID accessKeyUuid = UUID.fromString(accessKey);
		GxAccessKey key = findAccessKey(accessKeyUuid);
		GxResource resource = findResourceByResourceNameAndNamespace(resourceName, namespace);
		if (resource == null)
			throw new GxPermissionException("Rescource not found.");
		return key.canDoAction(resourceName, "access");
	}

	@Override
	public GxAuditLog auditEntityEventByUser(String auditEntity, Integer oidAuditEntity, String auditEvent, GxUserAccount userAccount) {
		return auditEntityEventByUserWithAdditionalData(auditEntity, oidAuditEntity, auditEvent, userAccount, null);
	}

	@Override
	public GxAuditLog auditEntityEventByUserWithAdditionalData(String auditEntity, Integer oidAuditEntity, String auditEvent, GxUserAccount userAccount, byte[] additionalData) {
		GxAuditLog entity = new GxAuditLog();
		entity.setAuditDate(new Timestamp(System.currentTimeMillis()));
		entity.setAuditEntity(auditEntity);
		entity.setAuditEvent(auditEvent);
		entity.setOidAuditEntity(oidAuditEntity);
		entity.setAdditionalData(additionalData);
		if (userAccount != null) {
			entity.setUserAccount(userAccount);
		}
		return auditLogRepo.save(entity);
	}

	@Override
	public GxAuditLog auditEvent(String auditEvent) {
		return auditEntityEventByUser(null, null, auditEvent, null);
	}

	@Override
	public GxAuditLog auditEventByUser(String auditEvent, GxUserAccount userAccount) {
		return auditEntityEventByUser(null, null, auditEvent, userAccount);
	}

	@Override
	public GxAuditLog auditEventByUserWithAdditionalData(String auditEvent, GxUserAccount userAccount, byte[] additionalData) {
		return auditEntityEventByUserWithAdditionalData(null, null, auditEvent, userAccount, additionalData);
	}

	@Override
	public GxAuditLog auditEventWithAdditionalData(String auditEvent, byte[] additionalData) {
		return auditEntityEventByUserWithAdditionalData(null, null, auditEvent, null, additionalData);
	}

	@Override
	public void delete(GxAccessKey entity) {
		accessKeyRepo.deleteById(entity.getOid());
	}

	@Override
	public void delete(GxCity entity) {
		if (entity.getOid() != null && entity.getIsActive())
			throw new RuntimeException("City is Active!");
		if (entity.getOid() != null)
			cityRepo.deleteById(entity.getOid());
	}

	@Override
	public void delete(GxCountry entity) {
		if (entity.getOid() != null && entity.getIsActive())
			throw new RuntimeException("Country is Active!");
		if (entity.getOid() != null)
			countryRepo.deleteById(entity.getOid());
	}

	@Override
	public void delete(GxCurrency entity) {
		if (entity.getOid() != null && entity.getIsActive())
			throw new RuntimeException("Currency is Active!");
		currencyRepo.deleteById(entity.getOid());
	}

	@Override
	public void delete(GxEmailTemplate entity) {
		if (entity.getOid() != null && !entity.getIsProtected()) {
			emailTemplateRepo.deleteById(entity.getOid());
		}
	}

	@Override
	public void delete(GxNamespace entity) {
		if (entity.getOid() != null && entity.getIsProtected())
			throw new RuntimeException("Namespace is Protected!");
		if (entity.getOid() != null)
			namespaceRepo.deleteById(entity.getOid());
	}

	@Override
	public void delete(GxNamespaceProperty entity) {
		if (entity.getOid() != null)
			namespacePropertyRepo.deleteById(entity.getOid());
	}

	@Override
	public void delete(GxRegisteredDevice entity) {
		registeredDeviceRepo.deleteById(entity.getOid());
	}

	@Override
	public void delete(GxResource entity) {
		resourceRepo.deleteById(entity.getOid());
	}

	@Override
	public void delete(GxSavedQuery entity) {
		savedQueryRepo.deleteById(entity.getOid());
	}

	@Override
	public void delete(GxSecurityGroup entity) {
		if (entity.getOid() != null && entity.getIsProtected())
			throw new RuntimeException("Security Group is Protected!");
		if (entity.getOid() != null)
			securityGroupRepo.deleteById(entity.getOid());
	}

	@Override
	public void delete(GxSecurityPolicy entity) {
		if (entity.getOid() != null && entity.getIsProtected())
			throw new RuntimeException("Security Policy is Protected!");
		if (entity.getOid() != null)
			securityPolicyRepo.deleteById(entity.getOid());
	}

	@Override
	public void delete(GxSmsProvider entity) {
		smsProviderRepo.deleteById(entity.getOid());
	}

	@Override
	public void delete(GxState entity) {
		if (entity.getOid() != null && entity.getIsActive())
			throw new RuntimeException("State is Active!");
		if (entity.getOid() != null)
			stateRepo.deleteById(entity.getOid());
	}

	@Override
	public void delete(GxSupportedLocale entity) {
		if (entity.getOid() != null && entity.getIsProtected())
			throw new RuntimeException("Supported Locale is Protected!");
		if (entity.getOid() != null)
			supportedLocaleRepo.deleteById(entity.getOid());
	}

	@Override
	public void delete(GxTerm entity) {
		if (entity.getOid() != null && !entity.getIsProtected()) {
			termRepo.deleteById(entity.getOid());
		}
	}

	@Override
	public void delete(GxUserAccount entity) {
		if (entity.getOid() != null && entity.getIsProtected())
			throw new RuntimeException("User Account is Protected!");
		if (entity.getOid() != null)
			userAccountRepo.deleteById(entity.getOid());
	}

	@Override
	public void deleteTermByTermKeyAndNamespace(String termKey, GxNamespace namespace) {
		termRepo.deleteByTermKeyAndNamespace(termKey, namespace);
	}

	@Override
	public GxAccessKey findAccessKey(UUID accessKey) {
		return accessKeyRepo.findByAccessKey(accessKey);
	}

	@Override
	public List<GxAccessKey> findAccessKey() {
		return accessKeyRepo.findAll(Sort.by("accessKey"));
	}

	@Override
	public List<GxAccessKey> findAccessKeyByIsActive(Boolean isActive) {
		return accessKeyRepo.findAllByIsActive(isActive);
	}

	@Override
	public List<GxAccessKey> findAccessKeyByIsActiveAndUserAccountIsNull(Boolean isActive) {
		return accessKeyRepo.findAllByIsActiveAndUserAccountIsNull(isActive);
	}

	@Override
	public List<GxAuditLog> findAuditLogByAuditEntity(String auditEntity) {
		return auditLogRepo.findAllByAuditEntityOrderByAuditDateDesc(auditEntity);
	}

	@Override
	public List<GxAuditLog> findAuditLogByAuditEntityAndOidAuditEntity(String auditEntity, Integer oidAuditEntity) {
		return auditLogRepo.findAllByAuditEntityAndOidAuditEntityOrderByAuditDateDesc(auditEntity, oidAuditEntity);
	}

	@Override
	public List<GxAuditLog> findAuditLogByOidAuditEntity(Integer oidAuditEntity) {
		return auditLogRepo.findAllByOidAuditEntity(oidAuditEntity);
	}

	@Override
	public List<GxAuditLog> findAuditLogByUser(GxUserAccount userAccount) {
		return auditLogRepo.findAllByUserAccountOrderByAuditDateDesc(userAccount);
	}

	@Override
	public List<GxCity> findCity() {
		return cityRepo.findAllByIsActiveTrueOrderByCityNameAsc();
	}

	@Override
	public GxCity findCity(Integer oid) {
		return cityRepo.findOne(oid);
	}

	@Override
	public GxCity findCityByCityName(String cityName) {
		return cityRepo.findOneByCityName(cityName);
	}

	@Override
	public List<GxCity> findCityByCountry(GxCountry country) {
		return cityRepo.findAllByIsActiveTrueAndCountryOrderByCityNameAsc(country);
	}

	@Override
	public List<GxCity> findCityByCountryNumericCode(Integer numericCode) {
		return cityRepo.findAllByIsActiveTrueAndCountryNumericCodeOrderByCityNameAsc(numericCode);
	}

	@Override
	public List<GxCity> findCityByState(GxState state) {
		return cityRepo.findAllByIsActiveTrueAndStateOrderByCityNameAsc(state);
	}

	@Override
	public List<GxCity> findCityByStateCode(String stateCode) {
		return cityRepo.findAllByIsActiveTrueAndStateStateCodeOrderByCityNameAsc(stateCode);
	}

	@Override
	public List<GxCountry> findCountry() {
		return countryRepo.findAll(Sort.by("countryName"));
	}

	@Override
	public GxCountry findCountry(Integer oid) {
		return countryRepo.findOne(oid);
	}

	@Override
	public GxCountry findCountryByCityName(String cityName) {
		return countryRepo.findOneByIsActiveTrueAndCitiesCityName(cityName);
	}

	@Override
	public GxCountry findCountryByCountryAlpha3Code(String alpha3Code) {
		return countryRepo.findOneByIsActiveTrueAndAlpha3Code(alpha3Code);
	}

	@Override
	public GxCountry findCountryByCountryName(String countryName) {
		return countryRepo.findOneByIsActiveTrueAndCountryName(countryName);
	}

	@Override
	public GxCountry findCountryByNumericCode(Integer numericCode) {
		return countryRepo.findOneByIsActiveTrueAndNumericCode(numericCode);
	}

	@Override
	public GxCountry findCountryByStateCode(String stateCode) {
		return countryRepo.findOneByIsActiveTrueAndStatesStateCode(stateCode);
	}

	@Override
	public GxCountry findCountryByStateName(String stateName) {
		return countryRepo.findOneByIsActiveTrueAndStatesStateName(stateName);
	}

	@Override
	public List<GxCurrency> findCurrency() {
		return currencyRepo.findAll(Sort.by("currencyName"));
	}

	@Override
	public GxCurrency findCurrency(Integer oid) {
		return currencyRepo.findOne(oid);
	}

	@Override
	public List<GxCurrency> findCurrencyActive() {
		return currencyRepo.findAllByIsActiveTrueOrderByCurrencyNameAsc();
	}

	@Override
	public GxCurrency findCurrencyByCurrencyAlpha3Code(String alpha3Code) {
		return currencyRepo.findOneByAlpha3Code(alpha3Code);
	}

	@Override
	public GxCurrency findCurrencyByCurrencyNumericCode(Integer numericCode) {
		return currencyRepo.findOneByNumericCode(numericCode);
	}

	@Override
	public List<GxTerm> findDistinctTermByNamespaceAndSupportedLocale(GxNamespace namespace, GxSupportedLocale supportedLocale) {
		List<GxTerm> distinctTerms = new ArrayList<>();
		Collection<GxTerm> entities = null;
		if (namespace != null && supportedLocale != null) {
			entities = termRepo.findByNamespaceOidAndSupportedLocaleOid(namespace.getOid(), supportedLocale.getOid());
		} else if (namespace != null) {
			entities = termRepo.findByNamespaceOid(namespace.getOid());
		} else if (supportedLocale != null) {
			entities = termRepo.findBySupportedLocaleOid(supportedLocale.getOid());
		}
		if (entities != null) {
			Set<String> termKeySet = new HashSet<>();
			entities.forEach(term -> {
				if (!termKeySet.contains(term.getTermKey())) {
					distinctTerms.add(term);
					termKeySet.add(term.getTermKey());
				}
			});
		}
		return distinctTerms;
	}

	@Override
	public GxTerm findEffectiveTermByTermKeyAndLocale(String termKey, Locale locale) {
		String localeCode = locale.toString();
		GxTerm term = termRepo.findTopByTermKeyAndSupportedLocaleLocaleCodeStartingWithOrderByOidDesc(termKey, localeCode);
		if (term == null) {
			localeCode = locale.getLanguage();
			term = termRepo.findTopByTermKeyAndSupportedLocaleLocaleCodeStartingWithOrderByOidDesc(termKey, localeCode);
		}
		return term;
	}

	@Override
	public List<GxEmailTemplate> findEmailTemplate() {
		return emailTemplateRepo.findAll(Sort.by("templateName"));
	}

	@Override
	public GxEmailTemplate findEmailTemplate(Integer oid) {
		return emailTemplateRepo.findOne(oid);
	}

	@Override
	public List<GxEmailTemplate> findEmailTemplateActive() {
		return emailTemplateRepo.findAllByIsActiveOrderByTemplateName(true);
	}

	@Override
	public List<GxEmailTemplate> findEmailTemplateByNamespace(GxNamespace namespace) {
		return emailTemplateRepo.findAllByNamespaceOrderByTemplateName(namespace);
	}

	@Override
	public List<GxEmailTemplate> findEmailTemplateByNamespaceActive(GxNamespace namespace) {
		return emailTemplateRepo.findAllByNamespaceAndIsActiveOrderByTemplateName(namespace, true);
	}

	@Override
	public List<GxEmailTemplate> findEmailTemplateByNamespaceInactive(GxNamespace namespace) {
		return emailTemplateRepo.findAllByNamespaceAndIsActiveOrderByTemplateName(namespace, false);
	}

	@Override
	public GxEmailTemplate findEmailTemplateByTemplateCodeActive(String templateCode) {
		GxNamespace namespace = findNamespace(GxNamespace.SYSTEM);
		GxEmailTemplate emailTemplate = null;
		if (namespace != null) {
			emailTemplate = emailTemplateRepo.findOneByTemplateCodeAndNamespaceAndIsActive(templateCode, namespace, true);
		} else {
			emailTemplate = emailTemplateRepo.findOneByTemplateCodeAndIsActive(templateCode, true);
		}
		return emailTemplate;
	}

	@Override
	public GxEmailTemplate findEmailTemplateByTemplateCodeAndNamespaceActive(String templateCode, GxNamespace namespace) {
		return emailTemplateRepo.findOneByTemplateCodeAndNamespaceAndIsActive(templateCode, namespace, true);
	}

	@Override
	public GxEmailTemplate findEmailTemplateByTemplateNameActive(String templateName) {
		GxNamespace namespace = findNamespace(GxNamespace.SYSTEM);
		GxEmailTemplate emailTemplate = null;
		if (namespace != null) {
			emailTemplate = emailTemplateRepo.findOneByTemplateNameAndNamespaceAndIsActive(templateName, namespace, true);
		} else {
			emailTemplate = emailTemplateRepo.findOneByTemplateNameAndIsActive(templateName, true);
		}
		return emailTemplate;
	}

	@Override
	public GxEmailTemplate findEmailTemplateByTemplateNameAndNamespaceActive(String templateName, GxNamespace namespace) {
		return emailTemplateRepo.findOneByTemplateNameAndNamespaceAndIsActive(templateName, namespace, true);
	}

	@Override
	public List<GxEmailTemplate> findEmailTemplateInactive() {
		return emailTemplateRepo.findAllByIsActiveOrderByTemplateName(false);
	}

	@Override
	public List<GxGender> findGender() {
		return genderRepo.findAll();
	}

	@Override
	public GxGender findGenderByCode(String genderCode) {
		return genderRepo.findOneByGenderCode(genderCode);
	}

	@Override
	public List<GxNamespace> findNamespace() {
		return namespaceRepo.findAll();
	}

	@Override
	public GxNamespace findNamespace(Integer oidNamespace) {
		return namespaceRepo.findOne(oidNamespace);
	}

	@Override
	public GxNamespace findNamespace(String namespace) {
		return namespaceRepo.findByNamespace(namespace);
	}

	@Override
	public List<GxNamespaceProperty> findNamespaceProperty() {
		return namespacePropertyRepo.findAll(Sort.by("propertyKey"));
	}

	@Override
	public GxNamespaceProperty findNamespaceProperty(Integer oidNamespaceProperty) {
		return namespacePropertyRepo.findOne(oidNamespaceProperty);
	}

	@Override
	public List<GxNamespaceProperty> findNamespacePropertyByNamespace(GxNamespace namespace) {
		return namespacePropertyRepo.findAllByNamespaceOrderByPropertyKey(namespace, Sort.by("propertyKey"));
	}

	@Override
	public GxNamespaceProperty findNamespacePropertyByNamespaceAndPropertyKey(GxNamespace namespace, String propertyKey) {
		return namespacePropertyRepo.findOneByNamespaceAndPropertyKey(namespace, propertyKey);
	}

	@Override
	public GxNamespace findOrCreateNamespace(String namespace) {
		GxNamespace entity = namespaceRepo.findByNamespace(namespace);
		if (entity == null) {
			entity = new GxNamespace();
			entity.setIsActive(true);
			entity.setIsProtected(false);
			entity.setNamespace(namespace);
			entity.setNamespaceDescription("-- Auto Generated --");
			entity = namespaceRepo.save(entity);
		}
		return entity;
	}

	@Override
	public GxSecurityGroup findOrCreateSecurityGroup(String groupName, GxNamespace namespace) {
		GxSecurityGroup entity = securityGroupRepo.findOneBySecurityGroupNameAndNamespace(groupName, namespace);
		if (entity == null) {
			entity = new GxSecurityGroup();
			entity.setIsActive(true);
			entity.setIsProtected(false);
			entity.setSecurityGroupName(groupName);
			entity.setNamespace(namespace);
			entity = securityGroupRepo.save(entity);
		}
		return entity;
	}

	@Override
	public GxSecurityPolicy findOrCreateSecurityPolicy(String policyName, GxNamespace namespace) {
		GxSecurityPolicy entity = securityPolicyRepo.findAllBySecurityPolicyNameAndNamespace(policyName, namespace);
		if (entity == null) {
			entity = new GxSecurityPolicy();
			entity.setIsActive(true);
			entity.setIsProtected(false);
			entity.setSecurityPolicyName(policyName);
			entity.setNamespace(namespace);
			entity = securityPolicyRepo.save(entity);
		}
		return entity;
	}

	@Override
	public Long countRegisterDevice(GxRegisteredDevice se) {
		return registeredDeviceRepo.count(registeredDeviceSpec(se));
	}

	private Specification<GxRegisteredDevice> registeredDeviceSpec(GxRegisteredDevice se) {
		JpaSpecificationBuilder<GxRegisteredDevice> sb = JpaSpecificationBuilder.get();
		return sb.build();
	}

	@Override
	public List<GxRegisteredDevice> findRegisteredDevice(GxRegisteredDevice se, Pageable page) {
		return registeredDeviceRepo.findAll(registeredDeviceSpec(se), page).getContent();
	}

	@Override
	public List<GxRegisteredDevice> findRegisteredDeviceByNamespace(GxNamespace entity) {
		return registeredDeviceRepo.findByNamespaceNamespace(entity.getNamespace());
	}

	private GxRegisteredDevice findRegisteredDeviceByNamespaceAndDeviceToken(String namespace, String deviceToken) {
		return registeredDeviceRepo.findByNamespaceNamespaceAndDeviceToken(namespace, deviceToken);
	}

	private GxRegisteredDevice findRegisteredDeviceByNamespaceAndDeviceTokenAndOwner(String namespace, String deviceToken, String ownerId) {
		return registeredDeviceRepo.findByNamespaceNamespaceAndDeviceTokenAndOwnerId(namespace, deviceToken, ownerId);
	}

	@Override
	public GxResource findResourceByResourceNameAndNamespace(String resourceName, GxNamespace namespace) {
		return resourceRepo.findOneByResourceNameAndNamespaceAndIsActiveTrue(resourceName, namespace);
	}

	@Override
	public List<GxResource> findResourceByNamespace(GxNamespace namespace) {
		return resourceRepo.findAllByNamespace(namespace, Sort.by("resourceName"));
	}

	@Override
	public List<GxSavedQuery> findSavedQuery() {
		return savedQueryRepo.findAll();
	}

	@Override
	public GxSavedQuery findSavedQuery(Integer oid) {
		return savedQueryRepo.findOne(oid);
	}

	@Override
	public List<GxSavedQuery> findSavedQueryByUsername(String username) {
		return savedQueryRepo.findAllByTargetUser(username, Sort.by("queryName"));
	}

	@Override
	public List<GxSecurityGroup> findSecurityGroup() {
		return securityGroupRepo.findAll();
	}

	@Override
	public GxSecurityGroup findSecurityGroup(Integer oidSecurityGroup) {
		return securityGroupRepo.findOne(oidSecurityGroup);
	}

	@Override
	public List<GxSecurityGroup> findSecurityGroupActive() {
		return securityGroupRepo.findAllByIsActive(true);
	}

	@Override
	public List<GxSecurityGroup> findSecurityGroupByNamespace(GxNamespace namespace) {
		GxNamespace entity = namespaceRepo.findOne(namespace.getOid());
		return securityGroupRepo.findByNamespace(entity);
	}

	@Override
	public List<GxSecurityGroup> findSecurityGroupByNamespaceActive(GxNamespace namespace) {
		GxNamespace entity = namespaceRepo.findOne(namespace.getOid());
		return securityGroupRepo.findByNamespace(entity).stream().filter(securityGroup -> securityGroup.getIsActive() == true).collect(Collectors.toList());
	}

	@Override
	public GxSecurityGroup findSecurityGroupByNamespaceAndGroupName(GxNamespace namespace, String groupName) {
		GxNamespace entity = namespaceRepo.findOne(namespace.getOid());
		Optional<GxSecurityGroup> securityGroupOptional = securityGroupRepo.findByNamespace(entity).stream().filter(sg -> {
			return sg.getSecurityGroupName().equalsIgnoreCase(groupName);
		}).findFirst();
		return securityGroupOptional.isPresent() ? securityGroupOptional.get() : null;
	}

	@Override
	public GxSecurityGroup findSecurityGroupByNamespaceAndGroupNameActive(GxNamespace namespace, String groupName) {
		GxNamespace entity = namespaceRepo.findOne(namespace.getOid());
		Optional<GxSecurityGroup> securityGroupOptional = securityGroupRepo.findByNamespace(entity).stream().filter(sg -> {
			return sg.getIsActive() && sg.getSecurityGroupName().equalsIgnoreCase(groupName);
		}).findFirst();
		return securityGroupOptional.isPresent() ? securityGroupOptional.get() : null;
	}

	@Override
	public List<GxSecurityGroup> findSecurityGroupByNamespaceInactive(GxNamespace namespace) {
		GxNamespace entity = namespaceRepo.findOne(namespace.getOid());
		return securityGroupRepo.findByNamespace(entity).stream().filter(securityGroup -> securityGroup.getIsActive() == false).collect(Collectors.toList());
	}

	@Override
	public List<GxSecurityGroup> findSecurityGroupInactive() {
		return securityGroupRepo.findAllByIsActive(false);
	}

	@Override
	public List<GxSecurityPolicy> findSecurityPolicy() {
		return securityPolicyRepo.findAll(Sort.by("securityPolicyName"));
	}

	@Override
	public List<GxSecurityPolicy> findSecurityPolicyActive() {
		return securityPolicyRepo.findAllByIsActive(true, Sort.by("securityPolicyName"));
	}

	@Override
	public List<GxSecurityPolicy> findSecurityPolicyByNamespace(GxNamespace namespace) {
		return securityPolicyRepo.findAllByNamespace(namespace, Sort.by("securityPolicyName"));
	}

	@Override
	public List<GxSecurityPolicy> findSecurityPolicyByNamespaceActive(GxNamespace namespace) {
		return securityPolicyRepo.findAllByNamespaceAndIsActive(namespace, true, Sort.by("securityPolicyName"));
	}

	@Override
	public List<GxSecurityPolicy> findSecurityPolicyByNamespaceInactive(GxNamespace namespace) {
		return securityPolicyRepo.findAllByNamespaceAndIsActive(namespace, false, Sort.by("securityPolicyName"));
	}

	@Override
	public List<GxSecurityPolicy> findSecurityPolicyInactive() {
		return securityPolicyRepo.findAllByIsActive(false, Sort.by("securityPolicyName"));
	}

	@Override
	public List<GxSmsProvider> findSmsProvider() {
		return smsProviderRepo.findAll(Sort.by("providerName"));
	}

	@Override
	public GxSmsProvider findSmsProvider(Integer oid) {
		return smsProviderRepo.findOne(oid);
	}

	@Override
	public List<GxSmsProvider> findSmsProviderActive() {
		return smsProviderRepo.findAllByIsActiveTrueOrderByProviderNameAsc();
	}

	@Override
	public GxSmsProvider findSmsProviderByProvider(SmsProvider smsProvider) {
		return smsProviderRepo.findOneByProviderName(smsProvider.getProviderName());
	}

	@Override
	public GxSmsProvider findSmsProviderByProviderName(String providerName) {
		return smsProviderRepo.findOneByProviderName(providerName);
	}

	@Override
	public GxSmsProvider findSmsProviderPrimary() {
		return smsProviderRepo.findOneByIsActiveTrueAndIsPrimaryTrue();
	}

	@Override
	public List<GxState> findState() {
		return stateRepo.findAll(Sort.by("stateName"));
	}

	@Override
	public GxState findState(Integer oid) {
		return stateRepo.findOne(oid);
	}

	@Override
	public GxState findStateByCityName(String cityName) {
		return stateRepo.findOneByIsActiveTrueAndCitiesCityNameOrderByStateNameAsc(cityName);
	}

	@Override
	public List<GxState> findStateByCountry(GxCountry country) {
		return stateRepo.findAllByIsActiveTrueAndCountryOrderByStateNameAsc(country);
	}

	@Override
	public List<GxState> findStateByCountryCountryName(String countryName) {
		return stateRepo.findAllByIsActiveTrueAndCountryCountryNameOrderByStateNameAsc(countryName);
	}

	@Override
	public List<GxState> findStateByCountryNumericCode(Integer numeriCode) {
		return stateRepo.findAllByIsActiveTrueAndCountryNumericCodeOrderByStateNameAsc(numeriCode);
	}

	@Override
	public GxState findStateByStateCode(String stateCode) {
		return stateRepo.findOneByIsActiveTrueAndStateCodeOrderByStateNameAsc(stateCode);
	}

	@Override
	public GxState findStateByStateName(String stateName) {
		return stateRepo.findOneByIsActiveTrueAndStateNameOrderByStateNameAsc(stateName);
	}

	@Override
	public List<GxSupportedLocale> findSupportedLocale() {
		return supportedLocaleRepo.findAll(Sort.by("localeName"));
	}

	@Override
	public List<GxTerm> findTermByLocale(Locale locale) {
		String localeCode = locale.toString();
		GxSupportedLocale supportedLocale = supportedLocaleRepo.findByLocaleCodeStartingWith(localeCode);
		if (supportedLocale == null) {
			localeCode = locale.getLanguage();
			supportedLocale = supportedLocaleRepo.findByLocaleCodeStartingWith(localeCode);
		}
		if (supportedLocale != null) {
			return supportedLocale.getTerms();
		}
		return Collections.emptyList();
	}

	@Override
	public List<GxTerm> findTermByNamespaceAndSupportedLocale(Integer page, Integer size, GxNamespace namespace, GxSupportedLocale supportedLocale) {
		PageRequest pageRequest = PageRequest.of(page, size);
		Page<GxTerm> result = null;
		if (namespace != null && supportedLocale != null) {
			result = termRepo.findByNamespaceOidAndSupportedLocaleOid(pageRequest, namespace.getOid(), supportedLocale.getOid());
		} else if (namespace != null) {
			result = termRepo.findByNamespaceOid(pageRequest, namespace.getOid());
		} else if (supportedLocale != null) {
			result = termRepo.findBySupportedLocaleOid(pageRequest, supportedLocale.getOid());
		} else {
			result = termRepo.findAll(pageRequest);
		}
		return result.getContent();
	}

	@Override
	public List<GxTerm> findTermByTermKey(String termKey) {
		if (termKey != null && !termKey.isEmpty()) {
			return termRepo.findByTermKey(termKey);
		}
		return Collections.emptyList();
	}

	@Override
	public List<GxTerm> findTermByTermKeyAndLocale(String termKey, Locale locale) {

		String localeCode = locale.toString();
		List<GxTerm> terms = termRepo.findByTermKeyAndSupportedLocaleLocaleCodeStartingWith(termKey, localeCode);
		if (terms.isEmpty()) {
			localeCode = locale.getLanguage();
			terms = termRepo.findByTermKeyAndSupportedLocaleLocaleCodeStartingWith(termKey, localeCode);
		}
		return terms;
	}

	@Override
	public List<GxUserAccount> findUserAccount() {
		return userAccountRepo.findAll(Sort.by("username"));
	}

	@Override
	public GxUserAccount findUserAccount(Integer oidUserAccount) {
		return userAccountRepo.findOne(oidUserAccount);
	}

	@Override
	public List<GxUserAccount> findUserAccountActive() {
		return userAccountRepo.findAllByIsActive(true, Sort.by("username"));
	}

	@Override
	public List<GxUserAccount> findUserAccountByNamespace(GxNamespace namespace) {
		return userAccountRepo.findAllByNamespace(namespace, Sort.by("username"));
	}

	@Override
	public List<GxUserAccount> findUserAccountBySecurityGroup(GxSecurityGroup securityGroup) {
		return userAccountRepo.findAllBySecurityGroupsEquals(securityGroup, Sort.by("username"));
	}

	@Override
	public List<GxUserAccount> findUserAccountBySecurityGroupActive(GxSecurityGroup securityGroup) {
		return userAccountRepo.findAllBySecurityGroupsEqualsAndIsActive(securityGroup, true, Sort.by("username"));
	}

	@Override
	public List<GxUserAccount> findUserAccountBySecurityGroupInactive(GxSecurityGroup securityGroup) {
		return userAccountRepo.findAllBySecurityGroupsEqualsAndIsActive(securityGroup, false, Sort.by("username"));
	}

	@Override
	public GxUserAccount findUserAccountByUsername(String username) {
		return userAccountRepo.findByUsername(username);
	}

	@Override
	public GxUserAccount findUserAccountByUsernameAndNamespace(String username, GxNamespace namespace) {
		return userAccountRepo.findByUsernameAndNamespace(username, namespace);
	}

	@Override
	public GxUserAccount findUserAccountByUsernameAndPassword(String username, String password) {
		GxUserAccount userAccount = userAccountRepo.findByUsername(username);
		if (userAccount == null)
			return null;
		String encryptedPassword = CryptoUtil.createPasswordHash(password);
		boolean authenticated = false;
		if (userAccount.getPassword() != null && userAccount.getPassword().equals(password)) {
			userAccount.setPassword(encryptedPassword);
			userAccountRepo.save(userAccount);
			authenticated = true;
		} else if (userAccount.getPassword().equals(encryptedPassword)) {
			authenticated = true;
		}
		if (!authenticated)
			return null;
		return userAccount;
	}

	@Override
	public GxUserAccount findUserAccountByUsernamePasswordAndNamespace(String username, String password, GxNamespace namespace) {
		GxUserAccount userAccount = userAccountRepo.findByUsernameAndNamespace(username, namespace);
		if (userAccount == null) {
			userAccount = userAccountRepo.findByUsernameAndNamespace(username, systemNamespace());
			if (userAccount == null)
				return null;
		}
		String encryptedPassword = CryptoUtil.createPasswordHash(password);
		boolean authenticated = false;
		if (userAccount.getPassword() != null && userAccount.getPassword().equals(password)) {
			userAccount.setPassword(encryptedPassword);
			userAccountRepo.save(userAccount);
			authenticated = true;
		} else if (userAccount.getPassword().equals(encryptedPassword)) {
			authenticated = true;
		}
		if (!authenticated)
			return null;
		return userAccount;
	}

	@Override
	public List<GxUserAccount> findUserAccountInactive() {
		return userAccountRepo.findAllByIsActive(false, Sort.by("username"));
	}

	@PostConstruct
	public void initialize() {
		TransactionTemplate tran = new TransactionTemplate(transactionManager);
		tran.execute(status -> {
			GxNamespace namespace = systemNamespace();
			GxSecurityGroup adminGroup = findOrCreateSecurityGroup("Admin", namespace);
			GxSecurityPolicy adminPolicy = findOrCreateSecurityPolicy("Admin Policy", namespace);
			GxSecurityPolicyDocument document = adminPolicy.defaultDocument();
			if (document == null) {
				document = new GxSecurityPolicyDocument();
				document.setIsDefault(true);
				document.setDocumentJson("grant all on all;");
				document.setTag(TRCalendarUtil.yyyyMMddHHmmssFormatter.format(new Timestamp(0)));
				adminPolicy.addSecurityPolicyDocument(document);
				save(adminPolicy);
			}
			if (!adminGroup.getSecurityPolicies().contains(adminPolicy)) {
				adminGroup.getSecurityPolicies().add(adminPolicy);
				save(adminGroup);
			}
			GxUserAccount admin = findUserAccountByUsernameAndNamespace("admin", namespace);
			if (admin == null) {
				admin = new GxUserAccount();
				admin.setUsername("admin");
				admin.setPassword("change_on_install");
				admin.setIsActive(true);
				admin.setIsProtected(true);
				admin.setNamespace(namespace);
				save(admin);
			}
			if (!admin.getSecurityGroups().contains(adminGroup)) {
				admin.getSecurityGroups().add(adminGroup);
				save(admin);
			}

			return null;
		});
	}

	@Override
	public void log(GxNamespace namespace, String accessKey, String resourceName, Timestamp timeStamp, Integer accessType, Boolean isSuccess) {
		GxAccessLog accessLog = new GxAccessLog();
		accessLog.setIsSuccess(isSuccess);
		accessLog.setAccessTime(timeStamp);
		UUID accessKeyUuid = UUID.fromString(accessKey);
		accessLog.setAccessKey(accessKeyRepo.findByAccessKey(accessKeyUuid));
		accessLog.setResource(resourceRepo.findOneByResourceNameAndNamespaceAndIsActiveTrue(resourceName, namespace));
		accessLog.setAccessType(accessType);
		accessLogRepo.save(accessLog);
	}

	@Override
	public void markAsPrimary(GxSmsProvider entity) {
		List<GxSmsProvider> allProviders = smsProviderRepo.findAll();
		allProviders.forEach(provider -> {
			if (entity.getOid().equals(provider.getOid()))
				provider.setIsPrimary(true);
			else
				provider.setIsPrimary(false);

		});
		smsProviderRepo.saveAll(allProviders);
	}

	@Override
	public GxRegisteredDevice registerDevice(String namespace, String deviceToken, String systemName, String brand, boolean isTablet, String ownerId)
			throws RegisterDeviceFailedException {
		GxNamespace ns = findNamespace(namespace);
		if (ns == null)
			throw new RegisterDeviceFailedException("Namespace " + namespace + " does not exist.");
		GxRegisteredDevice device = findRegisteredDeviceByNamespaceAndDeviceTokenAndOwner(namespace, deviceToken, ownerId);
		if (device != null)
			throw new RegisterDeviceFailedException("Device with deviceToken " + deviceToken + "and ownerId " + ownerId + " for namespace " + namespace + " already registered");
		GxRegisteredDevice entity = new GxRegisteredDevice();
		entity.setBrand(brand);
		entity.setIsActive(true);
		entity.setIsTablet(isTablet);
		entity.setOwnerId(ownerId);
		entity.setSystemName(systemName);
		entity.setDeviceToken(deviceToken);
		entity.setNamespace(ns);
		return registeredDeviceRepo.save(entity);
	}

	@Override
	public GxAccessKey save(GxAccessKey entity) {
		GxAccessKey saved = accessKeyRepo.save(entity);
		entity.setOid(saved.getOid());
		return entity;
	}

	@Override
	public GxAuditLog save(GxAuditLog entity) {
		return auditLogRepo.save(entity);
	}

	@Override
	public GxCity save(GxCity city) {
		return cityRepo.save(city);
	}

	@Override
	public GxCountry save(GxCountry entity) {
		return countryRepo.save(entity);
	}

	@Override
	public GxCurrency save(GxCurrency entity) {
		return currencyRepo.save(entity);
	}

	@Override
	public GxEmailTemplate save(GxEmailTemplate entity) {
		return emailTemplateRepo.save(entity);
	}

	@Override
	public GxNamespace save(GxNamespace entity) {
		return namespaceRepo.save(entity);
	}

	@Override
	public GxNamespaceProperty save(GxNamespaceProperty entity) {
		GxNamespaceProperty savedEntity = namespacePropertyRepo.save(entity);
		entity.setOid(savedEntity.getOid());
		return entity;
	}

	@Override
	public GxRegisteredDevice save(GxRegisteredDevice entity) {
		return registeredDeviceRepo.save(entity);
	}

	@Override
	public GxResource save(GxResource entity) {
		return resourceRepo.save(entity);
	}

	@Override
	public GxSavedQuery save(GxSavedQuery entity) {
		savedQueryRepo.save(entity);
		return entity;
	}

	@Override
	public GxSecurityGroup save(GxSecurityGroup entity) {
		return securityGroupRepo.save(entity);
	}

	@Override
	public GxSecurityPolicy save(GxSecurityPolicy entity) {
		return securityPolicyRepo.save(entity);
	}

	@Override
	public GxSmsProvider save(GxSmsProvider entity) {
		GxSmsProvider saved = smsProviderRepo.save(entity);
		if (saved.getIsPrimary()) {
			markAsPrimary(saved);
		}
		return saved;
	}

	@Override
	public GxState save(GxState entity) {
		return stateRepo.save(entity);
	}

	@Override
	public GxSupportedLocale save(GxSupportedLocale entity) {
		return supportedLocaleRepo.save(entity);
	}

	@Override
	public GxTerm save(GxTerm entity) {
		return termRepo.save(entity);
	}

	@Override
	public GxUserAccount save(GxUserAccount entity) {
		if (!Strings.isBlank(entity.getConfirmPassword())) {
			entity.setPassword(entity.getConfirmPassword());
			entity.setNewPassword(null);
			entity.setConfirmPassword(null);
		}
		return userAccountRepo.save(entity);
	}

	@Override
	public GxNamespace systemNamespace() {
		return findOrCreateNamespace(SYSTEM_NAMESPACE);
	}

	@Override
	public void unregisterDevice(String namespace, String deviceToken) throws UnregisterDeviceFailedException {
		GxRegisteredDevice device = findRegisteredDeviceByNamespaceAndDeviceToken(namespace, deviceToken);
		if (device == null)
			throw new UnregisterDeviceFailedException("Device with deviceToken " + deviceToken + " for namespace " + namespace + " does not exist.");
		registeredDeviceRepo.deleteById(device.getOid());
	}

	@Override
	public List<GxAccessKey> findAccessKeyByUserAccount(GxUserAccount user) {
		return accessKeyRepo.findAllByUserAccountEquals(user);
	}

	@Override
	public List<GxAccessKey> findAccessKeyBySecurityGroup(GxSecurityGroup group) {
		return accessKeyRepo.findAllBySecurityGroupsEquals(group);
	}

	@Override
	public List<GxAccessKey> findAccessKeyBySecurityPolicy(GxSecurityPolicy policy) {
		return accessKeyRepo.findAllBySecurityPoliciesEquals(policy);
	}

}
