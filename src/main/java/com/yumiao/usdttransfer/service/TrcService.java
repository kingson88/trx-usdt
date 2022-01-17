package com.yumiao.usdttransfer.service;

import com.yumiao.usdttransfer.exception.BizException;
import com.yumiao.usdttransfer.utils.*;
import com.yumiao.usdttransfer.wallet.TRXWallet;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.spongycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Slf4j
@Service
public class TrcService {

    @Autowired
    private TRXWallet trxWallet;

    public String usdttransferFrom(String from,String to,String privateKey,String tranNum) {
        log.info("from:{},to:{},privateKey:{},tran_num:{}",from,to,privateKey,tranNum);
        if(StringUtils.isEmpty(from)){
            throw new BizException("from 地址错误");
        }
        if(StringUtils.isEmpty(to)){
            throw new BizException("to 地址错误");
        }
        if(StringUtils.isEmpty(privateKey)){
            throw new BizException("privateKey 地址错误");
        }

        String result="";
        String authAddress= TronUtils.getAddressByPrivateKey(privateKey);
        log.info("from:{},to:{},privateKey:{},tran_num:{},authAddress:{}",from,to,privateKey,tranNum,authAddress);
        log.info("start usdtSendTransformTransaction:authAddress{},from:{},to:{},tranNum:{}",authAddress,from,to,tranNum);
        result= trxWallet.usdtSendTransformTransaction(authAddress,from,privateKey,tranNum,to);

        return  result;
    }

    public String getTransactionById(String txid){
        return trxWallet.getTransactionById(txid);
    }
}
