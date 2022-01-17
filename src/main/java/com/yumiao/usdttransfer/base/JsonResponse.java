package com.yumiao.usdttransfer.base;

import com.yumiao.usdttransfer.constant.RespCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("响应结果")
public final class JsonResponse<T> {

    /**
     * 成功
     */
    public static final JsonResponse SUCCESS = new JsonResponse(RespCode.SUCCESS);

    /**
     * 失败
     */
    public static final JsonResponse FAILURE = new JsonResponse(RespCode.FAILURE);

    /**
     * 系统错误
     */
    public static final JsonResponse ERROR = new JsonResponse(RespCode.FAILURE);



    @ApiModelProperty("状态码")
    private int code;

    @ApiModelProperty("返回消息")
    private String msg = "";

    @ApiModelProperty("返回数据")
    private T data = null;

    private JsonResponse(RespCode code) {
        this.code = code.getValue();
        this.msg = code.getName();
    }

    private JsonResponse(RespCode code, String msg) {
        this.code = code.getValue();
        this.msg = msg;
    }

    private JsonResponse(RespCode code, T data) {
        this.code = code.getValue();
        this.msg = code.getName();
        this.data = data;
    }

    public JsonResponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public JsonResponse() {

    }

    public static <T> JsonResponse<T> success() {
        return new JsonResponse(RespCode.SUCCESS);
    }
    public static <T> JsonResponse<T> success(T data) {
        return new JsonResponse(RespCode.SUCCESS, data);
    }

    public static <T> JsonResponse<T> success(RespCode code, T data) {
        return new JsonResponse(code, data);
    }

    public static <T> JsonResponse<T> failure(String msg) {
        return new JsonResponse(RespCode.FAILURE, msg);
    }

    public static <T> JsonResponse<T> failure(RespCode code) {
        return new JsonResponse(code);
    }

    public static <T> JsonResponse<T> failure(RespCode code, String msg) {
        return new JsonResponse(code, msg);
    }

    public static <T> JsonResponse<T> failure(int code, String msg) {
        return new JsonResponse(code, msg);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
