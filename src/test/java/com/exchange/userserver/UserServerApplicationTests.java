//package com.exchange.userserver;
//
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.io.*;
//import java.lang.reflect.Method;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class UserServerApplicationTests {
//
//
//    @Test
//    public void contextLoads() throws Exception {
//        //captchaService.sendPhone("8618664329584", "18664329584", UserVerifyCodeOperation.A);
//        //genRespCodes();
//        //captchaService.sendEmail("1428819869@qq.com",UserVerifyCodeOperation.A);
//    }
//
//    public void genRespCodes() throws Exception {
//        List<Map<String, Object>> list = getAllEnum("com.yumiao.usdttransfer.constant.RespCode");
//        System.out.println(list);
//
//        for (Map<String, Object> temp : list) {
//            write(temp.get("code") + "," + temp.get("value"));
//        }
//    }
//
//
//    public void write(String text) throws IOException {
//        try {
//            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("G:/RespCode.csv"), true), "UTF-8"));
//
//            bw.write(text);
//            bw.newLine();
//
//            bw.close();
//        } catch (Exception e) {
//            System.err.println("write errors :" + e);
//        }
//    }
//
//
//    /**
//     * 根据枚举的字符串获取枚举的值
//     *
//     * @param className 包名+类名
//     */
//    public static List<Map<String, Object>> getAllEnum(String className) throws Exception {
//        // 得到枚举类对象
//        Class<Enum> clazz = (Class<Enum>) Class.forName(className);
//        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//        //获取所有枚举实例
//        Enum[] enumConstants = clazz.getEnumConstants();
//        //根据方法名获取方法
//        Method getName = clazz.getMethod("getName");
//        Method getValue = clazz.getMethod("getValue");
//        Map<String, Object> map = null;
//        for (Enum enum1 : enumConstants) {
//            map = new HashMap<String, Object>();
//            //执行枚举方法获得枚举实例对应的值
//            map.put("value", getName.invoke(enum1));
//            map.put("code", getValue.invoke(enum1));
//            list.add(map);
//        }
//        return list;
//    }
//
//
//}
