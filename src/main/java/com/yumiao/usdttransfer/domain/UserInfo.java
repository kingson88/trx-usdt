package com.yumiao.usdttransfer.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    private String mobile;

    private int userType;
}