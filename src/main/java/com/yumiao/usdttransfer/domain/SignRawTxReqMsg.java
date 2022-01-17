package com.yumiao.usdttransfer.domain;

import java.io.Serializable;

public class SignRawTxReqMsg implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String currency;
	private String rawTransaction;
	private String unspents;
	
	public SignRawTxReqMsg() {
	}
	
	public SignRawTxReqMsg(String currency, String rawTransaction) {
		this.currency = currency;
		this.rawTransaction = rawTransaction;
	}
	
	public SignRawTxReqMsg(String currency, String rawTransaction, String unspents) {
		this.currency = currency;
		this.rawTransaction = rawTransaction;
		this.unspents = unspents;
	}
	
	public String getCurrency() {
		return currency;
	}
	
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public String getRawTransaction() {
		return rawTransaction;
	}
	
	public void setRawTransaction(String rawTransaction) {
		this.rawTransaction = rawTransaction;
	}
	
	public String getUnspents() {
		return unspents;
	}
	
	public void setUnspents(String unspents) {
		this.unspents = unspents;
	}
}
