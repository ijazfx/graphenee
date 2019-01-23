package io.graphenee.core.model.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.graphenee.core.model.entity.GxMobileApplication;

@Repository
public interface GxMobileApplicationRepository extends JpaRepository<GxMobileApplication, Integer> {

	List<GxMobileApplication> findByGxNamespaceOid(Integer oidNamespace);

	GxMobileApplication findByApplicationNameAndGxNamespaceNamespace(String applicationName, String namespace);

	@Modifying
	@Query("Delete from GxMobileApplication m where m.oid = :oidMobileApplication and m.gxNamespace.oid = :oidNamespace")
	void deleteByOidMobileApplicationAndOidNamespace(@Param("oidMobileApplication") Integer oidMobileApplication, @Param("oidNamespace") Integer oidNamespace);
}
