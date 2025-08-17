package io.graphenee.core.model.jpa.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import io.graphenee.core.model.entity.GxAuthentication;
import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.core.model.jpa.GxJpaRepository;

@Repository
public interface GxAuthenticationRepository extends GxJpaRepository<GxAuthentication, Integer> {

    List<GxAuthentication> findByNamespaceOrderByName(GxNamespace namespace);

}
