package com.github.xuqplus2.authserver.util;


import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

public class RandomUtil {

    private static final Random RANDOM = new Random();
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final Base64.Encoder BASE_64_ENCODER = Base64.getEncoder();

    private static final String numeric(Random random, int n) {
        if (n < 1) return "";
        else if (n < 10) {
            String i = String.format("%0" + n + "d", random.nextInt(Integer.MAX_VALUE)); // 避免负数
            return i.substring(i.length() - n);
        } else {
            StringBuilder sb = new StringBuilder();
            while ((n -= 9) > 0)
                sb.append(numeric(random, 9));
            sb.append(numeric(random, n + 9));
            return sb.toString();
        }
    }

    public static final String numeric(int n) {
        return numeric(RANDOM, n);
    }

    public static final String secureNumeric(int n) {
        return numeric(RANDOM, n);
    }

    private static final String string(Random random, int n) {
        if (n < 1) return "";
        byte[] bytes = new byte[n];
        random.nextBytes(bytes);
        return new String(BASE_64_ENCODER.encode(bytes)).substring(0, n);
    }

    public static final String string(int n) {
        return string(RANDOM, n);
    }

    public static final String secureString(int n) {
        return string(SECURE_RANDOM, n);
    }
}
