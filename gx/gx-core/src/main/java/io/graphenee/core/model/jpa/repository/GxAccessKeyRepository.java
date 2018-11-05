package io.graphenee.core.model.jpa.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import io.graphenee.core.model.entity.GxAccessKey;

public interface GxAccessKeyRepository extends JpaRepository<GxAccessKey, Integer> {

	List<GxAccessKey> findAllByGxSecurityPolicysOidEquals(Integer oidSecurityPolicy);

	GxAccessKey findByKey(UUID key);

	List<GxAccessKey> findAllByIsActive(Boolean isActive);

	List<GxAccessKey> findAllByGxSecurityGroupsOidEquals(Integer oidSecurityGroup);

	List<GxAccessKey> findAllByGxUserAccountOidEquals(Integer oidUserAccount);

	List<GxAccessKey> findAllByIsActiveAndGxUserAccountIsNull(Boolean isActive);
}
