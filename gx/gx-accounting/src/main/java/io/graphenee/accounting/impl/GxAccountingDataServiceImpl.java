package io.graphenee.accounting.impl;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.graphenee.accounting.api.GxAccountingDataService;
import io.graphenee.core.model.api.GxBeanFactory;
import io.graphenee.core.model.api.GxEntityFactory;
import io.graphenee.core.model.bean.GxAccountBean;
import io.graphenee.core.model.bean.GxAccountConfigurationBean;
import io.graphenee.core.model.bean.GxAccountTypeBean;
import io.graphenee.core.model.bean.GxBalanceSheetBean;
import io.graphenee.core.model.bean.GxGeneralLedgerBean;
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
import io.graphenee.core.model.entity.GxVoucher;
import io.graphenee.core.model.jpa.repository.GxAccountBalanceRepository;
import io.graphenee.core.model.jpa.repository.GxAccountConfigurationRepository;
import io.graphenee.core.model.jpa.repository.GxAccountRepository;
import io.graphenee.core.model.jpa.repository.GxAccountTypeRepository;
import io.graphenee.core.model.jpa.repository.GxBalanceSheetRepository;
import io.graphenee.core.model.jpa.repository.GxGeneralLedgerRepository;
import io.graphenee.core.model.jpa.repository.GxTransactionRepository;
import io.graphenee.core.model.jpa.repository.GxTrialBalanceRepository;
import io.graphenee.core.model.jpa.repository.GxVoucherRepository;
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
	GxVoucherRepository voucherRepository;

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
		return accountRepository.findAllByGxNamespaceNamespace(namespaceBean.getNamespace()).stream().map(entity -> beanFactory.makeGxAccountBean(entity))
				.collect(Collectors.toList());
	}

	@Override
	public void delete(GxAccountBean bean) {
		accountRepository.deleteById(bean.getOid());
	}

	@Override
	public GxAccountBean findByAccountNumberAndNamespace(Integer accountCode, GxNamespaceBean namespaceBean) {
		GxAccount entity = accountRepository.findByGxNamespaceNamespaceAndAccountCode(namespaceBean.getNamespace(), accountCode);
		if (entity != null)
			return beanFactory.makeGxAccountBean(entity);
		return null;
	}

	@Override
	public GxAccountBean findByAccountNumber(Integer accountCode) {
		return beanFactory.makeGxAccountBean(accountRepository.findByAccountCode(accountCode));
	}

	@Override
	public List<GxAccountBean> findAllAccountsByAccountType(GxAccountTypeBean bean) {
		return accountRepository.findAllByGxAccountTypeOid(bean.getOid()).stream().map(entity -> beanFactory.makeGxAccountBean(entity)).collect(Collectors.toList());
	}

	@Override
	public List<GxAccountBean> findAllAccountsByNamespaceAndAccountType(GxNamespaceBean namespaceBean, GxAccountTypeBean bean) {
		return accountRepository.findAllByGxNamespaceNamespaceAndGxAccountTypeOid(namespaceBean.getNamespace(), bean.getOid()).stream()
				.map(entity -> beanFactory.makeGxAccountBean(entity)).collect(Collectors.toList());
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
	public List<GxVoucherBean> findAllVouchersByNamespaceOrderByVoucherDateAsc(GxNamespaceBean namespaceBean) {
		return voucherRepository.findByGxNamespaceNamespaceOrderByVoucherDateAsc(namespaceBean.getNamespace()).stream().map(entity -> beanFactory.makeGxVoucherBean(entity))
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
	public void closeYear(List<GxAccountBean> accounts, GxNamespaceBean namespaceBean) {
		GxAccountConfigurationBean accountConfigurationBean = findAccountConfigurationByNamespace(namespaceBean);

		for (GxAccountBean accountBean : accounts) {
			GxAccountBalance entity = entityFactory.makeGxAccountBalanceEntity(accountBean, accountConfigurationBean);
			//			accountBalanceRepository.save(entity);
		}
	}

	@Override
	public List<GxBalanceSheetBean> findBalanceSheetByDateAndNamespace(Timestamp toDate, GxNamespaceBean namespaceBean) {
		List<Object[]> rows = balanceSheetRepository.findBalanceSheetByOidNamespaceAndMonthLessThanEqual(namespaceBean.getOid(), toDate);

		return rows.stream().map(row -> beanFactory.makeGxBalanceSheetBean(row)).collect(Collectors.toList());
	}

	@Override
	public List<GxIncomeStatementBean> findIncomeStatementByDateAndNamespace(Timestamp toDate, GxNamespaceBean namespaceBean) {
		List<Object[]> rows = balanceSheetRepository.findIncomeStatementByOidNamespaceAndMonthLessThanEqual(namespaceBean.getOid(), toDate);

		return rows.stream().map(row -> beanFactory.makeGxIncomeStatementBean(row)).collect(Collectors.toList());
	}

}
