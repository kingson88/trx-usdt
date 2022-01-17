package com.yumiao.usdttransfer.utils;

import com.yumiao.usdttransfer.exception.BizException;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Supplier;

public abstract class Assert {

    public static void isTrue(boolean expression, String message) {
        throwException(!expression, message);
    }

    public static <T extends BizException> void isTrue(boolean expression, Supplier<T> supplier) {
        throwException(!expression, supplier);
    }

    public static void isFalse(boolean expression, String message) {
        throwException(expression, message);
    }

    public static <T extends BizException> void isFalse(boolean expression, Supplier<T> supplier) {
        throwException(expression, supplier);
    }

    public static void isNull(Object object, String message) {
        throwException(Objects.nonNull(object), message);
    }

    public static <T extends BizException> void isNull(Object object, Supplier<T> supplier) {
        throwException(Objects.nonNull(object), supplier);
    }

    public static void notNull(Object object, String message) {
        throwException(Objects.isNull(object), message);
    }

    public static <T extends BizException> void notNull(Object object, Supplier<T> supplier) {
        throwException(Objects.isNull(object), supplier);
    }

    public static void isEmpty(String string, String message) {
        throwException(!StringUtils.isEmpty(string), message);
    }

    public static <T extends BizException> void isEmpty(String string, Supplier<T> supplier) {
        throwException(!StringUtils.isEmpty(string), supplier);
    }

    public static void notEmpty(String string, String message) {
        throwException(StringUtils.isEmpty(string), message);
    }

    public static <T extends BizException> void notEmpty(String string, Supplier<T> supplier) {
        throwException(StringUtils.isEmpty(string), supplier);
    }

    public static void isEmpty(Collection collection, String message) {
        throwException(!CollectionUtils.isEmpty(collection), message);
    }

    public static <T extends BizException> void isEmpty(Collection collection, Supplier<T> supplier) {
        throwException(!CollectionUtils.isEmpty(collection), supplier);
    }

    public static void notEmpty(Collection collection, String message) {
        throwException(CollectionUtils.isEmpty(collection), message);
    }

    public static <T extends BizException> void notEmpty(Collection collection, Supplier<T> supplier) {
        throwException(CollectionUtils.isEmpty(collection), supplier);
    }

    public static void isEquals(Object expected, Object actual, String message) {
        throwException(!Objects.equals(expected, actual), message);
    }

    public static <T extends BizException> void isEquals(Object expected, Object actual, Supplier<T> supplier) {
        throwException(!Objects.equals(expected, actual), supplier);
    }

    public static void notEquals(Object expected, Object actual, String message) {
        throwException(Objects.equals(expected, actual), message);
    }

    public static <T extends BizException> void notEquals(Object expected, Object actual, Supplier<T> supplier) {
        throwException(Objects.equals(expected, actual), supplier);
    }

    private static void throwException(boolean expression, String message) {
        if (expression) throw new BizException(message);
    }

    private static <T extends BizException> void throwException(boolean expression, Supplier<T> supplier) {
        if (expression) throw nullSafeGet(supplier);
    }

    private static <T extends BizException> T nullSafeGet(Supplier<T> supplier) {
        return supplier == null ? null : supplier.get();
    }
}
