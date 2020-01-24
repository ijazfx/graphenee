package io.graphenee.accounting.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.graphenee.accounting.api.GxAccountingDataService;
import io.graphenee.core.enums.AccountType;
import io.graphenee.core.model.api.GxBeanFactory;
import io.graphenee.core.model.api.GxEntityFactory;
import io.graphenee.core.model.bean.GxAccountBean;
import io.graphenee.core.model.bean.GxAccountConfigurationBean;
import io.graphenee.core.model.bean.GxAccountTypeBean;
import io.graphenee.core.model.bean.GxBalanceSheetBean;
import io.graphenee.core.model.bean.GxGeneralLedgerBean;
import io.graphenee.core.model.bean.GxImportChartOfAccountBean;
import io.graphenee.core.model.bean.GxIncomeStatementBean;
import io.graphenee.core.model.bean.GxNamespaceBean;
import io.graphenee.core.model.bean.GxTransactionBean;
import io.graphenee.core.model.bean.GxTrialBalanceBean;
import io.graphenee.core.model.bean.GxVoucherBean;
import io.graphenee.core.model.entity.GxAccount;
import io.graphenee.core.model.entity.GxAccountBalance;
import io.graphenee.core.model.entity.GxAccountConfiguration;
import io.graphenee.core.model.entity.GxAccountType;
import io.graphenee.core.model.entity.GxGeneralLedger;
import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.core.model.entity.GxVoucher;
import io.graphenee.core.model.jpa.repository.GxAccountBalanceRepository;
import io.graphenee.core.model.jpa.repository.GxAccountConfigurationRepository;
import io.graphenee.core.model.jpa.repository.GxAccountRepository;
import io.graphenee.core.model.jpa.repository.GxAccountTypeRepository;
import io.graphenee.core.model.jpa.repository.GxBalanceSheetRepository;
import io.graphenee.core.model.jpa.repository.GxGeneralLedgerRepository;
import io.graphenee.core.model.jpa.repository.GxJournalVoucherRepository;
import io.graphenee.core.model.jpa.repository.GxNamespaceRepository;
import io.graphenee.core.model.jpa.repository.GxTransactionRepository;
import io.graphenee.core.model.jpa.repository.GxTrialBalanceRepository;
import io.graphenee.core.util.TRCalendarUtil;

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

	@Autowired
	GxTransactionRepository transactionRepository;

	@Autowired
	GxJournalVoucherRepository voucherRepository;

	@Autowired
	GxGeneralLedgerRepository generalLedgerRepository;

	@Autowired
	GxTrialBalanceRepository trialBalanceRepository;

	@Autowired
	GxAccountConfigurationRepository accountConfigurationRepository;

	@Autowired
	GxAccountBalanceRepository accountBalanceRepository;

	@Autowired
	GxBalanceSheetRepository balanceSheetRepository;

	@Autowired
	GxNamespaceRepository namespaceRepository;

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
		refreshAllView();
	}

	@Override
	public GxAccountBean createOrUpdate(GxAccountBean bean) {
		GxAccount entity = entityFactory.makeGxAccountEntity(bean);
		accountRepository.save(entity);
		bean.setOid(entity.getOid());
		refreshAllView();
		return bean;
	}

	@Override
	public List<GxAccountBean> findAllAccounts() {
		return accountRepository.findAll().stream().map(entity -> beanFactory.makeGxAccountBean(entity)).collect(Collectors.toList());
	}

	@Override
	public List<GxAccountBean> findAllAccountsByNamespace(GxNamespaceBean namespaceBean) {
		return accountRepository.findAllByGxNamespaceNamespaceOrderByAccountCodeAsc(namespaceBean.getNamespace()).stream().map(entity -> beanFactory.makeGxAccountBean(entity))
				.collect(Collectors.toList());
	}

	@Override
	public void delete(GxAccountBean bean) {
		accountRepository.deleteById(bean.getOid());
	}

	@Override
	public GxAccountBean findByAccountNumberAndNamespace(String accountCode, GxNamespaceBean namespaceBean) {
		GxAccount entity = accountRepository.findByGxNamespaceNamespaceAndAccountCode(namespaceBean.getNamespace(), accountCode);
		if (entity != null)
			return beanFactory.makeGxAccountBean(entity);
		return null;
	}

	@Override
	public GxAccountBean findByAccountNumber(String accountCode) {
		return beanFactory.makeGxAccountBean(accountRepository.findByAccountCode(accountCode));
	}

	@Override
	public List<GxAccountBean> findAllAccountsByAccountType(GxAccountTypeBean bean) {
		return accountRepository.findAllByGxAccountTypeOid(bean.getOid()).stream().map(entity -> beanFactory.makeGxAccountBean(entity)).collect(Collectors.toList());
	}

	@Override
	public List<GxAccountBean> findAllAccountsByNamespaceAndAccountType(GxNamespaceBean namespaceBean, GxAccountTypeBean bean) {
		List<GxAccountBean> accountList = accountRepository.findAllByGxNamespaceNamespaceAndGxAccountTypeOidOrderByAccountCodeAsc(namespaceBean.getNamespace(), bean.getOid())
				.stream().map(entity -> beanFactory.makeGxAccountBean(entity)).collect(Collectors.toList());

		Map<GxAccountBean, GxAccountBean> accountMap = new HashMap<GxAccountBean, GxAccountBean>();
		accountList.forEach(account -> {
			GxAccountBean parent = account.getGxParentAccountBeanFault() != null ? account.getGxParentAccountBeanFault().getBean() : null;
			accountMap.put(account, parent);
		});

		List<GxAccountBean> sortedAccountList = new ArrayList<GxAccountBean>();

		accountMap.forEach((child, parent) -> {
			if (parent == null) {
				sortedAccountList.addAll(buildSubAccountHierarchy(child, new ArrayList<GxAccountBean>(), accountMap));
			}
		});

		return sortedAccountList;
	}

	private List<GxAccountBean> buildSubAccountHierarchy(GxAccountBean parent, List<GxAccountBean> accountList, Map<GxAccountBean, GxAccountBean> accountMap) {
		if (parent != null) {
			accountList.add(parent);
		}

		List<GxAccountBean> children = accountMap.entrySet().stream().filter(p -> parent.equals(p.getValue())).map(Map.Entry::getKey).collect(Collectors.toList());

		for (GxAccountBean child : children) {
			buildSubAccountHierarchy(child, accountList, accountMap);
		}

		return accountList;
	}

	@Override
	public List<GxTransactionBean> findAllTransactionsOrderByDateAsc() {
		return transactionRepository.findAllByOrderByTransactionDateAsc().stream().map(entity -> beanFactory.makeGxTransactionBean(entity)).collect(Collectors.toList());
	}

	@Override
	public List<GxTransactionBean> findAllTransactionsByNamespaceOrderByDateAsc(GxNamespaceBean namespaceBean) {
		return transactionRepository.findAllByGxNamespaceNamespaceOrderByTransactionDateAsc(namespaceBean.getNamespace()).stream()
				.map(entity -> beanFactory.makeGxTransactionBean(entity)).collect(Collectors.toList());
	}

	@Override
	public List<GxVoucherBean> findAllVouchersOrderByVoucherDateAsc() {
		return voucherRepository.findByOrderByVoucherDateAsc().stream().map(entity -> beanFactory.makeGxVoucherBean(entity)).collect(Collectors.toList());
	}

	@Override
	public List<GxVoucherBean> findAllVouchersByNamespaceOrderByVoucherDateDesc(GxNamespaceBean namespaceBean) {
		return voucherRepository.findByGxNamespaceNamespaceOrderByVoucherDateDesc(namespaceBean.getNamespace()).stream().map(entity -> beanFactory.makeGxVoucherBean(entity))
				.collect(Collectors.toList());
	}

	@Override
	public List<GxVoucherBean> findAllVouchersByNamespaceAndDateRangeOrderByVoucherDateDesc(GxNamespaceBean namespaceBean, Timestamp fromDate, Timestamp toDate) {
		return voucherRepository.findByGxNamespaceNamespaceAndVoucherDateIsBetweenOrderByVoucherDateDesc(namespaceBean.getNamespace(), fromDate, toDate).stream()
				.map(entity -> beanFactory.makeGxVoucherBean(entity)).collect(Collectors.toList());
	}

	@Override
	public List<GxVoucherBean> findAllVouchersByDateRangeOrderByVoucherDateAsc(Timestamp fromDate, Timestamp toDate) {
		return voucherRepository.findByVoucherDateIsBetweenOrderByVoucherDateAsc(fromDate, toDate).stream().map(entity -> beanFactory.makeGxVoucherBean(entity))
				.collect(Collectors.toList());
	}

	@Override
	public GxVoucherBean createOrUpdate(GxVoucherBean bean) {
		GxVoucher entity = entityFactory.makeGxVoucherEntity(bean);
		voucherRepository.save(entity);
		bean.setOid(entity.getOid());
		refreshAllView();
		return bean;
	}

	private void refreshAllView() {
		generalLedgerRepository.refreshGxGeneralLedgerView();
		trialBalanceRepository.refreshGxTrialBalanceView();
		balanceSheetRepository.refreshGxBalanceSheetView();
	}

	@Override
	public void delete(GxVoucherBean bean) {
		voucherRepository.deleteById(bean.getOid());
		refreshAllView();
	}

	@Override
	public List<GxGeneralLedgerBean> findAllByAccountAndNamespaceAndDateRangeOrderByTransactionDateAsc(GxAccountBean accountBean, GxNamespaceBean namespaceBean, Timestamp fromDate,
			Timestamp toDate) {
		Double previousBalance = findAccountBalanceByAccountAndDateIsBefore(accountBean, fromDate);

		Timestamp startDate = TRCalendarUtil.startOfDayAsTimestamp(fromDate);
		Timestamp endDate = TRCalendarUtil.endOfDayAsTimestamp(toDate);

		List<GxGeneralLedger> ledgerEntries = generalLedgerRepository.findAllByOidAccountAndOidNamespaceAndTransactionDateIsBetweenOrderByTransactionDateAsc(accountBean.getOid(),
				namespaceBean.getOid(), startDate, endDate);

		return beanFactory.makeGxGeneralLedgerBean(ledgerEntries, previousBalance);
	}

	@Override
	public Map<String, List<GxGeneralLedgerBean>> findAllByAccountAndNamespaceAndDateRangeOrderByTransactionDateAscGroupByAccountName(GxAccountBean accountBean,
			GxNamespaceBean namespaceBean, Timestamp fromDate, Timestamp toDate) {
		Double previousBalance = findAccountBalanceByAccountAndDateIsBefore(accountBean, fromDate);

		Timestamp startDate = TRCalendarUtil.startOfDayAsTimestamp(fromDate);
		Timestamp endDate = TRCalendarUtil.endOfDayAsTimestamp(toDate);

		List<GxGeneralLedger> ledgerEntries = generalLedgerRepository.findAllByOidAccountAndOidNamespaceAndTransactionDateIsBetweenOrderByTransactionDateAsc(accountBean.getOid(),
				namespaceBean.getOid(), startDate, endDate);

		return beanFactory.makeGxGeneralLedgerBean(ledgerEntries, previousBalance).stream().collect(Collectors.groupingBy(GxGeneralLedgerBean::getAccountName));
	}

	@Override
	public Double findAccountBalanceByAccountAndDateIsBefore(GxAccountBean accountBean, Timestamp date) {
		return generalLedgerRepository.findBalanceByAccountAndDateIsBefore(accountBean.getOid(), date);
	}

	@Override
	public List<GxTrialBalanceBean> findAllByMonthAndYearAndNamespace(Timestamp date, GxNamespaceBean namespaceBean) {
		Timestamp month = new Timestamp(TRCalendarUtil.endOfMonth(date).getTime());
		List<Object[]> rows = trialBalanceRepository.findAllByOidNamespaceAndMonthLessThanEqual(namespaceBean.getOid(), month);
		return rows.stream().map(entity -> beanFactory.makeGxTrialBalanceBean(entity)).collect(Collectors.toList());
	}

	@Override
	public List<GxGeneralLedgerBean> findAllByAccountAndChildAccountsAndNamespaceAndDateRangeOrderByTransactionDateAsc(GxAccountBean accountBean, GxNamespaceBean namespaceBean,
			Timestamp fromDate, Timestamp toDate) {

		List<Integer> oids = accountBean.getAllChildAccounts().stream().mapToInt(GxAccountBean::getOid).boxed().collect(Collectors.toList());
		oids.add(accountBean.getOid());

		Double previousBalance = findAccountBalanceByAccountAndChildAccountsAndDateIsBefore(oids, fromDate);

		Timestamp startDate = TRCalendarUtil.startOfDayAsTimestamp(fromDate);
		Timestamp endDate = TRCalendarUtil.endOfDayAsTimestamp(toDate);

		List<GxGeneralLedger> ledgerEntries = generalLedgerRepository.findAllByOidAccountInAndOidNamespaceAndTransactionDateIsBetweenOrderByTransactionDateAsc(oids,
				namespaceBean.getOid(), startDate, endDate);

		return beanFactory.makeGxGeneralLedgerBean(ledgerEntries, previousBalance);
	}

	@Override
	public Double findAccountBalanceByAccountAndChildAccountsAndDateIsBefore(List<Integer> oids, Timestamp date) {
		return generalLedgerRepository.findBalanceByAccountAndChildAccountsAndDateIsBefore(oids, date);
	}

	@Override
	public GxAccountConfigurationBean findAccountConfigurationByNamespace(GxNamespaceBean namespaceBean) {
		GxAccountConfiguration entity = accountConfigurationRepository.findTop1ByGxNamespaceOid(namespaceBean.getOid());
		if (entity != null)
			return beanFactory.makeGxAccountConfigurationBean(entity);
		return null;
	}

	@Override
	public GxAccountConfigurationBean createOrUpdate(GxAccountConfigurationBean bean) {
		GxAccountConfiguration entity = entityFactory.makeGxAccountConfigurationEntity(bean);
		accountConfigurationRepository.save(entity);
		bean.setOid(entity.getOid());
		return bean;
	}

	@Override
	public Boolean closeYear(GxNamespaceBean namespaceBean) {
		GxAccountConfigurationBean accountConfigurationBean = findAccountConfigurationByNamespace(namespaceBean);
		List<GxAccountBean> accounts = findAllAccountsByNamespace(namespaceBean);
		if (!accounts.isEmpty()) {
			for (GxAccountBean accountBean : accounts) {
				GxAccountBalance entity = entityFactory.makeGxAccountBalanceEntity(accountBean, accountConfigurationBean);
				accountBalanceRepository.save(entity);
			}
			accountConfigurationBean.setFiscalYearStart(new Timestamp(TRCalendarUtil.addMonthsToDate(accountConfigurationBean.getFiscalYearStart(), 12).getTime()));
			GxAccountConfiguration accountConfiguration = entityFactory.makeGxAccountConfigurationEntity(accountConfigurationBean);
			accountConfigurationRepository.save(accountConfiguration);
			return true;
		}
		return false;
	}

	@Override
	public List<GxBalanceSheetBean> findBalanceSheetByDateAndNamespace(Timestamp toDate, GxNamespaceBean namespaceBean) {
		Timestamp month = new Timestamp(TRCalendarUtil.endOfMonth(toDate).getTime());
		List<Object[]> rows = balanceSheetRepository.findBalanceSheetByOidNamespaceAndMonthLessThanEqual(namespaceBean.getOid(), month);

		return rows.stream().map(row -> beanFactory.makeGxBalanceSheetBean(row)).collect(Collectors.toList());
	}

	@Override
	public List<GxIncomeStatementBean> findIncomeStatementByDateAndNamespace(Timestamp toDate, GxNamespaceBean namespaceBean) {
		Timestamp month = new Timestamp(TRCalendarUtil.endOfMonth(toDate).getTime());
		List<Object[]> rows = balanceSheetRepository.findIncomeStatementByOidNamespaceAndMonthLessThanEqual(namespaceBean.getOid(), month);

		return rows.stream().map(row -> beanFactory.makeGxIncomeStatementBean(row)).collect(Collectors.toList());
	}

	@Override
	public Map<String, List<GxGeneralLedgerBean>> findAllByAccountAndChildAccountsAndNamespaceAndDateRangeGroupByAccountOrderByTransactionDateAsc(GxAccountBean accountBean,
			GxNamespaceBean namespaceBean, Timestamp fromDate, Timestamp toDate) {
		List<Integer> oids = accountBean.getAllChildAccounts().stream().mapToInt(GxAccountBean::getOid).boxed().collect(Collectors.toList());
		oids.add(accountBean.getOid());

		Double previousBalance = findAccountBalanceByAccountAndChildAccountsAndDateIsBefore(oids, fromDate);

		Timestamp startDate = TRCalendarUtil.startOfDayAsTimestamp(fromDate);
		Timestamp endDate = TRCalendarUtil.endOfDayAsTimestamp(toDate);

		List<GxGeneralLedgerBean> generalLedgers = beanFactory.makeGxGeneralLedgerBean(
				generalLedgerRepository.findAllByOidAccountInAndOidNamespaceAndTransactionDateIsBetweenOrderByTransactionDateAsc(oids, namespaceBean.getOid(), startDate, endDate),
				previousBalance);

		return generalLedgers.stream().collect(Collectors.groupingBy(GxGeneralLedgerBean::getAccountName));
	}

	@Override
	public Map<String, List<GxGeneralLedgerBean>> findAllByNamespaceAndDateRangeOrderByTransactionDateAsc(GxNamespaceBean namespaceBean, Timestamp fromDate, Timestamp toDate) {
		List<GxAccountBean> accounts = findAllAccountsByNamespace(namespaceBean);
		List<GxGeneralLedgerBean> generalLedgers = new ArrayList<GxGeneralLedgerBean>();
		if (accounts != null) {
			accounts.forEach(account -> {
				generalLedgers.addAll(findAllByAccountAndNamespaceAndDateRangeOrderByTransactionDateAsc(account, namespaceBean, fromDate, toDate));
			});
		}

		Map<String, List<GxGeneralLedgerBean>> ledgerMap = new TreeMap<String, List<GxGeneralLedgerBean>>((Comparator<String>) (o1, o2) -> o1.compareTo(o2));
		ledgerMap.putAll(generalLedgers.stream().collect(Collectors.groupingBy(entity -> entity.getAccountName())));

		return ledgerMap;
	}

	@Override
	public Double findNetIncomeByDateAndNamespace(Timestamp toDate, GxNamespaceBean namespaceBean) {
		List<GxIncomeStatementBean> incomeStatementList = findIncomeStatementByDateAndNamespace(toDate, namespaceBean);

		Double incomesTotalAmount = incomeStatementList.stream().filter(entity -> entity.getAccountTypeCode().equals(AccountType.INCOME.typeCode()))
				.mapToDouble(GxIncomeStatementBean::getAmount).sum();

		Double expensesTotalAmount = incomeStatementList.stream().filter(entity -> entity.getAccountTypeCode().equals(AccountType.EXPENSE.typeCode()))
				.mapToDouble(GxIncomeStatementBean::getAmount).sum();

		return incomesTotalAmount - expensesTotalAmount;
	}

	@Override
	public List<GxTransactionBean> findAllTransactionsByDateRangeOrderByDateAsc(Timestamp fromDate, Timestamp toDate) {
		return transactionRepository.findAllByTransactionDateIsBetweenOrderByTransactionDateAsc(fromDate, toDate).stream().map(entity -> beanFactory.makeGxTransactionBean(entity))
				.collect(Collectors.toList());
	}

	@Override
	public List<GxTransactionBean> findAllTransactionsByNamespaceAndDateRangeOrderByDateAsc(GxNamespaceBean namespaceBean, Timestamp fromDate, Timestamp toDate) {
		return transactionRepository.findAllByGxNamespaceNamespaceAndTransactionDateIsBetweenOrderByTransactionDateAsc(namespaceBean.getNamespace(), fromDate, toDate).stream()
				.map(entity -> beanFactory.makeGxTransactionBean(entity)).collect(Collectors.toList());
	}

	@Override
	public GxVoucherBean findByOidAndNamespace(Integer oid, GxNamespaceBean namespaceBean) {
		return beanFactory.makeGxVoucherBean(voucherRepository.findByOidAndGxNamespaceOid(oid, namespaceBean.getOid()));
	}

	@Override
	public void importAccounts(Map<GxAccountBean, GxAccountBean> accountMap, GxImportChartOfAccountBean importBean) {
		accountMap.forEach((child, parent) -> {
			GxNamespace namespace = namespaceRepository.findOne(importBean.getNamespaceBean().getOid());
			if (parent == null) {
				GxAccountType accountType = accountTypeRepository.findFirstByTypeNameIgnoreCase(child.getAccountType());
				GxAccount found = accountRepository.findByGxNamespaceNamespaceAndAccountCode(namespace.getNamespace(), child.getAccountCode());
				if (accountType != null && found == null) {
					GxAccount account = accountRepository.save(entityFactory.makeGxAccountEntity(child, accountType, namespace, null));
					GxAccountBalance accountBalance = entityFactory.makeGxAccountBalanceEntity(account, child.getClosingBalance(), importBean.getYear());
					accountBalanceRepository.save(accountBalance);
				}
			} else {
				GxAccount existingParentAccount = accountRepository.findByGxNamespaceNamespaceAndAccountCode(namespace.getNamespace(), parent.getAccountCode());
				if (existingParentAccount != null) {
					GxAccountType accountType = accountTypeRepository.findFirstByTypeNameIgnoreCase(child.getAccountType());
					GxAccount foundChild = accountRepository.findByGxNamespaceNamespaceAndAccountCode(namespace.getNamespace(), child.getAccountCode());
					if (accountType != null && foundChild == null) {
						GxAccount account = accountRepository.save(entityFactory.makeGxAccountEntity(child, accountType, namespace, existingParentAccount));
						GxAccountBalance accountBalance = entityFactory.makeGxAccountBalanceEntity(account, child.getClosingBalance(), importBean.getYear());
						accountBalanceRepository.save(accountBalance);
					}
				} else {
					GxAccountType parentAccountType = accountTypeRepository.findFirstByTypeNameIgnoreCase(parent.getAccountType());
					if (parentAccountType != null) {
						GxAccount parentAccount = accountRepository.save(entityFactory.makeGxAccountEntity(parent, parentAccountType, namespace, null));
						GxAccountBalance parentAccountBalance = entityFactory.makeGxAccountBalanceEntity(parentAccount, parent.getClosingBalance(), importBean.getYear());
						accountBalanceRepository.save(parentAccountBalance);

						GxAccountType accountType = accountTypeRepository.findFirstByTypeNameIgnoreCase(child.getAccountType());
						GxAccount foundChild = accountRepository.findByGxNamespaceNamespaceAndAccountCode(namespace.getNamespace(), child.getAccountCode());
						if (accountType != null && foundChild == null) {
							GxAccount account = accountRepository.save(entityFactory.makeGxAccountEntity(child, accountType, namespace, parentAccount));
							GxAccountBalance accountBalance = entityFactory.makeGxAccountBalanceEntity(account, child.getClosingBalance(), importBean.getYear());
							accountBalanceRepository.save(accountBalance);
						}
					}
				}
			}
		});
	}

}
