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
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.json.JSONObject;

import com.google.common.base.Strings;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.FinishedEvent;
import com.vaadin.flow.component.upload.Receiver;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.data.binder.BeanPropertySet;
import com.vaadin.flow.data.binder.PropertyDefinition;
import com.vaadin.flow.data.binder.PropertySet;
import com.vaadin.flow.data.provider.IdentifierProvider;

import io.graphenee.util.TRCalendarUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GxImportDataForm<T> extends Dialog {

	private static final long serialVersionUID = 1L;

	private GxStackLayout stackLayout = new GxStackLayout();

	private Class<T> entityClass;
	private File uploadedFile;
	private Grid<JSONObject> importGrid;
	private Grid<JSONObject> failedGrid;
	private Grid<T> successGrid;
	private ArrayList<JSONObject> rows;
	private ImportDataFormDelegate<T> delegate;

	Map<Integer, String> colMap = new HashMap<>();
	Map<String, String> patternMap = new HashMap<>();
	Map<String, String> missingMap = new HashMap<>();
	Map<String, PropertyDefinition<T, ?>> pdMap = new HashMap<>();

	private List<T> converted;

	public GxImportDataForm(Class<T> entityClass) {
		this.entityClass = entityClass;

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

	public void setDelegate(ImportDataFormDelegate<T> delegate) {
		this.delegate = delegate;
	}

	private Component importDataTab() {
		H5 convertedHeading = new H5("Converted");
		H5 failedHeading = new H5("Failed");
		Button finishButton = new Button("Save Converted");
		finishButton.setEnabled(true);
		finishButton.addClickListener(cl -> {
			if (delegate != null && converted != null && !converted.isEmpty()) {
				LongRunningTask.newTask(UI.getCurrent(), ui -> {
					delegate.saveConverted(converted);
				}).withProgressMessage("Saving records...").withSuccessMessage("Records have been saved.").start();
			}
			this.close();
		});

		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(false);
		layout.setPadding(false);
		layout.setSizeFull();
		failedGrid = new Grid<>();
		failedGrid.getListDataView().setIdentifierProvider(new IdentifierProvider<>() {

			private static final long serialVersionUID = 1L;

			@Override
			public Object apply(JSONObject source) {
				return UUID.randomUUID();
			}
		});
		failedGrid.setSizeFull();

		successGrid = new Grid<>(entityClass);
		successGrid.getListDataView().setIdentifierProvider(new IdentifierProvider<>() {

			private static final long serialVersionUID = 1L;

			@Override
			public Object apply(T source) {
				return UUID.randomUUID();
			}
		});
		successGrid.setSizeFull();
		successGrid.getColumns().forEach(c -> c.setVisible(false));
		List<Column<T>> ordered = new ArrayList<>();
		colMap.keySet().stream().sorted().forEach(i -> {
			String jsonKey = colMap.get(i);
			PropertyDefinition<T, ?> pd = pdMap.get(jsonKey);
			String key = pd.getName();
			Column<T> c = successGrid.getColumnByKey(key);
			if (c != null) {
				ordered.add(c);
				c.setVisible(true);
			}
		});
		successGrid.getColumns().forEach(c -> {
			if (!ordered.contains(c)) {
				successGrid.removeColumnByKey(c.getKey());
			}
		});
		successGrid.setColumnOrder(ordered);

		colMap.entrySet().stream().sorted((a, b) -> a.getKey() < b.getKey() ? -1 : 1).forEach(e -> {
			PropertyDefinition<T, ?> pd = pdMap.get(e.getValue());
			if (pd != null) {
				final Column<JSONObject> column = failedGrid.addColumn(vp -> {
					return vp.get(pd.getName());
				});
				column.setHeader(pd.getCaption());
			}
		});

		converted = new ArrayList<>();
		List<JSONObject> failed = new ArrayList<>();

		rows.forEach(row -> {
			JSONObject json = new JSONObject();
			row.toMap().forEach((key, value) -> {
				String missing = missingMap.get(key);
				String pattern = patternMap.get(key);
				PropertyDefinition<T, ?> pd = pdMap.get(key);
				if (pd != null) {
					try {
						Object convertedValue = convertSourceToTarget(value.toString(), pd, pattern, missing);
						json.put(pd.getName(), convertedValue);
					} catch (ParseException e) {
						json.put(pd.getName(), missing);
					}
				}
			});
			if (delegate != null) {
				try {
					T o = delegate.convertImportedJsonToEntity(json);
					converted.add(o);
				} catch (JsonToEntityConversionException e) {
					failed.add(e.getJson());
				}
			}
		});

		successGrid.getListDataView().addItems(converted);
		failedGrid.getListDataView().addItems(failed);

		layout.add(convertedHeading, successGrid, failedHeading, failedGrid, finishButton);
		return layout;
	}

	private Component mapColumnsTab() {
		H5 heading = new H5("Map Columns");
		Button nextButton = new Button("Next");
		nextButton.setEnabled(true);
		nextButton.addClickListener(cl -> {
			stackLayout.add(importDataTab());
		});

		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(false);
		layout.setPadding(false);
		layout.setSizeFull();

		FormLayout form = new FormLayout();
		form.setResponsiveSteps(new ResponsiveStep("100px", 7));
		List<Column<JSONObject>> cols = importGrid.getColumns();
		JSONObject item = importGrid.getListDataView().getItem(0);

		PropertySet<T> pset = BeanPropertySet.get(entityClass);
		List<PropertyDefinition<T, ?>> props = pset.getProperties().filter(p -> p.getSetter().isPresent() && !p.getName().contains(".")).collect(Collectors.toList());

		form.add(new H5("Source"), new H5("Data"), new H5("Target"), new H5("Type"), new H5("Pattern"), new H5("If Missing"), new H5("Output"));

		for (int i = 0; i < cols.size(); i++) {
			Column<JSONObject> column = cols.get(i);
			String key = column.getHeaderText();
			TextField sourceKey = new TextField();
			sourceKey.setValue(key);
			TextField data = new TextField();
			data.setValue(item.getString(key));

			TextField targetType = new TextField();

			List<String> patternList = new ArrayList<>(List.of("yyyy-MM-dd", "yyyy-dd-MM", "dd-MM-yyyy", "MM-dd-yyyy", "dd/MM/yyyy", "MM/dd/yyyy", "dd-MMM-yy"));
			ComboBox<String> pattern = new ComboBox<>();
			pattern.setAllowCustomValue(true);
			pattern.setItems(patternList);
			pattern.addCustomValueSetListener(vcl -> {
				patternList.add(vcl.getDetail());
			});

			TextField ifMissing = new TextField();
			TextField output = new TextField();

			ComboBox<PropertyDefinition<T, ?>> targetField = new ComboBox<>();
			targetField.setItemLabelGenerator(tf -> tf.getCaption());
			targetField.addValueChangeListener(vcl -> {
				targetType.setValue(vcl.getValue().getType().getTypeName());
				try {
					pdMap.put(key, vcl.getValue());
					Object value = convertSourceToTarget(item.getString(key), vcl.getValue(), pattern.getValue(), ifMissing.getValue());
					output.setValue(value.toString());
				} catch (Exception e) {
					output.setErrorMessage(e.getMessage());
					// pdMap.remove(key);
				}
			});
			targetField.setItems(props);

			String matchKey = key.replaceAll("_", "").toLowerCase();

			Optional<PropertyDefinition<T, ?>> propMatch = props.stream().filter(f -> {
				String matchProp = f.getName().toLowerCase();
				if (matchKey.equals(matchProp)) {
					return true;
				}
				return false;
			}).findFirst();
			if (propMatch.isPresent()) {
				targetField.setValue(propMatch.get());
			}

			ifMissing.addValueChangeListener(vcl -> {
				try {
					Object value = convertSourceToTarget(item.getString(key), targetField.getValue(), pattern.getValue(), vcl.getValue());
					output.setValue(value.toString());
					missingMap.put(key, vcl.getValue());
				} catch (Exception e) {
					output.setErrorMessage(e.getMessage());
					missingMap.remove(key);
				}
			});

			pattern.addValueChangeListener(vcl -> {
				try {
					Object value = convertSourceToTarget(item.getString(key), targetField.getValue(), vcl.getValue(), ifMissing.getValue());
					output.setValue(value.toString());
					patternMap.put(key, vcl.getValue());
				} catch (Exception e) {
					output.setErrorMessage(e.getMessage());
					patternMap.remove(key);
				}
			});

			form.add(sourceKey, data, targetField, targetType, pattern, ifMissing, output);

		}

		layout.add(heading, form, nextButton);

		return layout;
	}

	private Object convertSourceToTarget(String source, PropertyDefinition<T, ?> pd, String pattern, String missing) throws ParseException {
		Object output = null;
		if (source == null) {
			source = missing;
		}
		if (pd == null) {
			output = source;
		} else if (pd.getType().equals(Integer.TYPE)) {
			output = Integer.parseInt(source);
		} else if (pd.getType().equals(Long.TYPE)) {
			output = Long.parseLong(source);
		} else if (pd.getType().equals(Boolean.TYPE)) {
			output = Boolean.parseBoolean(source);
		} else if (pd.getType().equals(java.util.Date.class)) {
			if (Strings.isNullOrEmpty(pattern)) {
				output = TRCalendarUtil.getCustomDateFormatter().parse(source);
			} else {
				SimpleDateFormat sdf = new SimpleDateFormat(pattern.trim());
				output = sdf.parse(source);
			}
		} else if (pd.getType().equals(java.sql.Timestamp.class)) {
			if (Strings.isNullOrEmpty(pattern)) {
				output = new Timestamp(TRCalendarUtil.getCustomDateTimeFormatter().parse(source).getTime());
			} else {
				SimpleDateFormat sdf = new SimpleDateFormat(pattern.trim());
				output = new Timestamp(sdf.parse(source).getTime());
			}
		} else {
			output = source;
		}
		return output;
	}

	private Component browseFileTab() {
		H6 heading = new H6("Browse File");

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
					String[] parts = fileName.split("\\.");
					String ext = null;
					if (parts.length > 1) {
						ext = parts[parts.length - 1];
					} else {
						ext = "tmp";
					}
					uploadedFile = File.createTempFile("gximp", "." + ext);
					return new FileOutputStream(uploadedFile);
				} catch (IOException e) {
					log.error(e.getMessage(), e);
					return null;
				}
			}
		};

		Upload upload = new Upload(r);
		upload.setAcceptedFileTypes(".csv", ".xls", ".xlsx");

		upload.addFinishedListener(new ComponentEventListener<FinishedEvent>() {

			private static final long serialVersionUID = 1L;

			@Override
			public void onComponentEvent(FinishedEvent event) {
				updateGrid();
				nextButton.setEnabled(true);
			}

			private void updateGrid() {
				if (uploadedFile.getName().endsWith("csv")) {
					updateGridFromCsv();
				} else if (uploadedFile.getName().endsWith(".xlsx") || uploadedFile.getName().endsWith(".xls")) {
					updateGridFromExcel();
				}

			}

			private void updateGridFromExcel() {
				rows = new ArrayList<>();

				try (Workbook workbook = WorkbookFactory.create(uploadedFile)) {
					Sheet sheet = workbook.getSheetAt(0);
					Row line = sheet.getRow(0); // header
					if (line != null) {
						for (int i = 0; i < line.getLastCellNum(); i++) {
							Cell cell = line.getCell(i);
							final String key = cell.getStringCellValue();
							final Column<JSONObject> column = importGrid.addColumn(vp -> {
								return vp.get(key);
							});
							column.setHeader(key);
						}
						for (int rc = 1; rc < sheet.getLastRowNum(); rc++) {
							line = sheet.getRow(rc);
							JSONObject json = new JSONObject();
							for (int i = 0; i < line.getLastCellNum(); i++) {
								Cell cell = line.getCell(i);
								String key = colMap.get(i);
								json.put(key, cell.getStringCellValue());
							}
							rows.add(json);
						}
					}
					importGrid.getListDataView().addItems(rows);
				} catch (Exception e) {
					log.warn(e.getMessage(), e);
				}
			}

			private void updateGridFromCsv() {
				rows = new ArrayList<>();
				try (BufferedReader reader = new BufferedReader(new FileReader(uploadedFile))) {
					String line = reader.readLine();
					if (line != null) {
						String[] hc = line.split(",");
						for (int i = 0; i < hc.length; i++) {
							colMap.put(i, hc[i]);
							final String key = hc[i];
							final Column<JSONObject> column = importGrid.addColumn(vp -> {
								return vp.get(key);
							});
							column.setHeader(hc[i]);
						}
						while ((line = reader.readLine()) != null) {
							JSONObject json = new JSONObject();
							String[] dc = line.split(",");
							for (int i = 0; i < dc.length; i++) {
								String key = colMap.get(i);
								json.put(key, dc[i]);
							}
							rows.add(json);
						}
					}
					importGrid.getListDataView().addItems(rows);
				} catch (Exception e) {
					log.warn(e.getMessage(), e);
				}
			}

		});

		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(false);
		layout.setPadding(false);
		layout.setSizeFull();

		importGrid = new Grid<>();
		importGrid.getListDataView().setIdentifierProvider(new IdentifierProvider<>() {

			private static final long serialVersionUID = 1L;

			@Override
			public Object apply(JSONObject source) {
				return UUID.randomUUID();
			}
		});
		importGrid.setSizeFull();
		layout.add(heading, upload, importGrid, nextButton);

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

	public static interface ImportDataFormDelegate<T> {
		T convertImportedJsonToEntity(JSONObject json) throws JsonToEntityConversionException;

		Object convertValueForProperty(String key, Object value);

		void saveConverted(Collection<T> converted);
	}

	public static class JsonToEntityConversionException extends Throwable {

		private static final long serialVersionUID = 1L;
		private JSONObject json;

		public JsonToEntityConversionException(Throwable ex, JSONObject json) {
			super(ex);
			this.json = json;
		}

		public JSONObject getJson() {
			return this.json;
		}

	}

}
