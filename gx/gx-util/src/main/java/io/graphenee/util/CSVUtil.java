package io.graphenee.util;

/**
 * A utility class for CSV files.
 */
public class CSVUtil {

	/**
	 * Creates a new instance of this utility class.
	 */
	public CSVUtil() {
		// a default constructor
	}

	private static final char DEFAULT_SEPARATOR = ',';

	/**
	 * Gets the header row for a CSV file.
	 * @param strings The strings to use for the header row.
	 * @return The header row.
	 */
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
