package io.graphenee.core.model.jpa.repository;

import java.util.List;

import io.graphenee.core.model.entity.GxPasswordPolicy;
import io.graphenee.core.model.jpa.GxJpaRepository;

public interface GxPasswordPolicyRepository extends GxJpaRepository<GxPasswordPolicy, Integer> {
	List<GxPasswordPolicy> findAllByGxNamespaceNamespace(String namespace);

	GxPasswordPolicy findOneByGxNamespaceNamespaceAndIsActiveTrue(String namespace);

	GxPasswordPolicy findOneByGxNamespaceNamespace(String namespace);

}
