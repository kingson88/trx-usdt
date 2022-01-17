package com.yumiao.usdttransfer.utils;

import com.yumiao.usdttransfer.constant.RespCode;
import com.yumiao.usdttransfer.exception.BizException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Calendar;
import java.util.Date;

public class JwtUtil {

    /**
     * jwt
     */
    public static final String JWT_ID = "fcoinjwt";
    public static final String JWT_SECRET = "A53337C396C5A75D82E70DD7867A85B3";
    public static final int JWT_TTL = 60 * 60 * 1000; // millisecond
    public static final int JWT_REFRESH_INTERVAL = 55 * 60 * 1000; // millisecond
    public static final int JWT_REFRESH_TTL = 12 * 60 * 60 * 1000; // millisecond

    /**
     * 由字符串生成加密key
     *
     * @return
     */
    public static SecretKey generalKey() {
        String stringKey = JWT_SECRET;
        byte[] encodedKey = Base64.decodeBase64(stringKey);
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length,
                "AES");
        return key;
    }

    /**
     * 创建jwt
     * @param id
     * @param subject
     * @param expires
     * @return
     */
    public static String createJWT(String id, String subject, Date expires) {
        try {
            SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
            long nowMillis = System.currentTimeMillis();
            Date now = new Date(nowMillis);
            SecretKey key = generalKey();
            JwtBuilder builder = Jwts.builder().setId(id).setIssuedAt(now)
                    .setSubject(subject).signWith(signatureAlgorithm, key)
                    .setHeaderParam("userName", "123");
            if (expires.getTime() >= 0) {
                builder.setExpiration(expires);
            }
            return builder.compact();
        } catch (Exception e) {
            throw new BizException(RespCode.FAILURE);
        }
    }

    /**
     * 解密jwt
     * @param jwt
     * @return
     */
    public static Claims parseJWT(String jwt) {
        try {
            SecretKey key = generalKey();
            Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(jwt).getBody();
            return claims;
        } catch (Exception e) {
            throw new BizException(RespCode.FAILURE);
        }
    }

    public static Date getExpiryDate(int minutes) {
        if (minutes > 0) {
            // 根据当前日期，来得到到期日期
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.MINUTE, minutes);
            return calendar.getTime();
        } else {
            return new Date(0);
        }
    }


    public static void main(String[] args) throws Exception {
        String subj = "1231" + "," + "zhangsan";
        Date expiry = getExpiryDate(30 * 24 * 60); //设置过期时间，30天
        String resultToken = createJWT(JwtUtil.JWT_ID, subj, expiry);
        System.out.println(resultToken + "=============");

        Claims claims = JwtUtil.parseJWT(resultToken);
        String subj1 = claims.getSubject();
        if (StringUtils.isBlank(subj1)) {
            System.out.println("shibai===========");
        } else {
            String[] user = subj1.split(",");
            String userId = user[0];
            System.out.println("=userId====" + userId);
        }

    }

}
