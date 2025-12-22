package io.graphenee.core.utils;

import java.security.SecureRandom;
import java.util.stream.Collectors;

public class StringUtils {

    // Character set: A-Z, a-z, 0-9 (62 characters total)
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateStreamVersion(int length) {
        return RANDOM.ints(length, 0, ALPHABET.length())
                .mapToObj(ALPHABET::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());
    }
}
