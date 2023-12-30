package io.graphenee.core.model.jpa;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GxJpaSpecRepository<ENTITY, ID> extends GxJpaRepository<ENTITY, ID>, JpaSpecificationExecutor<ENTITY> {

}
