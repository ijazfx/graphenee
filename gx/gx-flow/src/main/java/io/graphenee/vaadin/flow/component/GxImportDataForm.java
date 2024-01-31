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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.FlexLayout.FlexDirection;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.FinishedEvent;
import com.vaadin.flow.component.upload.Receiver;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.data.binder.BeanPropertySet;
import com.vaadin.flow.data.binder.PropertyDefinition;
import com.vaadin.flow.data.binder.PropertySet;
import com.vaadin.flow.server.InputStreamFactory;

import io.graphenee.util.TRCalendarUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GxImportDataForm<T> extends Dialog {

	private static final long serialVersionUID = 1L;

	private GxStackLayout stackLayout = new GxStackLayout();

	private Class<T> entityClass;
	private File uploadedFile;
	private File templateFile;
	private String[] availableProperties;
	private Grid<T> grid;
	private ArrayList<T> rows;

	public GxImportDataForm(Class<T> entityClass, String[] availableProperties) {
		this.entityClass = entityClass;
		this.availableProperties = availableProperties;

		setHeaderTitle("Import Data");

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
		upload.setAcceptedFileTypes(".csv");

		upload.addFinishedListener(new ComponentEventListener<FinishedEvent>() {

			private static final long serialVersionUID = 1L;

			@Override
			public void onComponentEvent(FinishedEvent event) {
				nextButton.setEnabled(true);
			}

		});

		FlexLayout layout = new FlexLayout();
		layout.setFlexDirection(FlexDirection.COLUMN);
		layout.setSizeFull();
		layout.add(heading, upload);

		layout.setAlignSelf(Alignment.END, nextButton);

		return layout;
	}

	private Component browseFileTab() {
		H6 heading = new H6("Browse File");

		GxDownloadButton downloadTemplate = new GxDownloadButton("Download Template");
		downloadTemplate.setDefaultFileName("template.csv");
		downloadTemplate.setInputStreamFactory(new InputStreamFactory() {

			private static final long serialVersionUID = 1L;

			@Override
			public InputStream createInputStream() {
				try {
					templateFile = File.createTempFile("gximptemplate", ".csv");
					PropertySet<T> props = BeanPropertySet.get(entityClass);
					List<String> cols = new ArrayList<>();
					for (String ap : availableProperties) {
						Optional<PropertyDefinition<T, ?>> prop = props.getProperty(ap);
						if (prop.isPresent()) {
							cols.add(prop.get().getCaption());
						}
					}
					String header = String.join(",", cols);
					FileWriter writer = new FileWriter(templateFile);
					writer.write(header);
					writer.close();
					return new FileInputStream(templateFile);
				} catch (IOException e) {
					log.error(e.getMessage(), e);
					return null;
				}
			}
		});

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
		upload.setAcceptedFileTypes(".csv");

		upload.addFinishedListener(new ComponentEventListener<FinishedEvent>() {

			private static final long serialVersionUID = 1L;

			@Override
			public void onComponentEvent(FinishedEvent event) {
				updateGrid();
				nextButton.setEnabled(true);
			}

			private void updateGrid() {
				rows = new ArrayList<>();
				PropertySet<T> props = BeanPropertySet.get(entityClass);
				Map<String, String> propMap = new HashMap<>();
				Map<Integer, Field> fieldMap = new HashMap<>();
				Map<Integer, String> colMap = new HashMap<>();
				for (String ap : availableProperties) {
					Optional<PropertyDefinition<T, ?>> prop = props.getProperty(ap);
					if (prop.isPresent()) {
						propMap.put(prop.get().getCaption(), ap);
					}
				}
				try (BufferedReader reader = new BufferedReader(new FileReader(uploadedFile))) {
					String line = reader.readLine();
					if (line != null) {
						String[] hc = line.split(",");
						for (int i = 0; i < hc.length; i++) {
							String propName = propMap.get(hc[i]);
							if (propName != null) {
								Field field = entityClass.getDeclaredField(propName);
								field.setAccessible(true);
								fieldMap.put(i, field);
								colMap.put(i, propName);
							}
						}
						Constructor<T> clazz = entityClass.getConstructor();
						while ((line = reader.readLine()) != null) {
							T obj = clazz.newInstance();
							String[] dc = line.split(",");
							for (int i = 0; i < dc.length; i++) {
								Field f = fieldMap.get(i);
								String key = colMap.get(i);
								Object v = convertValueForKey(key, f.getType(), dc[i]);
								f.set(obj, v);
							}
							rows.add(obj);
						}
					}
					grid.setItems(rows);
				} catch (Exception e) {
					log.warn(e.getMessage(), e);
				}
			}

		});

		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(false);
		layout.setPadding(false);
		layout.setSizeFull();

		HorizontalLayout uploadLayout = new HorizontalLayout();
		uploadLayout.setMargin(false);
		uploadLayout.setPadding(false);
		uploadLayout.add(upload, downloadTemplate);

		grid = new Grid<>(entityClass);
		grid.setSizeFull();
		grid.getColumns().forEach(c -> c.setVisible(false));
		for (String ap : availableProperties) {
			Column<T> col = grid.getColumnByKey(ap);
			if (col != null) {
				col.setVisible(true);
			}
		}

		layout.add(heading, uploadLayout, grid, nextButton);

		return layout;
	}

	protected Object convertValueForKey(String key, Class<?> valueType, String value) {
		if (valueType == Boolean.class) {
			return Boolean.valueOf(value);
		}
		if (valueType == Integer.class) {
			try {
				return Integer.valueOf(value);
			} catch (NumberFormatException nfe) {
				return null;
			}
		}
		if (valueType == Long.class) {
			try {
				return Long.valueOf(value);
			} catch (NumberFormatException nfe) {
				return null;
			}
		}
		if (valueType == Timestamp.class) {
			try {
				return TRCalendarUtil.dateTimeFormatter.parse(value);
			} catch (ParseException e) {
				try {
					return new Timestamp(Long.valueOf(value));
				} catch (NumberFormatException nfe) {
					return null;
				}
			}
		}
		if (valueType == Date.class) {
			try {
				return TRCalendarUtil.dateFormatter.parse(value);
			} catch (ParseException e) {
				try {
					return new Date(Long.valueOf(value));
				} catch (NumberFormatException nfe) {
					return null;
				}
			}
		}
		return value;
	}

}
