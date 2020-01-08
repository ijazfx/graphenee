package io.graphenee.core.model.jpa.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.graphenee.core.model.entity.GxTransaction;
import io.graphenee.core.model.jpa.GxJpaRepository;

public interface GxTransactionRepository extends GxJpaRepository<GxTransaction, Integer> {

	List<GxTransaction> findAllByOrderByTransactionDateAsc();

	List<GxTransaction> findAllByTransactionDateIsBetweenOrderByTransactionDateAsc(Timestamp fromDate, Timestamp toDate);

	List<GxTransaction> findAllByGxNamespaceNamespaceOrderByTransactionDateAsc(String namespace);

	List<GxTransaction> findAllByGxNamespaceNamespaceAndTransactionDateIsBetweenOrderByTransactionDateAsc(String namespace, Timestamp fromDate, Timestamp toDate);

	List<GxTransaction> findAllByGxVouchersOidOrderByTransactionDateAsc(Integer oidVoucher);

	@Query("select sum(t.amount) from GxTransaction t where t.gxAccount.oid in :oids and t.transactionDate >= :startDate and t.transactionDate <= :endDate")
	Double findBalanceByAccountAndChildAccountsAndDateIsBetween(@Param("oids") List<Integer> oids, @Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

}