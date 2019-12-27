package io.graphenee.core.model.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import io.graphenee.core.model.entity.GxTrialBalance;
import io.graphenee.core.model.jpa.GxJpaRepository;

public interface GxTrialBalanceRepository extends GxJpaRepository<GxTrialBalance, Integer> {

	List<GxTrialBalance> findAllByOidNamespaceAndMonthAndYear(Integer oidNamespace, Integer month, Integer year);

	@Modifying
	@Query(value = "REFRESH MATERIALIZED VIEW gx_trial_balance_view;", nativeQuery = true)
	void refreshGxTrialBalanceView();
}
