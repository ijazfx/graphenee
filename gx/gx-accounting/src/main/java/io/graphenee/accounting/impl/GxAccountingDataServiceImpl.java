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
import io.graphenee.core.model.bean.GxAccountTypeBean;
import io.graphenee.core.model.bean.GxGeneralLedgerBean;
import io.graphenee.core.model.bean.GxNamespaceBean;
import io.graphenee.core.model.bean.GxTransactionBean;
import io.graphenee.core.model.bean.GxTrialBalanceBean;
import io.graphenee.core.model.bean.GxVoucherBean;
import io.graphenee.core.model.entity.GxAccount;
import io.graphenee.core.model.entity.GxAccountType;
import io.graphenee.core.model.entity.GxGeneralLedger;
import io.graphenee.core.model.entity.GxVoucher;
import io.graphenee.core.model.jpa.repository.GxAccountRepository;
import io.graphenee.core.model.jpa.repository.GxAccountTypeRepository;
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
		generalLedgerRepository.refreshGxGeneralLedgerView();
		trialBalanceRepository.refreshGxTrialBalanceView();
		return bean;
	}

	@Override
	public void delete(GxVoucherBean bean) {
		voucherRepository.deleteById(bean.getOid());
		generalLedgerRepository.refreshGxGeneralLedgerView();
		trialBalanceRepository.refreshGxTrialBalanceView();
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
		Integer month = TRCalendarUtil.getMonth(date);
		Integer year = TRCalendarUtil.getYear(date);
		return trialBalanceRepository.findAllByOidNamespaceAndMonthAndYear(namespaceBean.getOid(), month + 1, year).stream()
				.map(entity -> beanFactory.makeGxTrialBalanceBean(entity)).collect(Collectors.toList());
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

}
