package io.graphenee.core.model.api;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.graphenee.core.model.BeanCollectionFault;
import io.graphenee.core.model.BeanFault;
import io.graphenee.core.model.bean.GxAccountBean;
import io.graphenee.core.model.bean.GxAccountConfigurationBean;
import io.graphenee.core.model.bean.GxAccountTypeBean;
import io.graphenee.core.model.bean.GxBalanceSheetBean;
import io.graphenee.core.model.bean.GxGeneralLedgerBean;
import io.graphenee.core.model.bean.GxIncomeStatementBean;
import io.graphenee.core.model.bean.GxNamespaceBean;
import io.graphenee.core.model.bean.GxNamespacePropertyBean;
import io.graphenee.core.model.bean.GxTransactionBean;
import io.graphenee.core.model.bean.GxTrialBalanceBean;
import io.graphenee.core.model.bean.GxVoucherBean;
import io.graphenee.core.model.entity.GxAccount;
import io.graphenee.core.model.entity.GxAccountConfiguration;
import io.graphenee.core.model.entity.GxAccountType;
import io.graphenee.core.model.entity.GxGeneralLedger;
import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.core.model.entity.GxNamespaceProperty;
import io.graphenee.core.model.entity.GxTransaction;
import io.graphenee.core.model.entity.GxVoucher;
import io.graphenee.core.model.jpa.repository.GxAccountConfigurationRepository;
import io.graphenee.core.model.jpa.repository.GxAccountRepository;
import io.graphenee.core.model.jpa.repository.GxAccountTypeRepository;
import io.graphenee.core.model.jpa.repository.GxJournalVoucherRepository;
import io.graphenee.core.model.jpa.repository.GxNamespacePropertyRepository;
import io.graphenee.core.model.jpa.repository.GxNamespaceRepository;
import io.graphenee.core.model.jpa.repository.GxTransactionRepository;

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

	@Autowired
	GxTransactionRepository transactionRepository;

	@Autowired
	GxJournalVoucherRepository voucherRepository;

	@Autowired
	GxAccountConfigurationRepository accountConfigurationRepository;

	public GxAccountTypeBean makeGxAccountTypeBean(GxAccountType entity) {
		GxAccountTypeBean bean = new GxAccountTypeBean();
		bean.setOid(entity.getOid());
		bean.setTypeCode(entity.getTypeCode());
		bean.setTypeName(entity.getTypeName());
		bean.setAccountNumberSequence(entity.getAccountNumberSequence());

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

	public GxTransactionBean makeGxTransactionBean(GxTransaction entity) {
		GxTransactionBean bean = new GxTransactionBean();
		bean.setOid(entity.getOid());

		if (entity.getAmount() > 0)
			bean.setDebit(entity.getAmount());
		else
			bean.setCredit(Math.abs(entity.getAmount()));

		bean.setDescription(entity.getDescription());
		bean.setTransactionDate(entity.getTransactionDate());
		bean.setIsArchived(entity.getIsArchived());
		if (entity.getGxNamespace() != null) {
			bean.setGxNamespaceBeanFault(BeanFault.beanFault(entity.getGxNamespace().getOid(), oid -> {
				return makeNamespaceBean(namespaceRepository.findOne(oid));
			}));
		}
		if (entity.getGxAccount() != null) {
			bean.setGxAccountBeanFault(BeanFault.beanFault(entity.getGxAccount().getOid(), oid -> {
				return makeGxAccountBean(accountRepository.findOne(oid));
			}));
		}

		return bean;
	}

	public List<GxTransactionBean> makeGxTransactionBean(List<GxTransaction> entities) {
		return entities.stream().map(this::makeGxTransactionBean).collect(Collectors.toList());
	}

	public GxVoucherBean makeGxVoucherBean(GxVoucher entity) {
		GxVoucherBean bean = new GxVoucherBean();
		bean.setOid(entity.getOid());
		bean.setVoucherNumber(entity.getVoucherNumber());
		bean.setVoucherDate(entity.getVoucherDate());
		bean.setDescription(entity.getDescription());
		if (entity.getGxNamespace() != null) {
			bean.setGxNamespaceBeanFault(BeanFault.beanFault(entity.getGxNamespace().getOid(), oid -> {
				return makeNamespaceBean(namespaceRepository.findOne(oid));
			}));
		}
		bean.setGxTransactionBeanCollectionFault(BeanCollectionFault.collectionFault(() -> {
			List<GxTransaction> transactions = transactionRepository.findAllByGxVouchersOidOrderByTransactionDateAsc(entity.getOid());
			return makeGxTransactionBean(transactions);
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

	public GxGeneralLedgerBean makeGxGeneralLedgerBean(GxGeneralLedgerBean bean, GxGeneralLedger entity) {
		bean.setOid(entity.getOid());
		bean.setTransactionDate(entity.getTransactionDate());
		bean.setAccountName(entity.getAccountName());
		bean.setOidAccount(entity.getOidAccount());
		bean.setOidAccountType(entity.getOidAccountType());
		bean.setAccountTypeName(entity.getAccountTypeName());
		bean.setAmount(entity.getAmount());
		bean.setDescription(entity.getDescription());
		bean.setOidVoucher(entity.getOidVoucher());
		return bean;
	}

	public List<GxGeneralLedgerBean> makeGxGeneralLedgerBean(List<GxGeneralLedger> entities, Double previousBalance) {
		List<GxGeneralLedgerBean> generalLedger = new ArrayList<GxGeneralLedgerBean>();
		if (previousBalance == null)
			previousBalance = 0.0;
		Double balance = previousBalance;
		for (GxGeneralLedger entity : entities) {
			GxGeneralLedgerBean bean = new GxGeneralLedgerBean();
			balance = balance + entity.getAmount();
			bean.setBalance(balance);
			generalLedger.add(makeGxGeneralLedgerBean(bean, entity));
		}
		return generalLedger;
	}

	public GxTrialBalanceBean makeGxTrialBalanceBean(Object[] row) {
		GxTrialBalanceBean bean = new GxTrialBalanceBean();
		bean.setAccountName((String) row[0]);
		bean.setOidAccount((Integer) row[1]);
		bean.setOidAccountType((Integer) row[2]);
		bean.setAccountTypeName((String) row[3]);
		Double amount = (Double) row[4];
		if (amount > 0) {
			bean.setDebit(amount);
		} else
			bean.setCredit(Math.abs(amount));

		return bean;
	}

	public GxAccountConfigurationBean makeGxAccountConfigurationBean(GxAccountConfiguration entity) {
		GxAccountConfigurationBean bean = new GxAccountConfigurationBean();
		bean.setOid(entity.getOid());
		bean.setVoucherNumber(entity.getVoucherNumber());
		bean.setFiscalYearStart(entity.getFiscalYearStart());
		bean.setGxNamespaceBeanFault(BeanFault.beanFault(entity.getGxNamespace().getOid(), oid -> {
			return makeNamespaceBean(namespaceRepository.findOne(oid));
		}));
		bean.setFiscalYearStartBeanFault(BeanFault.beanFault(entity.getOid(), oid -> {
			return accountConfigurationRepository.findOne(oid).getFiscalYearStart();
		}));

		return bean;
	}

	public GxBalanceSheetBean makeGxBalanceSheetBean(Object[] row) {
		GxBalanceSheetBean bean = new GxBalanceSheetBean();
		bean.setAccountName((String) row[0]);
		bean.setOidAccount((Integer) row[1]);
		bean.setOidParentAccount((Integer) row[2]);
		bean.setParentAccountName((String) row[3]);
		bean.setOidAccountType((Integer) row[4]);
		bean.setAccountTypeName((String) row[5]);
		bean.setAccountTypeCode((String) row[6]);
		bean.setAmount(Math.abs((Double) row[7]));

		return bean;
	}

	public GxIncomeStatementBean makeGxIncomeStatementBean(Object[] row) {
		GxIncomeStatementBean bean = new GxIncomeStatementBean();
		bean.setAccountName((String) row[0]);
		bean.setOidAccount((Integer) row[1]);
		bean.setOidAccountType((Integer) row[2]);
		bean.setAccountTypeName((String) row[3]);
		bean.setAccountTypeCode((String) row[4]);
		bean.setAmount(Math.abs((Double) row[5]));

		return bean;
	}

}
