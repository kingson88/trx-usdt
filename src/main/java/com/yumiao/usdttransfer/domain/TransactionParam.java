package com.yumiao.usdttransfer.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(Include.NON_NULL)
public class TransactionParam implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String from;
	private String to;
	private String value;
	private String gas;
	private String gasPrice;
	private String data;
	private String nonce ;
	
	private String password;
	
	public TransactionParam() {
	}
	
	public String getFrom() {
		return from;
	}
	
	public void setFrom(String from) {
		this.from = from;
	}
	
	public String getTo() {
		return to;
	}
	
	public void setTo(String to) {
		this.to = to;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getGas() {
		return gas;
	}
	
	public void setGas(String gas) {
		this.gas = gas;
	}
	
	public String getGasPrice() {
		return gasPrice;
	}
	
	public void setGasPrice(String gasPrice) {
		this.gasPrice = gasPrice;
	}
	
	public String getData() {
		return data;
	}
	
	public void setData(String data) {
		this.data = data;
	}
	
	public String getNonce() {
		return nonce;
	}
	
	public void setNonce(String nonce) {
		this.nonce = nonce;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
}