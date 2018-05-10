package io.graphenee.vaadin.component;

import java.util.Collection;

import com.vaadin.data.Container;
import com.vaadin.ui.ComboBox;

import io.graphenee.core.model.BeanFault;
import io.graphenee.core.util.KeyValueWrapper;

public class BeanFaultComboBox extends ComboBox {

	public BeanFaultComboBox() {
		super();
	}

	public BeanFaultComboBox(String caption, Collection<?> options) {
		super(caption, options);
	}

	public BeanFaultComboBox(String caption, Container dataSource) {
		super(caption, dataSource);
	}

	public BeanFaultComboBox(String caption) {
		super(caption);
	}

	@Override
	public String getItemCaption(Object itemId) {
		if (itemId instanceof BeanFault) {
			if (((BeanFault) itemId) != null && ((BeanFault) itemId).getBean() != null) {
				return new KeyValueWrapper(((BeanFault) itemId).getBean()).stringForKeyPath((String) super.getItemCaptionPropertyId());
			}
		}
		return super.getItemCaption(itemId);
	}

	@Override
	public Object getValue() {
		BeanFault beanFault = (BeanFault) super.getValue();
		if (beanFault == null) {
			return null;
		}
		return beanFault;
	}

}
