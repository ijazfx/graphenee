package io.graphenee.core.model.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import io.graphenee.core.model.entity.GxVoucher;
import io.graphenee.core.model.jpa.GxJpaRepository;

public interface GxVoucherRepository extends GxJpaRepository<GxVoucher, Integer> {

	List<GxVoucher> findByOrderByVoucherDateAsc();

	List<GxVoucher> findByGxNamespaceNamespaceOrderByVoucherDateAsc(String namespace);

	@Query("select coalesce(max(v.oid), 0) from GxVoucher v")
	Integer findMaxVoucherNumber();
}
