package io.graphenee.core.model.jpa.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import io.graphenee.core.model.entity.GxDocumentType;
import io.graphenee.core.model.jpa.GxJpaRepository;

public interface GxDocumentTypeRepository extends GxJpaRepository<GxDocumentType, Integer>, JpaSpecificationExecutor<GxDocumentType> {

}
