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

public enum GxAudioType {
	WEBM_AUDIO("audio/webm", "webm"),
	MP3("audio/mpeg", "mp3"),
	WAV("audio/wav", "wav"),
	M4A("audio/m4a", "m4a"),
	OGG_AUDIO("audio/ogg", "ogg");

	private String mimeType;

	private String extension;

	private GxAudioType(String mimeType, String extension) {
		this.mimeType = mimeType;
		this.extension = extension;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public String getExtension() {
		return this.extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public static GxAudioType accessKeyType(String mimeType) {
		if (mimeType == GxAudioType.WEBM_AUDIO.mimeType)
			return GxAudioType.WEBM_AUDIO;
		if (mimeType == GxAudioType.MP3.mimeType)
			return GxAudioType.MP3;
		if (mimeType == GxAudioType.WAV.mimeType)
			return GxAudioType.WAV;
		if (mimeType == GxAudioType.M4A.mimeType)
			return GxAudioType.M4A;
		if (mimeType == GxAudioType.OGG_AUDIO.mimeType)
			return GxAudioType.OGG_AUDIO;

		return null;
	}

}
