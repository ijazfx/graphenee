package io.graphenee.core.model.jpa.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import io.graphenee.core.model.entity.GxRegisteredDevice;
import io.graphenee.core.model.jpa.GxJpaRepository;

@Repository
public interface GxRegisteredDeviceRepository extends GxJpaRepository<GxRegisteredDevice, Integer> {

	List<GxRegisteredDevice> findByGxNamespaceNamespace(String namespace);

	GxRegisteredDevice findByGxNamespaceNamespaceAndDeviceToken(String namespace, String deviceToken);
	
	GxRegisteredDevice findByGxNamespaceNamespaceAndDeviceTokenAndOwnerId(String namespace, String deviceToken, String ownerId);

}
