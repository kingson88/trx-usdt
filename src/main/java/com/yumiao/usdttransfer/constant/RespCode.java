package com.yumiao.usdttransfer.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * 返回状态码枚举
 */
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public enum RespCode {
    /*系统通用*/
    SUCCESS(0, "SUCCESS", true),
    FAILURE(-1, "FAILURE", true),
    TO_FASK(1, "操作太快，请休息一会"),
    APPKEY_ERROR(2, "appkey错误"),
    APPKEY_FROZEN(3, "appkey被冻结"),
    SIGN_ERROR(4, "签名错误"),
    PHONE_IS_EMPTY(5, "手机号码为空"),
    PHONE_ERROR(6, "手机号码错误"),
    WORKERS_FROZEN(7, "账号被冻结"),
    T_ISERROR(8, "请求超时"),
    ACCOUNT_EMPTY(9, "账号不存在"),
    STATUS_PARAMA_ERROR(10, "状态参数异常"),
    INVATION_CODE_EXIST(11, "邀请码已存在"),
    TASK_ERROR(12, "任务错误"),
    TASK_NOT_RECIEVER(13, "任务已过期"),
    TASK_NOT_START(13, "任务未到开始接单时间"),
    TASK_IS_RECEIVERED(14, "任务已接单，请勿重复接单"),
    COMPANY_IS_EXIST(15, "公司已经存在，请更换"),
    TOKEN_ERROR(401,"权限认证失败");

    @NonNull
    private Integer value;
    @NonNull
    private String name;
    private boolean global;
}
