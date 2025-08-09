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
package io.graphenee.util;

import org.springframework.util.MimeType;

/**
 * A utility class for handling file content.
 */
public class TRFileContentUtil {

	/**
	 * Creates a new instance of this utility class.
	 */
	public TRFileContentUtil() {
		// a default constructor
	}

	/**
	 * Gets the extension from a filename.
	 * @param filename The filename.
	 * @return The extension.
	 */
	public static String getExtensionFromFilename(String filename) {
		if (filename != null) {
			String[] parts = filename.trim().toLowerCase().split("\\.");
			if (parts.length > 1) {
				return parts[parts.length - 1];
			}
		}
		return null;
	}

	/**
	 * Gets the extension from a content type.
	 * @param contentType The content type.
	 * @return The extension.
	 */
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

	/**
	 * Gets the MIME type from a filename.
	 * @param filename The filename.
	 * @return The MIME type.
	 */
	public static String getMimeType(String filename) {
		String[] parts = filename.toLowerCase().split("[.]");
		String ext = parts[parts.length - 1];
		if (ext.matches("(jpg|jpeg|gif|png|bmp|tiff|webp|gif)")) {
			return "image/" + ext;
		}
		if (ext.matches("(pdf)")) {
			return "application/" + ext;
		}
		if (ext.matches("(js)")) {
			return "text/javascript";
		}
		if (ext.matches("(css|csv)")) {
			return "text/" + ext;
		}
		if (ext.matches("(avi)")) {
			return "video/x-msvideo";
		}
		if (ext.matches("aac|ac3|aic|amr|flac|aiff|ts|wma|ogg|opus|wav|m4a|mp1|mp2|mp3|weba"))
			return "audio/" + ext;
		if (ext.matches("(mpeg|mpg)")) {
			return "video/mpeg";
		}
		if (ext.matches("(webm|m4v|mp4)")) {
			return "video/" + ext;
		}
		if (ext.matches("(mov|qt)")) {
			return "video/quicktime";
		}
		if (ext.matches("(mkv)")) {
			return "video/x-matroska";
		}
		if (ext.matches("(flv)")) {
			return "video/x-flv";
		}
		if (ext.matches("(mpeg-4)")) {
			return "video/mp4";
		}
		if (ext.matches("(3gp)")) {
			return "video/3gpp";
		}
		if (ext.matches("(wmv)")) {
			return "video/x-ms-wmv";
		}
		if (ext.matches("(ts)")) {
			return "video/MP2T";
		}
		if (ext.matches("(doc)")) {
			return "application/msword";
		}
		if (ext.matches("(docx)")) {
			return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
		}
		if (ext.matches("(xls)")) {
			return "application/vnd.ms-excel";
		}
		if (ext.matches("(xlsx)")) {
			return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
		}
		if (ext.matches("(ppt)")) {
			return "application/vnd.ms-powerpoint";
		}
		if (ext.matches("(pptx)")) {
			return "application/vnd.openxmlformats-officedocument.presentationml.presentation";
		}
		if (ext.matches("(odp)")) {
			return "application/vnd.oasis.opendocument.presentation";
		}
		if (ext.matches("(ods)")) {
			return "application/vnd.oasis.opendocument.spreadsheet";
		}
		if (ext.matches("(odt)")) {
			return "application/vnd.oasis.opendocument.text";
		}
		if (ext.matches("(epub)")) {
			return "application/epub+zip";
		}
		return "application/" + ext;
	}

}


