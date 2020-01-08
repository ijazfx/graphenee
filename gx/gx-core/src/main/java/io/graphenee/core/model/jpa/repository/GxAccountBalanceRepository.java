package io.graphenee.core.model.jpa.repository;

import io.graphenee.core.model.entity.GxAccountBalance;
import io.graphenee.core.model.jpa.GxJpaRepository;

public interface GxAccountBalanceRepository extends GxJpaRepository<GxAccountBalance, Integer> {

	GxAccountBalance findTop1ByGxAccountOidOrderByFiscalYearDesc(Integer oidAccount);

}
