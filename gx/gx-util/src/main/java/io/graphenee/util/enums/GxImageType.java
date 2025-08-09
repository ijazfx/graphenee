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
package io.graphenee.util.enums;

/**
 * An enum that represents the type of an image file.
 */
public enum GxImageType {
	/**
	 * JPEG image.
	 */
	JPEG("image/jpeg", "jpeg"),
	/**
	 * JPG image.
	 */
	JPG("image/jpg", "jpg"),
	/**
	 * PNG image.
	 */
	PNG("image/png", "png"),
	/**
	 * WebP image.
	 */
	WEB_IMAGE("image/webp", "webp");

	private String mimeType;

	private String extension;

	private GxImageType(String mimeType, String extension) {
		this.mimeType = mimeType;
		this.extension = extension;
	}

	/**
	 * Gets the MIME type.
	 * @return The MIME type.
	 */
	public String getMimeType() {
		return mimeType;
	}

	/**
	 * Sets the MIME type.
	 * @param mimeType The MIME type.
	 */
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	/**
	 * Gets the extension.
	 * @return The extension.
	 */
	public String getExtension() {
		return extension;
	}

	/**
	 * Sets the extension.
	 * @param extension The extension.
	 */
	public void setExtension(String extension) {
		this.extension = extension;
	}

	/**
	 * Finds an image type by MIME type.
	 * @param mimeType The MIME type.
	 * @return The image type.
	 */
	public static GxImageType findByMimeType(String mimeType) {
		if (mimeType.equals(JPEG.mimeType))
			return GxImageType.JPEG;
		if (mimeType.equals(PNG.mimeType))
			return GxImageType.PNG;
		if (mimeType.equals(WEB_IMAGE.mimeType))
			return GxImageType.WEB_IMAGE;
		if (mimeType.equals(JPG.mimeType))
			return GxImageType.JPG;
		return null;
	}

}
