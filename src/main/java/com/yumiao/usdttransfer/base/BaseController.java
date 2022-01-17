package com.yumiao.usdttransfer.base;

import com.yumiao.usdttransfer.constant.Constant;
import com.yumiao.usdttransfer.constant.RespCode;
import com.yumiao.usdttransfer.domain.JwTUserInfo;
import com.yumiao.usdttransfer.exception.BizException;
import com.yumiao.usdttransfer.utils.DateUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.Date;
import java.util.Objects;

public abstract class BaseController {

    protected Object success(Object data){
        if(null==data){
            return JsonResponse.success("");
        }else {
            return JsonResponse.success(data);
        }
    }
    protected Object success(){
        return JsonResponse.success("");
    }

    protected void writeCokie(HttpServletRequest request, HttpServletResponse response,
                              String name, String value, int days) throws ServletException, IOException {
        int day = 24 * 60 * 60;

        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
    protected void clearCokie(HttpServletRequest request, HttpServletResponse response,
                              String name) throws ServletException, IOException {
        Cookie cookie = new Cookie(name, "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     * 获得登陆用户信息
     */
    protected com.yumiao.usdttransfer.domain.JwTUserInfo getLoginUser() {
        HttpSession session = getSession();
        if (Objects.isNull(session))
            return null;

        Object userInfoObj = session.getAttribute(Constant.SESSION_KEY);
        if (Objects.isNull(userInfoObj))
            return null;
        com.yumiao.usdttransfer.domain.JwTUserInfo userInfo=(com.yumiao.usdttransfer.domain.JwTUserInfo)userInfoObj;
        return userInfo;
    }

    /**
     * 获取客户端真实IP
     *
     * @param request
     * @return
     */
    protected String getClientIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.contains(",")) {
            return ip.split(",")[0];
        } else {
            return ip;
        }
    }

    private ServletRequestAttributes getServletRequestAttributes() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        return (ServletRequestAttributes) requestAttributes;
    }

    protected HttpServletRequest getRequest() {
        return getServletRequestAttributes().getRequest();
    }

    protected HttpServletResponse getResponse() {
        return getServletRequestAttributes().getResponse();
    }

    protected HttpSession getSession() {
        return getRequest().getSession();
    }

    protected String readCokie(HttpServletRequest request,
                               String name) throws ServletException, IOException {
        String value = null;
        if (name != null) {
            Cookie cookies[] = request.getCookies();
            if (cookies != null && cookies.length >= 0) {
                for (int i = 0; i < cookies.length; i++) {
                    Cookie cookie = cookies[i];
                    if (name.equals(cookie.getName())) {
                        value = cookie.getValue();
                    }
                }
            }
        }
        return value;
    }

    /**
     * 下载文件
     * @param response
     * @param fileName 下载文件名
     * @param path 文件服务器地址
     */
    public  void downloadFile(HttpServletResponse response, String fileName, String path){
        if (fileName != null) {
            //设置文件路径
            File file = new File(path);
            if (file.exists()) {
                response.setHeader("content-type", "application/octet-stream");
                response.setContentType("application/octet-stream");
                try {
                    response.setHeader("Content-Disposition", "attachment;filename="+new String(fileName.getBytes("utf-8"),"ISO-8859-1"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

}
