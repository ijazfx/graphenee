package io.graphenee.vaadin.flow.base;

import java.util.Collection;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import io.graphenee.core.model.api.GxDataService;
import io.graphenee.core.model.api.GxRegisteredDeviceDataService;
import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.core.model.entity.GxRegisteredDevice;

@Component
@Scope("prototype")
public class GxRegisteredDeviceList extends GxAbstractEntityLazyList<GxRegisteredDevice> {

    @Autowired
    private GxRegisteredDeviceDataService registeredDeviceDataService;

    @Autowired
    private GxRegisteredDeviceForm form;

    @Autowired
    private GxDataService gxDataService;

    private GxNamespace namespace;

    public GxRegisteredDeviceList() {
        super(GxRegisteredDevice.class);
    }

    @Override
    protected int getTotalCount(GxRegisteredDevice searchEntity) {
        return registeredDeviceDataService.countAll(searchEntity);
    }

    @Override
    protected Stream<GxRegisteredDevice> getData(int pageNumber, int pageSize, GxRegisteredDevice searchEntity) {
        PageRequest request = PageRequest.of(pageNumber, pageSize);
        return registeredDeviceDataService.findRegisteredDevice(searchEntity, request).stream();
    }

    @Override
    protected String[] visibleProperties() {
        return new String[] { "ownerId", "systemName", "brand", "isTablet", "isActive", "deviceToken" };
    }

    @Override
    protected void preEdit(GxRegisteredDevice entity) {
        if (entity.getOid() == null) {
            entity.setGxNamespace(namespace != null ? namespace : gxDataService.findSystemNamespaceEntity());
        }
    }

    @Override
    protected GxAbstractEntityForm<GxRegisteredDevice> getEntityForm(GxRegisteredDevice entity) {
        return form;
    }

    @Override
    protected void onSave(GxRegisteredDevice entity) {
        registeredDeviceDataService.save(entity);
    }

    @Override
    protected void onDelete(Collection<GxRegisteredDevice> entities) {
        registeredDeviceDataService.delete(entities);
    }

    public void initializeWithNamespace(GxNamespace namespace) {
        this.namespace = namespace;
        getSearchEntity().setGxNamespace(namespace);
        refresh();
    }

}
