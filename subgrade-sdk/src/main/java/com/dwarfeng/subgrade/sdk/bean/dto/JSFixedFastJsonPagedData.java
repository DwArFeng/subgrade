package com.dwarfeng.subgrade.sdk.bean.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.dwarfeng.subgrade.stack.bean.dto.Dto;
import com.dwarfeng.subgrade.stack.bean.dto.PagedData;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * 修正了 JS 精度问题的适用于 FastJson 的 PagedData。
 *
 * @author DwArFeng
 * @since 0.2.3-beta
 */
public class JSFixedFastJsonPagedData<E> implements Dto {

    private static final long serialVersionUID = 248702761438026673L;
    @JSONField(name = "current_page", ordinal = 1)
    private int currentPage;
    @JSONField(name = "total_pages", ordinal = 2)
    private int totalPages;
    @JSONField(name = "rows", ordinal = 3)
    private int rows;
    @JSONField(name = "count", ordinal = 4, serializeUsing = ToStringSerializer.class)
    private long count;
    @JSONField(name = "count", ordinal = 5)
    private List<E> data;

    public JSFixedFastJsonPagedData() {
    }

    public JSFixedFastJsonPagedData(int currentPage, int totalPages, int rows, long count, List<E> data) {
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.rows = rows;
        this.count = count;
        this.data = data;
    }

    /**
     * 通过指定的 PagedData 生成的 JSFixedFastJsonPagedData。
     *
     * @param pagedData 指定的 PagedData。
     * @param <E>       PageData 中的元素的类型。
     * @return 生成的 JSFixedFastJsonPagedData。
     */
    public static <E> JSFixedFastJsonPagedData<E> of(@NonNull PagedData<E> pagedData) {
        return new JSFixedFastJsonPagedData<>(
                pagedData.getCurrentPage(),
                pagedData.getTotalPages(),
                pagedData.getRows(),
                pagedData.getCount(),
                pagedData.getData()
        );
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
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
        return "JSFixedFastJsonPagedData{" +
                "currentPage=" + currentPage +
                ", totalPages=" + totalPages +
                ", rows=" + rows +
                ", count=" + count +
                ", data=" + data +
                '}';
    }
}
