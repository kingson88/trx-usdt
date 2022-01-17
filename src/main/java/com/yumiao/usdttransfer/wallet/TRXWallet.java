package com.yumiao.usdttransfer.wallet;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.yumiao.usdttransfer.domain.NewAddressRespMsg;
import com.yumiao.usdttransfer.exception.BizException;
import com.yumiao.usdttransfer.feign.dt.GetTransactionSign;
import com.yumiao.usdttransfer.feign.dt.TriggerSmartContract;
import com.yumiao.usdttransfer.utils.ByteArray;
import com.yumiao.usdttransfer.utils.HexUtils;
import com.yumiao.usdttransfer.utils.SpringApplicationContextUtil;
import com.yumiao.usdttransfer.utils.TronUtils;
import lombok.extern.slf4j.Slf4j;
import org.bitcoinj.core.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.ribbon.apache.HttpClientUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import org.tron.api.GrpcAPI;
import org.tron.common.crypto.ECKey;
import org.tron.common.utils.Base58;
import org.tron.common.utils.JsonFormat;
import org.tron.common.utils.Utils;
import org.tron.protos.Protocol;
import org.tron.protos.Protocol.Transaction;
import org.tron.walletserver.WalletApi;
import org.bitcoinj.core.Sha256Hash;
import org.bouncycastle.util.encoders.Hex;
import org.tron.protos.Contract;
import com.google.protobuf.Any;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.Type;


import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.CacheRequest;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.yumiao.usdttransfer.utils.TronUtils.toViewAddress;

@Slf4j
@Component
public class TRXWallet extends AbstractWallet{
	//状态解释
	//https://cn.developers.tron.network/docs/%E4%BA%A4%E6%98%93
	public static final String SUCCESS_STATUS = "SUCCESS" ;  
	public static final String TRANSFER_TYPE = "TransferContract" ;   
	public static final  int HEX_ADDRESS_SIZE = 21;
	public static final byte ADD_PRE_FIX_BYTE_TESTNET = (byte) 0xa0;   //a0 + address

	@Autowired
	private RestTemplate restTemplate;

	private static SecureRandom random = new SecureRandom();
	


	private static  final  Integer PRECIS=18;

	/**
	 * trc20合约地址 这个是USDT代币
	 */
	private String contract = "TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t";


	/**
	 * TRX主网
	 */
	private static final String tronUrl = "https://api.trongrid.io";

	private static final String WALLET_URL="https://api.trongrid.io";

	private static final long feeLimit=1000000L;

	/**
	 * 代币精度
	 */
	//token的精度  就是小数点后面有多少位小数 然后1后面加多少个0就可以
	private static final BigDecimal decimal = new BigDecimal("1000000");
//
//    @Autowired
//    private TronFullNodeFeign feign;

    public BigDecimal usdtBalanceOf(String address) {
        return balanceOf(contract,address);
    }



    public String getTransactionById(String txId){
		Map<String,Object> param = new HashMap<>();
		param.put("value", txId);
		String url=tronUrl + "/wallet/gettransactionbyid";
		String jsonStr =restTemplate.postForEntity(url,param, String.class).getBody();
		return jsonStr;
	}


	/**
     * 查询tron币数量
     *
     * @param address
     * @return
     */
    public BigDecimal balanceOfTron(String address) {
        final BigDecimal decimal = new BigDecimal("1000000");
        final int accuracy = 6;//六位小数
        Map<String, Object> param = new HashMap<>();
        param.put("address", castHexAddress(address));
        String url=tronUrl + "/wallet/getaccount";
        JSONObject obj  = restTemplate.postForEntity(url,param,JSONObject.class).getBody();
        if (obj != null) {
            BigInteger balance = obj.getBigInteger("balance");
            if(balance == null) balance = BigInteger.ZERO;
            return new BigDecimal(balance).divide(decimal, accuracy, RoundingMode.FLOOR);
        }
        return BigDecimal.ZERO;
    }


	/**
	 * 查询额度
	 *
	 * @param contract 合约地址
	 * @param address  查询地址
	 * @return
	 */
	public BigDecimal balanceOf(String contract, String address) {
		String hexAddress = address;
		if (address.startsWith("T")) {
			hexAddress = TronUtils.toHexAddress(address);
		}
		String hexContract = contract;
		if (contract.startsWith("T")) {
			hexContract = TronUtils.toHexAddress(contract);
		}
		TriggerSmartContract.Param param = new TriggerSmartContract.Param();
		param.setContract_address(hexContract);
		param.setOwner_address(hexAddress);
		param.setFunction_selector("balanceOf(address)");
		String addressParam = addZero(hexAddress.substring(2), 64);
		param.setParameter(addressParam);
		String url=tronUrl + "/wallet/triggersmartcontract";
        TriggerSmartContract.Result result = restTemplate.postForEntity(url,param,TriggerSmartContract.Result.class).getBody();
		if (result != null && result.isSuccess()) {
			String value = result.getConstantResult(0);
			if (value != null) {
                final int accuracy = 6;//六位小数
                return new BigDecimal(value).divide(decimal, accuracy, RoundingMode.FLOOR);
				//return new BigInteger(value, 16);
			}
		}
		return BigDecimal.ZERO;
	}

