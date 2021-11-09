package io.graphenee.vaadin.flow.device_mgmt;

import java.util.Collection;
import java.util.stream.Stream;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.data.binder.Binder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import io.graphenee.core.model.api.GxRegisteredDeviceDataService;
import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.core.model.entity.GxRegisteredDevice;
import io.graphenee.core.model.jpa.repository.GxNamespaceRepository;
import io.graphenee.vaadin.flow.base.GxAbstractEntityForm;
import io.graphenee.vaadin.flow.base.GxAbstractEntityLazyList;

@Component
@Scope("prototype")
public class GxRegisteredDeviceList extends GxAbstractEntityLazyList<GxRegisteredDevice> {

    @Autowired
    private GxRegisteredDeviceDataService registeredDeviceDataService;

    @Autowired
    private GxRegisteredDeviceForm form;

    @Autowired
    private GxNamespaceRepository namespaceRepo;

    public GxRegisteredDeviceList() {
        super(GxRegisteredDevice.class);
    }

    @Override
    protected int getTotalCount(GxRegisteredDevice searchEntity) {
        if(searchEntity.getGxNamespace() == null)
            return 0;
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
            entity.setGxNamespace(getSearchEntity().getGxNamespace());
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
        getSearchEntity().setGxNamespace(namespace);
        refresh();
    }

    @Override
    protected void decorateSearchForm(FormLayout searchForm, Binder<GxRegisteredDevice> searchBinder) {
        ComboBox<GxNamespace> namespace = new ComboBox<>("Namespace");
        namespace.setItemLabelGenerator(GxNamespace::getNamespace);
        namespace.setItems(namespaceRepo.findAll(Sort.by("namespace")));

        searchForm.add(namespace);

        searchBinder.bind(namespace, "gxNamespace");
        searchBinder.addValueChangeListener(vcl -> {
            refresh();
        });
    }

}
