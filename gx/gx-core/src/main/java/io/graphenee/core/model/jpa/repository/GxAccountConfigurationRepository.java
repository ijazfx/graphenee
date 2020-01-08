package io.graphenee.core.model.jpa.repository;

import io.graphenee.core.model.entity.GxAccountConfiguration;
import io.graphenee.core.model.jpa.GxJpaRepository;

public interface GxAccountConfigurationRepository extends GxJpaRepository<GxAccountConfiguration, Integer> {

	GxAccountConfiguration findTop1ByGxNamespaceOid(Integer oidNamespace);

}
