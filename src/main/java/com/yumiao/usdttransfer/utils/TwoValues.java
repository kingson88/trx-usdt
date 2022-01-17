package com.yumiao.usdttransfer.utils;

public class TwoValues<U, V> {
    private U one;
    private V two;

    public TwoValues(U u, V v) {
        this.one = u;
        this.two = v;
    }

    public U getOne() {
        return this.one;
    }

    public void setOne(U one) {
        this.one = one;
    }

    public V getTwo() {
        return this.two;
    }

    public void setTwo(V two) {
        this.two = two;
    }
}