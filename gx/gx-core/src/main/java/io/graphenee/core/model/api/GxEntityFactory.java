package io.graphenee.core.model.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.graphenee.core.model.bean.GxAccountBean;
import io.graphenee.core.model.bean.GxAccountTypeBean;
import io.graphenee.core.model.entity.GxAccount;
import io.graphenee.core.model.entity.GxAccountType;
import io.graphenee.core.model.jpa.repository.GxAccountRepository;
import io.graphenee.core.model.jpa.repository.GxAccountTypeRepository;
import io.graphenee.core.model.jpa.repository.GxNamespaceRepository;

@Service
public class GxEntityFactory {

	@Autowired
	GxAccountTypeRepository accountTypeRepository;

	@Autowired
	GxAccountRepository accountRepository;

	@Autowired
	GxNamespaceRepository namespaceRepository;

	public GxAccountType makeGxAccountTypeEntity(GxAccountTypeBean bean) {
		GxAccountType entity = null;
		if (bean.getOid() != null)
			entity = accountTypeRepository.findOne(bean.getOid());
		else
			entity = new GxAccountType();
		entity.setTypeCode(bean.getTypeCode());
		entity.setTypeName(bean.getTypeName());
		return entity;
	}

	public GxAccount makeGxAccountEntity(GxAccountBean bean) {
		GxAccount entity = null;
		if (bean.getOid() != null)
			entity = accountRepository.findOne(bean.getOid());
		else
			entity = new GxAccount();
		entity.setAccountCode(bean.getAccountCode());
		entity.setAccountName(bean.getAccountName());
		if (bean.getGxNamespaceBeanFault() != null) {
			entity.setGxNamespace(namespaceRepository.findOne(bean.getGxNamespaceBeanFault().getOid()));
		}
		if (bean.getGxAccountTypeBeanFault() != null) {
			entity.setGxAccountType(accountTypeRepository.findOne(bean.getGxAccountTypeBeanFault().getOid()));
		}
		if (bean.getGxParentAccountBeanFault() != null) {
			entity.setGxParentAccount(accountRepository.findOne(bean.getGxParentAccountBeanFault().getOid()));
		}
		return entity;
	}
}
