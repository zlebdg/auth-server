package com.github.xuqplus2.authserver.util;

import java.util.Random;

public class RandomUtil {

    private static final Random RANDOM = new Random();

    public static final String numiric(int n) {
        if (n <= 0 || n > 30) {
            throw new RuntimeException(String.format("given n=%s error", n));
        }
        return String.format("%04d", RANDOM.nextInt(1 << n));
    }
}
