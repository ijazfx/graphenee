package io.graphenee.core.model.jpa.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import io.graphenee.core.model.entity.GxEntityCallback;
import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.core.model.jpa.GxJpaRepository;

@Repository
public interface GxEntityCallbackRepository extends GxJpaRepository<GxEntityCallback, Integer> {

    List<GxEntityCallback> findByNamespaceOrderByName(GxNamespace namespace);

}
