package com.yumiao.usdttransfer.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ValidateUtil {
    static Pattern P_ID_15 = Pattern
            .compile("^(\\d{2})\\d{4}(\\d{2})(\\d{2})(\\d{2})\\d{3}$");
    static Pattern P_ID_18 = Pattern
            .compile("^((\\d{2})\\d{4}(\\d{4})(\\d{2})(\\d{2})\\d{3})(\\w)$");
    static int[] TIMES = new int[]{7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10,
            5, 8, 4, 2};
    static String[] VFCODES = new String[]{"1", "0", "X", "9", "8", "7", "6",
            "5", "4", "3", "2"};
    static Set<String> AREAS = Arrays
            .stream("11,12,13,14,15,21,22,23,31,32,33,34,35,36,37,41,42,43,44,45,46,50,51,52,53,54,61,62,63,64,65,71,81,82"
                    .split(",")).collect(Collectors.toSet());

    static boolean validateDate(int year, int month, int day) {
        if (year < 20)
            year += 2000;
        else if (year < 1900)
            year += 1900;
        if (year <= 1900 || year >= DateUtils.getCurrentGradeYear() - 10)
            return false;
        if (month > 12)
            return false;
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        if (day > cal.getActualMaximum(Calendar.DAY_OF_MONTH))
            return false;
        return true;
    }

    public static boolean id(String id) {
        if (id == null)
            return false;

        Matcher m = P_ID_18.matcher(id);
        if (m.find()) { // 18位身份证
            // 区域码
            if (!AREAS.contains(m.group(2)))
                return false;

            // 检查日期
            if (!validateDate(Integer.parseInt(m.group(3)),
                    Integer.parseInt(m.group(4)), Integer.parseInt(m.group(5))))
                return false;

            // 检查校验码
            int v = 0;
            char[] chars = m.group(1).toCharArray();
            for (int i = 0; i < chars.length; i++)
                v += Integer.parseInt(Character.toString(chars[i])) * TIMES[i];
            if (!m.group(6).equalsIgnoreCase(VFCODES[v % 11]))
                return false;

            return true;
        }

        m = P_ID_15.matcher(id);
        if (m.find()) { // 15位身份证
            // 区域码
            if (!AREAS.contains(m.group(1)))
                return false;

            // 检查日期
            if (!validateDate(Integer.parseInt(m.group(2)),
                    Integer.parseInt(m.group(3)), Integer.parseInt(m.group(4))))
                return false;

            return true;
        }

        return false;
    }

    static Pattern P_SIN = Pattern.compile("^\\d{9}$");

    public static String plainId(String str) {
        return str == null ? "" : str.replaceAll("\\s?-?", "");
    }

    public static boolean sin(String str) {
        if (str == null)
            return false;
        return P_SIN.matcher(plainId(str)).find();
    }

    static Pattern P_SSN = Pattern.compile("^\\d{9}$");

    public static boolean ssn(String str) {
        if (str == null)
            return false;
        return P_SSN.matcher(plainId(str)).find();
    }

    // 短式手机号
    static Pattern P_PHONE_US_SHORT = Pattern.compile("^\\d{10}$");
    static Pattern P_PHONE_CN_SHORT = Pattern.compile("^\\d{11,12}$");
    static Pattern P_PHONE_HK_SHORT = Pattern.compile("^\\d{8}$");

    // 全式手机号
    static Pattern P_PHONE_CN = Pattern.compile("^\\+86\\d{11,12}$");
    static Pattern P_PHONE_US_CA = Pattern.compile("^\\+1\\d{10}$");
    static Pattern P_PHONE_HK = Pattern.compile("^\\+852\\d{8}$");

    public static String plainPhone(String str) {
        return str == null ? "" : str.replaceAll("\\s?-?\\+?\\(?\\)?", "");
    }

    public static boolean isChina(String str) {
        if (str == null)
            return false;
        return P_PHONE_CN_SHORT.matcher(plainPhone(str)).find() ||
                P_PHONE_CN.matcher(plainPhone(str)).find();
    }

    public static boolean phone(String countryPrefix, String noPrefixPhone) {
        if (StringUtils.isEmpty(noPrefixPhone))
            return false;

        if (countryPrefix.equals("+86"))
            return P_PHONE_CN_SHORT.matcher(noPrefixPhone).find();
        if (countryPrefix.startsWith("+1"))
            return P_PHONE_US_SHORT.matcher(noPrefixPhone).find();
        if (countryPrefix.startsWith("+852"))
            return P_PHONE_HK.matcher(noPrefixPhone).find();

        return true;
    }

    public static boolean password(String str) {
        if (str == null)
            return false;
        return str.length() >= 6;
    }

    public static String passwordRequirement() {
        return "密码长度不应少于6位";
    }

    static Pattern P_T_PWD = Pattern.compile("^\\d{6}$");

    public static boolean transactionPassword(String str) {
        if (str == null)
            return false;
        return P_T_PWD.matcher(str).find();
    }

    public static String transactionPasswordRequirement() {
        return "交易密码应为6位数字";
    }

    static Pattern P_CA_POSTAL_CODE = Pattern
            .compile("[A-Za-z]\\d[A-Za-z]\\s*\\d[A-Za-z]\\d");

    public static boolean caPostalCode(String str) {
        if (str == null)
            return false;
        return P_CA_POSTAL_CODE.matcher(str).find();
    }

    static Pattern EMPLOYEE_NUM = Pattern.compile("^[A-Za-z0-9]+$");

    public static boolean isEmployeeNum(String str) {
        if (str == null)
            return false;
        return EMPLOYEE_NUM.matcher(str).find();
    }

    static Pattern USER_NICKNAME = Pattern.compile("^[\\u4e00-\\u9fa5a-zA-Z0-9]+$");

    public static boolean isLegalNickname(String str) {
        if (str == null)
            return false;
        return USER_NICKNAME.matcher(str).find();
    }

    public static boolean isMainlandChina(String country) {
        if (StringUtils.isBlank(country)) {
            return false;
        }
        String pattern = "CN,CH";
        return pattern.contains(country.toUpperCase());
    }

    public static boolean isMainlandChina(String country, Integer idType) {
        boolean[] ret = new boolean[]{false, false};
        if (StringUtils.isNotBlank(country)) {
            ret[0] = "CN,CH".contains(country.toUpperCase());
        }
        if (idType != null) {
            ret[1] = Integer.valueOf(1).equals(idType);
        }
        return ret[0] || ret[1];
    }
}
