package com.yumiao.usdttransfer.utils;

import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * 字符串工具类
 *
 */
public class StringUtils extends org.springframework.util.StringUtils {


    /**
     * ^ 匹配输入字符串开始的位置
     * \d 匹配一个或多个数字，其中 \ 要转义，所以是 \\d
     * $ 匹配输入字符串结尾的位置
     */
    public static final String CHINA_REGEX_EXP = "^((13[0-9])|(14[5,7,9])|(15[0-3,5-9])|(166)|(17[0-9])|(18[0-9])|(19[1,8,9]))\\d{8}$";
    public static final String HK_REGEX_EXP = "^(5|6|8|9)\\d{7}$";


    private final static String[] chars = new String[]{"a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};

    //生成8位appKey
    public static String getAppKey() {
        StringBuffer shortBuffer = new StringBuffer();
        //获取用户id进行字符串截取
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();

    }

    //生成32位appSecret
    public static String getAppSecret(String appId){
        String EncryoAppSecret="";
        try {
            EncrypDES des1 = new EncrypDES();// 使用默认密钥
            EncryoAppSecret=des1.encrypt(appId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EncryoAppSecret;
    }

    //length用户要求产生字符串的长度
    public static String getRandomString(int length){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String appKey = getAppKey();  //定义变量接收
        String appSecret = getAppSecret(appKey);
        System.out.println(appKey+ "\r\n");
        System.out.println(appSecret);
    }


        /**
         * 校验是否为大陆号码或香港号码
         *
         * @param str
         * @return 符合规则返回true
         * @throws PatternSyntaxException
         */
    public static boolean isPhoneNum(String str) throws PatternSyntaxException {
        return isChinaPhoneNum(str) || isHkPhoneNum(str);
    }

    /**
     * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数
     * 此方法中前三位格式有：
     * 13+任意数
     * 145,147,149
     * 15+除4的任意数(不要写^4，这样的话字母也会被认为是正确的)
     * 166
     * 17+任意数
     * 18+任意数
     * 198,199
     *
     * @param str
     * @return 正确返回true
     * @throws PatternSyntaxException
     */
    public static boolean isChinaPhoneNum(String str) throws PatternSyntaxException {
        // ^ 匹配输入字符串开始的位置
        // \d 匹配一个或多个数字，其中 \ 要转义，所以是 \\d
        // $ 匹配输入字符串结尾的位置
        Pattern p = Pattern.compile(CHINA_REGEX_EXP);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 香港手机号码8位数，5|6|8|9开头+7位任意数
     *
     * @param str
     * @return 正确返回true
     * @throws PatternSyntaxException
     */
    public static boolean isHkPhoneNum(String str) throws PatternSyntaxException {
        Pattern p = Pattern.compile(HK_REGEX_EXP);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 生成0-10的验证码
     * @param len 验证码长度
     * @return
     */
    public static String getVerifyCode(int len){
        String n="";
        Random rd = new Random();
        for(int i=0;i<len;i++){
            int v=rd.nextInt(10);
            n=n+String.valueOf(v);
        }
        return n;
    }


    public static String getUUID(){
        String uuid = UUID.randomUUID().toString();
        //去掉“-”符号
        return uuid.replaceAll("-", "");
    }

    public static boolean isEmpty(String str) {
        if (str == null || str.length() == 0) {
            return true;
        }
        return false;
    }

    public static boolean isNotEmpty(String s) {
    	return !isEmpty(s);
    }

    /***
     * 驼峰命名转为下划线命名
     *
     * @param para
     *        驼峰命名的字符串
     */

    public static String HumpToUnderline(String para){
        StringBuilder sb=new StringBuilder(para);
        int temp=0;//定位
        if (!para.contains("_")) {
            for(int i=0;i<para.length();i++){
                if(Character.isUpperCase(para.charAt(i))){
                    sb.insert(i+temp, "_");
                    temp+=1;
                }
            }
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 数字前面补0
     * @param length  补0长度
     * @param num 数字
     * @return
     */
    public static  String StringFormatZero(int length, long  num){
        String format="%0"+length +"d";
        return String.format(format, num); //25为int型
    }


    /**

     * 利用正则表达式判断字符串是否是数字

     * @param str

     * @return

     */
    public static boolean isNumeric(String str){

        Pattern pattern = Pattern.compile("[0-9]*");

        Matcher isNum = pattern.matcher(str);

        if( !isNum.matches() ){

            return false;

        }
        return true;

    }
}