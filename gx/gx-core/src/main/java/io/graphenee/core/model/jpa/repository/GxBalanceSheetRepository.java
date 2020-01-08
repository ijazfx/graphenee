package io.graphenee.core.model.jpa.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.graphenee.core.model.entity.GxBalanceSheet;
import io.graphenee.core.model.jpa.GxJpaRepository;

public interface GxBalanceSheetRepository extends GxJpaRepository<GxBalanceSheet, Integer> {

	@Query("select b.accountName, b.oidAccount, b.oidParentAccount, b.parentAccountName, b.oidAccountType, b.accountTypeName, b.accountTypeCode, sum(b.amount) as amount from  GxBalanceSheet b where oidNamespace = :oidNamespace and month <= :toDate group by b.accountName, b.oidAccount, b.oidParentAccount, b.parentAccountName, b.oidAccountType, b.accountTypeName, b.accountTypeCode")
	List<Object[]> findBalanceSheetByOidNamespaceAndMonthLessThanEqual(@Param("oidNamespace") Integer oidNamepace, @Param("toDate") Timestamp toDate);

	@Query("select b.accountName, b.oidAccount, b.oidAccountType, b.accountTypeName, b.accountTypeCode, sum(b.amount) as amount from  GxBalanceSheet b where oidNamespace = :oidNamespace and month <= :toDate group by b.accountName, b.oidAccount, b.oidParentAccount, b.parentAccountName, b.oidAccountType, b.accountTypeName, b.accountTypeCode")
	List<Object[]> findIncomeStatementByOidNamespaceAndMonthLessThanEqual(@Param("oidNamespace") Integer oidNamepace, @Param("toDate") Timestamp toDate);

	@Modifying
	@Query(value = "REFRESH MATERIALIZED VIEW gx_balance_sheet_view;", nativeQuery = true)
	void refreshGxBalanceSheetView();
}
