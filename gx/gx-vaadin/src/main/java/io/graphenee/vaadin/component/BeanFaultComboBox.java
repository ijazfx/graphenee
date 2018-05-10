/*******************************************************************************
 * Copyright (c) 2016, 2018 Farrukh Ijaz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
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
