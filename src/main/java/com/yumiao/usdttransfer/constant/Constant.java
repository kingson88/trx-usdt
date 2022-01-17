package com.yumiao.usdttransfer.constant;

import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

public final class Constant {

    /**
     * 授权header名字
     */
    public static final String AUTH = "Authorization";

    /**
     * 登录状态有效期天数
     */
    public static final int JWTOUTTIME=30;


    /**
     *邀请码
     */
    public static final String INVITATIONCODEKEY="invitationcode";

    /**
     * 创建用户
     */
    public static final String CREATEID = "SYSTEM";


    public static final String GAODE_ADDRESS_LOCALHOST = "https://restapi.amap.com/v3/geocode/regeo";

    public static final String GD_POI_ADDRESS_LOCALHOST = "https://restapi.amap.com/v3/place/around";

    public static final String GAODE_LOCALHOST_BY_ADDRESS = "https://restapi.amap.com/v3/geocode/geo";

    public static final String GD_POI_LOCALHOST_BY_ADDRESS = "https://restapi.amap.com/v3/place/text";

    // 关键字搜索API服务地址
    public static final String GAODE_ADDRESS_AROUD = "https://restapi.amap.com/v3/place/text";

    // 周边搜索API服务地址
    public static final String GAODE_ADDRESS_PLACE = "https://restapi.amap.com/v3/place/around";

    public static final String DISTANCE_LOCATION = "https://restapi.amap.com/v4/direction/bicycling";

    // 5公里经纬度最大偏差值
    public static final Double JINWEI = 0.05;

    // 3公里经纬度最大偏差值
    public static final Double DELIVERY_JINWEI = 0.03;

    /**
     * 请求t验证时间差 超过这个时间t无效
     */
    public static final int TMINUTE=2;

    public static final String SESSION_KEY = "CURRENT_USER";


    public static final String USER_REDIS_KEY_PREFIX = "userInfo:";
    public static final String ONCE_TOKEN = "ONCE_TOKEN:";
    public static final String PRE_LOGIN_TOKEN = "PRE_TOKEN:";

    //用户登入游戏的key缓存
    public static final String USER_GAME_LOGIN_KEY = "GAME_KEY_CACHE:";


    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static final String DATE_YM = "yyyy-MM";
    public static final String DATE_YM_CN = "yyyy年MM月";

    public static final String DATE_YMD = "yyyy-MM-dd";
    public static final String DATE_YMD_CN = "yyyy年MM月dd日";

    public static final String DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final String TIME = "MM-dd HH:mm:ss";
    public static final String DATE_TIME_CN = "yyyy年MM月dd日 HH:mm:ss";

    public static final String DATE_YM_01 = "yyyy-MM-01";

    public static final String MSEC_TIME = "yyyy-MM-dd HH:mm:ss.S";

    public static final String DATE_TIME_YMDH_CN = "yyyy年MM月dd日HH点";

    public static final String DATE_TIME_YMDHM_CN = "yyyy年MM月dd日 HH:mm";

    public static final String TIMEZONE_CN = "GMT+8";

    public static final String REGEXP_PHONE = "^1\\d{10}$";

    public static final String REGEXP_EMAIL = "^[A-Za-z\\d]+([-_.][A-Za-z\\d]+)*@([A-Za-z\\d]+[-.])+[A-Za-z\\d]{2,4}$";

    public static final String REGEXP_IMG = "^(.+?)\\.(png|jpg)$";

    public static final String CNY_USDT = "CNYUSDT";

    public static final String USDT_CNY = "USDTCNY";

    public static final String USD_USDT = "USDUSDT";

    public static final String CNY = "CNY";

    public static final String USD = "USD";

    public static final String USDT = "USDT";

    public static final int STRING_LENGTH = 20;

    public static final Pattern GOOGLE_PATTERN = Pattern.compile("^\\d{6}$");

    public static final String VOLUMEINCREASE_USER = "VOLUMEINCREASE_USER";
    public static final String VOLUMEINCREASE_VALUE = "VOLUMEINCREASE_VALUE";
    public static final String VOLUMEINCREASE = "volumeIncrease";

    /**
     * 分页默认值
     */
    public final static int DEFAULT_PAGE_NUM = 1;
    public final static int DEFAULT_LIMIT_15 = 15;
    public final static int DEFAULT_LIMIT_10 = 10;

    /**
     * 删除标识
     */
    public final static int DELETE_FLAG = 1;
    public final static int NOT_DELETE_FLAG = 0;

}
