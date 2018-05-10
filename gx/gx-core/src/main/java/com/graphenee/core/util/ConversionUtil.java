package com.graphenee.core.util;

import java.util.HashMap;
import java.util.Map;

public class ConversionUtil {

	private static Map<Integer, String> NUMBER_RANK_TO_WORD_MAP = new HashMap<>();

	static {
		NUMBER_RANK_TO_WORD_MAP.put(1, "First");
		NUMBER_RANK_TO_WORD_MAP.put(2, "Second");
		NUMBER_RANK_TO_WORD_MAP.put(3, "Third");
		NUMBER_RANK_TO_WORD_MAP.put(4, "Fourth");
		NUMBER_RANK_TO_WORD_MAP.put(5, "Fifth");
		NUMBER_RANK_TO_WORD_MAP.put(6, "Sixth");
		NUMBER_RANK_TO_WORD_MAP.put(7, "Seventh");
		NUMBER_RANK_TO_WORD_MAP.put(8, "Eighth");
		NUMBER_RANK_TO_WORD_MAP.put(9, "Nineth");
		NUMBER_RANK_TO_WORD_MAP.put(10, "Tenth");
		NUMBER_RANK_TO_WORD_MAP.put(11, "Eleventh");
		NUMBER_RANK_TO_WORD_MAP.put(12, "Twelfth");
		NUMBER_RANK_TO_WORD_MAP.put(13, "Thirteenth");
		NUMBER_RANK_TO_WORD_MAP.put(14, "Fourteenth");
		NUMBER_RANK_TO_WORD_MAP.put(15, "Fifteenth");
		NUMBER_RANK_TO_WORD_MAP.put(16, "Sixteenth");
		NUMBER_RANK_TO_WORD_MAP.put(17, "Seventeenth");
		NUMBER_RANK_TO_WORD_MAP.put(18, "Eighteenth");
		NUMBER_RANK_TO_WORD_MAP.put(19, "Nineteenth");
		NUMBER_RANK_TO_WORD_MAP.put(20, "Twentieth");
	}

	public static String numberRankToWord(Integer number) {
		return NUMBER_RANK_TO_WORD_MAP.get(number);
	}

}
