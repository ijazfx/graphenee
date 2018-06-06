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
package io.graphenee.core.model.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.graphenee.core.model.BeanCollectionFault;
import io.graphenee.core.model.BeanFault;
import io.graphenee.core.model.api.GxDataService;
import io.graphenee.core.model.bean.GxCityBean;
import io.graphenee.core.model.bean.GxCountryBean;
import io.graphenee.core.model.bean.GxEmailTemplateBean;
import io.graphenee.core.model.bean.GxGenderBean;
import io.graphenee.core.model.bean.GxNamespaceBean;
import io.graphenee.core.model.bean.GxSavedQueryBean;
import io.graphenee.core.model.bean.GxSecurityGroupBean;
import io.graphenee.core.model.bean.GxSecurityPolicyBean;
import io.graphenee.core.model.bean.GxSecurityPolicyDocumentBean;
import io.graphenee.core.model.bean.GxStateBean;
import io.graphenee.core.model.bean.GxSupportedLocaleBean;
import io.graphenee.core.model.bean.GxTermBean;
import io.graphenee.core.model.bean.GxUserAccountBean;
import io.graphenee.core.model.entity.GxCity;
import io.graphenee.core.model.entity.GxCountry;
import io.graphenee.core.model.entity.GxEmailTemplate;
import io.graphenee.core.model.entity.GxGender;
import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.core.model.entity.GxSavedQuery;
import io.graphenee.core.model.entity.GxSecurityGroup;
import io.graphenee.core.model.entity.GxSecurityPolicy;
import io.graphenee.core.model.entity.GxSecurityPolicyDocument;
import io.graphenee.core.model.entity.GxState;
import io.graphenee.core.model.entity.GxSupportedLocale;
import io.graphenee.core.model.entity.GxTerm;
import io.graphenee.core.model.entity.GxUserAccount;
import io.graphenee.core.model.jpa.repository.GxCityRepository;
import io.graphenee.core.model.jpa.repository.GxCountryRepository;
import io.graphenee.core.model.jpa.repository.GxEmailTemplateRepository;
import io.graphenee.core.model.jpa.repository.GxGenderRepository;
import io.graphenee.core.model.jpa.repository.GxNamespaceRepository;
import io.graphenee.core.model.jpa.repository.GxSavedQueryRepository;
import io.graphenee.core.model.jpa.repository.GxSecurityGroupRepository;
import io.graphenee.core.model.jpa.repository.GxSecurityPolicyDocumentRepository;
import io.graphenee.core.model.jpa.repository.GxSecurityPolicyRepository;
import io.graphenee.core.model.jpa.repository.GxStateRepository;
import io.graphenee.core.model.jpa.repository.GxSupportedLocaleRepository;
import io.graphenee.core.model.jpa.repository.GxTermRepository;
import io.graphenee.core.model.jpa.repository.GxUserAccountRepository;
import io.graphenee.core.util.CryptoUtil;

@Service
@Transactional("gxtm")
public class GxDataServiceImpl implements GxDataService {

	@Autowired
	@Qualifier("gxemf")
	EntityManager em;

	@Autowired
	GxGenderRepository genderRepo;

	@Autowired
	GxSupportedLocaleRepository supportedLocaleRepo;

	@Autowired
	GxTermRepository termRepo;

	@Autowired
	GxNamespaceRepository namespaceRepo;

	@Autowired
	GxSecurityGroupRepository securityGroupRepo;

	@Autowired
	GxUserAccountRepository userAccountRepo;

	@Autowired
	GxSecurityPolicyRepository securityPolicyRepo;

	@Autowired
	GxSecurityPolicyDocumentRepository securityPolicyDocumentRepo;

	@Autowired
	GxCountryRepository countryRepository;

	@Autowired
	GxStateRepository stateRepository;

	@Autowired
	GxCityRepository cityRepository;

	@Autowired
	GxSavedQueryRepository savedQueryRepository;

	@Autowired
	GxEmailTemplateRepository emailTemplateRepository;

	@Override
	public List<GxGenderBean> findGender() {
		return genderRepo.findAll().stream().map(this::makeGenderBean).collect(Collectors.toList());
	}

	@Override
	public GxGenderBean findGenderByCode(String genderCode) {
		GxGender gxGender = genderRepo.findOneByGenderCode(genderCode);
		if (gxGender != null) {
			return makeGenderBean(gxGender);
		}
		return null;
	}

	private GxGenderBean makeGenderBean(GxGender entity) {
		GxGenderBean bean = new GxGenderBean();
		bean.setOid(entity.getOid());
		bean.setGenderName(entity.getGenderName());
		bean.setGenderCode(entity.getGenderCode());
		return bean;
	}

	@Override
	public List<GxSupportedLocaleBean> findSupportedLocale() {
		return supportedLocaleRepo.findAll().stream().map(this::makeSupportedLocaleBean).collect(Collectors.toList());
	}

	private GxSupportedLocaleBean makeSupportedLocaleBean(GxSupportedLocale entity) {
		GxSupportedLocaleBean bean = new GxSupportedLocaleBean();
		bean.setOid(entity.getOid());
		bean.setLocaleCode(entity.getLocaleCode());
		bean.setLocaleName(entity.getLocaleName());
		bean.setIsLeftToRight(entity.getIsLeftToRight());
		bean.setIsActive(entity.getIsActive());
		bean.setIsProtected(entity.getIsProtected());
		return bean;
	}

