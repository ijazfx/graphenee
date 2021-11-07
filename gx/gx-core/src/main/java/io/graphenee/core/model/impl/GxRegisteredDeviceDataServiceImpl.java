package io.graphenee.core.model.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.graphenee.core.exception.RegisterDeviceFailedException;
import io.graphenee.core.exception.UnregisterDeviceFailedException;
import io.graphenee.core.model.api.GxRegisteredDeviceDataService;
import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.core.model.entity.GxRegisteredDevice;
import io.graphenee.core.model.jpa.repository.GxNamespaceRepository;
import io.graphenee.core.model.jpa.repository.GxRegisteredDeviceRepository;
import io.graphenee.core.util.JpaSpecificationBuilder;

@Service
@Transactional
public class GxRegisteredDeviceDataServiceImpl implements GxRegisteredDeviceDataService {

    @Autowired
    private GxRegisteredDeviceRepository repository;

    @Autowired
    private GxNamespaceRepository namespaceRepo;

    @Override
    public Integer countAll(GxRegisteredDevice searchEntity) {
        return (int) repository.count(makeRegisteredDeviceSpec(searchEntity));
    }

    @Override
    public List<GxRegisteredDevice> findRegisteredDevice(GxRegisteredDevice searchEntity, Pageable pageable) {
        return repository.findAll(makeRegisteredDeviceSpec(searchEntity), pageable).toList();
    }

    private Specification<GxRegisteredDevice> makeRegisteredDeviceSpec(GxRegisteredDevice searchEntity) {
        JpaSpecificationBuilder<GxRegisteredDevice> sb = JpaSpecificationBuilder.get();
        if (searchEntity.getGxNamespace() != null) {
            sb.eq("gxNamespace", searchEntity.getGxNamespace());
        }
        sb.like("systemName", searchEntity.getSystemName());
        sb.like("deviceToken", searchEntity.getDeviceToken());
        sb.like("brand", searchEntity.getBrand());
        sb.like("ownerId", searchEntity.getOwnerId());
        return sb.build();
    }

    @Override
    public void delete(Collection<GxRegisteredDevice> entities) {
        repository.delete(entities);
    }

    @Override
    public GxRegisteredDevice save(GxRegisteredDevice entity) {
        entity = repository.save(entity);
        return entity;
    }

    @Override
    public GxRegisteredDevice registerDevice(String namespace, String deviceToken, String systemName, String brand, boolean isTablet, String ownerId)
            throws RegisterDeviceFailedException {
        GxNamespace namespaceEntity = namespaceRepo.findByNamespace(namespace);
        if (namespaceEntity == null)
            throw new RegisterDeviceFailedException("Namespace " + namespace + " does not exist.");
        GxRegisteredDevice device = repository.findByGxNamespaceNamespaceAndDeviceTokenAndOwnerId(namespace, deviceToken, ownerId);
        if (device != null)
            throw new RegisterDeviceFailedException("Device with deviceToken " + deviceToken + "and ownerId " + ownerId + " for namespace " + namespace + " already registered");
        GxRegisteredDevice entity = new GxRegisteredDevice();
        entity.setBrand(brand);
        entity.setIsActive(true);
        entity.setIsTablet(isTablet);
        entity.setOwnerId(ownerId);
        entity.setSystemName(systemName);
        entity.setDeviceToken(deviceToken);
        entity.setGxNamespace(namespaceRepo.findOne(namespaceEntity.getOid()));
        entity = repository.save(entity);
        return entity;
    }

    @Override
    public void unregisterDevice(String namespace, String deviceToken) throws UnregisterDeviceFailedException {
        GxRegisteredDevice device = repository.findByGxNamespaceNamespaceAndDeviceToken(namespace, deviceToken);
        if (device == null)
            throw new UnregisterDeviceFailedException("Device with deviceToken " + deviceToken + " for namespace " + namespace + " does not exist.");
        repository.deleteById(device.getOid());
    }

}
