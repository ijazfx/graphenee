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

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Date;
import java.util.function.Supplier;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.vaadin.viritin.button.DownloadButton;
import org.vaadin.viritin.button.DownloadButton.ContentWriter;

import com.vaadin.server.FontAwesome;

import io.graphenee.core.util.KeyValueWrapper;
import io.graphenee.core.util.TRCalendarUtil;

public class ExportDataSpreadSheetComponent {

	private static final String FILE_EXTENSION_XLS = ".xls";
	private Collection<String> columnsCaptions;
	private Collection<String> dataColumns;
	private Collection<Object> dataItems;
	private Sheet sheet;
	private String fileName;
	private Supplier<Collection<String>> columnsCaptionsSupplier;
	private Supplier<Collection<String>> dataColumnSupplier;
	private Supplier<Collection<Object>> dataItemsSupplier;
	private DownloadButton downloadButton;

	private CellStyle defaultDateStyle = null;

	public ExportDataSpreadSheetComponent withFileName(String fileName) {
		this.fileName = fileName;
		return this;
	}

	public ExportDataSpreadSheetComponent withDataColumns(Collection<String> dataColumns) {
		this.dataColumns = dataColumns;
		return this;
	}

	public ExportDataSpreadSheetComponent withColumnsCaptions(Collection<String> columnsCaptions) {
		this.columnsCaptions = columnsCaptions;
		return this;
	}

	public ExportDataSpreadSheetComponent withDataColumns(Supplier<Collection<String>> supplier) {
		this.dataColumnSupplier = supplier;
		return this;
	}

	public ExportDataSpreadSheetComponent withColumnsCaptions(Supplier<Collection<String>> supplier) {
		this.columnsCaptionsSupplier = supplier;
		return this;
	}

	public ExportDataSpreadSheetComponent withDataItems(Supplier<Collection<Object>> supplier) {
		this.dataItemsSupplier = supplier;
		return this;
	}

	public ExportDataSpreadSheetComponent withDataItems(Collection<Object> dataItems) {
		this.dataItems = dataItems;
		return this;
	}

	public DownloadButton getDownloadButton() {
		if (downloadButton == null) {
			downloadButton = new DownloadButton();
			downloadButton.setCaption("Download");
			downloadButton.setIcon(FontAwesome.FILE_EXCEL_O);
			if (StringUtils.isEmpty(fileName)) {
				fileName = TRCalendarUtil.getFormattedDateTime(TRCalendarUtil.getCurrentTimeStamp(), "yyyyMMdd-HHmmss") + FILE_EXTENSION_XLS;
			}
			downloadButton.setFileName(fileName);
			downloadButton.setWriter(new ContentWriter() {
				@Override
				public void write(OutputStream stream) {
					try {
						writeToOutputStream(stream);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
		}
		return downloadButton;
	}

	public void writeToOutputStream(OutputStream stream) throws IOException {
		if (columnsCaptionsSupplier != null) {
			columnsCaptions = columnsCaptionsSupplier.get();
		}
		if (dataColumnSupplier != null) {
			dataColumns = dataColumnSupplier.get();
		}
		if (!CollectionUtils.isEmpty(dataColumns)) {
			if (dataItemsSupplier != null) {
				dataItems = dataItemsSupplier.get();
			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			defaultDateStyle = workbook.createCellStyle();
			defaultDateStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat(TRCalendarUtil.dateFormatter.toPattern()));
			sheet = workbook.createSheet();
			buildHeaderRow();
			buildDataRows();
			workbook.write(stream);
			workbook.close();
		}
	}

	private void buildHeaderRow() {
		Row headerRow = sheet.createRow(0);
		int i = 0;
		for (String property : columnsCaptions) {
			headerRow.createCell(i++).setCellValue(camelCaseToRegular(property));
		}
	}

	private void buildDataRows() {
		if (!CollectionUtils.isEmpty(dataItems)) {
			int i = 1;
			for (Object dataItem : dataItems) {
				Row row = sheet.createRow(i++);
				buildDataRow(row, dataItem);
			}
		}
	}

	private void buildDataRow(Row row, Object item) {
		int i = 0;
		KeyValueWrapper kvw = new KeyValueWrapper(item);
		for (String property : dataColumns) {
			Object value = kvw.valueForKeyPath(property);
			Cell cell = row.createCell(i++);
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
				cell.setCellValue("");
			}
		}
	}

	private String camelCaseToRegular(String string) {
		return StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(string.substring(0, 1).toUpperCase() + string.substring(1)), ' ');
	}

}
