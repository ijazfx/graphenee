package io.graphenee.core.model.api;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.graphenee.core.model.bean.GxAccountBean;
import io.graphenee.core.model.bean.GxAccountTypeBean;
import io.graphenee.core.model.bean.GxTransactionBean;
import io.graphenee.core.model.bean.GxVoucherBean;
import io.graphenee.core.model.entity.GxAccount;
import io.graphenee.core.model.entity.GxAccountType;
import io.graphenee.core.model.entity.GxTransaction;
import io.graphenee.core.model.entity.GxVoucher;
import io.graphenee.core.model.jpa.repository.GxAccountRepository;
import io.graphenee.core.model.jpa.repository.GxAccountTypeRepository;
import io.graphenee.core.model.jpa.repository.GxNamespaceRepository;
import io.graphenee.core.model.jpa.repository.GxTransactionRepository;
import io.graphenee.core.model.jpa.repository.GxVoucherRepository;

@Service
public class GxEntityFactory {

	@Autowired
	GxAccountTypeRepository accountTypeRepository;

	@Autowired
	GxAccountRepository accountRepository;

	@Autowired
	GxNamespaceRepository namespaceRepository;

	@Autowired
	GxTransactionRepository transactionRepository;

	@Autowired
	GxVoucherRepository voucherRepository;

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

	public GxTransaction makeGxTransactionEntity(GxTransactionBean bean) {
		GxTransaction entity = null;
		if (bean.getOid() != null)
			entity = transactionRepository.findOne(bean.getOid());
		else
			entity = new GxTransaction();

		entity.setAmount(bean.getAmount());

		entity.setTransactionDate(bean.getTransactionDate());
		entity.setDescription(bean.getDescription());
		if (bean.getGxNamespaceBeanFault() != null) {
			entity.setGxNamespace(namespaceRepository.findOne(bean.getGxNamespaceBeanFault().getOid()));
		}
		if (bean.getGxAccountBeanFault() != null) {
			entity.setGxAccount(accountRepository.findOne(bean.getGxAccountBeanFault().getOid()));
		}

		return entity;
	}

	public GxVoucher makeGxVoucherEntity(GxVoucherBean bean) {
		GxVoucher entity = null;
		if (bean.getOid() != null)
			entity = voucherRepository.findOne(bean.getOid());
		else
			entity = new GxVoucher();
		entity.setVoucherDate(bean.getVoucherDate());
		entity.setVoucherNumber(bean.getVoucherNumber());
		entity.setDescription(bean.getDescription());
		if (bean.getGxNamespaceBeanFault() != null) {
			entity.setGxNamespace(namespaceRepository.findOne(bean.getGxNamespaceBeanFault().getOid()));
		}
		if (bean.getGxTransactionBeanCollectionFault().isModified()) {
			Set<Integer> oids = bean.getGxTransactionBeanCollectionFault().getBeansRemoved().stream().mapToInt(GxTransactionBean::getOid).boxed().collect(Collectors.toSet());
			for (Integer oid : oids) {
				entity.getGxTransactions().removeIf(t -> {
					return t.getOid().intValue() == oid;
				});
			}
			for (GxTransactionBean added : bean.getGxTransactionBeanCollectionFault().getBeansAdded()) {
				entity.getGxTransactions().add(makeGxTransactionEntity(added));
			}
			for (GxTransactionBean updated : bean.getGxTransactionBeanCollectionFault().getBeansUpdated()) {
				makeGxTransactionEntity(updated);
			}
		}

		return entity;
	}
}
