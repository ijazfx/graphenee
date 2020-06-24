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

import io.graphenee.core.model.Fault;
import io.graphenee.core.util.KeyValueWrapper;

public class FaultComboBox extends ComboBox {

	public FaultComboBox() {
		super();
	}

	public FaultComboBox(String caption, Collection<?> options) {
		super(caption, options);
	}

	public FaultComboBox(String caption, Container dataSource) {
		super(caption, dataSource);
	}

	public FaultComboBox(String caption) {
		super(caption);
	}

	@Override
	public String getItemCaption(Object itemId) {
		if (itemId instanceof Fault) {
			if (((Fault) itemId) != null && ((Fault) itemId).getValue() != null) {
				return new KeyValueWrapper(((Fault) itemId).getValue()).stringForKeyPath((String) super.getItemCaptionPropertyId());
			}
		}
		return super.getItemCaption(itemId);
	}

	@Override
	public Object getValue() {
		Fault beanFault = (Fault) super.getValue();
		if (beanFault == null) {
			return null;
		}
		return beanFault;
	}

}
