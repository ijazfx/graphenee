package io.graphenee.core.model.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.graphenee.core.model.entity.GxDocument;
import io.graphenee.core.model.jpa.GxJpaRepository;

public interface GxDocumentRepository extends GxJpaRepository<GxDocument, Integer>, JpaSpecificationExecutor<GxDocument> {

	@Query(value = "select max(d.versionNo) from GxDocument d where d.document = :parentDocument")
	Integer findMaxVersionByDocument(@Param("parentDocument") GxDocument document);

	@Query(nativeQuery = true, value = "select gd.* from gx_document gd, (select gd2.oid_folder, gd2.oid, rank() over (partition by gd2.oid_document order by gd2.version_no desc) rank_no from gx_document gd2) gdr where gd.oid = gdr.oid and gdr.rank_no = 1 and gdr.oid_folder = :oidFolder and not exists (select 1 from gx_document gd3 where gd3.oid_document = gd.oid) order by gd.name")
	List<GxDocument> findByOidFolder(@Param("oidFolder") Integer oidFolder);

	@Query(nativeQuery = true, value = "select count(gd.*) from gx_document gd, (select gd2.oid_folder, gd2.oid, rank() over (partition by gd2.oid_document order by gd2.version_no desc) rank_no from gx_document gd2) gdr where gd.oid = gdr.oid and gdr.rank_no = 1 and gdr.oid_folder = :oidFolder and not exists (select 1 from gx_document gd3 where gd3.oid_document = gd.oid)")
	Integer countByOidFolder(@Param("oidFolder") Integer oidFolder);

	@Query(nativeQuery = true, value = "select * from gx_document gd  where version_no = (select max (version_no) from gx_document gd2 where gd2.name= gd.name limit 1) and oid_folder = ?1 order  by gd.name")
	List<GxDocument> findByFolder(Integer oidFolder);

}
