package io.graphenee.core.model.jpa.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import io.graphenee.core.model.entity.GxFolder;
import io.graphenee.core.model.jpa.GxJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GxFolderRepository extends GxJpaRepository<GxFolder, Integer>, JpaSpecificationExecutor<GxFolder> {

    @Query(value = "SELECT COUNT(*) FROM gx_folder gd WHERE (:name IS NULL OR gd.name ILIKE '%' || :name || '%') AND (:oidFolder IS NULL OR oid_folder = :oidFolder) AND (:oidTags IS NULL OR gd.oid IN (SELECT oid_folder FROM gx_file_tag_folder_join WHERE oid_tag IN (:oidTags)))",
            nativeQuery = true)
    Integer countByNameFolderAndTag(@Param("oidFolder") Integer oidFolder,
                                    @Param("oidTags") List<Integer> oidTags,
                                    @Param("name") String name);

    @Query(value = "SELECT * FROM gx_folder gd WHERE (:name IS NULL OR gd.name ILIKE '%' || :name || '%') AND (:oidFolder IS NULL OR oid_folder = :oidFolder) AND (:oidTags IS NULL OR gd.oid IN (SELECT oid_folder FROM gx_file_tag_folder_join WHERE oid_tag IN (:oidTags)))",
            nativeQuery = true)
    List<GxFolder> findByNameFolderAndTag(@Param("oidFolder") Integer oidFolder,
                                    @Param("oidTags") List<Integer> oidTags,
                                    @Param("name") String name);

}
