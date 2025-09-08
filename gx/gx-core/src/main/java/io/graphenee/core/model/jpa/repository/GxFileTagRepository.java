package io.graphenee.core.model.jpa.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import io.graphenee.core.model.entity.GxFileTag;
import io.graphenee.core.model.jpa.GxJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GxFileTagRepository
        extends GxJpaRepository<GxFileTag, Integer>, JpaSpecificationExecutor<GxFileTag> {

}
