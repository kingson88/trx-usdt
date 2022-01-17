package com.yumiao.usdttransfer.utils;

import java.util.Random;

public class wcgPwdUtil {
	
	public static String getRandomString(int length) {
		String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < length; ++i) {
			// int number = random.nextInt(62);// [0,62)
			sb.append(str.charAt(random.nextInt(52)));
		}
		return sb.toString();
	}
	public static String getNumber(int length) {  
        String str = "0123456789";  
        Random random = new Random();  
        StringBuffer sb = new StringBuffer();  
        for (int i = 0; i < length; ++i) {  
            sb.append(str.charAt(random.nextInt(10)));  
        }  
        return sb.toString();  
    }  
	
	public static String WcgPwd(int getRandomString, int getNumber, int lengths) {
		String WcgPwd = "";
		String number = getNumber(getNumber);
		for (int i = 0; i < lengths; i++) {
			if (WcgPwd.equals("") && !String.valueOf(i).equals(number))
				WcgPwd = getRandomString(getRandomString);
			else if (WcgPwd.equals("") && String.valueOf(i).equals(number))
				WcgPwd = String.valueOf(System.currentTimeMillis());
			else if (String.valueOf(i).equals(number))
				WcgPwd += " " + System.currentTimeMillis();
			else
				WcgPwd += " " + getRandomString(getRandomString);
		}

		return WcgPwd;
	}
	
	
	public static void main(String[] args) {
		/*System.out.println(getRandomString(8));
		System.out.println(getNumber(10));
		 System.out.println(System.currentTimeMillis());
	        System.out.println(Calendar.getInstance().getTimeInMillis());
	        System.out.println(new Date().getTime());*/
		for(int i = 0 ;i<100;i++)
		System.out.println(WcgPwd(5, 1, 10));
	}
}