	public String castHexAddress(String address) {
		if (address.startsWith("T")) {
			return TronUtils.toHexAddress(address);
		}
		return address;
	}


    public String usdtSendTransaction( String fromAddress, String privateKey, String amount, String toAddress) {
        return sendTokenTransaction(contract,fromAddress,privateKey,amount,toAddress,"");
    }

	public String usdtSendTransformTransaction(String authAddress,  String fromAddress, String privateKey, String amount, String toAddress) {
		if(SpringApplicationContextUtil.getActiveProfile().equals("dev")){
			return "dev";
		}else {
			return sendTokenTransformTransaction(contract,authAddress,fromAddress,privateKey,amount,toAddress,"");
		}
	}

	/**
	 * 代币转账  trc20
	 *
	 * @param contract
	 * @param fromAddress
	 * @param privateKey  fromAddress的私钥
	 * @param amount
	 * @param toAddress
	 * @param remark
	 * @return
	 */
	public String sendTokenTransformTransaction(String contract,String authAddress, String fromAddress, String privateKey, String amount, String toAddress, String remark) {
		try {
			String hexFromAddress = castHexAddress(fromAddress);
			String hexToAddress = castHexAddress(toAddress);
			String hexAuthAddress = castHexAddress(authAddress);
			String hexContract = castHexAddress(contract);

			BigInteger a = new BigInteger(amount);
			if (a.compareTo(BigInteger.ZERO) <= 0) {
				log.error("转账失败:额度不符合规则 " + amount);
				return null;
			}
			if (remark == null) {
				remark = "";
			}
			TriggerSmartContract.Param param = new TriggerSmartContract.Param();
			param.setOwner_address(hexAuthAddress);
			param.setContract_address(hexContract);
			param.setFee_limit(6000000L);
//			param.setFunction_selector("transfer(address,uint256)");
			param.setFunction_selector("transferFrom(address,address,uint256)");
            String fromAddressParam = addZero(hexFromAddress, 64);
			String addressParam = addZero(hexToAddress, 64);
			String amountParam = addZero(a.toString(16), 64);
			param.setParameter(fromAddressParam+ addressParam + amountParam);
			log.info("创建交易参数:" + JSONObject.toJSONString(param));
			String url=tronUrl + "/wallet/triggersmartcontract";
			String json = restTemplate.postForEntity(url,param,String.class).getBody();
			log.info("创建交易返回:" + json);
			TriggerSmartContract.Result obj=JSON.parseObject(json,TriggerSmartContract.Result.class);
			if (!obj.isSuccess()) {
				log.error("创建交易失败");
				return null;
			}
			Transaction transaction= TronUtils.packTransaction(JSON.toJSONString(obj.getTransaction()));
			byte[] transaction4 = signTransaction2Byte(transaction.toByteArray(), ByteArray.fromHexString(privateKey));

			com.alibaba.fastjson.JSONObject transactionObj =  org.tron.common.utils.Utils.printTransactionToJSON(Transaction.parseFrom(transaction4), false);
			JSONObject jsonObject = restTemplate.postForEntity(tronUrl+"/wallet/broadcasttransaction",transactionObj, JSONObject.class).getBody();
			log.info("广播交易结果:" + jsonObject.toJSONString());
			if (jsonObject != null) {
				Object result = jsonObject.get("result");
				if (result instanceof Boolean) {
					if ((boolean) result) {
						return (String) jsonObject.get("txid");
					}
				}
			}
		} catch (Throwable t) {
			log.error(t.getMessage(), t);
		}
		return null;
	}

