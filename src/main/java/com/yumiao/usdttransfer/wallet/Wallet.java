package com.yumiao.usdttransfer.wallet;

import org.web3j.utils.Numeric;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.function.Function;

public interface Wallet { 
	
	String REDIS_KEY_NEXT_SCAN_BLOCK = "WALLET_NEXT_SCAN_BLOCK";
	String REDIS_KEY_FORCE_SCAN_BLOCK = "WALLET_FORCE_SCAN_BLOCK";
	String REDIS_KEY_CURRENCY_ADDRESSES = "WALLET_ADDRESS_";
	String REDIS_KEY_PENDING_DEPOSIT = "WALLET_PENDING_DEPOSIT_";
	String REDIS_KEY_PENDING_WITHDRAW = "WALLET_PENDING_WITHDRAW_";
	String REDIS_KEY_AMOUNT_UPDATE = "WALLET_AMOUNT_UPDATE";
	String REDIS_KEY_GAS_LIMIT = "WALLET_GAS_LIMIT";
	String REDIS_KEY_GAS_PRICE = "WALLET_GAS_PRICE";
	String REDIS_WALLET_SEND_MSG_KEY = "WALLET_SEND_MSG_KEY";
	
	/**
	 * a9059cbb = sha3("transfer(address,uint256)")
	 * 23b872dd = sha3("transferFrom(address,address,uint256)")
	 * 095ea7b3 = sha3("approve(address,uint256)")
	 * 70a08231 = sha3("balanceOf(address)")
	 */
	String erc20_transfer = "a9059cbb";
	String erc20_transferFrom = "23b872dd";
	String erc20_approve = "095ea7b3";
	String erc20_balanceOf = "70a08231";
	String erc20_decimals = "313ce567";
	
	long MAX_TOKEN_APPROVE = 1_0000_0000;
	BigInteger MIN_GAS_LIMIT = new BigInteger("60000");
	BigDecimal MAX_ETHER_APPROVE = new BigDecimal("0.1");
	BigDecimal FIX_QTUM_APPROVE = new BigDecimal("0.1");
	BigDecimal MIN_BALANCE_USER_WALLET = new BigDecimal("0.01");
	BigDecimal MIN_BALANCE_HOT_MASTER_ACCOUNT = new BigDecimal("0.01");
	
	

	
	static BigDecimal toDecimal(String hex, Integer precis) {
		return toDecimal(Numeric.toBigInt(hex), precis);
	}
	
	static BigDecimal toDecimal(BigInteger i, Integer precis) {
		return new BigDecimal(i).divide(BigDecimal.valueOf(Math.pow(10, precis)), precis, RoundingMode.FLOOR);
	}
	
	static BigInteger toInteger(BigDecimal d, int precis) {
		return d.multiply(BigDecimal.valueOf(Math.pow(10, precis))).toBigInteger();
	}
	
	static <F, T> T convert(F from, Function<F, T> get) {
		if (from == null)
			return null;
		return get.apply(from);
	}
	
	static <F, T> T convert(F from, Function<F, T> get, T def) {
		if (from == null)
			return def;
		return get.apply(from);
	}
}
