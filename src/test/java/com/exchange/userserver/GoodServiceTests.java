//package com.exchange.userserver;
//
//import com.alibaba.fastjson.JSON;
//import com.yumiao.usdttransfer.AppApplication;
//import com.yumiao.usdttransfer.domain.GoodInfoVo;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = AppApplication.class)
//public class GoodServiceTests {
//
//    @Autowired
//    private GoodService goodService;
//
//    @Test
//    public void getUserInfoById() throws Exception {
//        GoodInfoVo goodInfoVo= goodService.getGoodInfoByGoodId(1L);
//        System.out.println(JSON.toJSONString(goodInfoVo));
//    }
//
//}
