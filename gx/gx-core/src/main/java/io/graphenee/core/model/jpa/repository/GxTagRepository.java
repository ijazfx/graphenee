package io.graphenee.core.model.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.core.model.entity.GxTag;
import io.graphenee.core.model.jpa.GxJpaRepository;

@Repository
public interface GxTagRepository extends GxJpaRepository<GxTag, Integer>, JpaSpecificationExecutor<GxTag> {

        List<GxTag> findAllByNamespaceOrderByTag(GxNamespace namespace);

}
