package io.graphenee.vaadin.flow.utils;

import com.vaadin.flow.component.html.Image;

public class IconUtils {
	public static Image fileExtensionIconResource(String extension, Integer width) {
		return getImage(extension + ".png", width);
	}

	public static Image fileExtensionIconResource(String extension) {
		return getImage(extension + ".png");
	}

	public static Image getImage(String imagePath) {
		return new Image("frontend/images/" + imagePath, imagePath);
	}

	public static Image getImage(String imagePath, Integer width) {
		Image img = new Image("frontend/images/" + imagePath, imagePath);
		if (img != null) {
			img.setHeight(width + "px");
		}
		return img;
	}
}
