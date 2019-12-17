package io.graphenee.core.model.api;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.graphenee.core.model.BeanCollectionFault;
import io.graphenee.core.model.BeanFault;
import io.graphenee.core.model.bean.GxAccountBean;
import io.graphenee.core.model.bean.GxAccountTypeBean;
import io.graphenee.core.model.bean.GxNamespaceBean;
import io.graphenee.core.model.bean.GxNamespacePropertyBean;
import io.graphenee.core.model.entity.GxAccount;
import io.graphenee.core.model.entity.GxAccountType;
import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.core.model.entity.GxNamespaceProperty;
import io.graphenee.core.model.jpa.repository.GxAccountRepository;
import io.graphenee.core.model.jpa.repository.GxAccountTypeRepository;
import io.graphenee.core.model.jpa.repository.GxNamespacePropertyRepository;
import io.graphenee.core.model.jpa.repository.GxNamespaceRepository;

@Service
public class GxBeanFactory {

	@Autowired
	GxAccountTypeRepository accountTypeRepository;

	@Autowired
	GxNamespaceRepository namespaceRepository;

	@Autowired
	GxNamespacePropertyRepository namespacePropertyRepository;

	@Autowired
	GxAccountRepository accountRepository;

	public GxAccountTypeBean makeGxAccountTypeBean(GxAccountType entity) {
		GxAccountTypeBean bean = new GxAccountTypeBean();
		bean.setOid(entity.getOid());
		bean.setTypeCode(entity.getTypeCode());
		bean.setTypeName(entity.getTypeName());

		return bean;
	}

	public GxAccountBean makeGxAccountBean(GxAccount entity) {
		GxAccountBean bean = new GxAccountBean();
		bean.setOid(entity.getOid());
		bean.setAccountCode(entity.getAccountCode());
		bean.setAccountName(entity.getAccountName());
		if (entity.getGxAccountType() != null) {
			bean.setGxAccountTypeBeanFault(BeanFault.beanFault(entity.getGxAccountType().getOid(), oid -> {
				return makeGxAccountTypeBean(accountTypeRepository.findOne(oid));
			}));
		}
		if (entity.getGxNamespace() != null) {
			bean.setGxNamespaceBeanFault(BeanFault.beanFault(entity.getGxNamespace().getOid(), oid -> {
				return makeNamespaceBean(namespaceRepository.findOne(oid));
			}));
		}
		if (entity.getGxParentAccount() != null) {
			bean.setGxParentAccountBeanFault(BeanFault.beanFault(entity.getGxParentAccount().getOid(), oid -> {
				return makeGxAccountBean(accountRepository.findOne(oid));
			}));
		}
		bean.setGxChildAccountBeanCollectionFault(BeanCollectionFault.collectionFault(() -> {
			return accountRepository.findAllByGxParentAccountOid(bean.getOid()).stream().map(this::makeGxAccountBean).collect(Collectors.toList());
		}));

		return bean;
	}

	public GxNamespaceBean makeNamespaceBean(GxNamespace entity) {
		GxNamespaceBean bean = new GxNamespaceBean();
		bean.setOid(entity.getOid());
		bean.setNamespace(entity.getNamespace());
		bean.setNamespaceDescription(entity.getNamespaceDescription());
		bean.setIsActive(entity.getIsActive());
		bean.setIsProtected(entity.getIsProtected());
		bean.setNamespacePropertyBeanCollectionFault(BeanCollectionFault.collectionFault(() -> {
			return namespacePropertyRepository.findAllByGxNamespaceOidOrderByPropertyKey(bean.getOid()).stream().map(this::makeGxNamespacePropertyBean)
					.collect(Collectors.toList());
		}));

		return bean;
	}

	public GxNamespacePropertyBean makeGxNamespacePropertyBean(GxNamespaceProperty entity) {
		GxNamespacePropertyBean bean = new GxNamespacePropertyBean();
		bean.setOid(entity.getOid());
		bean.setPropertyDefaultValue(entity.getPropertyDefaultValue());
		bean.setPropertyKey(entity.getPropertyKey());
		bean.setPropertyValue(entity.getPropertyValue());
		bean.setNamespaceFault(BeanFault.beanFault(entity.getGxNamespace().getOid(), oid -> {
			return makeNamespaceBean(namespaceRepository.findOne(oid));
		}));
		return bean;
	}

}
