package com.yumiao.usdttransfer.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;

/**
 * API签名工具类
 *
 * @ClassName: EncryptionUtil
 * @Description: TODO
 * @author:
 * @date: 2018年7月2日 上午11:35:45
 */
public class EncryptUtil {


    public static String sha256_HMAC(SortedMap<String, String> paramMap, String secret) {
        String params = "";

        String firstKey = paramMap.firstKey();
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            // 如果是第一个不加&
            String key = entry.getKey();
            String value = entry.getValue();
            if (!firstKey.equals(key)) {
                params += "&";

            }
            params += key + "=" + value;
        }

        return sha256_HMAC(params, secret);
    }

    /**
     * sha256_HMAC 加密
     *
     * @author nouseen
     * @since 2018年06月18日 17:17:23
     */
    public static String sha256_HMAC(String param, String secret) {

        String hash = "";

        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");

            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");

            sha256_HMAC.init(secret_key);

            byte[] bytes = sha256_HMAC.doFinal(param.getBytes());
            hash = byteArrayToHexString(bytes);
        } catch (Exception e) {
            System.out.println("Error HmacSHA256 ===========" + e.getMessage());
        }
        return hash;
    }

    /**
     * 将加密后的字节数组转成字符串
     *
     * @author nouseen
     * @since 2018年06月18日 17:20:36
     */
    private static String byteArrayToHexString(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString().toLowerCase();
    }

    public static void main(String[] args) throws Exception {
        HashMap<String, String> paramMap = new HashMap<>(16);
        String nonceStr = UUID.randomUUID().toString();

        //https://api2.vvbtc.com/v1/order/placeOrder?symbol=HCETH&action=S&orderType=0&price=0.00004245&size=100&nonceStr=34da4d660b4141e9bd2c74c4e77e7d13&timestamp=1542007597138&accessKey=K8102911341&sign=c32837b6374d6c1657e1b9bd15626026ad69558e1fe9f4a47aee4b29c47babea
        paramMap.put("timestamp", "1532932448225");
        paramMap.put("nonceStr", "abc");
        paramMap.put("accessKey", "K9115512595");
        //paramMap.put("symbol", "HCETH");
        //paramMap.put("action", "S");
        //paramMap.put("orderType", "0");
        //paramMap.put("price", "0.00004245");
        //paramMap.put("size", "100");
        SortedMap<String, String> sortedMap = new TreeMap<>(paramMap);

        String sign = sha256_HMAC(sortedMap, "O55DptPiAxZHx9B+Dl40wNNwUd59NIa8");
        System.out.println(sign);
    }
}
