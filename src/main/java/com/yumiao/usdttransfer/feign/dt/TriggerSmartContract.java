package com.yumiao.usdttransfer.feign.dt;

import lombok.Data;
import org.tron.protos.Protocol;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class TriggerSmartContract implements Serializable {

	@Data
	public static class Param implements Serializable {
		private String contract_address;//默认为hexString格式
		private String function_selector;//函数签名，不能有空格
		private String parameter;//调用参数[1,2]的虚拟机格式，使用remix提供的js工具，将合约调用者调用的参数数组[1,2]转化为虚拟机所需要的参数格式
		private long fee_limit;//最大消耗的SUN（1TRX = 1,000,000SUN）
		private long call_value;//本次调用往合约转账的SUN（1TRX = 1,000,000SUN）
		private String owner_address;//发起triggercontract的账户地址，默认为hexString格式
		private int call_token_value;//本次调用往合约中转账10币的数量，如果不设置token_id，这项设置为0或者不设置
		private int token_id;//本次调用往合约中转账10币的id，如果没有，不需要设置
		private long Permission_id;//可选参数Permission_id，多重签名时使用，设置交易多重签名时使用的permissionId
	}


	@Data
	public static class Result implements Serializable {
		private Map<String, Object> result;
		private List<Object> constant_result;
		private Transaction transaction;


		/**
		 * 获取结果
		 *
		 * @param index
		 * @param <T>
		 * @return
		 */
		public <T> T getConstantResult(int index) {
			if (constant_result == null || constant_result.size() <= index) {
				return null;
			}
			return (T) constant_result.get(index);
		}


		public boolean isSuccess() {
			if (result != null) {
				Object obj = this.result.get("result");
				if (obj instanceof Boolean) {
					return (boolean) obj;
				}
			}
			return false;
		}
	}


	@Data
	public static class Transaction implements Serializable {
		private String txID;
		private Map<String, Object> raw_data;
		private String raw_data_hex;
		private boolean visible;
		private String[] signature;
	}

	@Data
	public static class RawData implements Serializable {
		private List<Contract> contract;
		private String ref_block_bytes;
		private String ref_block_hash;
		private long expiration;
		private long timestamp;
		private long fee_limit;
	}

	@Data
	public static class Contract implements Serializable {
		private Map<String, Object> parameter;
		private String type;
	}
}
