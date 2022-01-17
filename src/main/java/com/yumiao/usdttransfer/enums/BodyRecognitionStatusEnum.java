package com.yumiao.usdttransfer.enums;

public enum BodyRecognitionStatusEnum {
    SUCCESS("1","认证通过"),
    FAILURE("0","认证失败");

    String value;
    String name;

    BodyRecognitionStatusEnum(String v,String n) {
        value = v;
        name=n;
    }

    public String getValue() {
        return value;
    }
}
