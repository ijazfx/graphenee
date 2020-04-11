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
package io.graphenee.vaadin;

import java.util.Timer;
import java.util.TimerTask;

import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.server.FileDownloader;
import com.vaadin.server.Resource;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import io.graphenee.gx.theme.graphenee.GrapheneeTheme;

@SuppressWarnings("serial")
public class ResourcePreviewPanel extends TRAbstractPanel {

	private BrowserFrame viewer;
	private MButton downloadButton;
	private FileDownloader downloader;
	private MLabel note;

	@Override
	protected boolean isSpringComponent() {
		return false;
	}

	@Override
	protected void addButtonsToFooter(MHorizontalLayout layout) {
		downloadButton = new MButton("Download").withStyleName(ValoTheme.BUTTON_PRIMARY).withVisible(true);
		downloadButton.addClickListener(event -> {
			downloadButton.setEnabled(false);
			TimerTask task = new TimerTask() {

				@Override
				public void run() {
					UI.getCurrent().access(() -> {
						closePopup();
						UI.getCurrent().push();
					});
				}
			};

			Timer t = new Timer();
			t.schedule(task, 3000);
		});
		layout.addComponentAsFirst(downloadButton);
		layout.setExpandRatio(downloadButton, 1);
		layout.setWidth("100%");
	}

	@Override
	protected String panelTitle() {
		return "Preview";
	}

	@Override
	protected void addComponentsToContentLayout(MVerticalLayout layout) {
		viewer = new BrowserFrame(null);
		viewer.setResponsive(true);
		viewer.setSizeFull();
		layout.add(viewer);
		layout.setExpandRatio(viewer, 1);
		note = new MLabel().withFullWidth().withStyleName(ValoTheme.LABEL_LARGE, GrapheneeTheme.STYLE_MARGIN_ALL);
		note.setValue("No preview is available, please download the file using Download button.");
		layout.add(note);
	}

	@Override
	protected String popupHeight() {
		return "600px";
	}

	@Override
	protected String popupWidth() {
		return "800px";
	}

	public Window openInModalPopup(boolean visible) {
		Window wnd = super.openInModalPopup();
		if (!visible) {
			wnd.setWidth("350px");
			wnd.setHeight("200px");
		}
		return wnd;
	}

	public void preview(Resource resource) {
		downloadButton.setEnabled(true);
		if (resource.getMIMEType().startsWith("image/") || resource.getMIMEType().endsWith("/pdf")) {
			viewer.setSource(resource);
			downloadButton.setVisible(false);
			note.setVisible(false);
			viewer.setVisible(true);
			openInModalPopup(true);
		} else {
			viewer.setSource(null);
			viewer.setVisible(false);
			note.setVisible(true);
			if (downloader == null) {
				downloader = new FileDownloader(resource);
				downloader.extend(downloadButton);
			} else {
				downloader.setFileDownloadResource(resource);
			}
			downloadButton.setVisible(true);
			openInModalPopup(false);
		}
	}

}
