package com.yumiao.usdttransfer.utils;

import sun.misc.BASE64Encoder;

import java.io.*;
import java.util.Objects;

public class FileUtil {


    public static void main(String[] args) {
        String path="///data/project/html/image/z2_4.jpg";
        String name=getFileExtendName(path);
        System.out.println(name + "=============");
    }

    /**
     * 获取文件名 z2_4.jpg
     * @param path
     * @return
     */
    public static String getFileName(String path){
        ///data/project/html/image/z2_4.jpg
        String[] sss=path.split("/");
        return sss[sss.length-1];
    }

    /**
     * 获取文件扩展名
     * @param path
     * @return
     */
    public static String getFileExtendName(String path){
        String[] array = path.split("\\.");
        return   array[array.length-1];
    }

    /**
     * 创建文件夹
     * @param path
     */
    public static  void createDir(String path){
        File dir = new File(path);
        if(!dir.exists()){
            dir.mkdir();
        }
    }

    /**
     * 本地图片转换Base64的方法
     *
     * @param imgPath
     */

    public static String ImageToBase64(String imgPath) throws Exception {
        byte[] data = null;
        // 读取图片字节数组
        try {
            InputStream in = new FileInputStream(imgPath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        // 返回Base64编码过的字节数组字符串
        return encoder.encode(Objects.requireNonNull(data));
    }

}
