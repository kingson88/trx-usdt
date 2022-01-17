package com.yumiao.usdttransfer.api;

import com.yumiao.usdttransfer.base.BaseController;
import com.yumiao.usdttransfer.utils.Base58;
import com.yumiao.usdttransfer.utils.TronUtils;
import com.yumiao.usdttransfer.wallet.TRXWallet;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.spongycastle.util.encoders.Hex;

import java.io.UnsupportedEncodingException;

@Api(tags = "base58")
@RestController
@RequestMapping("/base58")
@Slf4j
public class Base58Controller extends BaseController {

    @Autowired
    private TRXWallet trxWallet;

    

    @ApiOperation(value = "encode")
    @GetMapping("/encode")
    public Object encode(
            @ApiParam(value = "hexString", required = true) @RequestParam(value = "hexString", required = true) String hexString
    ) {
        String result = Base58.encode(Hex.decode(hexString));
        return success(result);
    }
    @ApiOperation(value = "decode")
    @GetMapping("/decode")
    public Object decode(
            @ApiParam(value = "base58", required = true) @RequestParam(value = "base58", required = true) String base58) {
        byte[] base58Str= Base58.decode(base58);
        String result = Hex.toHexString(base58Str);
        return success(result);
    }


    @ApiOperation(value = "toViewAddress")
    @GetMapping("/toViewAddress")
    public Object toViewAddress(
            @ApiParam(value = "hexString", required = true) @RequestParam(value = "hexString", required = true) String hexString
    ) {
        String result =TronUtils.toViewAddress(hexString);
        //String hexFromAddress = trxWallet.castHexAddress(fromAddress);
        return success(result);
    }

    @ApiOperation(value = "toHexAddress")
    @GetMapping("/toHexAddress")
    public Object toHexAddress(
            @ApiParam(value = "base58", required = true) @RequestParam(value = "base58", required = true) String base58) {
        String result =  TronUtils.toHexAddress(base58);
        return success(result);
    }

}
