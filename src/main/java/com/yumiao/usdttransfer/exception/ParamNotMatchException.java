package com.yumiao.usdttransfer.exception;

public class ParamNotMatchException extends RuntimeException {

    private final int code;
    private final String msg;

    private final String parameterName;

    public ParamNotMatchException(int code, String msg, String parameterName) {
        super("");
        this.parameterName = parameterName;
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String getMessage() {
        return this.msg;
    }

    public final String getParameterName() {
        return this.parameterName;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
