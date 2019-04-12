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

import java.nio.file.Path;

import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.server.Resource;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;

import server.droporchoose.UploadComponent;

public class DropOrChoose extends CustomField<Resource> {

	UploadComponent uploadComponent;
	DropOrChooseDelegate delegate;
	private String componentHeight;
	private String componentWidth;

	public DropOrChoose() {
		componentHeight = "200px";
		componentWidth = "100%";
	}

	public void setDelegate(DropOrChoose.DropOrChooseDelegate delegate) {
		this.delegate = delegate;
	}

	private void uploadReceived(String inputFileName, Path inputFilePath) {
		if (delegate != null) {
			delegate.uploadReceived(inputFileName, inputFilePath);
		}
	}

	private void uploadStarted(String fileName) {
		if (delegate != null) {
			delegate.uploadStarted(fileName);
		}
	}

	private void uploadProgress(String fileName, long readBytes, long contentLength) {

		if (delegate != null) {
			delegate.uploadProgress(fileName, readBytes, contentLength);
		}
	}

	private void uploadFailed(String fileName, Path file) {
		if (delegate != null) {
			delegate.uploadFailed(fileName, file);
		}
	}

	public static interface DropOrChooseDelegate {

		void uploadReceived(String inputFileName, Path inputFilePath);

		void uploadStarted(String fileName);

		void uploadProgress(String fileName, long readBytes, long contentLength);

		void uploadFailed(String fileName, Path file);

	}

	public void setComponentHeight(String height) {
		componentHeight = height;
	}

	public void setComponentWidth(String width) {
		componentWidth = width;
	}

	public void setComponentCaption(String caption) {
		uploadComponent.setCaption(caption);
	}

	@Override
	protected Component initContent() {
		MVerticalLayout layout = new MVerticalLayout();
		uploadComponent = new UploadComponent();
		uploadComponent.setSizeFull();
		uploadComponent.setHeight(componentHeight);
		uploadComponent.setWidth(componentWidth);
		uploadComponent.setReceivedCallback(this::uploadReceived);
		uploadComponent.setStartedCallback(this::uploadStarted);
		uploadComponent.setProgressCallback(this::uploadProgress);
		uploadComponent.setFailedCallback(this::uploadFailed);
		uploadComponent.setStyleName("dropBoxLayout");
		layout.setMargin(false);
		uploadComponent.getChoose().setVisible(true);
		layout.addComponent(uploadComponent);
		return layout;
	}

	@Override
	public Class<? extends Resource> getType() {
		return Resource.class;
	}

}
