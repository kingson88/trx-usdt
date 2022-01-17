package com.yumiao.usdttransfer.exception;

import com.yumiao.usdttransfer.base.JsonResponse;
import com.yumiao.usdttransfer.exception.BizException;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    /**
     * 缺少参数, @RequestParam(required = true)
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public JsonResponse handler(MissingServletRequestParameterException e) {
        log.error("MissingServletRequestParameterException",e);
        return JsonResponse.failure("缺少参数'" + e.getParameterName() + "'" + e.getMessage());
    }

    /**
     * 缺少参数, @RequestParam(required = true)
     */
    @ExceptionHandler
    public JsonResponse handler(ParamIsNullException e) {
        log.error("ParamIsNullException",e);
        return JsonResponse.failure(e.getCode(), e.getMessage());
    }

    /**
     * 参数格式不正确, @RequestParam(required = true)
     */
    @ExceptionHandler
    public JsonResponse handler(ParamNotMatchException e) {
        log.error("ParamNotMatchException",e);
        return JsonResponse.failure(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(BizException.class)
    public JsonResponse handler(BizException e) {
        log.error("ExceptionHandler",e);
        return JsonResponse.failure(e.getCode(), e.getMessage());
    }

    /**
     * 请求方法不支持
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public JsonResponse handler(HttpRequestMethodNotSupportedException e) {
        return JsonResponse.failure("请求方法'" + e.getMethod() + "'不支持" + e.getMessage());
    }

    /**
     * 参数绑定异常
     */
    @ExceptionHandler(BindException.class)
    public JsonResponse handler(BindException e) {
        FieldError fieldError = e.getBindingResult().getFieldErrors().get(0);
        if ("typeMismatch".equals(fieldError.getCode()))
            return JsonResponse.failure("参数类型不正确" + fieldError.getDefaultMessage());

        return JsonResponse.failure(fieldError.getDefaultMessage());
    }

    /**
     * 未知异常
     */
    @ExceptionHandler
    public JsonResponse handler(Exception e) {
        log.error("系统异常 ==> " + e.getMessage(), e);
        String errorMsg = e + " at " + e.getStackTrace()[0];
        return JsonResponse.failure("系统异常,请联系管理员!");
    }
}

