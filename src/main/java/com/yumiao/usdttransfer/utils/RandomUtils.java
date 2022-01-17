package com.yumiao.usdttransfer.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class RandomUtils {
    private static final Random RANDOM = new Random();

    private RandomUtils() {
    }

    public static int randomInt(int bound) {
        return RANDOM.nextInt(bound);
    }

    public static String randomString(int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(randomInt(10));
        }
        return sb.toString();
    }

    public static boolean randomBoolean() {
        return RANDOM.nextBoolean();
    }

    public static String randomUUID() {
        return UUID.randomUUID().toString();
    }

    public static String serialNumber(int n) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        return format.format(new Date()).concat(randomString(n));
    }
}
