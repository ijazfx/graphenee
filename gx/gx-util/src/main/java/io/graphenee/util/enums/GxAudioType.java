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
 * An enum that represents the type of an audio file.
 */
public enum GxAudioType {
	/**
	 * WebM audio.
	 */
	WEBM_AUDIO("audio/webm", "webm"),
	/**
	 * MP3 audio.
	 */
	MP3("audio/mpeg", "mp3"),
	/**
	 * WAV audio.
	 */
	WAV("audio/wav", "wav"),
	/**
	 * M4A audio.
	 */
	M4A("audio/m4a", "m4a"),
	/**
	 * OGG audio.
	 */
	OGG_AUDIO("audio/ogg", "ogg");

	private String mimeType;

	private String extension;

	private GxAudioType(String mimeType, String extension) {
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
		return this.extension;
	}

	/**
	 * Sets the extension.
	 * @param extension The extension.
	 */
	public void setExtension(String extension) {
		this.extension = extension;
	}

	/**
	 * Finds an audio type by MIME type.
	 * @param mimeType The MIME type.
	 * @return The audio type.
	 */
	public static GxAudioType findByMimeType(String mimeType) {
		if (mimeType.equals(GxAudioType.WEBM_AUDIO.mimeType))
			return GxAudioType.WEBM_AUDIO;
		if (mimeType.equals(GxAudioType.MP3.mimeType))
			return GxAudioType.MP3;
		if (mimeType.equals(GxAudioType.WAV.mimeType))
			return GxAudioType.WAV;
		if (mimeType.equals(GxAudioType.M4A.mimeType))
			return GxAudioType.M4A;
		if (mimeType.equals(GxAudioType.OGG_AUDIO.mimeType))
			return GxAudioType.OGG_AUDIO;

		return null;
	}

}
