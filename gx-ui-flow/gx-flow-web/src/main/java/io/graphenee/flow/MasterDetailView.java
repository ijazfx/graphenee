package io.graphenee.flow;

import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import io.graphenee.core.model.api.GxDataService;
import io.graphenee.core.model.bean.GxSupportedLocaleBean;
import io.graphenee.core.model.bean.GxTermBean;
import io.graphenee.flow.GxMasterDetailView.FormConfigurator.FormPosition;
import io.graphenee.flow.converter.BeanFaultToBeanConverter;

@Route(value = "master-detail", layout = MainAppLayout.class)
@PageTitle("Master-Detail")
@CssImport("styles/master-detail-view.css")
public class MasterDetailView extends GxMasterDetailView<GxTermBean> {

	private static final long serialVersionUID = 1L;

	private ComboBox<GxSupportedLocaleBean> supportedLocaleFault;

	@Autowired
	GxDataService service;

	public MasterDetailView() {
		super(GxTermBean.class);
	}

	@Override
	protected void configure(GridConfigurator<GxTermBean> gc) {
		gc.caption("Terms");
		gc.visible("termKey", "termSingular", "termPlural");
		gc.propertyCaption("termKey", "Key");
		gc.propertyCaption("termSingular", "Singular");
		gc.propertyCaption("termPlural", "Plural");
	}

	@Override
	protected void configure(FormConfigurator<GxTermBean> fc) {
		fc.position(FormPosition.POPUP);
		fc.caption("Term Detail");
		fc.editable("termKey", "termSingular", "termPlural", "supportedLocaleFault");
		fc.required("supportedLocaleFault", "termKey", "termSingular");
		fc.propertyCaption("supportedLocaleFault", "Locale");
		fc.propertyCaption("termKey", "Key");
		fc.propertyCaption("termSingular", "Singular");
		fc.propertyCaption("termPlural", "Plural");
		supportedLocaleFault = new ComboBox<GxSupportedLocaleBean>();
		fc.propertyConfigurator("supportedLocaleFault").component(supportedLocaleFault).converter(new BeanFaultToBeanConverter());

		fc.saveCaption("OK");
		fc.onSave(entity -> {
			service.save(entity);
		});
	}

	@Override
	protected Collection<GxTermBean> fetchEntities() {
		return service.findTermByLocale(Locale.ENGLISH);
	}

	@Override
	protected void preBinding(GxTermBean bean) {
		supportedLocaleFault.setItems(service.findSupportedLocale());
	}

}
