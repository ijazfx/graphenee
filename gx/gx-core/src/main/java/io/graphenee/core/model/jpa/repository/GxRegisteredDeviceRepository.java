package io.graphenee.core.model.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import io.graphenee.core.model.entity.GxRegisteredDevice;
import io.graphenee.core.model.jpa.GxJpaRepository;

@Repository
public interface GxRegisteredDeviceRepository extends GxJpaRepository<GxRegisteredDevice, Integer>, JpaSpecificationExecutor<GxRegisteredDevice> {

	List<GxRegisteredDevice> findByNamespaceNamespace(String namespace);

	GxRegisteredDevice findByNamespaceNamespaceAndDeviceToken(String namespace, String deviceToken);

	GxRegisteredDevice findByNamespaceNamespaceAndDeviceTokenAndOwnerId(String namespace, String deviceToken, String ownerId);

}
