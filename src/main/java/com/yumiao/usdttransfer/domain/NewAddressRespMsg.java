package com.yumiao.usdttransfer.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NewAddressRespMsg implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String address;
	private String password; // 只对ETH类钱包的账户密码
	
	public NewAddressRespMsg() {
	}
	
	public NewAddressRespMsg(String address, String password) {
		this.address = address;
		this.password = password;
	}
	
	public String getAddress() {
		return address;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
}
