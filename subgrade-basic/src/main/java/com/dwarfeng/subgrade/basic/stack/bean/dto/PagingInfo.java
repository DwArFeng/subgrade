package com.dwarfeng.subgrade.basic.stack.bean.dto;

import java.io.Serial;

/**
 * 分页信息对象。
 *
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public class PagingInfo implements Dto {

    public static final PagingInfo FIRST_ONE = new PagingInfo(0, 1);

    @Serial
    private static final long serialVersionUID = 402462824806679692L;

    /**
     * 查询的页数。
     */
    private int page;

    /**
     * 每页返回的行数。
     */
    private int rows;

    public PagingInfo() {
    }

    public PagingInfo(int page, int rows) {
        this.page = page;
        this.rows = rows;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    @Override
    public String toString() {
        return "PagingInfo{" +
                "page=" + page +
                ", rows=" + rows +
                '}';
    }
}
