package com.yumiao.usdttransfer.exception;

public class ParamIsNullException extends RuntimeException {

    private final int code;
    private final String msg;

    private final String parameterName;

    public ParamIsNullException(int code, String msg, String parameterName) {
        super("");
        this.parameterName = parameterName;
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String getMessage() {
        if (this.msg.equals("ParamNotNull"))
            return "Parameter " + this.parameterName + " cannot be empty";
        else
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
