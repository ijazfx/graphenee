package io.graphenee.core.model.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.graphenee.core.model.entity.GxPasswordPolicy;

public interface GxPasswordPolicyRepository extends JpaRepository<GxPasswordPolicy, Integer> {
	List<GxPasswordPolicy> findAllByGxNamespaceNamespace(String namespace);

	GxPasswordPolicy findOneByGxNamespaceNamespaceAndIsActiveTrue(String namespace);
}
