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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.vaadin.event.Transferable;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.server.StreamVariable;
import com.vaadin.ui.Component;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.Html5File;

public class FileDropBox extends DragAndDropWrapper implements DropHandler {

	FileDropBoxDelegate delegate;

	public FileDropBox(final Component root, FileDropBox.FileDropBoxDelegate delegate) {
		super(root);
		this.setDropHandler(this);
		this.delegate = delegate;
	}

	@Override
	public void drop(DragAndDropEvent event) {
		final Transferable transferable = event.getTransferable();
		final Component sourceComponent = transferable.getSourceComponent();
		if (delegate.isDragAndDropWrapperEnable()) {
			if (sourceComponent instanceof DragAndDropWrapper) {
				DragAndDropWrapper wrapper = (DragAndDropWrapper) sourceComponent;
				delegate.onComponentDrop(wrapper.getData());

			}
		}
		if (event.getTransferable() instanceof WrapperTransferable) {
			WrapperTransferable wrappedTransferable = (WrapperTransferable) transferable;
			if (wrappedTransferable.getFiles() != null) {
				for (Html5File html5File : wrappedTransferable.getFiles()) {
					String filename = html5File.getFileName();

					final StreamVariable streamVariable = new StreamVariable() {
						private File tempFile;
						private InputStream uploadedFileStream;
						FileOutputStream fos;
						private ByteArrayOutputStream alternateStream;

						@Override
						public OutputStream getOutputStream() {
							try {
								String tempDir = System.getProperty("java.io.tmpdir");
								if (!tempDir.endsWith(File.separator)) {
									tempDir = tempDir + File.separator;
								}
								tempFile = new File(tempDir + html5File.getFileName());
								fos = new FileOutputStream(tempFile);
							} catch (final java.io.FileNotFoundException e) {
								fos = null;
							}
							return fos;
						}

						@Override
						public boolean listenProgress() {
							return true;
						}

						@Override
						public void onProgress(final StreamingProgressEvent event) {
							delegate.uploadProgressing(event.getFileName(), event.getBytesReceived(), event.getContentLength());
						}

						@Override
						public void streamingStarted(final StreamingStartEvent event) {
							delegate.uploadStarted(event.getFileName());
						}

						@Override
						public void streamingFinished(final StreamingEndEvent event) {
							if (fos != null) {
								delegate.uploadFinished(event.getFileName(), event.getMimeType(), tempFile.getAbsolutePath());
							}
						}

						@Override
						public void streamingFailed(final StreamingErrorEvent event) {
							delegate.uploadFailed(event.getFileName());
						}

						@Override
						public boolean isInterrupted() {
							return false;
						}
					};
					html5File.setStreamVariable(streamVariable);

				}
			}
		}
	}

	@Override
	public AcceptCriterion getAcceptCriterion() {
		return AcceptAll.get();
	}

	public static interface FileDropBoxDelegate {
		void uploadStarted(String filename);

		void uploadFinished(String filename, String fileType, String uploadedFilePath);

		void uploadFailed(String filename);

		void uploadProgressing(String filename, long bytesReceived, long totalBytes);

		void onComponentDrop(Object componentData);

		boolean isDragAndDropWrapperEnable();

	}

}
