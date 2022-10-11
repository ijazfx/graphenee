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
package io.graphenee.vaadin.flow.component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamRegistration;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;

import io.graphenee.util.KeyValueWrapper;
import io.graphenee.util.TRCalendarUtil;
import io.reactivex.rxjava3.core.Observable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GxExportDataComponent<T> {

	private static final String FILE_EXTENSION_XLS = "xls";
	private Collection<String> columnsCaptions;
	private Collection<String> dataColumns;
	private Observable<T> dataProvider;
	private Sheet sheet;
	private String fileName;
	private Supplier<Collection<String>> columnsCaptionsSupplier;
	private Supplier<Collection<String>> dataColumnSupplier;
	private CellStyle defaultDateStyle = null;
	private CellStyle defaultCellStyle = null;
	private CellStyle defaultRowStyle = null;

	private XSSFWorkbook workbook = null;

	private GxExportDataComponentDelegate delegate;

	public GxExportDataComponent<T> withDelegate(GxExportDataComponentDelegate delegate) {
		this.delegate = delegate;
		return this;
	}

	public GxExportDataComponent<T> withFileName(String fileName) {
		this.fileName = fileName;
		return this;
	}

	public GxExportDataComponent<T> withDataColumns(Collection<String> dataColumns) {
		this.dataColumns = dataColumns;
		return this;
	}

	public GxExportDataComponent<T> withColumnsCaptions(Collection<String> columnsCaptions) {
		this.columnsCaptions = columnsCaptions;
		return this;
	}

	public GxExportDataComponent<T> withDataColumns(Supplier<Collection<String>> supplier) {
		this.dataColumnSupplier = supplier;
		return this;
	}

	public GxExportDataComponent<T> withColumnsCaptions(Supplier<Collection<String>> supplier) {
		this.columnsCaptionsSupplier = supplier;
		return this;
	}

	public GxExportDataComponent<T> withDataProvider(Observable<T> dataProvider) {
		this.dataProvider = dataProvider;
		return this;
	}

	public void prepareDownload() {
		try {
			File file = File.createTempFile(UUID.randomUUID().toString(), "xls");
			LongRunningTask task = LongRunningTask.newTask(UI.getCurrent(), ui -> {
				try {
					FileOutputStream fos = new FileOutputStream(file);
					writeToOutputStream(fos);
					fos.close();
				} catch (Exception ex) {
					log.error("Failed to export data", ex);
				}
			}).withDoneCallback(ui -> {
				String fileName = GxExportDataComponent.this.fileName != null ? GxExportDataComponent.this.fileName
						: "exported-data." + FILE_EXTENSION_XLS;
				StreamResource resource = new StreamResource(fileName, new InputStreamFactory() {

					@Override
					public InputStream createInputStream() {
						try {
							return new FileInputStream(file);
						} catch (FileNotFoundException e) {
							log.error("Failed to export data", e);
							return null;
						}
					}
				});
				StreamRegistration sr = VaadinSession.getCurrent().getResourceRegistry().registerResource(resource);
				ui.getPage().open(sr.getResourceUri().toString(), "_blank");
			}).withProgressMessage("Exporting data...").withErrorMessage("Failed to export data")
					.withSuccessMessage("Data exported successfully!").withDoneCaption("Download");
			task.start();
		} catch (Exception ex) {
			log.error("Failed to export data", ex);
		}
	}

	public void writeToOutputStream(OutputStream stream) throws IOException {
		if (columnsCaptionsSupplier != null) {
			columnsCaptions = columnsCaptionsSupplier.get();
		}
		if (dataColumnSupplier != null) {
			dataColumns = dataColumnSupplier.get();
		}
		if (!CollectionUtils.isEmpty(dataColumns)) {
			workbook = new XSSFWorkbook();
			defaultDateStyle = workbook.createCellStyle();
			defaultDateStyle.setDataFormat(workbook.getCreationHelper().createDataFormat()
					.getFormat(TRCalendarUtil.dateFormatter.toPattern()));
			sheet = workbook.createSheet();
			buildHeaderRow();
			buildDataRows();
			workbook.write(stream);
			workbook.close();
		}
	}

	private void buildHeaderRow() {
		Row headerRow = sheet.createRow(0);
		CellStyle rowStyle = null;
		if (delegate != null) {
			defaultRowStyle = workbook.createCellStyle();
			rowStyle = delegate.decorateExportHeaderRow(defaultRowStyle);
			if (rowStyle != null) {
				headerRow.setRowStyle(rowStyle);
			}
		}
		int i = 0;
		for (String property : columnsCaptions) {
			Cell cell = headerRow.createCell(i++);
			if (delegate != null) {
				defaultCellStyle = workbook.createCellStyle();
				CellStyle style = delegate.decorateExportHeaderCell(property, defaultCellStyle);
				if (style != null) {
					cell.setCellStyle(style);
				}
				if (style == null && rowStyle != null) {
					cell.setCellStyle(rowStyle);
				}
			}
			cell.setCellValue(camelCaseToRegular(property));

		}
		// if (delegate != null) {
		// delegate.decorateExportHeaderRow(headerRow);
		// }
	}

	private void buildDataRows() {
		if (dataProvider != null) {
			AtomicInteger i = new AtomicInteger(1);
			log.debug("Exporting data...");
			dataProvider.subscribe(d -> {
				Row row = sheet.createRow(i.getAndIncrement());
				buildDataRow(row, d);
			}, e -> {
				log.error("Error during export", e);
			}, () -> {
				log.debug("Export completed!");
			});
		}
	}

	private void buildDataRow(Row row, Object item) {
		CellStyle rowStyle = null;
		if (delegate != null) {
			defaultRowStyle = workbook.createCellStyle();
			rowStyle = delegate.decorateExportDataRow(defaultRowStyle);
			if (rowStyle != null) {
				row.setRowStyle(rowStyle);
			}
		}
		int i = 0;
		KeyValueWrapper kvw = new KeyValueWrapper(item);
		for (String property : dataColumns) {
			Object value = kvw.valueForKeyPath(property);
			Cell cell = row.createCell(i++);
			if (delegate != null) {
				defaultCellStyle = workbook.createCellStyle();
				CellStyle style = delegate.decorateExportDataCell(property, defaultCellStyle, item);
				if (style != null) {
					cell.setCellStyle(style);
				}
				if (style == null && rowStyle != null) {
					cell.setCellStyle(rowStyle);
				}
			}
			if (value instanceof String) {
				cell.setCellValue(value.toString());
			} else if (value instanceof Boolean) {
				cell.setCellValue((Boolean) value);
			} else if (value instanceof Double) {
				cell.setCellValue(((Double) value).doubleValue());
			} else if (value instanceof Number) {
				cell.setCellValue(((Number) value).intValue());
			} else if (value instanceof Date) {
				if (defaultDateStyle != null)
					cell.setCellStyle(defaultDateStyle);
				cell.setCellValue((Date) value);
			} else {
				if (value != null)
					cell.setCellValue(value.toString());
				else
					cell.setBlank();
			}
		}
	}

	private String camelCaseToRegular(String string) {
		return StringUtils.join(
				StringUtils.splitByCharacterTypeCamelCase(string.substring(0, 1).toUpperCase() + string.substring(1)),
				' ');
	}

	public static interface GxExportDataComponentDelegate {

		default CellStyle decorateExportHeaderRow(CellStyle rowStyle) {
			return rowStyle;
		}

		default CellStyle decorateExportDataRow(CellStyle rowStyle) {
			return rowStyle;
		}

		default CellStyle decorateExportDataCell(String propertyName, CellStyle cellStyle, Object entity) {
			return cellStyle;
		}

		default CellStyle decorateExportHeaderCell(String propertyName, CellStyle cellStyle) {
			return cellStyle;
		}

	}

}
