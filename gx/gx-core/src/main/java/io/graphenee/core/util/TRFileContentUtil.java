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
package io.graphenee.core.util;

import org.springframework.util.MimeType;

public class TRFileContentUtil {

	public static String getExtensionFromFilename(String filename) {
		if (filename != null) {
			String[] parts = filename.trim().toLowerCase().split("\\.");
			if (parts.length > 1) {
				return parts[parts.length - 1];
			}
		}
		return null;
	}

	public static String getExtensionFromContentType(String contentType) {
		if (contentType != null) {
			MimeType mimeType = MimeType.valueOf(contentType);
			String subType = mimeType.getSubtype();
			if (subType != null) {
				if (subType.contains("+")) {
					return subType.split("\\+")[0];
				}
				return subType;
			}
		}
		return null;
	}

	public static String getMimeType(String filename) {
		String[] parts = filename.toLowerCase().split("[.]");
		String ext = parts[parts.length - 1];
		if (ext.matches("(jpg|jpeg|gif|png|bmp|tiff)")) {
			return "image/" + ext;
		}
		if (ext.matches("(pdf)")) {
			return "application/" + ext;
		}
		if (ext.matches("(txt)")) {
			return "text/plain";
		}
		if (ext.matches("(js)")) {
			return "text/javascript";
		}
		if (ext.matches("(css)")) {
			return "text/css";
		}
		if (ext.matches("(csv)")) {
			return "text/csv";
		}
		if (ext.matches("(avi)")) {
			return "video/avi";
		}
		if (ext.matches("(m1v|m2v|mp2|mp3|mp4|mpa|mpe|mpeg|mpg)")) {
			return "video/mpeg";
		}
		if (ext.matches("(m1v|m2v|mp2|mp3|mp4|mpa|mpe|mpeg|mpg)")) {
			return "video/mpeg";
		}
		if (ext.matches("(mov)")) {
			return "video/quicktime";
		}
		if (ext.matches("(wav)")) {
			return "audio/wav";
		}
		if (ext.matches("(doc|docx)")) {
			return "application/msword";
		}
		if (ext.matches("(xls|xlsx)")) {
			return "application/vnd.ms-excel";
		}
		if (ext.matches("(ppt|pptx)")) {
			return "application/vnd.ms-powerpoint";
		}
		if (ext.matches("(epub)")) {
			return "application/epub+zip";
		}
		return "application/" + ext;
	}

}
