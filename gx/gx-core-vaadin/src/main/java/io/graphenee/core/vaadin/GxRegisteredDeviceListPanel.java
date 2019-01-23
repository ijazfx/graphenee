package io.graphenee.core.vaadin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.ComboBox;

import io.graphenee.core.model.api.GxDataService;
import io.graphenee.core.model.bean.GxMobileApplicationBean;
import io.graphenee.core.model.bean.GxRegisteredDeviceBean;
import io.graphenee.vaadin.AbstractEntityListPanel;
import io.graphenee.vaadin.TRAbstractForm;
import io.graphenee.vaadin.renderer.BooleanRenderer;

@SpringComponent
@Scope("prototype")
public class GxRegisteredDeviceListPanel extends AbstractEntityListPanel<GxRegisteredDeviceBean> {

	@Autowired
	GxDataService dataService;

	@Autowired
	GxRegisteredDeviceForm editorForm;

	private ComboBox mobileApplicationComboBox;

	private GxMobileApplicationBean selectedMobileApplication;

	public GxRegisteredDeviceListPanel() {
		super(GxRegisteredDeviceBean.class);
	}

	@Override
	protected boolean onSaveEntity(GxRegisteredDeviceBean entity) {
		dataService.createOrUpdate(entity);
		return true;
	}

	@Override
	protected boolean onDeleteEntity(GxRegisteredDeviceBean entity) {
		dataService.delete(entity);
		return true;
	}

	@Override
	protected String panelCaption() {
		return null;
	}

	@Override
	protected List<GxRegisteredDeviceBean> fetchEntities() {
		return dataService.findByMobileApplication(selectedMobileApplication);
	}

	@Override
	protected String[] visibleProperties() {
		return new String[] { "systemName", "brand", "mobileApplicationName", "ownerId", "isTablet", "isActive" };
	}

	@Override
	protected TRAbstractForm<GxRegisteredDeviceBean> editorForm() {
		return editorForm;
	}

	@Override
	protected void addButtonsToToolbar(AbstractOrderedLayout toolbar) {
		mobileApplicationComboBox = new ComboBox("Mobile Application");
		List<GxMobileApplicationBean> gxMobileApplicationBeans = dataService.findMobileApplication();
		mobileApplicationComboBox.addItems(gxMobileApplicationBeans);
		if (gxMobileApplicationBeans != null) {
			mobileApplicationComboBox.setValue(gxMobileApplicationBeans.get(0));
			selectedMobileApplication = (GxMobileApplicationBean) mobileApplicationComboBox.getValue();
		}
		mobileApplicationComboBox.addValueChangeListener(event -> {
			selectedMobileApplication = (GxMobileApplicationBean) event.getProperty().getValue();
			refresh();
		});

		toolbar.addComponent(mobileApplicationComboBox);
		super.addButtonsToToolbar(toolbar);
	}

	@Override
	protected void postBuild() {
		super.postBuild();
		for (com.vaadin.ui.Grid.Column column : entityGrid().getColumns()) {
			if (column.getPropertyId().toString().matches("(isActive)")) {
				column.setRenderer(new BooleanRenderer(event -> {
					GxRegisteredDeviceBean item = (GxRegisteredDeviceBean) event.getItemId();
					item.setIsActive(!item.getIsActive());
					dataService.createOrUpdate(item);
					entityGrid().refreshRow(item);
				}), BooleanRenderer.SWITCH_CONVERTER);
			}
		}
	}

}
