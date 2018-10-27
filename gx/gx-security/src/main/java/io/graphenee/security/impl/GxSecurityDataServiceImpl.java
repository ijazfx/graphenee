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
package io.graphenee.security.impl;

import java.sql.Timestamp;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import io.graphenee.core.enums.AccessTypeStatus;
import io.graphenee.core.model.BeanCollectionFault;
import io.graphenee.core.model.BeanFault;
import io.graphenee.core.model.api.GxDataService;
import io.graphenee.core.model.bean.GxAccessKeyBean;
import io.graphenee.core.model.bean.GxNamespaceBean;
import io.graphenee.core.model.bean.GxResourceBean;
import io.graphenee.core.model.bean.GxSecurityGroupBean;
import io.graphenee.core.model.bean.GxSecurityPolicyBean;
import io.graphenee.core.model.bean.GxSecurityPolicyDocumentBean;
import io.graphenee.core.model.entity.GxAccessKey;
import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.core.model.entity.GxResource;
import io.graphenee.core.model.entity.GxSecurityGroup;
import io.graphenee.core.model.entity.GxSecurityPolicy;
import io.graphenee.core.model.entity.GxSecurityPolicyDocument;
import io.graphenee.core.model.entity.GxUserAccount;
import io.graphenee.core.model.jpa.repository.GxAccessKeyRepository;
import io.graphenee.core.model.jpa.repository.GxAccessLogRepository;
import io.graphenee.core.model.jpa.repository.GxNamespaceRepository;
import io.graphenee.core.model.jpa.repository.GxResourceRepository;
import io.graphenee.core.model.jpa.repository.GxSecurityGroupRepository;
import io.graphenee.core.model.jpa.repository.GxSecurityPolicyDocumentRepository;
import io.graphenee.core.model.jpa.repository.GxSecurityPolicyRepository;
import io.graphenee.core.model.jpa.repository.GxUserAccountRepository;
import io.graphenee.security.api.GxSecurityDataService;
import io.graphenee.security.exception.GxPermissionException;

@Service
@ConditionalOnProperty(prefix = "graphenee", name = "modules.enabled", havingValue = "true")
@Transactional
public class GxSecurityDataServiceImpl implements GxSecurityDataService {

	@Autowired
	GxDataService dataService;
	@Autowired
	GxAccessKeyRepository gxAccessKeyRepository;
	@Autowired
	GxResourceRepository gxResourceRepository;
	@Autowired
	GxUserAccountRepository gxUserAccountRepository;
	@Autowired
	GxSecurityGroupRepository securityGroupRepo;
	@Autowired
	GxSecurityPolicyRepository securityPolicyRepo;
	@Autowired
	GxNamespaceRepository namespaceRepo;
	@Autowired
	GxSecurityPolicyDocumentRepository securityPolicyDocumentRepo;
	@Autowired
	GxAccessLogRepository accessLogRepo;
	@Autowired
	GxResourceRepository resourceRepo;

	@Override
	public void access(GxAccessKey gxAccessKey, GxResource gxResource, Timestamp timeStamp) throws GxPermissionException {
		GxUserAccount gxUserAccount = gxUserAccountRepository.findByGxAccessKeysKeyAndGxAccessKeysIsActiveTrueAndIsActiveTrue(gxAccessKey.getKey());
		GxSecurityGroup sg = securityGroupRepo.findByGxAccessKeysKeyAndGxAccessKeysIsActiveTrueAndIsActiveTrue(gxAccessKey.getKey());

		if (gxUserAccount != null || (sg != null && sg.getGxUserAccounts().size() > 0)) {
			if (canAccessResource(gxAccessKey, gxResource, timeStamp)) {
				dataService.log(gxAccessKey, gxResource, timeStamp, AccessTypeStatus.ACCESS.statusCode(), true);
			} else {
				dataService.log(gxAccessKey, gxResource, timeStamp, AccessTypeStatus.ACCESS.statusCode(), false);
				throw new GxPermissionException("access failed");
			}
		} else
			throw new GxPermissionException("access failed");
	}

	@Override
	public void checkIn(GxAccessKey gxAccessKey, GxResource gxResource, Timestamp timeStamp) throws GxPermissionException {
		GxUserAccount gxUserAccount = gxUserAccountRepository.findByGxAccessKeysKeyAndGxAccessKeysIsActiveTrueAndIsActiveTrue(gxAccessKey.getKey());
		GxSecurityGroup sg = securityGroupRepo.findByGxAccessKeysKeyAndGxAccessKeysIsActiveTrueAndIsActiveTrue(gxAccessKey.getKey());

		if (gxUserAccount != null || (sg != null && sg.getGxUserAccounts().size() > 0)) {
			if (canAccessResource(gxAccessKey, gxResource, timeStamp)) {
				dataService.log(gxAccessKey, gxResource, timeStamp, AccessTypeStatus.CHECKIN.statusCode(), true);
			} else {
				dataService.log(gxAccessKey, gxResource, timeStamp, AccessTypeStatus.CHECKIN.statusCode(), false);
				throw new GxPermissionException("check-in failed");
			}
		} else
			throw new GxPermissionException("check-in failed");
	}

