package io.graphenee.core.model.jpa.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.graphenee.core.model.entity.GxTrialBalance;
import io.graphenee.core.model.jpa.GxJpaRepository;

public interface GxTrialBalanceRepository extends GxJpaRepository<GxTrialBalance, Integer> {

	@Query("select tb.accountCode, tb.accountName, tb.oidAccount, tb.oidAccountType, tb.accountTypeName, sum(tb.amount) as amount from GxTrialBalance tb where tb.oidNamespace = :oidNamespace and tb.month <= :month group by tb.accountCode, tb.accountName, tb.oidAccount,tb.oidAccountType, tb.accountTypeName order by tb.accountName asc")
	List<Object[]> findAllByOidNamespaceAndMonthLessThanEqual(@Param("oidNamespace") Integer oidNamespace, @Param("month") Timestamp month);

	@Modifying
	@Query(value = "REFRESH MATERIALIZED VIEW gx_trial_balance_view;", nativeQuery = true)
	void refreshGxTrialBalanceView();
}
