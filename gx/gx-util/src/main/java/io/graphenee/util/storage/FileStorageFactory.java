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

import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.tika.Tika;
import org.springframework.util.StreamUtils;

/**
 * A factory for creating file storage instances.
 */
public class FileStorageFactory {

	/**
	 * Creates a new instance of this factory.
	 */
	public FileStorageFactory() {
		// a default constructor
	}

	/**
	 * Creates a new local file storage.
	 * @param rootFolder The root folder.
	 * @return The new file storage.
	 */
	public static FileStorage createLocalFileStorage(File rootFolder) {
		return new LocalFileStorage(rootFolder);
	}

	/**
	 * Creates a new S3 file storage.
	 * @param awsKey The AWS key.
	 * @param awsSecret The AWS secret.
	 * @param bucketName The bucket name.
	 * @return The new file storage.
	 */
	public static FileStorage createS3FileStorage(String awsKey, String awsSecret, String bucketName) {
		throw new UnsupportedOperationException("This feature is not yet implemented.");
	}

	static class LocalFileStorage implements FileStorage {

		private File rootFolder;

		public LocalFileStorage(File rootFolder) {
			this.rootFolder = rootFolder;
		}

		@Override
		public URI resolveToURI(String resourcePath) throws ResolveFailedException {
			File resource = new File(rootFolder, resourcePath.replace('/', File.separatorChar));
			try {
				return resource.toURI();
			} catch (Exception e) {
				throw new ResolveFailedException("Failed to resolve resource " + resourcePath);
			}
		}

		@Override
		public InputStream resolve(String resourcePath) throws ResolveFailedException {
			File resource = new File(rootFolder, resourcePath.replace('/', File.separatorChar));
			try {
				if (resource.exists() && resource.length() > 0) {
					return new FileInputStream(resource);
				} else {
					throw new ResolveFailedException("Failed to resolve resource " + resourcePath);
				}
			} catch (FileNotFoundException e) {
				throw new ResolveFailedException("Failed to resolve resource " + resourcePath, e);
			}
		}

		@Override
		public Future<FileMetaData> save(String folder, String fileName, InputStream inputStream) throws SaveFailedException {
            String lowerName = fileName.toLowerCase();
            if (ALLOWED_EXTENSIONS.stream().noneMatch(lowerName::endsWith)) {
                throw new SaveFailedException("File extension not allowed: " + fileName);
            }

            Tika tika = new Tika();
            byte[] headerBuffer;
            Path tempFile;

            try {
                final int MAX_HEADER_SIZE = 16 * 1024; // using 16kb for mime detection
                ByteArrayOutputStream headerOut = new ByteArrayOutputStream();
                byte[] buffer = new byte[4096];
                int bytesRead;
                int totalRead = 0;

                while ((bytesRead = inputStream.read(buffer)) != -1 && totalRead < MAX_HEADER_SIZE) {
                    int toWrite = Math.min(bytesRead, MAX_HEADER_SIZE - totalRead);
                    headerOut.write(buffer, 0, toWrite);
                    totalRead = totalRead + toWrite;
                    if (totalRead >= MAX_HEADER_SIZE) {
                        break;
                    }
                }
                headerBuffer = headerOut.toByteArray();

                String detectedMimeType = tika.detect(headerBuffer, fileName);
                if (detectedMimeType == null || ALLOWED_MIME_TYPES.stream().noneMatch(m -> m.equalsIgnoreCase(detectedMimeType))) {
                    throw new SaveFailedException("Unsupported or suspicious file type: " + detectedMimeType);
                }

                tempFile = Files.createTempFile("upload-", "-" + fileName);
                try (OutputStream tempOut = Files.newOutputStream(tempFile)) {
                    tempOut.write(headerBuffer);
                    StreamUtils.copy(inputStream, tempOut);
                }
            } catch (IOException e) {
                throw new SaveFailedException("Failed to validate file before saving: " + fileName, e);
            }

			return Executors.newCachedThreadPool().submit(() -> {
				String resourcePath = resourcePath(folder, tempFile.getFileName().toString());
				File resource = new File(rootFolder, resourcePath.replace('/', File.separatorChar));
				resource.getParentFile().mkdirs();
				try {
					Files.move(tempFile, resource.toPath(), StandardCopyOption.REPLACE_EXISTING);
					long fileSize = resource.length();
					// checksum will be computed in future...
					String checksum = null;
					FileMetaData fileMetaData = new FileMetaData(resourcePath, tempFile.getFileName().toString(), Long.valueOf(fileSize).intValue(), checksum);
					return fileMetaData;
				} catch (Exception e) {
					throw new SaveFailedException(e);
				}
			});
		}

		@Override
		public boolean exists(String resourcePath) {
			if (resourcePath == null)
				return false;
			File file = new File(rootFolder, resourcePath);
			return file.exists();
		}

	}

}