	@Override
	public void checkOut(GxAccessKey gxAccessKey, GxResource gxResource, Timestamp timeStamp) throws GxPermissionException {
		GxUserAccount gxUserAccount = gxUserAccountRepository.findByGxAccessKeysKeyAndGxAccessKeysIsActiveTrueAndIsActiveTrue(gxAccessKey.getKey());
		GxSecurityGroup sg = securityGroupRepo.findByGxAccessKeysKeyAndGxAccessKeysIsActiveTrueAndIsActiveTrue(gxAccessKey.getKey());

		if (gxUserAccount != null || (sg != null && sg.getGxUserAccounts().size() > 0)) {
			if (canAccessResource(gxAccessKey, gxResource, timeStamp)) {
				dataService.log(gxAccessKey, gxResource, timeStamp, AccessTypeStatus.CHECKOUT.statusCode(), true);
			} else {
				dataService.log(gxAccessKey, gxResource, timeStamp, AccessTypeStatus.CHECKOUT.statusCode(), false);
				throw new GxPermissionException("check-out failed");
			}
		} else
			throw new GxPermissionException("check-out failed");
	}

	private GxNamespaceBean makeNamespaceBean(GxNamespace entity) {
		GxNamespaceBean bean = new GxNamespaceBean();
		bean.setOid(entity.getOid());
		bean.setNamespace(entity.getNamespace());
		bean.setNamespaceDescription(entity.getNamespaceDescription());
		bean.setIsActive(entity.getIsActive());
		bean.setIsProtected(entity.getIsProtected());
		return bean;
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
		bean.setAccessKeyCollectionFault(BeanCollectionFault.collectionFault(() -> {
			return gxAccessKeyRepository.findAllByGxSecurityPolicysOidEquals(entity.getOid()).stream().map(this::makeAccessKeyBean).collect(Collectors.toList());
		}));
		bean.setSecurityPolicyDocumentCollectionFault(BeanCollectionFault.collectionFault(() -> {
			return securityPolicyDocumentRepo.findAllByGxSecurityPolicyOidEquals(entity.getOid()).stream().map(this::makeSecurityPolicyDocumentBean).collect(Collectors.toList());
		}));
		return bean;
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
		bean.setAccessKeyCollectionFault(BeanCollectionFault.collectionFault(() -> {
			return gxAccessKeyRepository.findAllByGxSecurityPolicysOidEquals(entity.getOid()).stream().map(this::makeAccessKeyBean).collect(Collectors.toList());
		}));
		return bean;
	}

	@Override
	public boolean canAccessResource(GxAccessKey gxAccessKey, GxResource gxResource, Timestamp timeStamp) {
		GxAccessKeyBean accessKeyBean = makeAccessKeyBean(gxAccessKeyRepository.findByKey(gxAccessKey.getKey()));
		GxResourceBean resourceBean = makeResourceBean(gxResourceRepository.findByName(gxResource.getName()));
		return accessKeyBean.canDoAction(resourceBean.getName(), "access");
	}

	private GxResourceBean makeResourceBean(GxResource gxResource) {
		GxResourceBean bean = new GxResourceBean();
		bean.setOid(gxResource.getOid());
		bean.setName(gxResource.getName());
		return bean;
	}

	private GxAccessKeyBean makeAccessKeyBean(GxAccessKey gxAccessKey) {
		GxAccessKeyBean bean = new GxAccessKeyBean();
		bean.setOid(gxAccessKey.getOid());
		bean.setKey(gxAccessKey.getKey());
		bean.setSecret(gxAccessKey.getSecret());
		bean.setIsActive(gxAccessKey.getIsActive());
		bean.setType(gxAccessKey.getType());

		bean.setSecurityGroupCollectionFault(BeanCollectionFault.collectionFault(() -> {
			return securityGroupRepo.findAllByGxAccessKeysOidEquals(gxAccessKey.getOid()).stream().map(this::makeSecurityGroupBean).collect(Collectors.toList());
		}));

		bean.setSecurityPolicyCollectionFault(BeanCollectionFault.collectionFault(() -> {
			return securityPolicyRepo.findAllByGxAccessKeysOidEquals(gxAccessKey.getOid()).stream().map(this::makeSecurityPolicyBean).collect(Collectors.toList());
		}));
		return bean;
	}

}
