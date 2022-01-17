package com.yumiao.usdttransfer.domain;

import lombok.Data;

@Data
public class PageInfo {
    private Integer page = 1;
    private Integer pageSize = 10;
}
