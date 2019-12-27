package io.graphenee.core.model.jpa.repository;

import java.util.List;

import io.graphenee.core.model.entity.GxTransaction;
import io.graphenee.core.model.jpa.GxJpaRepository;

public interface GxTransactionRepository extends GxJpaRepository<GxTransaction, Integer> {

	List<GxTransaction> findAllByOrderByTransactionDateAsc();

	List<GxTransaction> findAllByGxNamespaceNamespaceOrderByTransactionDateAsc(String namespace);

	List<GxTransaction> findAllByGxVouchersOidOrderByTransactionDateAsc(Integer oidVoucher);

}