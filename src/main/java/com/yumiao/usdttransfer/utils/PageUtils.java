package com.yumiao.usdttransfer.utils;


import com.yumiao.usdttransfer.domain.PageInfo;

import java.io.Serializable;
import java.util.List;

/**
 * 分页工具类
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2016年11月4日 下午12:59:00
 */
public class PageUtils extends PageInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    //总记录数
    private int totalCount;
    //总页数
    private int totalPage;

    //列表数据
    private List<?> list;

    /**
     * 分页
     *
     * @param list       列表数据
     * @param totalCount 总记录数
     * @param pageSize   每页记录数
     * @param page   当前页数
     */
    public PageUtils(List<?> list, int totalCount, int pageSize, int page) {
        this.list = list;
        this.totalCount = totalCount;
        this.setPageSize(pageSize);
        this.setPage(page);
        this.totalPage = (int) Math.ceil((double) totalCount / pageSize);
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }

}
