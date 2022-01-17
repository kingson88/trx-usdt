package com.yumiao.usdttransfer.feign.dt;

import lombok.Data;

import java.io.Serializable;

public class ContractEvent implements Serializable {

	@Data
	public static class Param implements Serializable {
		private Object transaction;//所签名的交易
		private String privateKey;//交易发送账户的私钥, HEX 格式

	}

	@Data
	public static class Result implements Serializable {
		private Object transaction;//所签名的交易
		private String privateKey;//交易发送账户的私钥, HEX 格式

	}
}
