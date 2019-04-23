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

import java.util.Arrays;

import com.vaadin.ui.ComboBox;

@SuppressWarnings("serial")
public class MIntegerRangeField extends ComboBox {

	public MIntegerRangeField(String caption, Integer from, Integer to, Integer stepSize, Integer... startWith) {
		super(caption);
		if (startWith != null) {
			addItems(Arrays.asList(startWith));
		}
		for (int i = from; i <= to; i += stepSize) {
			addItem(i);
		}
	}

	public MIntegerRangeField(String caption, Integer from, Integer to, Integer stepSize) {
		super(caption);
		for (int i = from; i <= to; i += stepSize) {
			addItem(i);
		}
	}

}
