/*******************************************************************************
 * Copyright (c) 2016, 2017, Graphenee
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
package io.graphenee.vaadin.component.upload;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.vaadin.easyuploads.Streams;

import io.graphenee.vaadin.component.upload.TRUploadField.FieldType;

@SuppressWarnings("serial")
public abstract class TRFileBuffer implements TRUploadFieldReceiver {
	String mimeType;

	String fileName;

	private File file;

	private FieldType fieldType;

	private boolean deleteFiles = true;

	public TRFileBuffer() {
		this(FieldType.UTF8_STRING);
	}

	public TRFileBuffer(FieldType fieldType) {
		setFieldType(fieldType);
	}

	@Override
	public OutputStream receiveUpload(String filename, String MIMEType) {
		fileName = filename;
		mimeType = MIMEType;
		try {
			if (file == null) {
				file = getTRFileFactory().createFile(filename, mimeType);
			}
			return new FileOutputStream(file);
		} catch (final FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Object getValue() {
		if (file == null || !file.exists()) {
			return null;
		}

		if (getFieldType() == FieldType.FILE) {
			return file;
		}

		InputStream valueAsStream = getContentAsStream();

		try {
			ByteArrayOutputStream bas = new ByteArrayOutputStream((int) file.length());
			Streams.copy(valueAsStream, bas);
			byte[] byteArray = bas.toByteArray();
			if (getFieldType() == FieldType.BYTE_ARRAY) {
				return byteArray;
			} else {
				return new String(byteArray);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public FieldType getFieldType() {
		return fieldType;
	}

	@Override
	public InputStream getContentAsStream() {
		try {
			return new FileInputStream(getFile());
		} catch (final FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setValue(Object newValue) {
		if (getFieldType() == FieldType.FILE) {
			if (isDeleteFiles() && file != null && file.exists()) {
				file.delete();
			}
			file = (File) newValue;
			fileName = file != null ? file.getName() : null;
		} else {
			if (isDeleteFiles() && file != null && file.exists()) {
				file.delete();
			}
			if (newValue == null) {
				return;
			}
			// we set the contents of the file
			if (file == null || !file.exists()) {
				// TODO attributes may be nulls
				file = getTRFileFactory().createFile(fileName, mimeType);
			}
			try {
				FileOutputStream fileOutputStream = new FileOutputStream(file);
				InputStream is;
				if (getFieldType() == FieldType.UTF8_STRING) {
					is = new ByteArrayInputStream(((String) newValue).getBytes());
				} else {
					is = new ByteArrayInputStream((byte[]) newValue);
				}
				Streams.copy(is, fileOutputStream);

			} catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

	}

	abstract public TRFileFactory getTRFileFactory();

	@Override
	public boolean isEmpty() {
		return file == null || !file.exists();
	}

	@Override
	public long getLastFileSize() {
		return file.length();
	}

	@Override
	public String getLastMimeType() {
		return mimeType;
	}

	@Override
	public String getLastFileName() {
		return fileName;
	}

	public File getFile() {
		return file;
	}

	public void setFieldType(FieldType fieldType) {
		this.fieldType = fieldType;
	}

	/**
	 * @param deleteFiles true if file should be deleted when setting value to
	 * null or any other new value
	 */
	public void setDeleteFiles(boolean deleteFiles) {
		this.deleteFiles = deleteFiles;
	}

	/**
	 * @return true if files should be deleted when setting value to null/new
	 * value
	 */
	public boolean isDeleteFiles() {
		return deleteFiles;
	}
}