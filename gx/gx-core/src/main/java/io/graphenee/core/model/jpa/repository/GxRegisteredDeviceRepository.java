package io.graphenee.core.model.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.graphenee.core.model.entity.GxRegisteredDevice;

@Repository
public interface GxRegisteredDeviceRepository extends JpaRepository<GxRegisteredDevice, Integer> {

	List<GxRegisteredDevice> findByGxNamespaceNamespace(String namespace);

	GxRegisteredDevice findByGxNamespaceNamespaceAndDeviceToken(String namespace, String deviceToken);

}
