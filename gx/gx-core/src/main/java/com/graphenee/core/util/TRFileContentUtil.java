package com.graphenee.core.util;

import org.springframework.util.MimeType;

public class TRFileContentUtil {

	public static String getExtensionFromFilename(String filename) {
		if (filename != null) {
			String[] parts = filename.trim().split("\\.");
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

}
