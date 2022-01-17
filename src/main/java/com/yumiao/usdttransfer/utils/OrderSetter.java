package com.yumiao.usdttransfer.utils;

@FunctionalInterface
public interface OrderSetter<T> {
	void apply(T t, Integer v);
}
