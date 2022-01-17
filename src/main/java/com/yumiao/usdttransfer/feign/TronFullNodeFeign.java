//package com.yumiao.usdttransfer.feign;
//
//import com.alibaba.fastjson.JSONObject;
//import com.yumiao.usdttransfer.feign.dt.GetTransactionSign;
//import com.yumiao.usdttransfer.feign.dt.TriggerSmartContract;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//
//import feign.jackson.JacksonDecoder;
//import feign.jackson.JacksonEncoder;
//
//import java.util.Map;
//
//@FeignClient(url = "https://api.trongrid.io", name = "tron-node", configuration = {JacksonEncoder.class, JacksonDecoder.class})
//public interface TronFullNodeFeign {
//
//    /**
//     * 智能合约调用接口
//     *
//     * @param param
//     * @return
//     */
//    @PostMapping("/wallet/triggersmartcontract")
//    TriggerSmartContract.Result triggerSmartContract(@RequestBody TriggerSmartContract.Param param);
//
//
//    /**
//     * 使用私钥签名交易.
//     * （存在安全风险，trongrid已经关闭此接口服务，请使用离线方式或者自己部署的节点）
//     * @param param
//     * @return
//     */
//    @PostMapping("/wallet/gettransactionsign")
//    JSONObject getTransactionSign(@RequestBody GetTransactionSign.Param param);
//
//
//    /**
//     * 广播签名后的交易.
//     *
//     * @param rawBody
//     * @return
//     */
//    @PostMapping("/wallet/broadcasttransaction")
//    JSONObject broadcastTransaction(@RequestBody Object rawBody);
//
//
//    /**
//     * 生成随机私钥和相应的账户地址.
//     *（存在安全风险，trongrid已经关闭此接口服务，请使用离线方式或者使用自己部署的节点）
//     * @return
//     */
//    @PostMapping("/wallet/generateaddress")
//    JSONObject generateAddress();
//
//    /**
//     * 获取账号信息
//     * 查询一个账号的信息, 包括余额, TRC10 余额, 冻结资源, 权限等.
//     * @param param
//     * @return
//     */
//    @PostMapping("/wallet/getaccount")
//    JSONObject getAccount(@RequestBody Map<String, Object> param);
//
//}
