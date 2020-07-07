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
package io.graphenee.core.enums;

public enum GxImageType {
	JPEG("image/jpeg", "jpeg"),
	PNG("image/png", "png"),
	WEB_IMAGE("image/webp", "webp");

	private String mimeType;

	private String Extension;

	private GxImageType(String mimeType, String extension) {
		this.mimeType = mimeType;
		Extension = extension;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public String getExtension() {
		return Extension;
	}

	public void setExtension(String extension) {
		Extension = extension;
	}

	public static GxImageType accessKeyType(String mimeType) {
		if (mimeType == GxImageType.JPEG.mimeType)
			return GxImageType.JPEG;
		if (mimeType == GxImageType.PNG.mimeType)
			return GxImageType.PNG;
		if (mimeType == GxImageType.WEB_IMAGE.mimeType)
			return GxImageType.WEB_IMAGE;

		return null;
	}

}
