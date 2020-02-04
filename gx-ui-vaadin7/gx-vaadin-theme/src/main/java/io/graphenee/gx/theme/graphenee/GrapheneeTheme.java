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
package io.graphenee.gx.theme.graphenee;

import com.vaadin.server.ClassResource;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.themes.BaseTheme;

public class GrapheneeTheme extends BaseTheme {

	public static final String THEME_NAME = "graphenee";
	public static final String ICONS_FOLDER = "/VAADIN/themes/" + THEME_NAME + "/icons/";
	public static final String IMAGES_FOLDER = "/VAADIN/themes/" + THEME_NAME + "/images/";

	public static final Resource BACK_ICON = iconResource("back.png");
	public static final Resource UP_ICON = iconResource("arrow_up.png");
	public static final Resource UPLOAD_ICON = iconResource("upload.png");
	public static final Resource DOWNLOAD_ICON = iconResource("download.png");
	public static final Resource IMAGE_NOT_AVAILBLE = imageResource("image_not_available.png");
	public static final Resource AVATAR_FEMALE = imageResource("female.png");
	public static final Resource AVATAR_MALE = imageResource("male.png");
	public static final Resource BACKGROUND = imageResource("background.png");

	public static Resource fileExtensionIconResource(String extension) {
		return iconResource(extension + ".png");
	}

	public static Resource iconResource(String fileName) {
		return new ClassResource(GrapheneeTheme.class, ICONS_FOLDER + fileName);
	}

	public static Resource imageResource(String fileName) {
		return new ClassResource(GrapheneeTheme.class, IMAGES_FOLDER + fileName);
	}

	public static Resource themeResource(String fileName) {
		return new ThemeResource("./../" + THEME_NAME + "/" + fileName);
	}

	public static String STYLE_CARD_ITEM = "card-item";
	public static String STYLE_HOVER_ELEVATED = "hover-elevated";
	public static String STYLE_ELEVATED = "elevated";
	public static String STYLE_CODE = "code";
	public static String STYLE_V_ALIGN_RIGHT = "v-align-right";
	public static String STYLE_V_ALIGN_LEFT = "v-align-left";
	public static String STYLE_V_ALIGN_CENTER = "v-align-center";
	public static String STYLE_CENTER = "center";
	public static String STYLE_TEXT_ALIGN_RIGHT = "text-align-right";

}
