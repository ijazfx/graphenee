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
 * An enum that represents the type of a video file.
 */
public enum GxVideoType {
	/**
	 * WebM video.
	 */
	WEBM_VIDEO("video/webm", "webm"),
	/**
	 * MP4 video.
	 */
	MP4("video/mp4", "mp4"),
	/**
	 * M4V video.
	 */
	M4V("video/m4v", "m4v"),
	/**
	 * MPEG video.
	 */
	MPEG("video/mpeg", "mpeg"),
	/**
	 * OGG video.
	 */
	OGG_VIDEO("video/ogg", "ogg");

	private String mimeType;

	private String extension;

	private GxVideoType(String mimeType, String extension) {
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
	 * Finds a video type by MIME type.
	 * @param mimeType The MIME type.
	 * @return The video type.
	 */
	public static GxVideoType findByMimeType(String mimeType) {
		if (mimeType.equals(GxVideoType.WEBM_VIDEO.mimeType))
			return GxVideoType.WEBM_VIDEO;
		if (mimeType.equals(GxVideoType.MP4.mimeType))
			return GxVideoType.MP4;
		if (mimeType.equals(GxVideoType.MPEG.mimeType))
			return GxVideoType.MPEG;
		if (mimeType.equals(GxVideoType.M4V.mimeType))
			return GxVideoType.M4V;
		if (mimeType.equals(GxVideoType.OGG_VIDEO.mimeType))
			return GxVideoType.OGG_VIDEO;

		return null;
	}

}
