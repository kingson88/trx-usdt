package com.yumiao.usdttransfer.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class GoodInfoVo {
    private Long id;
    private String goodsName;
    private Long goodsType;
}
