package io.graphenee.core.model.entity;

import java.util.HashSet;
import java.util.Set;

public class GxDocumentFilter {

	Set<String> mimeTypes = new HashSet<>();
	Set<String> extensions = new HashSet<>();

	public boolean test(GxDocumentExplorerItem item) {
		if (!item.isFile())
			return true;
		if (mimeTypes.isEmpty() && extensions.isEmpty())
			return true;
		boolean result = false;
		if (item.getExtension() != null && !extensions.isEmpty()) {
			result = extensions.contains(item.getExtension());
		}
		if (!result && item.getMimeType() != null && !mimeTypes.isEmpty()) {
			String mimeType = item.getMimeType();
			result = mimeTypes.stream().filter(f -> mimeType.equals(f) || mimeType.startsWith(f)).count() > 0;
		}
		return result;
	}
}
