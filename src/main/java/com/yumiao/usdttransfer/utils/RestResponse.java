package com.yumiao.usdttransfer.utils;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_NULL)
@JsonPropertyOrder({ "status", "message", "innerException", "data" })
public class RestResponse implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String status;
	private String message;
	private Object data;
	private String innerException;

	public RestResponse() {
	}

	public RestResponse(String status, String message) {
		this.status = status;
		this.message = message;
	}

	public RestResponse(String status, String message, Object data) {
		this.status = status;
		this.message = message;
		this.data = data;
	}

	public RestResponse(String status, String message, Object data, String innerException) {
		this.status = status;
		this.message = message;
		this.data = data;
		this.innerException = innerException;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getInnerException() {
		return innerException;
	}

	public void setInnerException(String innerException) {
		this.innerException = innerException;
	}  
}
