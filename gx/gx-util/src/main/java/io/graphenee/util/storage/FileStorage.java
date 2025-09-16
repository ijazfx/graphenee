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
package io.graphenee.util.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.concurrent.Future;

/**
 * An interface for file storage.
 */
public interface FileStorage {

	/**
	 * The default implementation of method returns "folder/fileName"
	 * 
	 * @param folder - must be a valid folder path where file needs to upload.
	 * @param filePath - file to upload.
	 * @return - depends on implementation and should return logical resource
	 * path.
	 */
	default String resourcePath(String folder, String filePath) {
		if (folder == null || filePath == null) {
			return filePath;
		}
		File file = new File(filePath);
		return folder + "/" + file.getName();
	}

	/**
	 * Resolves a resource path to an input stream.
	 * @param resourcePath The resource path.
	 * @return The input stream.
	 * @throws ResolveFailedException If the resource cannot be resolved.
	 */
	default InputStream resolve(String resourcePath) throws ResolveFailedException {
		File file = new File(resourcePath);
		try {
			return new FileInputStream(file);
		} catch (FileNotFoundException e) {
			throw new ResolveFailedException("Failed to resolve resource " + resourcePath);
		}
	}

	/**
	 * Checks if a resource exists.
	 * @param resourcePath The resource path.
	 * @return True if the resource exists, false otherwise.
	 */
	boolean exists(String resourcePath);

	/**
	 * Checks if a resource exists.
	 * @param folder The folder.
	 * @param filePath The file path.
	 * @return True if the resource exists, false otherwise.
	 */
	default boolean exists(String folder, String filePath) {
		return exists(resourcePath(folder, filePath));
	}

	/**
	 * Resolves a resource path to a URI.
	 * @param resourcePath The resource path.
	 * @return The URI.
	 * @throws ResolveFailedException If the resource cannot be resolved.
	 */
	default URI resolveToURI(String resourcePath) throws ResolveFailedException {
		try {
			File file = new File(resourcePath);
			return file.toURI();
		} catch (Exception e) {
			throw new ResolveFailedException("Failed to resolve resource " + resourcePath);
		}
	}

	/**
	 * Saves a file.
	 * @param folder The folder to save the file in.
	 * @param fileToSave The file to save.
	 * @return The file metadata.
	 * @throws SaveFailedException If the file cannot be saved.
	 */
	default Future<FileMetaData> save(String folder, String fileToSave) throws SaveFailedException {
		File file = new File(fileToSave);
		if (file.isFile()) {
			try {
				FileInputStream inputStream = new FileInputStream(file);
				return save(folder, file.getName(), inputStream);
			} catch (IOException e) {
				throw new SaveFailedException(e);
			}
		}
		throw new SaveFailedException(fileToSave + " is not a file");
	}

    /**
     * Create Directory.
     * @param directoryPath Directory name i.e. ABC
     */
    void createDirectory(String directoryPath);

    /**
	 * Saves a file.
	 * @param folder The folder to save the file in.
	 * @param fileName The name of the file.
	 * @param inputStream The input stream of the file.
	 * @return The file metadata.
	 * @throws SaveFailedException If the file cannot be saved.
	 */
	Future<FileMetaData> save(String folder, String fileName, InputStream inputStream) throws SaveFailedException;

    /**
     * Creating file sharing path.
     * @param filePath file name with complete path
     * @return shared url.
     * @throws FileSharingFailedException
     */
    String share(String filePath) throws FileSharingFailedException;

    /**
	 * A delegate for file storage.
	 */
	public static interface FileStorageDelegate {
		/**
		 * Called when a file is saved.
		 * @param fileMetaData The file metadata.
		 */
		void onComplete(FileMetaData fileMetaData);
	}

	/**
	 * A class that represents the metadata of a file.
	 */
	public static class FileMetaData {
		private String resourcePath;
		private int fileSize;
		private String checksum;
		private String fileName;

		/**
		 * Creates a new instance of this class.
		 * @param resourcePath The resource path.
		 * @param fileName The file name.
		 * @param fileSize The file size.
		 * @param checksum The checksum.
		 */
		public FileMetaData(String resourcePath, String fileName, int fileSize, String checksum) {
			this.resourcePath = resourcePath;
			this.fileName = fileName;
			this.fileSize = fileSize;
			this.checksum = checksum;
		}

		/**
		 * Gets the resource path.
		 * @return The resource path.
		 */
		public String getResourcePath() {
			return resourcePath;
		}

		/**
		 * Gets the file name.
		 * @return The file name.
		 */
		public String getFileName() {
			return fileName;
		}

		/**
		 * Sets the file name.
		 * @param fileName The file name.
		 */
		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		/**
		 * Gets the file size.
		 * @return The file size.
		 */
		public int getFileSize() {
			return fileSize;
		}

		/**
		 * Sets the file size.
		 * @param fileSize The file size.
		 */
		public void setFileSize(int fileSize) {
			this.fileSize = fileSize;
		}

		/**
		 * Gets the checksum.
		 * @return The checksum.
		 */
		public String getChecksum() {
			return checksum;
		}

	}

}