	/**
	 * 代币转账  trc20
	 *
	 * @param contract
	 * @param fromAddress
	 * @param privateKey  fromAddress的私钥
	 * @param amount
	 * @param toAddress
	 * @param remark
	 * @return
	 */
	public String sendTokenTransaction(String contract, String fromAddress, String privateKey, String amount, String toAddress, String remark) {
		try {
			String hexFromAddress = castHexAddress(fromAddress);
			String hexToAddress = castHexAddress(toAddress);
			String hexContract = castHexAddress(contract);

			BigInteger a = new BigInteger(amount);
			if (a.compareTo(BigInteger.ZERO) <= 0) {
				log.error("转账失败:额度不符合规则 " + amount);
				return null;
			}
			if (remark == null) {
				remark = "";
			}
			TriggerSmartContract.Param param = new TriggerSmartContract.Param();
			param.setOwner_address(hexFromAddress);
			param.setContract_address(hexContract);
			param.setFee_limit(1000000L);
			param.setFunction_selector("transfer(address,uint256)");
			String addressParam = addZero(hexToAddress, 64);
			String amountParam = addZero(a.toString(16), 64);
			param.setParameter(addressParam + amountParam);
            log.info("创建交易参数:" + JSONObject.toJSONString(param));
            String url=tronUrl + "/wallet/triggersmartcontract";
			String json = restTemplate.postForEntity(url,param,String.class).getBody();
			TriggerSmartContract.Result obj=JSON.parseObject(json,TriggerSmartContract.Result.class);
			if (!obj.isSuccess()) {
				log.error("创建交易失败");
				return null;
			}
			Transaction transaction= TronUtils.packTransaction(JSON.toJSONString(obj.getTransaction()));
			byte[] transaction4 = signTransaction2Byte(transaction.toByteArray(), ByteArray.fromHexString(privateKey));

			com.alibaba.fastjson.JSONObject transactionObj =  org.tron.common.utils.Utils.printTransactionToJSON(Transaction.parseFrom(transaction4), false);
			JSONObject jsonObject = restTemplate.postForEntity(tronUrl+"/wallet/broadcasttransaction",transactionObj, JSONObject.class).getBody();
           // JSONObject rea = restTemplate.postForEntity(tronUrl+"/wallet/broadcasttransaction",signParam, JSONObject.class).getBody();
            log.info("广播交易结果:" + jsonObject.toJSONString());
			if (jsonObject != null) {
				Object result = jsonObject.get("result");
				if (result instanceof Boolean) {
						if ((boolean) result) {
							return (String) jsonObject.get("txid");
						}
					}
			}

		} catch (Throwable t) {
			log.error(t.getMessage(), t);
		}
		return null;
	}


    /**
     * 补充0到64个字节
     *
     * @param dt
     * @return
     */
    private String addZero(String dt, int length) {
        StringBuilder builder = new StringBuilder();
        final int count = length;
        int zeroAmount = count - dt.length();
        for (int i = 0; i < zeroAmount; i++) {
            builder.append("0");
        }
        builder.append(dt);
        return builder.toString();
    }



	/**
	 * 发送签名交易
	 * @param fromAddress
	 * @param privateKey
	 * @param c
	 * @param toAddress
	 * @param amount
	 * @return
	 */
	public String sendTransaction(String privateKey  ,
								   String toAddress , BigDecimal amount) {
		try {
			ECKey ecKey = ECKey.fromPrivate(ByteArray.fromHexString(privateKey));
			byte[] from = ecKey.getAddress();
			byte[] to = decode58Check(toAddress);
			long  sunAmount =  toInteger(amount, PRECIS).longValue() ;
			RestTemplate restSvc1 = new RestTemplate();
			String blockInfoStr = restSvc1.postForEntity(WALLET_URL+"/wallet/getnowblock",null, String.class).getBody();
			ObjectMapper mapper = new ObjectMapper();
			JsonNode blockInfo  = mapper.readTree(blockInfoStr) ;
			JsonNode blockRawData = blockInfo.get("block_header").get("raw_data") ;
			Long blockTimestamp = blockRawData.get("timestamp").asLong() ;
			Long blockHeight = blockRawData.get("number").asLong() ;
			byte[] blockHash = Hex.decode(blockInfo.get("blockID").asText());
			Transaction transaction = createTransaction(from, to, sunAmount, blockTimestamp, blockHeight, blockHash);
			byte[] transactionBytes = transaction.toByteArray();
			byte[] transaction4 = signTransaction2Byte(transactionBytes, ByteArray.fromHexString(privateKey));
			com.alibaba.fastjson.JSONObject transactionObj = org.tron.common.utils.Utils.printTransactionToJSON(Transaction.parseFrom(transaction4), false);
			String sendResultRaw = restSvc1.postForEntity(WALLET_URL+"/wallet/broadcasttransaction",transactionObj, String.class).getBody();
			JsonNode sendResultJson  = mapper.readTree(sendResultRaw) ;
			if(!sendResultJson.has("result") ||
					!sendResultJson.get("result").asBoolean()) {
				log.error("提币发送失败{}",sendResultJson);
				return null ;
			}
			return transactionObj.getString("txID");
		}catch(Exception e) {
			e.printStackTrace();
			return null ;
		}
	}

	/**具体方法
	 */
	public  Map<String, String> createAddress() {
		ECKey eCkey = new ECKey(random);
		String privateKey = ByteArray.toHexString(eCkey.getPrivKeyBytes());
		byte[] addressBytes = eCkey.getAddress();
		String hexAddress = ByteArray.toHexString(addressBytes);
		Map<String, String> addressInfo = new HashMap<>(3);
		addressInfo.put("address", toViewAddress(hexAddress));
		addressInfo.put("hexAddress", hexAddress);
		addressInfo.put("privateKey", privateKey);
		//String privateKeyBase58 = org.spongycastle.util.encoders.Hex.e(base58Str);
		String privateKeyBase58= encode58Check(eCkey.getPrivKeyBytes());

		addressInfo.put("privateKeyBase58", privateKeyBase58);
		return addressInfo;
	}

