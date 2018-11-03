package io.graphenee.core.model.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.graphenee.core.model.entity.GxResource;

public interface GxResourceRepository extends JpaRepository<GxResource, Integer> {

  GxResource findOneByResourceNameAndGxNamespaceNamespaceAndIsActiveTrue(String resourceName, String namespace);

  List<GxResource> findAllByGxNamespaceNamespace(String namespace);

}
