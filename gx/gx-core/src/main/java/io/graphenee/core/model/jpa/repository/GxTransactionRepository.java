package io.graphenee.core.model.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.graphenee.core.model.entity.GxTransaction;
import io.graphenee.core.model.jpa.GxJpaRepository;

public interface GxTransactionRepository extends GxJpaRepository<GxTransaction, Integer> {

	List<GxTransaction> findAllByOrderByTransactionDateAsc();

	List<GxTransaction> findAllByGxNamespaceNamespaceOrderByTransactionDateAsc(String namespace);

	List<GxTransaction> findAllByGxVouchersOidOrderByTransactionDateAsc(Integer oidVoucher);

	@Query("select sum(t.amount) from GxTransaction t where t.gxAccount.oid = :oidAccount")
	Double findAccountBalanceByAccount(@Param("oidAccount") Integer oidAccount);

}