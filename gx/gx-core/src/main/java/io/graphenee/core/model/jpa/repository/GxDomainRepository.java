package io.graphenee.core.model.jpa.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import io.graphenee.core.model.entity.GxDomain;
import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.core.model.jpa.GxJpaRepository;

@Repository
public interface GxDomainRepository extends GxJpaRepository<GxDomain, Integer>, JpaSpecificationExecutor<GxDomain> {

        List<GxDomain> findAllByNamespaceOrderByDns(GxNamespace namespace);

        Optional<GxDomain> findByDnsAndIsActiveTrueAndIsVerifiedTrue(String dns);

}
