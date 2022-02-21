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

import com.vaadin.server.ErrorHandler;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import io.graphenee.vaadin.ui.GxNotification;
import io.graphenee.vaadin.util.VaadinUtils;

@SuppressWarnings("serial")
public class ResourcePreviewPanel extends TRAbstractPanel {

    private BrowserFrame viewer;
    private MButton downloadButton;

    @Override
    protected boolean isSpringComponent() {
        return false;
    }

    @Override
    protected void addButtonsToFooter(MHorizontalLayout layout) {
        downloadButton = new MButton("Download").withStyleName(ValoTheme.BUTTON_PRIMARY).withVisible(true);
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
    }

    @Override
    protected String popupHeight() {
        return Math.round(VaadinUtils.browserHeight() * 0.9) + "px";
    }

    @Override
    protected String popupWidth() {
        return Math.round(VaadinUtils.browserWidth() * 0.9) + "px";
    }

    public void preview(Resource resource) {
        build();
        viewer.setSource(resource);
        FileDownloader downloader = new FileDownloader(resource);
        downloader.setErrorHandler(new ErrorHandler() {

            @Override
            public void error(com.vaadin.server.ErrorEvent event) {
                event.getThrowable().printStackTrace();
                UI.getCurrent().access(() -> {
                    GxNotification.closable("Download the file again and wait until the download is finished before you open the file.", Type.ERROR_MESSAGE)
                            .show(Page.getCurrent());
                    UI.getCurrent().push();
                });
            }
        });
        downloader.extend(downloadButton);
        openInModalPopup();
    }

    public void download(Resource resource) {
        preview(resource);
    }

}
