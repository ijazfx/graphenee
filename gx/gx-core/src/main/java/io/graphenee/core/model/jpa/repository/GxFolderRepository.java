package io.graphenee.core.model.jpa.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import io.graphenee.core.model.entity.GxFolder;
import io.graphenee.core.model.jpa.GxJpaRepository;

public interface GxFolderRepository extends GxJpaRepository<GxFolder, Integer>, JpaSpecificationExecutor<GxFolder> {

}
