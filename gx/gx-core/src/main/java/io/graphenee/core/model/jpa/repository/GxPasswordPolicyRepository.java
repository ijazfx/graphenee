package io.graphenee.core.model.jpa.repository;

import java.util.List;

import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.core.model.entity.GxPasswordPolicy;
import io.graphenee.core.model.jpa.GxJpaRepository;

public interface GxPasswordPolicyRepository extends GxJpaRepository<GxPasswordPolicy, Integer> {

	List<GxPasswordPolicy> findAllByNamespace(GxNamespace namespace);

	GxPasswordPolicy findOneByNamespaceAndIsActiveTrue(GxNamespace namespace);

	GxPasswordPolicy findOneByNamespace(GxNamespace namespace);

}
