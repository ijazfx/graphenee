package io.graphenee.core.model.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.graphenee.core.model.entity.GxPasswordHistory;

public interface GxPasswordHistoryRepository extends JpaRepository<GxPasswordHistory, Integer> {

	List<GxPasswordHistory> findAllByGxUserAccountOidOrderByPasswordDateDesc(Integer oidUserAccount);

	GxPasswordHistory findTop1ByGxUserAccountOidOrderByPasswordDateDesc(Integer oidUserAccount);

}
