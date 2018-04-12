package hut34.wallet.util;

import org.apache.commons.lang3.RandomStringUtils;

import java.security.SecureRandom;

public class PasswordGenerator {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public static String alphanumeric(int length) {
        // Force using SecureRandom instead of default random generator
        return RandomStringUtils.random(length, 0, 0, true, true, null, SECURE_RANDOM);
    }
}
