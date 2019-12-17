package io.graphenee.accounting.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.graphenee.accounting.api.GxAccountingDataService;
import io.graphenee.core.model.api.GxBeanFactory;
import io.graphenee.core.model.api.GxEntityFactory;
import io.graphenee.core.model.bean.GxAccountBean;
import io.graphenee.core.model.bean.GxAccountTypeBean;
import io.graphenee.core.model.bean.GxNamespaceBean;
import io.graphenee.core.model.entity.GxAccount;
import io.graphenee.core.model.entity.GxAccountType;
import io.graphenee.core.model.jpa.repository.GxAccountRepository;
import io.graphenee.core.model.jpa.repository.GxAccountTypeRepository;

@Transactional
@Service
public class GxAccountingDataServiceImpl implements GxAccountingDataService {

	@Autowired
	GxBeanFactory beanFactory;

	@Autowired
	GxEntityFactory entityFactory;

	@Autowired
	GxAccountTypeRepository accountTypeRepository;

	@Autowired
	GxAccountRepository accountRepository;

	@Override
	public List<GxAccountTypeBean> findAllAccountTypes() {
		return accountTypeRepository.findAllByOrderByTypeName().stream().map(entity -> beanFactory.makeGxAccountTypeBean(entity)).collect(Collectors.toList());
	}

	@Override
	public GxAccountTypeBean createOrUpdate(GxAccountTypeBean bean) {
		GxAccountType entity = entityFactory.makeGxAccountTypeEntity(bean);
		accountTypeRepository.save(entity);
		bean.setOid(entity.getOid());
		return bean;
	}

	@Override
	public void delete(GxAccountTypeBean bean) {
		accountTypeRepository.deleteById(bean.getOid());
	}

	@Override
	public GxAccountBean createOrUpdate(GxAccountBean bean) {
		GxAccount entity = entityFactory.makeGxAccountEntity(bean);
		accountRepository.save(entity);
		bean.setOid(entity.getOid());
		return bean;
	}

	@Override
	public List<GxAccountBean> findAllAccounts() {
		return accountRepository.findAll().stream().map(entity -> beanFactory.makeGxAccountBean(entity)).collect(Collectors.toList());
	}

	@Override
	public List<GxAccountBean> findAllAccountsByNamespace(GxNamespaceBean namespaceBean) {
		return accountRepository.findAllByGxNamespaceNamespace(namespaceBean.getNamespace()).stream().map(entity -> beanFactory.makeGxAccountBean(entity))
				.collect(Collectors.toList());
	}

	@Override
	public void delete(GxAccountBean bean) {
		accountRepository.deleteById(bean.getOid());
	}

	@Override
	public GxAccountBean findByAccountNumberAndNamespace(Integer accountCode, GxNamespaceBean namespaceBean) {
		return beanFactory.makeGxAccountBean(accountRepository.findByGxNamespaceNamespaceAndAccountCode(namespaceBean.getNamespace(), accountCode));
	}

	@Override
	public GxAccountBean findByAccountNumber(Integer accountCode) {
		return beanFactory.makeGxAccountBean(accountRepository.findByAccountCode(accountCode));
	}

}
