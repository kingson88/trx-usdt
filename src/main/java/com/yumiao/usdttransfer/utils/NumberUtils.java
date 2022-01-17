package com.yumiao.usdttransfer.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class NumberUtils {
    public static final String FRACTION_TWO = "0.00";

    private NumberUtils() {
    }

    public static String format(long number) {
        return format(number, FRACTION_TWO);
    }

    public static String format(double number) {
        return format(number, FRACTION_TWO);
    }

    public static String format(long number, String pattern) {
        return new DecimalFormat(pattern).format(number);
    }

    public static String format(double number, String pattern) {
        return new DecimalFormat(pattern).format(number);
    }

    public static String format(BigDecimal number, String pattern) {
        return new DecimalFormat(pattern).format(number);
    }

    public static BigDecimal format(BigDecimal number) {
        return format(number, 2);
    }

    public static BigDecimal format(BigDecimal number, int scale) {
        if (number == null)
            return null;

        return number.setScale(scale, RoundingMode.DOWN);
    }
}
