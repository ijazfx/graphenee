package io.graphenee.core.model.jpa.repository;

import java.util.List;

import io.graphenee.core.model.entity.GxVoucher;
import io.graphenee.core.model.jpa.GxJpaRepository;

public interface GxVoucherRepository extends GxJpaRepository<GxVoucher, Integer> {

	//	List<GxVoucher> findAllByOrderByVoucherDateAsc();
	List<GxVoucher> findByOrderByVoucherDateAsc();

	//	List<GxVoucher> findAllByGxNamespaceNamespaceOrderByVoucherDateAsc(String namespace);
	List<GxVoucher> findByGxNamespaceNamespaceOrderByVoucherDateAsc(String namespace);

}
