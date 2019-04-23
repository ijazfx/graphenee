package io.graphenee.core.model.jpa.repository;

import java.util.List;

import io.graphenee.core.model.entity.GxPasswordHistory;
import io.graphenee.core.model.jpa.GxJpaRepository;

public interface GxPasswordHistoryRepository extends GxJpaRepository<GxPasswordHistory, Integer> {

	List<GxPasswordHistory> findAllByGxUserAccountOidOrderByPasswordDateDesc(Integer oidUserAccount);

}
