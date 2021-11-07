package io.graphenee.core.model.api;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Pageable;

import io.graphenee.core.exception.RegisterDeviceFailedException;
import io.graphenee.core.exception.UnregisterDeviceFailedException;
import io.graphenee.core.model.entity.GxRegisteredDevice;

public interface GxRegisteredDeviceDataService {

    GxRegisteredDevice save(GxRegisteredDevice entity);

    Integer countAll(GxRegisteredDevice searchEntity);

    List<GxRegisteredDevice> findRegisteredDevice(GxRegisteredDevice searchEntity, Pageable pageable);

    void delete(Collection<GxRegisteredDevice> entities);

    GxRegisteredDevice registerDevice(String namespace, String uniqueId, String systemName, String brand, boolean isTablet, String ownerId) throws RegisterDeviceFailedException;

    void unregisterDevice(String namespace, String uniqueId) throws UnregisterDeviceFailedException;

}
