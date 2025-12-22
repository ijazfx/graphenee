package io.graphenee.vaadin.flow.utils;

import java.security.SecureRandom;
import java.util.stream.Collectors;

public class StringUtils {

    // Character set: A-Z, a-z, 0-9 (62 characters total)
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

	public static String toTitleCase(String input) {

		if (input == null || input.length() == 0) {
			return input;
		}

		char[] str = input.toCharArray();
		StringBuilder sb = new StringBuilder();

		boolean capRepeated = false;
		for (int i = 0, prev = -1, next; i < str.length; ++i, prev = next) {
			next = getCharType(str[i]);

			if (prev == 1 && next == 1) {
				capRepeated = true;
			} else if (next != 0) {
				capRepeated = false;
			}

			if (next == -1) {

				continue;
			}

			if (prev == next) {
				sb.append(str[i]);
				continue;
			}

			if (next == 2) {
				sb.append(str[i]);
				continue;
			}
			if (prev == -1 || prev == 2 || prev == 0) {
				if (sb.length() != 0) {
					sb.append(' ');
				}
				sb.append(Character.toUpperCase(str[i]));
				continue;
			}

			if (prev == 1) {
				if (capRepeated) {
					sb.insert(sb.length() - 1, ' ');
					capRepeated = false;
				}
				sb.append(str[i]);

			}
		}
		String output = sb.toString().trim();
		output = (output.length() == 0) ? input : output;
		return output;
	}

	private static int getCharType(char ch) {
		if (Character.isLowerCase(ch)) {
			return 0;
		} else if (Character.isUpperCase(ch)) {
			return 1;
		} else if (Character.isDigit(ch)) {
			return 2;
		}
		return -1;
	}

    public static String generateStreamVersion(int length) {
        return RANDOM.ints(length, 0, ALPHABET.length())
                .mapToObj(ALPHABET::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());
    }

}
