package com.dwarfeng.subgrade.stack.bean.dto;

import java.util.List;

/**
 * 分页的数据。
 *
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public class PagedData<E> implements Dto {

    private static final long serialVersionUID = -3109709137312688572L;

    /**
     * 当前的页数，从0开始计数。
     */
    private int currentPage;
    /**
     * 总共的页数，该值最小是1。
     */
    private int totlePages;
    /**
     * 每页返回的行数。
     */
    private int rows;
    /**
     * 总共数量。
     */
    private long count;
    /**
     * 当前页数据。
     */
    private List<E> data;

    public PagedData() {
    }

    public PagedData(int currentPage, int totlePages, int rows, long count, List<E> data) {
        this.currentPage = currentPage;
        this.totlePages = totlePages;
        this.rows = rows;
        this.count = count;
        this.data = data;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotlePages() {
        return totlePages;
    }

    public void setTotlePages(int totlePages) {
        this.totlePages = totlePages;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<E> getData() {
        return data;
    }

    public void setData(List<E> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "PagedData{" +
                "currentPage=" + currentPage +
                ", totlePages=" + totlePages +
                ", rows=" + rows +
                ", count=" + count +
                ", data=" + data +
                '}';
    }
}
