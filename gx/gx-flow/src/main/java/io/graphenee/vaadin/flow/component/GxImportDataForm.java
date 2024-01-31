/*******************************************************************************
 * Copyright (c) 2016, 2024 Farrukh Ijaz
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
package io.graphenee.vaadin.flow.component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.upload.FinishedEvent;
import com.vaadin.flow.component.upload.Receiver;
import com.vaadin.flow.component.upload.Upload;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GxImportDataForm<T> extends Dialog {

	private static final long serialVersionUID = 1L;

	private TabSheet root;

	private Class<T> entityClass;
	private File uploadedFile;
	GxStackLayout stackLayout = new GxStackLayout();

	public GxImportDataForm(Class<T> entityClass) {
		this.entityClass = entityClass;

		setSizeFull();
		addClassName("gx-import-data-form");

		build();

	}

	private void build() {
		stackLayout = new GxStackLayout();
		stackLayout.add(browseFileTab());

		add(stackLayout);
	}

	private Component importDataTab() {
		VerticalLayout layout = new VerticalLayout();
		return layout;
	}

	private Component mapColumnsTab() {
		H5 heading = new H5("Map Columns");
		Button nextButton = new Button("Next");
		nextButton.setEnabled(false);
		nextButton.addClickListener(cl -> {
			stackLayout.add(importDataTab());
		});

		Receiver r = new Receiver() {

			private static final long serialVersionUID = 1L;

			@Override
			public OutputStream receiveUpload(String fileName, String mimeType) {
				try {
					uploadedFile = File.createTempFile("gximp", ".csv");
					return new FileOutputStream(uploadedFile);
				} catch (IOException e) {
					log.error(e.getMessage(), e);
					return null;
				}
			}
		};

		Upload upload = new Upload(r);

		upload.addFinishedListener(new ComponentEventListener<FinishedEvent>() {

			private static final long serialVersionUID = 1L;

			@Override
			public void onComponentEvent(FinishedEvent event) {
				nextButton.setEnabled(true);
			}

		});

		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
		layout.setMargin(false);
		layout.setPadding(false);
		layout.add(heading, upload);

		getFooter().removeAll();
		getFooter().add(nextButton);

		return layout;
	}

	private Component browseFileTab() {
		H5 heading = new H5("Browse File");
		Button nextButton = new Button("Next");
		nextButton.setEnabled(false);
		nextButton.addClickListener(cl -> {
			stackLayout.add(mapColumnsTab());
		});

		Receiver r = new Receiver() {

			private static final long serialVersionUID = 1L;

			@Override
			public OutputStream receiveUpload(String fileName, String mimeType) {
				try {
					uploadedFile = File.createTempFile("gximp", ".csv");
					return new FileOutputStream(uploadedFile);
				} catch (IOException e) {
					log.error(e.getMessage(), e);
					return null;
				}
			}
		};

		Upload upload = new Upload(r);

		upload.addFinishedListener(new ComponentEventListener<FinishedEvent>() {

			private static final long serialVersionUID = 1L;

			@Override
			public void onComponentEvent(FinishedEvent event) {
				nextButton.setEnabled(true);
			}

		});

		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
		layout.setMargin(false);
		layout.setPadding(false);
		layout.add(heading, upload);

		getFooter().add(nextButton);

		return layout;
	}

}
