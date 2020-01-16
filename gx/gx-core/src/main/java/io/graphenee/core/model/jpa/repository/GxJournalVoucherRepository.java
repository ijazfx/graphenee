package io.graphenee.core.model.jpa.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.Query;

import io.graphenee.core.model.entity.GxVoucher;
import io.graphenee.core.model.jpa.GxJpaRepository;

public interface GxJournalVoucherRepository extends GxJpaRepository<GxVoucher, Integer> {

	List<GxVoucher> findByOrderByVoucherDateAsc();

	List<GxVoucher> findByVoucherDateIsBetweenOrderByVoucherDateAsc(Timestamp fromDate, Timestamp toDate);

	List<GxVoucher> findByGxNamespaceNamespaceOrderByVoucherDateDesc(String namespace);

	List<GxVoucher> findByGxNamespaceNamespaceAndVoucherDateIsBetweenOrderByVoucherDateDesc(String namespace, Timestamp fromDate, Timestamp toDate);

	GxVoucher findByOidAndGxNamespaceOid(Integer oid, Integer oidNamespace);

	@Query("select coalesce(max(v.oid), 0) from GxVoucher v")
	Integer findMaxVoucherNumber();
}
