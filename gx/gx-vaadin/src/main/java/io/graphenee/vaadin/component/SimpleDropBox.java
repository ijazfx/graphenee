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

import com.vaadin.event.Transferable;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.ui.Component;
import com.vaadin.ui.DragAndDropWrapper;

public class SimpleDropBox extends DragAndDropWrapper implements DropHandler {

	SimpleDropBoxDelegate delegate;

	public SimpleDropBox(final Component root, SimpleDropBox.SimpleDropBoxDelegate delegate) {
		super(root);
		this.setDropHandler(this);
		this.delegate = delegate;
	}

	@Override
	public void drop(DragAndDropEvent event) {
		final Transferable transferable = event.getTransferable();
		final Component sourceComponent = transferable.getSourceComponent();
		if (delegate.isDragAndDropWrapperEnable()) {
			if (sourceComponent instanceof DragAndDropWrapper) {
				DragAndDropWrapper wrapper = (DragAndDropWrapper) sourceComponent;
				delegate.onComponentDrop(wrapper.getData());
			}
		}
	}

	@Override
	public AcceptCriterion getAcceptCriterion() {
		return AcceptAll.get();
	}

	public static interface SimpleDropBoxDelegate {

		void onComponentDrop(Object componentData);

		boolean isDragAndDropWrapperEnable();

	}

}