	@Override
	public List<GxTermBean> findTermByLocale(Locale locale) {
		String localeCode = locale.toString();
		GxSupportedLocale supportedLocale = supportedLocaleRepo.findByLocaleCodeStartingWith(localeCode);
		if (supportedLocale == null) {
			localeCode = locale.getLanguage();
			supportedLocale = supportedLocaleRepo.findByLocaleCodeStartingWith(localeCode);
		}
		if (supportedLocale != null) {
			return supportedLocale.getGxTerms().stream().map(this::makeTermBean).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	private GxTermBean makeTermBean(GxTerm entity) {
		GxTermBean bean = new GxTermBean();
		bean.setOid(entity.getOid());
		bean.setTermKey(entity.getTermKey());
		bean.setTermSingular(entity.getTermSingular());
		bean.setTermPlural(entity.getTermPlural());
		bean.setIsActive(entity.getIsActive());
		bean.setIsProtected(entity.getIsProtected());
		bean.setNamespaceFault(BeanFault.beanFault(entity.getGxNamespace().getOid(), (oid) -> {
			return makeNamespaceBean(namespaceRepo.findOne(oid));
		}));
		bean.setSupportedLocaleFault(BeanFault.beanFault(entity.getGxSupportedLocale().getOid(), (oid) -> {
			return makeSupportedLocaleBean(supportedLocaleRepo.findOne(oid));
		}));
		return bean;
	}

	@Override
	public List<GxTermBean> findTermByTermKeyAndLocale(String termKey, Locale locale) {
		String localeCode = locale.toString();
		List<GxTerm> terms = termRepo.findByTermKeyAndGxSupportedLocaleLocaleCodeStartingWith(termKey, localeCode);
		if (terms.isEmpty()) {
			localeCode = locale.getLanguage();
			terms = termRepo.findByTermKeyAndGxSupportedLocaleLocaleCodeStartingWith(termKey, localeCode);
		}
		if (!terms.isEmpty()) {
			return terms.stream().map(this::makeTermBean).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	@Override
	public GxTermBean findEffectiveTermByTermKeyAndLocale(String termKey, Locale locale) {
		String localeCode = locale.toString();
		GxTerm term = termRepo.findTopByTermKeyAndGxSupportedLocaleLocaleCodeStartingWithOrderByOidDesc(termKey, localeCode);
		if (term == null) {
			localeCode = locale.getLanguage();
			term = termRepo.findTopByTermKeyAndGxSupportedLocaleLocaleCodeStartingWithOrderByOidDesc(termKey, localeCode);
		}
		if (term != null) {
			return makeTermBean(term);
		}
		return null;
	}

	private GxTerm toEntity(GxTermBean bean) {
		GxTerm entity = null;
		if (bean.getOid() != null) {
			entity = termRepo.findOne(bean.getOid());
		} else {
			entity = new GxTerm();
		}
		entity.setGxNamespace(namespaceRepo.findOne(bean.getNamespaceFault().getOid()));
		entity.setGxSupportedLocale(supportedLocaleRepo.findOne(bean.getSupportedLocaleFault().getOid()));
		entity.setTermKey(bean.getTermKey());
		entity.setTermSingular(bean.getTermSingular());
		entity.setTermPlural(bean.getTermPlural());
		entity.setIsActive(bean.getIsActive());
		entity.setIsProtected(entity.getGxNamespace().getIsProtected());
		return entity;
	}

	@Override
	public GxTermBean save(GxTermBean bean) {
		GxTerm saved = termRepo.save(toEntity(bean));
		bean.setOid(saved.getOid());
		return bean;
	}

	@Override
	public void delete(GxTermBean bean) {
		if (bean.getOid() != null && !bean.getIsProtected()) {
			termRepo.delete(bean.getOid());
		}
	}

	@Override
	public List<GxNamespaceBean> findNamespace() {
		return namespaceRepo.findAll().stream().map(this::makeNamespaceBean).collect(Collectors.toList());
	}

	private GxNamespaceBean makeNamespaceBean(GxNamespace entity) {
		GxNamespaceBean bean = new GxNamespaceBean();
		bean.setOid(entity.getOid());
		bean.setNamespace(entity.getNamespace());
		bean.setIsActive(entity.getIsActive());
		bean.setIsProtected(entity.getIsProtected());
		return bean;
	}

	@Override
	public List<GxTermBean> findTermByNamespaceAndSupportedLocale(Integer page, Integer size, GxNamespaceBean namespace, GxSupportedLocaleBean supportedLocale) {
		PageRequest pageRequest = new PageRequest(page, size);
		Page<GxTerm> result = null;
		if (namespace != null && supportedLocale != null) {
			result = termRepo.findByGxNamespaceOidAndGxSupportedLocaleOid(pageRequest, namespace.getOid(), supportedLocale.getOid());
		} else if (namespace != null) {
			result = termRepo.findByGxNamespaceOid(pageRequest, namespace.getOid());
		} else if (supportedLocale != null) {
			result = termRepo.findByGxSupportedLocaleOid(pageRequest, supportedLocale.getOid());
		} else {
			result = termRepo.findAll(pageRequest);
		}
		return result.getContent().stream().map(this::makeTermBean).collect(Collectors.toList());
	}

	private GxSupportedLocale toEntity(GxSupportedLocaleBean bean) {
		GxSupportedLocale entity = null;
		if (bean.getOid() != null) {
			entity = supportedLocaleRepo.findOne(bean.getOid());
		} else {
			entity = new GxSupportedLocale();
		}
		entity.setIsLeftToRight(bean.getIsLeftToRight());
		entity.setLocaleCode(bean.getLocaleCode());
		entity.setLocaleName(bean.getLocaleName());
		entity.setIsActive(bean.getIsActive());
		entity.setIsProtected(false);
		return entity;
	}

	private GxNamespace toEntity(GxNamespaceBean bean) {
		GxNamespace entity = null;
		if (bean.getOid() != null) {
			entity = namespaceRepo.findOne(bean.getOid());
		} else {
			entity = new GxNamespace();
		}
		entity.setNamespace(bean.getNamespace());
		entity.setNamespaceDescription(bean.getNamespaceDescription());
		entity.setIsActive(bean.getIsActive());
		entity.setIsProtected(false);
		return entity;
	}

	@Override
	public GxSupportedLocaleBean save(GxSupportedLocaleBean bean) {
		GxSupportedLocale saved = supportedLocaleRepo.save(toEntity(bean));
		bean.setOid(saved.getOid());
		return bean;
	}

	@Override
	public void delete(GxSupportedLocaleBean bean) {
		if (bean.getOid() != null && !bean.getIsProtected()) {
			supportedLocaleRepo.delete(bean.getOid());
		}
	}

	@Override
	public GxNamespaceBean findNamespace(String namespace) {
		GxNamespace entity = namespaceRepo.findByNamespace(namespace);
		if (entity != null) {
			return makeNamespaceBean(entity);
		}
		return null;
	}

	@Override
	public GxNamespaceBean findOrCreateNamespace(String namespace) {
		GxNamespace entity = namespaceRepo.findByNamespace(namespace);
		if (entity != null) {
			return makeNamespaceBean(entity);
		}
		GxNamespaceBean bean = new GxNamespaceBean();
		bean.setIsActive(true);
		bean.setIsProtected(false);
		bean.setNamespace(namespace);
		bean.setNamespaceDescription("-- Auto Generated --");
		save(bean);
		return bean;
	}

	@Override
	public GxNamespaceBean save(GxNamespaceBean bean) {
		GxNamespace saved = namespaceRepo.save(toEntity(bean));
		bean.setOid(saved.getOid());
		return bean;
	}

	@Override
	public void delete(GxNamespaceBean bean) {
		if (bean.getOid() != null && !bean.getIsProtected()) {
			securityGroupRepo.delete(bean.getOid());
		}
	}

	@Override
	public List<GxSecurityGroupBean> findSecurityGroup() {
		return securityGroupRepo.findAll().stream().map(this::makeSecurityGroupBean).collect(Collectors.toList());
	}

	@Override
	public GxSecurityGroupBean findSecurityGroup(Integer oidSecurityGroup) {
		return makeSecurityGroupBean(securityGroupRepo.findOne(oidSecurityGroup));
	}

	@Override
	public List<GxSecurityGroupBean> findSecurityGroupActive() {
		return securityGroupRepo.findAllByIsActive(true).stream().map(this::makeSecurityGroupBean).collect(Collectors.toList());
	}

	@Override
	public List<GxSecurityGroupBean> findSecurityGroupInactive() {
		return securityGroupRepo.findAllByIsActive(false).stream().map(this::makeSecurityGroupBean).collect(Collectors.toList());
	}

	private GxSecurityGroupBean makeSecurityGroupBean(GxSecurityGroup entity) {
		GxSecurityGroupBean bean = new GxSecurityGroupBean();
		bean.setOid(entity.getOid());
		bean.setSecurityGroupName(entity.getSecurityGroupName());
		bean.setSecurityGroupDescription(entity.getSecurityGroupDescription());
		bean.setPriority(entity.getPriority());
		bean.setIsActive(entity.getIsActive());
		bean.setIsProtected(entity.getIsProtected());
		bean.setNamespaceFault(BeanFault.beanFault(entity.getGxNamespace().getOid(), (oid) -> {
			return makeNamespaceBean(namespaceRepo.findOne(oid));
		}));
		bean.setSecurityPolicyCollectionFault(BeanCollectionFault.collectionFault(() -> {
			return securityPolicyRepo.findAllByGxSecurityGroupsOidEquals(entity.getOid()).stream().map(this::makeSecurityPolicyBean).collect(Collectors.toList());
		}));
		bean.setUserAccountCollectionFault(BeanCollectionFault.collectionFault(() -> {
			return userAccountRepo.findAllByGxSecurityGroupsOidEquals(entity.getOid()).stream().map(this::makeUserAccountBean).collect(Collectors.toList());
		}));
		return bean;
	}

	private GxSecurityGroup toEntity(GxSecurityGroupBean bean) {
		final GxSecurityGroup entity;
		if (bean.getOid() != null) {
			entity = securityGroupRepo.findOne(bean.getOid());
		} else {
			entity = new GxSecurityGroup();
		}
		entity.setSecurityGroupName(bean.getSecurityGroupName());
		entity.setSecurityGroupDescription(bean.getSecurityGroupDescription());
		entity.setPriority(bean.getPriority());
		entity.setGxNamespace(namespaceRepo.findOne(bean.getNamespaceFault().getOid()));
		entity.setIsActive(bean.getIsActive());
		entity.setIsProtected(false);

		if (bean.getSecurityPolicyCollectionFault().isModified()) {
			entity.getGxSecurityPolicies().clear();
			bean.getSecurityPolicyCollectionFault().getBeans().forEach(added -> {
				entity.getGxSecurityPolicies().add(securityPolicyRepo.findOne(added.getOid()));
			});
		}

		if (bean.getUserAccountCollectionFault().isModified()) {
			entity.getGxUserAccounts().clear();
			bean.getUserAccountCollectionFault().getBeans().forEach(added -> {
				System.err.println(added.getOid());
				entity.getGxUserAccounts().add(userAccountRepo.findOne(added.getOid()));
			});
		}

		return entity;
	}

	@Override
	public GxSecurityGroupBean save(GxSecurityGroupBean bean) {
		GxSecurityGroup saved = securityGroupRepo.save(toEntity(bean));
		bean.setOid(saved.getOid());
		return bean;
	}

	@Override
	public void delete(GxSecurityGroupBean bean) {
		if (bean.getOid() != null && !bean.getIsProtected()) {
			securityGroupRepo.delete(bean.getOid());
		}
	}

	@Override
	public List<GxSecurityGroupBean> findSecurityGroupByNamespace(GxNamespaceBean namespace) {
		GxNamespace entity = namespaceRepo.findOne(namespace.getOid());
		return entity.getGxSecurityGroups().stream().map(this::makeSecurityGroupBean).collect(Collectors.toList());
	}

	@Override
	public GxSecurityGroupBean findSecurityGroupByNamespaceAndGroupName(GxNamespaceBean namespace, String groupName) {
		GxNamespace entity = namespaceRepo.findOne(namespace.getOid());
		Optional<GxSecurityGroupBean> securityGroupOptional = entity.getGxSecurityGroups().stream().filter(sg -> {
			return sg.getSecurityGroupName().equalsIgnoreCase(groupName);
		}).map(this::makeSecurityGroupBean).findFirst();
		return securityGroupOptional.isPresent() ? securityGroupOptional.get() : null;
	}

	@Override
	public List<GxSecurityGroupBean> findSecurityGroupByNamespaceActive(GxNamespaceBean namespace) {
		GxNamespace entity = namespaceRepo.findOne(namespace.getOid());
		return entity.getGxSecurityGroups().stream().filter(securityGroup -> securityGroup.getIsActive() == true).map(this::makeSecurityGroupBean).collect(Collectors.toList());
	}

	@Override
	public GxSecurityGroupBean findSecurityGroupByNamespaceAndGroupNameActive(GxNamespaceBean namespace, String groupName) {
		GxNamespace entity = namespaceRepo.findOne(namespace.getOid());
		Optional<GxSecurityGroupBean> securityGroupOptional = entity.getGxSecurityGroups().stream().filter(sg -> {
			return sg.getIsActive() && sg.getSecurityGroupName().equalsIgnoreCase(groupName);
		}).map(this::makeSecurityGroupBean).findFirst();
		return securityGroupOptional.isPresent() ? securityGroupOptional.get() : null;
	}

	@Override
	public List<GxSecurityGroupBean> findSecurityGroupByNamespaceInactive(GxNamespaceBean namespace) {
		GxNamespace entity = namespaceRepo.findOne(namespace.getOid());
		return entity.getGxSecurityGroups().stream().filter(securityGroup -> securityGroup.getIsActive() == false).map(this::makeSecurityGroupBean).collect(Collectors.toList());
	}

	@Override
	public List<GxUserAccountBean> findUserAccount() {
		return userAccountRepo.findAll(new Sort("username")).stream().map(this::makeUserAccountBean).collect(Collectors.toList());
	}

	@Override
	public List<GxUserAccountBean> findUserAccountActive() {
		return userAccountRepo.findAll().stream().filter(userAccount -> userAccount.getIsActive() == true).map(this::makeUserAccountBean).collect(Collectors.toList());
	}

	@Override
	public List<GxUserAccountBean> findUserAccountInactive() {
		return userAccountRepo.findAll().stream().filter(userAccount -> userAccount.getIsActive() == false).map(this::makeUserAccountBean).collect(Collectors.toList());
	}

	private GxUserAccountBean makeUserAccountBean(GxUserAccount entity) {
		GxUserAccountBean bean = new GxUserAccountBean();
		bean.setOid(entity.getOid());
		bean.setUsername(entity.getUsername());
		bean.setEmail(entity.getEmail());
		bean.setFirstName(entity.getFirstName());
		bean.setLastName(entity.getLastName());
		bean.setFullNameNative(entity.getFullNameNative());
		bean.setIsLocked(entity.getIsLocked());
		bean.setIsActive(entity.getIsActive());
		bean.setIsProtected(entity.getIsProtected());
		bean.setSecurityGroupCollectionFault(BeanCollectionFault.collectionFault(() -> {
			return securityGroupRepo.findAllByGxUserAccountsOidEquals(entity.getOid()).stream().map(this::makeSecurityGroupBean).collect(Collectors.toList());
		}));
		bean.setSecurityPolicyCollectionFault(BeanCollectionFault.collectionFault(() -> {
			return securityPolicyRepo.findAllByGxUserAccountsOidEquals(entity.getOid()).stream().map(this::makeSecurityPolicyBean).collect(Collectors.toList());
		}));
		return bean;
	}

	private GxUserAccount toEntity(GxUserAccountBean bean) {
		final GxUserAccount entity;
		if (bean.getOid() != null) {
			entity = userAccountRepo.findOne(bean.getOid());
		} else {
			entity = new GxUserAccount();
		}
		entity.setUsername(bean.getUsername());
		entity.setEmail(bean.getEmail());
		entity.setFirstName(bean.getFirstName());
		entity.setLastName(bean.getLastName());
		entity.setFullNameNative(bean.getFullNameNative());
		entity.setIsLocked(bean.getIsLocked());
		entity.setIsActive(bean.getIsActive());
		entity.setIsProtected(false);
		entity.setIsPasswordChangeRequired(bean.getIsPasswordChangeRequired());
		entity.setCountLoginFailed(bean.getCountLoginFailed());

		if (bean.getSecurityGroupCollectionFault().isModified()) {
			entity.getGxSecurityGroups().clear();
			bean.getSecurityGroupCollectionFault().getBeans().forEach(added -> {
				entity.getGxSecurityGroups().add(securityGroupRepo.findOne(added.getOid()));
			});
		}

		if (bean.getSecurityPolicyCollectionFault().isModified()) {
			entity.getGxSecurityPolicies().clear();
			bean.getSecurityPolicyCollectionFault().getBeans().forEach(added -> {
				entity.getGxSecurityPolicies().add(securityPolicyRepo.findOne(added.getOid()));
			});
		}

		if (bean.getPassword() != null) {
			entity.setPassword(bean.getPassword());
		}

		return entity;
	}

	@Override
	public GxUserAccountBean save(GxUserAccountBean bean) {
		GxUserAccount saved = userAccountRepo.save(toEntity(bean));
		bean.setOid(saved.getOid());
		return bean;
	}

	@Override
	public void delete(GxUserAccountBean bean) {
		if (bean.getOid() != null && !bean.getIsProtected()) {
			userAccountRepo.delete(bean.getOid());
		}
	}

	@Override
	public List<GxUserAccountBean> findUserAccountBySecurityGroup(GxSecurityGroupBean securityGroup) {
		GxSecurityGroup entity = securityGroupRepo.findOne(securityGroup.getOid());
		return entity.getGxUserAccounts().stream().map(this::makeUserAccountBean).collect(Collectors.toList());
	}

	@Override
	public List<GxUserAccountBean> findUserAccountBySecurityGroupActive(GxSecurityGroupBean securityGroup) {
		GxSecurityGroup entity = securityGroupRepo.findOne(securityGroup.getOid());
		return entity.getGxUserAccounts().stream().filter(userAccount -> userAccount.getIsActive() == true).map(this::makeUserAccountBean).collect(Collectors.toList());
	}

	@Override
	public List<GxUserAccountBean> findUserAccountBySecurityGroupInactive(GxSecurityGroupBean securityGroup) {
		GxSecurityGroup entity = securityGroupRepo.findOne(securityGroup.getOid());
		return entity.getGxUserAccounts().stream().filter(userAccount -> userAccount.getIsActive() == false).map(this::makeUserAccountBean).collect(Collectors.toList());
	}

	@Override
	public List<GxSecurityPolicyBean> findSecurityPolicy() {
		return securityPolicyRepo.findAll().stream().map(this::makeSecurityPolicyBean).collect(Collectors.toList());
	}

	@Override
	public List<GxSecurityPolicyBean> findSecurityPolicyActive() {
		return securityPolicyRepo.findAll().stream().filter(securityPolicy -> securityPolicy.getIsActive() == true).map(this::makeSecurityPolicyBean).collect(Collectors.toList());
	}

	@Override
	public List<GxSecurityPolicyBean> findSecurityPolicyInactive() {
		return securityPolicyRepo.findAll().stream().filter(securityPolicy -> securityPolicy.getIsActive() == false).map(this::makeSecurityPolicyBean).collect(Collectors.toList());
	}

	private GxSecurityPolicyBean makeSecurityPolicyBean(GxSecurityPolicy entity) {
		GxSecurityPolicyBean bean = new GxSecurityPolicyBean();
		bean.setOid(entity.getOid());
		bean.setPriority(entity.getPriority());
		bean.setSecurityPolicyName(entity.getSecurityPolicyName());
		bean.setSecurityPolicyDescription(entity.getSecurityPolicyDescription());
		bean.setIsActive(entity.getIsActive());
		bean.setIsProtected(entity.getIsProtected());
		bean.setNamespaceFault(BeanFault.beanFault(entity.getGxNamespace().getOid(), (oid) -> {
			return makeNamespaceBean(namespaceRepo.findOne(oid));
		}));
		bean.setSecurityGroupCollectionFault(BeanCollectionFault.collectionFault(() -> {
			return securityGroupRepo.findAllByGxSecurityPoliciesOidEquals(entity.getOid()).stream().map(this::makeSecurityGroupBean).collect(Collectors.toList());
		}));
		bean.setUserAccountCollectionFault(BeanCollectionFault.collectionFault(() -> {
			return userAccountRepo.findAllByGxSecurityPoliciesOidEquals(entity.getOid()).stream().map(this::makeUserAccountBean).collect(Collectors.toList());
		}));
		bean.setSecurityPolicyDocumentCollectionFault(BeanCollectionFault.collectionFault(() -> {
			return securityPolicyDocumentRepo.findAllByGxSecurityPolicyOidEquals(entity.getOid()).stream().map(this::makeSecurityPolicyDocumentBean).collect(Collectors.toList());
		}));
		return bean;
	}

	private GxSecurityPolicy toEntity(GxSecurityPolicyBean bean) {
		final GxSecurityPolicy entity;
		if (bean.getOid() != null) {
			entity = securityPolicyRepo.findOne(bean.getOid());
		} else {
			entity = new GxSecurityPolicy();
		}
		entity.setSecurityPolicyName(bean.getSecurityPolicyName());
		entity.setSecurityPolicyDescription(bean.getSecurityPolicyDescription());
		entity.setPriority(bean.getPriority());
		entity.setIsActive(bean.getIsActive());
		entity.setIsProtected(false);

		entity.setGxNamespace(namespaceRepo.findOne(bean.getNamespaceFault().getOid()));

		if (bean.getSecurityGroupCollectionFault().isModified()) {
			entity.getGxSecurityGroups().clear();
			bean.getSecurityGroupCollectionFault().getBeans().forEach(added -> {
				entity.getGxSecurityGroups().add(securityGroupRepo.findOne(added.getOid()));
			});
		}

		if (bean.getUserAccountCollectionFault().isModified()) {
			entity.getGxUserAccounts().clear();
			bean.getUserAccountCollectionFault().getBeans().forEach(added -> {
				entity.getGxUserAccounts().add(userAccountRepo.findOne(added.getOid()));
			});
		}

		if (bean.getSecurityPolicyDocumentCollectionFault().isModified()) {
			bean.getSecurityPolicyDocumentCollectionFault().getBeansRemoved().forEach(removed -> {
				entity.getGxSecurityPolicyDocuments().removeIf(sp -> sp.getOid().equals(removed.getOid()));
			});
			Map<Integer, GxSecurityPolicyDocument> documentMap = entity.getGxSecurityPolicyDocuments().stream().collect(Collectors.toMap(e -> {
				return e.getOid();
			}, e -> {
				return e;
			}));
			bean.getSecurityPolicyDocumentCollectionFault().getBeansAdded().forEach(added -> {
				GxSecurityPolicyDocument spd = null;
				if (added.getOid() != null) {
					spd = documentMap.get(added.getOid());
				} else {
					spd = new GxSecurityPolicyDocument();
					entity.addGxSecurityPolicyDocument(spd);
				}
				spd = toEntity(spd, added);
			});
			bean.getSecurityPolicyDocumentCollectionFault().getBeansUpdated().forEach(updated -> {
				GxSecurityPolicyDocument spd = documentMap.get(updated.getOid());
				if (spd != null) {
					spd = toEntity(spd, updated);
				}
			});
		}

		return entity;
	}

	private GxSecurityPolicyDocumentBean makeSecurityPolicyDocumentBean(GxSecurityPolicyDocument entity) {
		GxSecurityPolicyDocumentBean bean = new GxSecurityPolicyDocumentBean();
		bean.setOid(entity.getOid());
		bean.setDocumentJson(entity.getDocumentJson());
		bean.setTag(entity.getTag());
		bean.setIsDefault(entity.getIsDefault());
		bean.setSecurityPolicyBeanFault(new BeanFault<Integer, GxSecurityPolicyBean>(entity.getGxSecurityPolicy().getOid(), oid -> {
			return makeSecurityPolicyBean(securityPolicyRepo.findOne(oid));
		}));
		return bean;
	}

	private GxSecurityPolicyDocument toEntity(GxSecurityPolicyDocument entity, GxSecurityPolicyDocumentBean bean) {
		entity.setTag(bean.getTag());
		entity.setIsDefault(bean.getIsDefault());
		entity.setDocumentJson(bean.getDocumentJson());
		return entity;
	}

	@Override
	public GxSecurityPolicyBean save(GxSecurityPolicyBean bean) {
		GxSecurityPolicy saved = securityPolicyRepo.save(toEntity(bean));
		bean.setOid(saved.getOid());
		return bean;
	}

	@Override
	public void delete(GxSecurityPolicyBean bean) {
		if (bean.getOid() != null && !bean.getIsProtected()) {
			securityPolicyRepo.delete(bean.getOid());
		}
	}

	@Override
	public List<GxSecurityPolicyBean> findSecurityPolicyByNamespace(GxNamespaceBean namespace) {
		GxNamespace entity = namespaceRepo.findOne(namespace.getOid());
		return entity.getGxSecurityPolicies().stream().map(this::makeSecurityPolicyBean).collect(Collectors.toList());
	}

	@Override
	public List<GxSecurityPolicyBean> findSecurityPolicyByNamespaceActive(GxNamespaceBean namespace) {
		GxNamespace entity = namespaceRepo.findOne(namespace.getOid());
		return entity.getGxSecurityPolicies().stream().filter(securityPolicy -> securityPolicy.getIsActive() == true).map(this::makeSecurityPolicyBean)
				.collect(Collectors.toList());
	}

	@Override
	public List<GxSecurityPolicyBean> findSecurityPolicyByNamespaceInactive(GxNamespaceBean namespace) {
		GxNamespace entity = namespaceRepo.findOne(namespace.getOid());
		return entity.getGxSecurityPolicies().stream().filter(securityPolicy -> securityPolicy.getIsActive() == false).map(this::makeSecurityPolicyBean)
				.collect(Collectors.toList());
	}

	private List<GxCountryBean> makeCountryBean(List<GxCountry> countryList) {
		List<GxCountryBean> beans = new ArrayList<>();
		countryList.forEach(country -> {
			GxCountryBean bean = makeCountryBean(country);
			beans.add(bean);
		});
		return beans;
	}

	private GxCountryBean makeCountryBean(GxCountry country) {
		GxCountryBean bean = new GxCountryBean();
		bean.setOid(country.getOid());
		bean.setCountryName(country.getCountryName());
		bean.setIsActive(country.getIsActive());
		bean.setAlpha3Code(country.getAlpha3Code());
		bean.setNumericCode(country.getNumericCode());

		bean.setStateBeanColltionFault(BeanCollectionFault.collectionFault(() -> {
			return findStateByCountry(country.getOid());
		}));

		bean.setCityBeanColltionFault(BeanCollectionFault.collectionFault(() -> {
			return findCityByCountry(country.getOid());
		}));
		return bean;
	}

	@Override
	public List<GxCountryBean> findCountry() {
		return makeCountryBean(countryRepository.findAllByIsActiveTrueOrderByCountryNameAsc());
	}

	@Override
	public GxCountryBean findCountry(Integer oid) {
		return makeCountryBean(countryRepository.findOne(oid));
	}

	@Override
	public GxCountryBean findCountryByCountryName(String countryName) {
		return makeCountryBean(countryRepository.findOneByIsActiveTrueAndCountryNameOrderByCountryNameAsc(countryName));
	}

	@Override
	public GxCountryBean findCountryByCountryAlpha3Code(String alpha3Code) {
		return makeCountryBean(countryRepository.findOneByIsActiveTrueAndAlpha3CodeOrderByCountryNameAsc(alpha3Code));
	}

	@Override
	public GxCountryBean findCountryByNumericCode(Integer numericCode) {
		return makeCountryBean(countryRepository.findOneByIsActiveTrueAndNumericCodeOrderByCountryNameAsc(numericCode));
	}

	@Override
	public GxCountryBean findCountryByStateName(String stateName) {
		return makeCountryBean(countryRepository.findOneByIsActiveTrueAndGxStatesStateNameOrderByCountryNameAsc(stateName));
	}

	@Override
	public GxCountryBean findCountryByStateCode(String stateCode) {
		return makeCountryBean(countryRepository.findOneByIsActiveTrueAndGxStatesStateCodeOrderByCountryNameAsc(stateCode));
	}

	@Override
	public GxCountryBean findCountryByCityName(String cityName) {
		return makeCountryBean(countryRepository.findOneByIsActiveTrueAndGxCitiesCityNameOrderByCountryNameAsc(cityName));
	}

	@Override
	public GxCountryBean createOrUpdate(GxCountryBean bean) {
		GxCountry country = null;
		if (bean.getOid() != null) {
			country = countryRepository.findOne(bean.getOid());
		} else {
			country = new GxCountry();
		}

		country.setCountryName(bean.getCountryName());
		country.setAlpha3Code(bean.getAlpha3Code());
		country.setNumericCode(bean.getNumericCode());
		country.setIsActive(bean.getIsActive());

		if (bean.getStateBeanColltionFault().isModified()) {
			country.getGxStates().clear();
			for (GxStateBean stateBean : bean.getStateBeanColltionFault().getBeans()) {
				GxState state = _findState(stateBean.getOid());
				state.setGxCountry(country);
				country.getGxStates().add(state);
			}
		}
		if (bean.getCityBeanColltionFault().isModified()) {
			country.getGxCities().clear();
			for (GxCityBean cityBean : bean.getCityBeanColltionFault().getBeans()) {
				GxCity city = _findCity(cityBean.getOid());
				city.setGxCountry(country);
				country.getGxCities().add(city);
			}
		}

		country = countryRepository.save(country);
		bean.setOid(country.getOid());
		return bean;
	}

	private GxCountry _findCountry(Integer oid) {
		return countryRepository.findOne(oid);
	}

	private GxCity _findCity(Integer oid) {
		return cityRepository.findOne(oid);
	}

	@Override
	public void delete(GxCountryBean bean) {
		countryRepository.delete(bean.getOid());
	}

	private List<GxStateBean> makeStateBean(List<GxState> stateList) {
		List<GxStateBean> beans = new ArrayList<>();
		stateList.forEach(state -> {
			GxStateBean bean = makeStateBean(state);
			beans.add(bean);
		});
		return beans;
	}

	private GxStateBean makeStateBean(GxState state) {
		GxStateBean bean = new GxStateBean();
		bean.setOid(state.getOid());
		bean.setIsActive(state.getIsActive());
		bean.setStateCode(state.getStateCode());
		bean.setStateName(state.getStateName());

		bean.setCountryBeanFault(BeanFault.beanFault(state.getGxCountry().getOid(), (oid) -> {
			return findCountry(oid);
		}));
		bean.setCityBeanColltionFault(BeanCollectionFault.collectionFault(() -> {
			return findCityByState(state.getOid());
		}));
		return bean;
	}

	@Override
	public GxStateBean createOrUpdate(GxStateBean bean) {
		GxState state = null;
		if (bean.getOid() != null) {
			state = stateRepository.findOne(bean.getOid());
		} else {
			state = new GxState();
		}

		state.setGxCountry(_findCountry(bean.getCountryBeanFault().getOid()));
		state.setStateName(bean.getStateName());
		state.setStateCode(bean.getStateCode());
		state.setIsActive(bean.getIsActive());
		if (bean.getCityBeanColltionFault().isModified()) {
			state.getGxCities().clear();
			for (GxCityBean cityBean : bean.getCityBeanColltionFault().getBeans()) {
				GxCity city = _findCity(cityBean.getOid());
				city.setGxState(state);
				state.getGxCities().add(city);
			}
		}

		state = stateRepository.save(state);
		bean.setOid(state.getOid());
		return bean;
	}

	@Override
	public GxCityBean createOrUpdate(GxCityBean bean) {
		GxCity city = null;
		if (bean.getOid() != null) {
			city = cityRepository.findOne(bean.getOid());
		} else {
			city = new GxCity();
		}

		city.setGxCountry(_findCountry(bean.getCountryBeanFault().getOid()));
		if (bean.getStateBeanFault() != null) {
			city.setGxState(_findState(bean.getStateBeanFault().getOid()));
		} else {
			city.setGxState(null);
		}
		city.setCityName(bean.getCityName());
		city.setIsActive(bean.getIsActive());

		city = cityRepository.save(city);
		bean.setOid(city.getOid());
		return bean;
	}

	@Override
	public void delete(GxCityBean bean) {
		cityRepository.delete(bean.getOid());
	}

	@Override
	public void delete(GxStateBean bean) {
		stateRepository.delete(bean.getOid());
	}

	private GxState _findState(Integer oid) {
		return stateRepository.findOne(oid);
	}

	@Override
	public List<GxStateBean> findState() {
		return makeStateBean(stateRepository.findAllByIsActiveTrueOrderByStateNameAsc());
	}

	@Override
	public GxStateBean findState(Integer oid) {
		return makeStateBean(stateRepository.findOne(oid));
	}

	@Override
	public GxStateBean findStateByStateCode(String stateCode) {
		return makeStateBean(stateRepository.findOneByIsActiveTrueAndStateCodeOrderByStateNameAsc(stateCode));
	}

	@Override
	public GxStateBean findStateByStateName(String stateName) {
		return makeStateBean(stateRepository.findOneByIsActiveTrueAndStateNameOrderByStateNameAsc(stateName));
	}

	@Override
	public GxStateBean findStateByCityName(String cityName) {
		return makeStateBean(stateRepository.findOneByIsActiveTrueAndGxCitiesCityNameOrderByStateNameAsc(cityName));
	}

	@Override
	public List<GxStateBean> findStateByCountry(Integer oidCountry) {
		return makeStateBean(stateRepository.findAllByIsActiveTrueAndGxCountryOidOrderByStateNameAsc(oidCountry));
	}

	@Override
	public List<GxStateBean> findStateByCountryNumericCode(Integer numeriCode) {
		return makeStateBean(stateRepository.findAllByIsActiveTrueAndGxCountryNumericCodeOrderByStateNameAsc(numeriCode));
	}

	@Override
	public List<GxStateBean> findStateByCountryCountryName(String countryName) {
		return makeStateBean(stateRepository.findAllByIsActiveTrueAndGxCountryCountryNameOrderByStateNameAsc(countryName));
	}

	private List<GxCityBean> makeCityBean(List<GxCity> cityList) {
		List<GxCityBean> beans = new ArrayList<>();
		cityList.forEach(city -> {
			GxCityBean bean = makeCityBean(city);
			beans.add(bean);
		});
		return beans;
	}

	private GxCityBean makeCityBean(GxCity city) {
		GxCityBean bean = new GxCityBean();
		bean.setOid(city.getOid());
		bean.setIsActive(city.getIsActive());
		bean.setCityName(city.getCityName());

		bean.setCountryBeanFault(BeanFault.beanFault(city.getGxCountry().getOid(), (oid) -> {
			return findCountry(oid);
		}));
		if (city.getGxState() != null) {
			bean.setStateBeanFault(BeanFault.beanFault(city.getGxState().getOid(), (oid) -> {
				return findState(oid);
			}));
		}

		return bean;
	}

	@Override
	public List<GxCityBean> findCity() {
		return makeCityBean(cityRepository.findAllByIsActiveTrueOrderByCityNameAsc());
	}

	@Override
	public GxCityBean findCity(Integer oid) {
		return makeCityBean(cityRepository.findOne(oid));
	}

	@Override
	public GxCityBean findCityByCityName(String cityName) {
		return makeCityBean(cityRepository.findOneByCityName(cityName));
	}

	@Override
	public List<GxCityBean> findCityByCountry(Integer oidCountry) {
		return makeCityBean(cityRepository.findAllByIsActiveTrueAndGxCountryOidOrderByCityNameAsc(oidCountry));
	}

	@Override
	public List<GxCityBean> findCityByState(Integer oidState) {
		return makeCityBean(cityRepository.findAllByIsActiveTrueAndGxStateOidOrderByCityNameAsc(oidState));
	}

	@Override
	public List<GxCityBean> findCityByCountryNumericCode(Integer numericCode) {
		return makeCityBean(cityRepository.findAllByIsActiveTrueAndGxCountryNumericCodeOrderByCityNameAsc(numericCode));
	}

	@Override
	public List<GxCityBean> findCityByStateCode(String stateCode) {
		return makeCityBean(cityRepository.findAllByIsActiveTrueAndGxStateStateCodeOrderByCityNameAsc(stateCode));
	}

	@Override
	public GxSavedQueryBean findSavedQuery(Integer oid) {
		return makeSavedQueryBean(savedQueryRepository.findOne(oid));
	}

	private GxSavedQueryBean makeSavedQueryBean(GxSavedQuery entity) {
		GxSavedQueryBean bean = new GxSavedQueryBean();
		bean.setOid(entity.getOid());
		bean.setQueryName(entity.getQueryName());
		bean.setTargetUser(entity.getTargetUser());
		bean.setQueryBeanClassName(entity.getQueryBeanClassName());
		bean.setAdditionalInfo(entity.getAdditionalInfo());
		bean.setQueryBeanJson(entity.getQueryBeanJson());
		return bean;
	}

	@Override
	public List<GxSavedQueryBean> findSavedQueryByUsername(String username) {
		List<GxSavedQueryBean> beans = new ArrayList<>();
		beans.addAll(savedQueryRepository.findAll().stream().filter(entity -> {
			return entity.getTargetUser() == null || entity.getTargetUser().equalsIgnoreCase(username);
		}).map(this::makeSavedQueryBean).collect(Collectors.toList()));
		return beans;
	}

	@Override
	public void delete(GxSavedQueryBean bean) {
		savedQueryRepository.delete(bean.getOid());
	}

	@Override
	public GxSavedQueryBean save(GxSavedQueryBean bean) {
		savedQueryRepository.save(toEntity(bean));
		return bean;
	}

	private GxSavedQuery toEntity(GxSavedQueryBean bean) {
		GxSavedQuery entity = null;
		if (bean.getOid() != null) {
			entity = savedQueryRepository.findOne(bean.getOid());
		} else {
			entity = new GxSavedQuery();
		}
		entity.setQueryName(bean.getQueryName());
		entity.setTargetUser(bean.getTargetUser());
		entity.setQueryBeanClassName(bean.getQueryBeanClassName());
		entity.setAdditionalInfo(bean.getAdditionalInfo());
		entity.setQueryBeanJson(bean.getQueryBeanJson());
		return entity;
	}

	@Override
	public List<GxSavedQueryBean> findSavedQuery() {
		List<GxSavedQueryBean> beans = new ArrayList<>();
		beans.addAll(savedQueryRepository.findAll().stream().map(this::makeSavedQueryBean).collect(Collectors.toList()));
		return beans;
	}

	@Override
	public List<GxEmailTemplateBean> findEmailTemplateByNamespace(GxNamespaceBean namespace) {
		List<GxEmailTemplateBean> beans = new ArrayList<>();
		beans.addAll(emailTemplateRepository.findAllByGxNamespaceOidOrderByTemplateName(namespace.getOid()).stream().map(template -> {
			return makeEmailTemplateBean(template, namespace);
		}).collect(Collectors.toList()));
		return beans;
	}

	@Override
	public List<GxEmailTemplateBean> findEmailTemplateByNamespaceActive(GxNamespaceBean namespace) {
		List<GxEmailTemplateBean> beans = new ArrayList<>();
		beans.addAll(emailTemplateRepository.findAllByGxNamespaceOidAndIsActiveOrderByTemplateName(namespace.getOid(), true).stream().map(template -> {
			return makeEmailTemplateBean(template, namespace);
		}).collect(Collectors.toList()));
		return beans;
	}

	@Override
	public List<GxEmailTemplateBean> findEmailTemplateByNamespaceInactive(GxNamespaceBean namespace) {
		List<GxEmailTemplateBean> beans = new ArrayList<>();
		beans.addAll(emailTemplateRepository.findAllByGxNamespaceOidAndIsActiveOrderByTemplateName(namespace.getOid(), false).stream().map(template -> {
			return makeEmailTemplateBean(template, namespace);
		}).collect(Collectors.toList()));
		return beans;
	}

	@Override
	public GxEmailTemplateBean findEmailTemplateByTemplateNameActive(String templateName) {
		GxNamespaceBean namespace = findNamespace(GxNamespaceBean.SYSTEM);
		GxEmailTemplate emailTemplate = null;
		if (namespace != null) {
			emailTemplate = emailTemplateRepository.findOneByTemplateNameAndGxNamespaceOidAndIsActive(templateName, namespace.getOid(), true);
		} else {
			emailTemplate = emailTemplateRepository.findOneByTemplateNameAndIsActive(templateName, true);
		}
		if (emailTemplate != null) {
			return makeEmailTemplateBean(emailTemplate, namespace);
		}
		return null;
	}

	@Override
	public GxEmailTemplateBean findEmailTemplateByTemplateNameAndNamespaceActive(String templateName, GxNamespaceBean namespace) {
		GxEmailTemplate emailTemplate = emailTemplateRepository.findOneByTemplateNameAndGxNamespaceOidAndIsActive(templateName, namespace.getOid(), true);
		if (emailTemplate != null) {
			return makeEmailTemplateBean(emailTemplate, namespace);
		}
		return null;
	}

	@Override
	public List<GxEmailTemplateBean> findEmailTemplate() {
		List<GxEmailTemplateBean> beans = new ArrayList<>();
		beans.addAll(emailTemplateRepository.findAll(new Sort("templateName")).stream().map(template -> {
			return makeEmailTemplateBean(template, null);
		}).collect(Collectors.toList()));
		return beans;
	}

	@Override
	public List<GxEmailTemplateBean> findEmailTemplateActive() {
		List<GxEmailTemplateBean> beans = new ArrayList<>();
		beans.addAll(emailTemplateRepository.findAllByIsActiveOrderByTemplateName(true).stream().map(template -> {
			return makeEmailTemplateBean(template, null);
		}).collect(Collectors.toList()));
		return beans;
	}

	@Override
	public List<GxEmailTemplateBean> findEmailTemplateInactive() {
		List<GxEmailTemplateBean> beans = new ArrayList<>();
		beans.addAll(emailTemplateRepository.findAllByIsActiveOrderByTemplateName(false).stream().map(template -> {
			return makeEmailTemplateBean(template, null);
		}).collect(Collectors.toList()));
		return beans;
	}

	private GxEmailTemplateBean makeEmailTemplateBean(GxEmailTemplate entity, GxNamespaceBean namespace) {
		GxEmailTemplateBean bean = new GxEmailTemplateBean();
		bean.setOid(entity.getOid());
		bean.setBccList(entity.getBccList());
		bean.setBody(entity.getBody());
		bean.setCcList(entity.getCcList());
		bean.setIsActive(entity.getIsActive());
		bean.setIsProtected(entity.getIsProtected());
		bean.setSubject(entity.getSubject());
		bean.setTemplateName(entity.getTemplateName());

		if (entity.getGxNamespace() != null) {
			if (namespace != null) {
				bean.setNamespaceBeanFault(BeanFault.beanFault(namespace.getOid(), namespace));
			} else {
				bean.setNamespaceBeanFault(new BeanFault<>(entity.getGxNamespace().getOid(), (oid) -> {
					return makeNamespaceBean(namespaceRepo.findOne(oid));
				}));
			}
		}

		return bean;
	}

	private GxEmailTemplate toEntity(GxEmailTemplateBean bean) {
		GxEmailTemplate entity = null;
		if (bean.getOid() != null) {
			entity = emailTemplateRepository.findOne(bean.getOid());
		} else {
			entity = new GxEmailTemplate();
		}
		entity.setGxNamespace(namespaceRepo.findOne(bean.getNamespaceBeanFault().getOid()));
		entity.setBccList(bean.getBccList());
		entity.setBody(bean.getBody());
		entity.setCcList(bean.getCcList());
		entity.setIsActive(bean.getIsActive());
		entity.setIsProtected(bean.getIsProtected());
		entity.setSubject(bean.getSubject());
		entity.setTemplateName(bean.getTemplateName());
		return entity;
	}

	@Override
	public GxEmailTemplateBean save(GxEmailTemplateBean bean) {
		GxEmailTemplate entity = emailTemplateRepository.save(toEntity(bean));
		bean.setOid(entity.getOid());
		return bean;
	}

	@Override
	public void delete(GxEmailTemplateBean bean) {
		if (bean.getOid() != null && !bean.getIsProtected()) {
			emailTemplateRepository.delete(bean.getOid());
		}
	}

	@Override
	public GxEmailTemplateBean findEmailTemplate(Integer oid) {
		GxEmailTemplate gxEmailTemplate = emailTemplateRepository.findOne(oid);
		if (gxEmailTemplate == null) {
			return null;
		}
		return makeEmailTemplateBean(gxEmailTemplate, null);
	}

	@Override
	public GxUserAccountBean findUserAccount(Integer oidUserAccount) {
		GxUserAccount userAccount = userAccountRepo.findOne(oidUserAccount);
		if (userAccount == null)
			return null;
		return makeUserAccountBean(userAccount);
	}

	@Override
	public GxUserAccountBean findUserAccountByUsername(String username) {
		GxUserAccount userAccount = userAccountRepo.findByUsername(username);
		if (userAccount == null)
			return null;
		return makeUserAccountBean(userAccount);
	}

	@Override
	public GxUserAccountBean findUserAccountByUsernameAndPassword(String username, String password) {
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
		return makeUserAccountBean(userAccount);
	}

	@Override
	public GxSecurityGroupBean findOrCreateSecurityGroup(String groupName, GxNamespaceBean namespaceBean) {
		GxSecurityGroup entity = securityGroupRepo.findAllBySecurityGroupNameAndGxNamespaceNamespace(groupName, namespaceBean.getNamespace());
		if (entity != null) {
			return makeSecurityGroupBean(entity);
		}
		GxSecurityGroupBean bean = new GxSecurityGroupBean();
		bean.setIsActive(true);
		bean.setIsProtected(false);
		bean.setSecurityGroupName(groupName);

		bean.setNamespaceFault(new BeanFault<>(namespaceBean.getOid(), namespaceBean));
		createOrUpdate(bean);
		return bean;
	}

	@Override
	public GxSecurityGroupBean createOrUpdate(GxSecurityGroupBean bean) {
		GxSecurityGroup entity = toEntity(bean);
		entity = securityGroupRepo.save(entity);
		bean.setOid(entity.getOid());
		return bean;
	}

	@Override
	public GxSecurityPolicyBean findOrCreateSecurityPolicy(String policyName, GxNamespaceBean namespaceBean) {
		GxSecurityPolicy entity = securityPolicyRepo.findAllBySecurityPolicyNameAndGxNamespaceNamespace(policyName, namespaceBean.getNamespace());
		if (entity != null) {
			return makeSecurityPolicyBean(entity);
		}

		GxSecurityPolicyBean bean = new GxSecurityPolicyBean();
		bean.setIsActive(true);
		bean.setIsProtected(false);
		bean.setSecurityPolicyName(policyName);

		bean.setNamespaceFault(new BeanFault<>(namespaceBean.getOid(), namespaceBean));

		return bean;
	}

	@Override
	public GxSecurityPolicyBean createOrUpdate(GxSecurityPolicyBean bean) {
		GxSecurityPolicy entity = toEntity(bean);
		entity = securityPolicyRepo.save(entity);
		bean.setOid(entity.getOid());
		return bean;
	}

}
