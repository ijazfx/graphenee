package io.graphenee.workshop.vaadin;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.google.common.base.Strings;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.server.streams.DownloadEvent;
import com.vaadin.flow.server.streams.DownloadHandler;
import com.vaadin.flow.server.streams.DownloadResponse;
import com.vaadin.flow.server.streams.InputStreamDownloadCallback;
import com.vaadin.flow.spring.annotation.SpringComponent;

import io.graphenee.core.GxDataService;
import io.graphenee.core.flow.GxCoreMenuItemFactory;
import io.graphenee.vaadin.flow.GxAbstractFlowSetup;
import io.graphenee.vaadin.flow.GxMenuItem;

@SpringComponent
@Scope("prototype")
public class FlowSetup extends GxAbstractFlowSetup {

    @Autowired
    GxDataService dataService;

    @Override
    public List<GxMenuItem> menuItems() {
        List<GxMenuItem> items = new ArrayList<>();

        items.add(GxCoreMenuItemFactory.setupMenuItem());
        items.add(GxCoreMenuItemFactory.documentsMenuItem());
        items.add(GxCoreMenuItemFactory.messageTemplateMenuItem());

        return items;
    }

    @Override
    public Class<? extends RouterLayout> routerLayout() {
        return MainLayout.class;
    }

    @Override
    public String appTitle() {
        String appTitle = dataService.appTitleByHost(host());
        if (!Strings.isNullOrEmpty(appTitle))
            return appTitle;
        return super.appTitle();
    }

    @Override
    public String appVersion() {
        return "1.0";
    }

    @Override
    public Image appLogo() {
        byte[] logoBytes = dataService.appLogoByHost(host());
        if (logoBytes != null) {
            return new Image(DownloadHandler.fromInputStream(new InputStreamDownloadCallback() {

                @Override
                public DownloadResponse complete(DownloadEvent downloadEvent) throws IOException {
                    return new DownloadResponse(new ByteArrayInputStream(logoBytes), null, null, logoBytes.length);
                }

            }), "logo");
        }
        return super.appLogo();
    }

}
