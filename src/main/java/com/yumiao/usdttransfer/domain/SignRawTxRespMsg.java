package com.yumiao.usdttransfer.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SignRawTxRespMsg implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private boolean success;
	private String result;
	
	public SignRawTxRespMsg() {
	}
	
	public SignRawTxRespMsg(boolean success, String result) {
		this.success = success;
		this.result = result;
	}
	
	public boolean isSuccess() {
		return success;
	}
	
	public String getResult() {
		return result;
	}
	
	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	public void setResult(String result) {
		this.result = result;
	}
}
