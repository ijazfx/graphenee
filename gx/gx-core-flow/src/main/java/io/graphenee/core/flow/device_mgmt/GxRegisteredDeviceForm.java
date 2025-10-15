package io.graphenee.core.flow.device_mgmt;

import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;

import io.graphenee.core.model.entity.GxRegisteredDevice;
import io.graphenee.vaadin.flow.GxAbstractEntityForm;

@SpringComponent
@Scope("prototype")
public class GxRegisteredDeviceForm extends GxAbstractEntityForm<GxRegisteredDevice> {

	private TextField systemName;
	private TextField deviceToken;
	private TextField awsDeviceArn;
	private TextField brand;
	private TextField ownerId;
	private Checkbox isActive;
	private Checkbox isTablet;

	public GxRegisteredDeviceForm() {
		super(GxRegisteredDevice.class);
	}

	@Override
	protected void decorateForm(HasComponents entityForm) {
		systemName = new TextField("System Name");
		systemName.setMaxLength(50);

		deviceToken = new TextField("Device Token");
		deviceToken.setMaxLength(200);

		brand = new TextField("Brand Name");
		brand.setMaxLength(50);

		ownerId = new TextField("Device Owner Id");
		ownerId.setMaxLength(100);

		isActive = new Checkbox("Is Active?");
		isTablet = new Checkbox("Is Tablet?");

		awsDeviceArn = new TextField("AWS Device ARN");
		awsDeviceArn.setMaxLength(1000);

		entityForm.add(systemName, brand, deviceToken, ownerId, isActive, isTablet, awsDeviceArn);
		expand(deviceToken, ownerId, awsDeviceArn);
	}

	@Override
	protected void bindFields(Binder<GxRegisteredDevice> dataBinder) {
		dataBinder.forMemberField(deviceToken).asRequired("Device token is required");
		dataBinder.forMemberField(ownerId).asRequired("Owner id is required");
		dataBinder.forMemberField(systemName).asRequired("System name is required");
	}

	@Override
	protected String formTitle() {
		return "Device Detail";
	}

	@Override
	protected String dialogHeight() {
		return "37.5rem";
	}

	@Override
	protected String dialogWidth() {
		return "50rem";
	}

}
