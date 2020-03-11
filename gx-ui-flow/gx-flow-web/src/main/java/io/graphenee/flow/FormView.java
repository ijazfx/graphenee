package io.graphenee.flow;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import io.graphenee.core.enums.GenderEnum;
import io.graphenee.core.model.bean.GxUserAccountBean;

@Route(value = "form", layout = MainAppLayout.class)
@PageTitle("Form")
@CssImport("styles/form-view.css")
public class FormView extends GxFormView<GxUserAccountBean> {

	private static final long serialVersionUID = 1L;

	private ComboBox<GenderEnum> gender;

	public FormView() {
		super(GxUserAccountBean.class);
	}

	@Override
	protected void configure(FormConfigurator<GxUserAccountBean> fc) {
		gender = new ComboBox<GenderEnum>();
		gender.setItems(GenderEnum.values());

		fc.caption("User Form").editable("firstName", "lastName", "fullNameNative", "email", "username", "password", "isActive", "isLocked", "gender", "isPasswordChangeRequired")
				.onSave(entity -> {
					Notification.show("User account saved successfully!");
				}).onCancel(entity -> {
					Notification.show("Changes reverted successfully!");
				});

		fc.propertyConfigurator("gender").component(gender);
	}

	@Override
	protected GxUserAccountBean entityToEdit() {
		GxUserAccountBean bean = new GxUserAccountBean();
		bean.setFirstName("Farrukh");
		return bean;
	}

}
