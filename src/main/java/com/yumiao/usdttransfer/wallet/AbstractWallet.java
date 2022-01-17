package com.yumiao.usdttransfer.wallet;


import org.springframework.beans.factory.annotation.Autowired;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


public abstract class AbstractWallet implements Wallet  {

	
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
