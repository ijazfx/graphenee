package io.graphenee.core.model.jpa.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import io.graphenee.core.model.entity.GxFolder;
import io.graphenee.core.model.jpa.GxJpaRepository;

public interface GxFolderRepository extends GxJpaRepository<GxFolder, Integer>, JpaSpecificationExecutor<GxFolder> {

    List<GxFolder> findAllByIsArchivedTrueAndUpdatedAtBefore(LocalDateTime sevenDaysAgo);

}
