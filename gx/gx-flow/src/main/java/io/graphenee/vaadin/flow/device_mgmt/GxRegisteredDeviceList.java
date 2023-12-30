package io.graphenee.vaadin.flow.device_mgmt;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.QuerySortOrder;

import io.graphenee.core.model.api.GxDataService;
import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.core.model.entity.GxRegisteredDevice;
import io.graphenee.core.model.jpa.repository.GxNamespaceRepository;
import io.graphenee.core.model.jpa.repository.GxRegisteredDeviceRepository;
import io.graphenee.vaadin.flow.base.GxAbstractEntityForm;
import io.graphenee.vaadin.flow.base.GxAbstractEntityLazyList;
import io.graphenee.vaadin.flow.base.GxFormLayout;

@SuppressWarnings("serial")
@Component
@Scope("prototype")
public class GxRegisteredDeviceList extends GxAbstractEntityLazyList<GxRegisteredDevice> {

	@Autowired
	private GxDataService dataService;

	@Autowired
	private GxRegisteredDeviceRepository repo;

	@Autowired
	private GxRegisteredDeviceForm form;

	@Autowired
	private GxNamespaceRepository namespaceRepo;

	public GxRegisteredDeviceList() {
		super(GxRegisteredDevice.class);
	}

	@Override
	protected int getTotalCount(GxRegisteredDevice searchEntity) {
		if (searchEntity.getNamespace() == null)
			return 0;
		return (int) repo.count();
	}

	@Override
	protected Stream<GxRegisteredDevice> getData(int pageNumber, int pageSize, GxRegisteredDevice searchEntity, List<QuerySortOrder> sortOrders) {
		PageRequest request = PageRequest.of(pageNumber, pageSize, createSort(sortOrders, Sort.by("deviceToken")));
		return dataService.findRegisteredDevice(searchEntity, request).stream();
	}

	@Override
	protected String[] visibleProperties() {
		return new String[] { "ownerId", "systemName", "brand", "isTablet", "isActive", "deviceToken" };
	}

	@Override
	protected void preEdit(GxRegisteredDevice entity) {
		if (entity.getOid() == null) {
			entity.setNamespace(getSearchEntity().getNamespace());
		}
	}

	@Override
	protected GxAbstractEntityForm<GxRegisteredDevice> getEntityForm(GxRegisteredDevice entity) {
		return form;
	}

	@Override
	protected void onSave(GxRegisteredDevice entity) {
		dataService.save(entity);
	}

	@Override
	protected void onDelete(Collection<GxRegisteredDevice> entities) {
		entities.forEach(e -> {
			dataService.delete(e);
		});
	}

	public void initializeWithNamespace(GxNamespace namespace) {
		getSearchEntity().setNamespace(namespace);
		refresh();
	}

	@Override
	protected void decorateSearchForm(GxFormLayout searchForm, Binder<GxRegisteredDevice> searchBinder) {
		ComboBox<GxNamespace> namespace = new ComboBox<>("Namespace");
		namespace.setItemLabelGenerator(GxNamespace::getNamespace);
		namespace.setItems(namespaceRepo.findAll(Sort.by("namespace")));

		searchForm.add(namespace);

		searchBinder.bind(namespace, "namespace");
		searchBinder.addValueChangeListener(vcl -> {
			refresh();
		});
	}

}