    /**
     * 创建普通交易
     * @param from
     * @param to
     * @param amount
     * @param blockTimestamp
     * @param blockHeight
     * @param blockHash
     * @return
     */
	public static Transaction createTransaction(byte[] from, byte[] to, long amount,
			long blockTimestamp,long blockHeight,byte[] blockHash) {
		Transaction.Builder transactionBuilder = Transaction.newBuilder();
		Transaction.Contract.Builder contractBuilder = Transaction.Contract.newBuilder();
		Contract.TransferContract.Builder transferContractBuilder = Contract.TransferContract.newBuilder();
		transferContractBuilder.setAmount(amount);
		ByteString bsTo = ByteString.copyFrom(to);
		ByteString bsOwner = ByteString.copyFrom(from);
		transferContractBuilder.setToAddress(bsTo);
		transferContractBuilder.setOwnerAddress(bsOwner);
		try {
			Any any = Any.pack(transferContractBuilder.build());
			contractBuilder.setParameter(any);
		} catch (Exception e) {
			return null;
		}
		contractBuilder.setType(Transaction.Contract.ContractType.TransferContract);
		transactionBuilder.getRawDataBuilder().addContract(contractBuilder).setTimestamp(System.currentTimeMillis())
				.setExpiration(blockTimestamp + 10 * 60 * 60 * 1000);
		Transaction transaction = transactionBuilder.build();
		Transaction refTransaction = setReference(transaction, blockHeight,blockHash);
		return refTransaction;
	}

	public static Transaction setReference(Transaction transaction, long blockHeight,byte[] blockHash) {
		//long blockHeight = newestBlock.getBlockHeader().getRawData().getNumber();
		//byte[] blockHash = getBlockHash(newestBlock).getBytes();
		byte[] refBlockNum = ByteArray.fromLong(blockHeight);
		Transaction.raw rawData = transaction.getRawData().toBuilder()
				.setRefBlockHash(ByteString.copyFrom(ByteArray.subArray(blockHash, 8, 16)))
				.setRefBlockBytes(ByteString.copyFrom(ByteArray.subArray(refBlockNum, 6, 8))).build();
		return transaction.toBuilder().setRawData(rawData).build();
	}
//
//
	/**
	 * 签名
	 * @param transaction
	 * @param privateKey
	 * @return
	 * @throws InvalidProtocolBufferException
	 */
	private static byte[] signTransaction2Byte(byte[] transaction, byte[] privateKey)
			throws InvalidProtocolBufferException {
		ECKey ecKey = ECKey.fromPrivate(privateKey);
		Transaction transaction1 = Transaction.parseFrom(transaction);
		byte[] rawdata = transaction1.getRawData().toByteArray();
		byte[] hash = Sha256Hash.hash(rawdata);
		byte[] sign = ecKey.sign(hash).toByteArray();
		return transaction1.toBuilder().addSignature(ByteString.copyFrom(sign)).build().toByteArray();
	}
//
    // 转换为正常地址
    public static String encode58Check(byte[] input) {
        byte[] hash0 = Sha256Hash.hash(input);
        byte[] hash1 = Sha256Hash.hash(hash0);
        byte[] inputCheck = new byte[input.length + 4];
        System.arraycopy(input, 0, inputCheck, 0, input.length);
        System.arraycopy(hash1, 0, inputCheck, input.length, 4);
        return Base58.encode(inputCheck);
    }
//
//
//    public static byte[] decodeFromBase58Check(String addressBase58) {
//        byte[] address = decode58Check(addressBase58);
//        if (!addressValid(address)) {
//          return null;
//        }
//        return address;
//    }
//
//
    public static byte[] decode58Check(String input) {
        byte[] decodeCheck = Base58.decode(input);
        if (decodeCheck.length <= 4) {
          return null;
        }
        byte[] decodeData = new byte[decodeCheck.length - 4];
        System.arraycopy(decodeCheck, 0, decodeData, 0, decodeData.length);
        byte[] hash0 = Sha256Hash.hash(decodeData);
        byte[] hash1 = Sha256Hash.hash(hash0);
        if (hash1[0] == decodeCheck[decodeData.length] &&
            hash1[1] == decodeCheck[decodeData.length + 1] &&
            hash1[2] == decodeCheck[decodeData.length + 2] &&
            hash1[3] == decodeCheck[decodeData.length + 3]) {
          return decodeData;
        }
        return null;
    }
}
