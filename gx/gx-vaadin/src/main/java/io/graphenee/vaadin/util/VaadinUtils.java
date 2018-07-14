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
package io.graphenee.vaadin.util;

import java.util.Locale;

import com.google.common.base.Strings;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.server.VaadinServletService;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.AbstractSingleComponentContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.UI;

import io.graphenee.i18n.api.LocalizerService;

public class VaadinUtils {

	public static String getUriWithContextPath(String uri) {
		String contextPath = VaadinServletService.getCurrentServletRequest().getServletContext().getContextPath();
		return contextPath + uri;
	}

	public static Resource findResourceByMimeType(String mimeType) {
		FontAwesome resource = FontAwesome.FILE_O;

		if (!Strings.isNullOrEmpty(mimeType)) {

			if (mimeType.contains("pdf")) {
				resource = FontAwesome.FILE_PDF_O;
			} else if (mimeType.contains("jpg")) {
				resource = FontAwesome.FILE_IMAGE_O;
			} else if (mimeType.contains("image")) {
				resource = FontAwesome.FILE_IMAGE_O;
			} else if (mimeType.contains("video")) {
				resource = FontAwesome.FILE_MOVIE_O;
			} else if (mimeType.contains("audio")) {
				resource = FontAwesome.FILE_AUDIO_O;
			} else if (mimeType.contains("wordprocessing")) {
				resource = FontAwesome.FILE_WORD_O;
			} else if (mimeType.contains("spreadsheet")) {
				resource = FontAwesome.FILE_EXCEL_O;
			} else if (mimeType.contains("presentation")) {
				resource = FontAwesome.FILE_POWERPOINT_O;
			} else if (mimeType.contains("zip")) {
				resource = FontAwesome.FILE_ARCHIVE_O;
			} else if (mimeType.contains("css")) {
				resource = FontAwesome.FILE_CODE_O;
			} else if (mimeType.contains("javascript")) {
				resource = FontAwesome.FILE_CODE_O;
			} else if (mimeType.contains("rss")) {
				resource = FontAwesome.RSS;
			} else if (mimeType.contains("text")) {
				resource = FontAwesome.FILE_TEXT_O;
			}
		}
		return resource;
	}

	public static void localizeRecursively(Component component) {
		localizeRecursively(locale(), component);
	}

	public static void localizeRecursively(Locale locale, Component component) {
		if (locale == null || component == null)
			return;
		if (component instanceof UI)
			((UI) component).getPage().setTitle(component.getCaption());
		else
			component.setCaption(localizedSingularValue(locale, component.getCaption()));
		if (component instanceof TabSheet) {
			TabSheet ts = (TabSheet) component;
			for (int i = 0; i < ts.getComponentCount(); i++) {
				Tab tab = ts.getTab(i);
				tab.setCaption(localizedSingularValue(locale, tab.getCaption()));
			}
		}
		if (component instanceof CustomComponent) {
			((CustomComponent) component).iterator().forEachRemaining(child -> {
				localizeRecursively(locale, child);
			});
		}
		if (component instanceof AbstractComponentContainer) {
			((AbstractComponentContainer) component).iterator().forEachRemaining(child -> {
				localizeRecursively(locale, child);
			});
		}
		if (component instanceof AbstractOrderedLayout) {
			((AbstractOrderedLayout) component).iterator().forEachRemaining(child -> {
				localizeRecursively(locale, child);
			});
		}
		if (component instanceof AbstractSingleComponentContainer) {
			localizeRecursively(locale, ((AbstractSingleComponentContainer) component).getContent());
		}
		if (component instanceof AbstractSingleComponentContainer) {
			localizeRecursively(locale, ((AbstractSingleComponentContainer) component).getContent());
		}
	}

	public static LocalizerService localizer() {
		return VaadinSession.getCurrent().getAttribute(LocalizerService.class);
	}

	public static Locale locale() {
		return VaadinSession.getCurrent().getAttribute(Locale.class);
	}

	public static String localizedSingularValue(String key) {
		if (localizer() == null)
			return key;
		return localizer().getSingularValue(locale(), key);
	}

	public static String localizedPluralValue(String key) {
		if (localizer() == null)
			return key;
		return localizer().getPluralValue(locale(), key);
	}

	public static String localizedSingularValue(Locale locale, String key) {
		if (locale == null || localizer() == null)
			return key;
		return localizer().getSingularValue(locale, key);
	}

	public static String localizedPluralValue(Locale locale, String key) {
		if (locale == null || localizer() == null)
			return key;
		return localizer().getPluralValue(locale, key);
	}

	public static void applyStyleRecursively(MenuItem component, String style) {
		if (component == null)
			return;
		component.setStyleName(component.getStyleName() + " " + style);
		if (component.hasChildren()) {
			component.getChildren().forEach(child -> {
				applyStyleRecursively(child, style);
			});
		}
	}

	public static void applyStyleRecursively(Component component, String style) {
		if (component == null)
			return;
		component.setStyleName(component.getStyleName() + " " + style);
		if (component instanceof MenuBar) {
			((MenuBar) component).getItems().forEach(menuItem -> {
				applyStyleRecursively(menuItem, style);
			});
		}
		if (component instanceof TabSheet) {
			TabSheet ts = (TabSheet) component;
			for (int i = 0; i < ts.getComponentCount(); i++) {
				Tab tab = ts.getTab(i);
				applyStyleRecursively(tab.getComponent(), style);
			}
		}
		if (component instanceof CustomComponent) {
			((CustomComponent) component).iterator().forEachRemaining(child -> {
				applyStyleRecursively(child, style);
			});
		}
		if (component instanceof AbstractComponentContainer) {
			((AbstractComponentContainer) component).iterator().forEachRemaining(child -> {
				applyStyleRecursively(child, style);
			});
		}
		if (component instanceof AbstractOrderedLayout) {
			((AbstractOrderedLayout) component).iterator().forEachRemaining(child -> {
				applyStyleRecursively(child, style);
			});
		}
		if (component instanceof AbstractSingleComponentContainer) {
			applyStyleRecursively(((AbstractSingleComponentContainer) component).getContent(), style);
		}
		if (component instanceof AbstractSingleComponentContainer) {
			applyStyleRecursively(((AbstractSingleComponentContainer) component).getContent(), style);
		}
	}

}
