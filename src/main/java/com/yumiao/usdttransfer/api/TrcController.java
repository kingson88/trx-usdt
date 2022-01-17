package com.yumiao.usdttransfer.api;


import com.yumiao.usdttransfer.base.BaseController;
import com.yumiao.usdttransfer.domain.PageInfo;
import com.yumiao.usdttransfer.service.TrcService;
import com.yumiao.usdttransfer.utils.Base58;
import com.yumiao.usdttransfer.utils.PageUtils;
import com.yumiao.usdttransfer.utils.StringUtils;
import com.yumiao.usdttransfer.utils.TronUtils;
import com.yumiao.usdttransfer.wallet.TRXWallet;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.spongycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "usdt转账-trc")
@RestController
@RequestMapping("/trc")
@Slf4j
public class TrcController extends BaseController {

    @Autowired
    private TRXWallet trxWallet;

    @Autowired
    private TrcService trcService;


    @ApiOperation(value = "转账")
    @GetMapping("/createAddress")
    public Object createAddress() {
        Map result= trxWallet.createAddress();
        return success(result);
    }

    @ApiOperation(value = "授权转账")
    @RequestMapping(value = "/usdttransferform",method ={RequestMethod.POST,RequestMethod.GET})
    public Object transfer(
            @ApiParam(value = "from", required = false) @RequestParam(value = "from", required = false)String from,
            @ApiParam(value = "to", required = false) @RequestParam(value = "to", required = false)String to,
            @ApiParam(value = "privateKey", required = false) @RequestParam(value = "privateKey", required = false)String privateKey,
            @ApiParam(value = "tran_num", required = false) @RequestParam(value = "tran_num", required = false)String tran_num
    ) {
        String result= trcService.usdttransferFrom(from,to,privateKey,tran_num);
        return success(result);
    }

    @ApiOperation(value = "USDT转账")
    @GetMapping("/usdttransfer")
    public Object totransfer(
            @ApiParam(value = "privateKey", required = true) @RequestParam(value = "privateKey", required = true)String privateKey,
            @ApiParam(value = "to", required = true) @RequestParam(value = "to", required = true)String to,
            @ApiParam(value = "tran_num", required = true) @RequestParam(value = "tran_num", required = true)String tran_num
    ) {
        log.info("to:{},privateKey:{},tran_num:{}",to,privateKey,tran_num);
        String authAddress= TronUtils.getAddressByPrivateKey(privateKey);
        log.info("to:{},privateKey:{},tran_num:{},authAddress:{}",to,privateKey,tran_num,authAddress);
        String result= trxWallet.usdtSendTransaction(authAddress,privateKey,tran_num,to);
        return success(result);
    }

    @ApiOperation(value = "TRX转账")
    @GetMapping("/trxtransfer")
    public Object trxtransfer(
            @ApiParam(value = "privateKey", required = true) @RequestParam(value = "privateKey", required = true)String privateKey,
            @ApiParam(value = "to", required = true) @RequestParam(value = "to", required = true)String to,
            @ApiParam(value = "tran_num", required = true) @RequestParam(value = "tran_num", required = true)BigDecimal tranNum
    ) {
        log.info("to:{},privateKey:{},tran_num:{}",to,privateKey,tranNum);
        log.info("from:{},to:{},privateKey:{},tran_num:{}",to,privateKey,tranNum);
        String result= trxWallet.sendTransaction(privateKey,to, tranNum);
        return success(result);
    }


    @ApiOperation(value = "交易信息")
    @GetMapping("/getTransactionById")
    public Object getTransactionById(
            @ApiParam(value = "txId", required = true) @RequestParam(value = "txId", required = true)String txId) {
        String  json=trcService.getTransactionById(txId);
        return success(json);
    }


    @ApiOperation(value = "余额")
    @GetMapping("/balance")
    public Object balance(
            @ApiParam(value = "address", required = true) @RequestParam(value = "address", required = true)String address) {
        Map<String,BigDecimal> result=new HashMap<>();
        BigDecimal balance=trxWallet.usdtBalanceOf(address);
        result.put("usdt",balance);
        BigDecimal trx=trxWallet.balanceOfTron(address);
        result.put("trx",trx);
        return success(result);
    }

    @ApiOperation(value = "根据私钥获取地址")
    @GetMapping("/getAddressByPrivateKey")
    public Object getAddressByPrivateKey(
            @ApiParam(value = "privateKey", required = false) @RequestParam(value = "privateKey", required = false)String privateKey,
            @ApiParam(value = "privateKeybase58", required = false) @RequestParam(value = "privateKeybase58", required = false)String privateKeybase58) {
        String authAddress="";
        if(StringUtils.isNotEmpty(privateKeybase58)){
            byte[] base58Str= Base58.decode(privateKeybase58);
            privateKey = Hex.toHexString(base58Str);
            authAddress = TronUtils.getAddressByPrivateKey(privateKey);
        }
        else {
            authAddress = TronUtils.getAddressByPrivateKey(privateKey);
        }
        return success(authAddress);
    }


}
