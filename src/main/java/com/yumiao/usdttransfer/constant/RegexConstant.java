package com.yumiao.usdttransfer.constant;

/**
 * @ClassName RegexConstant
 * @Description
 * @Author Administrator
 * @Date 2019/6/11 11:38
 * @Version 1.0
 **/
public class RegexConstant {
    /**
     * 邮箱账号
     */
    public static final String EMAIL_REGEX = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";

    /**
     * 密码格式
     */
    public static final String PWD_REGEX = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,21}$";

    /**
     * IP地址正则
     */
    public static final String IP_ADDRESS_REGEX = "((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))";
}
