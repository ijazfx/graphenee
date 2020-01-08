package io.graphenee.core.model.jpa.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.graphenee.core.model.entity.GxGeneralLedger;
import io.graphenee.core.model.jpa.GxJpaRepository;

public interface GxGeneralLedgerRepository extends GxJpaRepository<GxGeneralLedger, Integer> {

	List<GxGeneralLedger> findAllByOidNamespaceOrderByTransactionDateAsc(Integer oidNamespace);

	List<GxGeneralLedger> findAllByOidNamespaceAndTransactionDateIsBetweenOrderByTransactionDateAsc(Integer oidNamespace, Timestamp fromDate, Timestamp toDate);

	List<GxGeneralLedger> findAllByOidAccountAndOidNamespaceAndTransactionDateIsBetweenOrderByTransactionDateAsc(Integer oidAccount, Integer oidNamespace, Timestamp fromDate,
			Timestamp toDate);

	List<GxGeneralLedger> findAllByOidAccountInAndOidNamespaceAndTransactionDateIsBetweenOrderByTransactionDateAsc(List<Integer> oids, Integer oidNamespace, Timestamp fromDate,
			Timestamp toDate);

	@Query("select sum(gl.amount) from GxGeneralLedger gl where gl.oidAccount = :oidAccount and gl.transactionDate < :date")
	Double findBalanceByAccountAndDateIsBefore(@Param("oidAccount") Integer oidAccount, @Param("date") Timestamp date);

	@Modifying
	@Query(value = "REFRESH MATERIALIZED VIEW gx_general_ledger_view;", nativeQuery = true)
	void refreshGxGeneralLedgerView();

	@Query("select sum(gl.amount) from GxGeneralLedger gl where gl.oidAccount in :oids and gl.transactionDate < :date")
	Double findBalanceByAccountAndChildAccountsAndDateIsBefore(@Param("oids") List<Integer> oids, @Param("date") Timestamp date);
}
