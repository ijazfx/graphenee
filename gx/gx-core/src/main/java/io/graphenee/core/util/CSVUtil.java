package io.graphenee.core.util;

public class CSVUtil {

	private static final char DEFAULT_SEPARATOR = ',';

	public static String getHeaderRow(String[] strings) {
		boolean first = true;

		StringBuilder sb = new StringBuilder();
		for (String value : strings) {
			if (!first)
				sb.append(DEFAULT_SEPARATOR);
			sb.append(value);
			first = false;
		}

		return sb.toString();
	}

}
