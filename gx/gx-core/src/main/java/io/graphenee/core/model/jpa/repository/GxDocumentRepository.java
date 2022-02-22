package io.graphenee.core.model.jpa.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import io.graphenee.core.model.entity.GxDocument;
import io.graphenee.core.model.jpa.GxJpaRepository;

public interface GxDocumentRepository extends GxJpaRepository<GxDocument, Integer>, JpaSpecificationExecutor<GxDocument> {

	List<GxDocument> findAllByFolderIsNull(Sort sort);

}
