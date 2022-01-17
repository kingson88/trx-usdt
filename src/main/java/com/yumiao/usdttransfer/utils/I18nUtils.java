package com.yumiao.usdttransfer.utils;

import org.springframework.context.MessageSource;

import java.util.Locale;

public class I18nUtils {
    public I18nUtils() {
    }

    public static String getMessage(Locale locale, String key, Object... objects) {
        return SpringUtils.getBean(MessageSource.class).getMessage(key, objects, locale);
    }

    public static String getMessage(String key, Object... objects) {
        return getMessage(Locale.US, key, objects);
    }

    public static String getMessageByArea(String area, String key, Object... objects) {
        if ("+86".equals(area)) {
            return getMessage(Locale.CHINA, key, objects);
        }
        return getMessage(Locale.US, key, objects);
    }
}
