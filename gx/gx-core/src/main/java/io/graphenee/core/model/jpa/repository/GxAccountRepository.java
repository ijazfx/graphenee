package io.graphenee.core.model.jpa.repository;

import java.util.List;

import io.graphenee.core.model.entity.GxAccount;
import io.graphenee.core.model.jpa.GxJpaRepository;

public interface GxAccountRepository extends GxJpaRepository<GxAccount, Integer> {

	List<GxAccount> findAllByGxNamespaceNamespace(String namespace);

	GxAccount findByGxNamespaceNamespaceAndAccountCode(String namespace, Integer accountCode);

	GxAccount findByAccountCode(Integer accountCode);

	List<GxAccount> findAllByGxParentAccountOid(Integer oidAccount);

}
