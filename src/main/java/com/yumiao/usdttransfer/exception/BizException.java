package com.yumiao.usdttransfer.exception;

import com.yumiao.usdttransfer.constant.RespCode;
import lombok.Getter;

@Getter
public class BizException extends RuntimeException {
    private int code=9999;

    public BizException(String message) {
        super(message);
    }

    public BizException(String message, int code) {
        super(message);
        this.code = code;
    }

    public BizException(RespCode errorCodeEnum) {
        this(errorCodeEnum.getName(), errorCodeEnum.getValue());
    }
}
