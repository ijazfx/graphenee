package io.graphenee.core.model.api;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.graphenee.core.exception.InvalidImportFormatException;

public abstract class GxImportDataProcessor<T> {
	protected static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd-MM-yyyy");

	private int totalRowCount = 0;
	private String row;
	private List<T> importDataBeans = new ArrayList<>();
	String header[];

	Map<String, Integer> headerMap = new HashMap<String, Integer>();

	private Class<T> entityClass;

	public GxImportDataProcessor(Class<T> entityClass) {
		this.setEntityClass(entityClass);
	}

	public void loadFile(String filepath) {
		try (BufferedReader csvReader = new BufferedReader(new FileReader(filepath))) {
			importDataBeans.clear();
			totalRowCount = 0;
			headerMap.clear();
			while ((row = csvReader.readLine()) != null) {
				if (totalRowCount == 0) {
					getHeader(row);
					if (checkFileValidility(requiredColoumnHeader())) {
						throw new InvalidImportFormatException("Invalid file format, required column is missing");
					}

				} else {
					String[] rowData = row.split(",");
					getImportDataBeans().add(processRowData(rowData));
				}
				totalRowCount++;
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected abstract T processRowData(String[] rowData);

	public abstract void saveData();

	public abstract String[] getVisibleProperties();

	public abstract String[] requiredColoumnHeader();

	public String[] getHeader(String row) {
		headerMap.clear();
		header = row.split(",");
		for (int headerIndex = 0; headerIndex < header.length; headerIndex++) {
			headerMap.put(header[headerIndex].trim(), headerIndex);
		}
		return header;
	}

	public Map<String, Integer> getHeaderMap() {
		return headerMap;
	}

	public Integer getColumnIndex(String headerName) {
		return headerMap.get(headerName);
	}

	public Class<T> getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	public List<T> getImportDataBeans() {
		return importDataBeans;
	}

	boolean checkFileValidility(String headers[]) {
		for (String fileHeader : headers) {
			if (getColumnIndex(fileHeader) == null) {
				return true;
			}
		}
		return false;

	}

}
