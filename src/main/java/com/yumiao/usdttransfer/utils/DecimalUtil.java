package com.yumiao.usdttransfer.utils;

import java.math.BigDecimal;

public class DecimalUtil {
    public static final BigDecimal DEFAULT_COMMISSION_RATE = new BigDecimal("0.002");

    public static BigDecimal fix(BigDecimal val, BigDecimal min, BigDecimal max, BigDecimal def) {
        if (val == null)
            val = def;
        if (val == null)
            return null;

        if (val.compareTo(min) < 0)
            return min;
        if (val.compareTo(max) > 0)
            return max;
        return val;
    }

    public static BigDecimal fixRate(BigDecimal val, BigDecimal def) {
        return fix(val, BigDecimal.ZERO, BigDecimal.ONE, def);
    }
}
