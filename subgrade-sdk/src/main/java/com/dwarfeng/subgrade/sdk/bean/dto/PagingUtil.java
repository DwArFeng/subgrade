package com.dwarfeng.subgrade.sdk.bean.dto;

import com.dwarfeng.subgrade.stack.bean.Bean;
import com.dwarfeng.subgrade.stack.bean.BeanTransformer;
import com.dwarfeng.subgrade.stack.bean.dto.PagedData;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 分页工具类。
 *
 * @author DwArFeng
 * @since 0.0.3-beta
 */
public final class PagingUtil {

    private PagingUtil() {
        throw new IllegalStateException("禁止外部实例化。");
    }

    /**
     * 根据分页信息、数据总量、当前页的数据构造一个分页信息。
     *
     * @param pagingInfo 分页信息。
     * @param count      总数据量。
     * @param data       当前页的数据。
     * @param <E>        数据的类型。
     * @return 构造的分页信息。
     */
    public static <E> PagedData<E> pagedData(PagingInfo pagingInfo, int count, List<E> data) {
        return new PagedData<>(
                pagingInfo.getPage(),
                count == 0 || pagingInfo.getRows() == 0 ? 0 : count / pagingInfo.getRows() + 1,
                pagingInfo.getRows(),
                count,
                data
        );
    }

    /**
     * 根据所有的数据构造一个分页信息。
     *
     * @param data 所有的数据组成的列表。
     * @param <E>  数据的类型。
     * @return 构造的分页信息。
     */
    public static <E> PagedData<E> pagedData(List<E> data) {
        return new PagedData<>(
                1,
                1,
                data.size(),
                data.size(),
                data
        );
    }

    /**
     * 获取分页信息对应的序号边界。
     *
     * @param pagingInfo 指定的分页信息。
     * @return 分页信息对应的序号边界。
     */
    public static IntIndexBounds intIndexBound(PagingInfo pagingInfo) {
        return intIndexBound(pagingInfo, Integer.MAX_VALUE);
    }

    /**
     * 获取分页信息对应的序号边界。
     *
     * @param pagingInfo 指定的分页信息。
     * @param totleSize  列表的总长度。
     * @return 分页信息对应的序号边界。
     */
    public static IntIndexBounds intIndexBound(PagingInfo pagingInfo, int totleSize) {
        int beginIndex = pagingInfo.getRows() * pagingInfo.getPage();
        int endIndex = Math.min(totleSize, beginIndex + pagingInfo.getRows()) - 1;
        return new IntIndexBounds(beginIndex, endIndex);
    }

    /**
     * 获取分页信息对应的序号边界。
     *
     * @param pagingInfo 指定的分页信息。
     * @return 分页信息对应的序号边界。
     */
    public static LongIndexBounds longIndexBound(PagingInfo pagingInfo) {
        return longIndexBound(pagingInfo, Long.MAX_VALUE);
    }

    /**
     * 获取分页信息对应的序号边界。
     *
     * @param pagingInfo 指定的分页信息。
     * @param totleSize  列表的总长度。
     * @return 分页信息对应的序号边界。
     */
    public static LongIndexBounds longIndexBound(PagingInfo pagingInfo, long totleSize) {
        long beginIndex = pagingInfo.getRows() * pagingInfo.getPage();
        long endIndex = Math.min(totleSize, beginIndex + pagingInfo.getRows()) - 1;
        return new LongIndexBounds(beginIndex, endIndex);
    }

    public static final class IntIndexBounds {

        private final int beginIndex;
        private final int endIndex;

        public IntIndexBounds(int beginIndex, int endIndex) {
            this.beginIndex = beginIndex;
            this.endIndex = endIndex;
        }

        public int getBeginIndex() {
            return beginIndex;
        }

        public int getEndIndex() {
            return endIndex;
        }

        @Override
        public String toString() {
            return "IntIndexBounds{" +
                    "beginIndex=" + beginIndex +
                    ", endIndex=" + endIndex +
                    '}';
        }
    }

    public static final class LongIndexBounds {

        private final long beginIndex;
        private final long endIndex;

        public LongIndexBounds(long beginIndex, long endIndex) {
            this.beginIndex = beginIndex;
            this.endIndex = endIndex;
        }

        public long getBeginIndex() {
            return beginIndex;
        }

        public long getEndIndex() {
            return endIndex;
        }

        @Override
        public String toString() {
            return "IntIndexBounds{" +
                    "beginIndex=" + beginIndex +
                    ", endIndex=" + endIndex +
                    '}';
        }
    }

    /**
     * 通过指定的Bean转换器将第一个类型的PagedData转换成第二个类型的PagedData。
     *
     * @param pagedData   第一个类型的PagedData。
     * @param transformer 第二个类型的PagedData。
     * @param <U>         第一个类型。
     * @param <V>         第二个类型。
     * @return 第二个类型的PagedData。
     */
    public static <U extends Bean, V extends Bean> PagedData<V>
    transform(@NonNull PagedData<U> pagedData, @NonNull BeanTransformer<U, V> transformer) {
        PagedData<V> p = new PagedData<>();
        p.setCount(pagedData.getCount());
        p.setCurrentPage(pagedData.getCurrentPage());
        p.setRows(pagedData.getRows());
        p.setTotalPages(pagedData.getTotalPages());
        p.setData(pagedData.getData().stream().map(transformer::transform).collect(Collectors.toList()));
        return p;
    }

    /**
     * 通过指定的Bean转换器将第二个类型的PagedData转换成第一个类型的PagedData。
     *
     * @param pagedData   第二个类型的PagedData。
     * @param transformer 第一个类型的PagedData。
     * @param <U>         第一个类型。
     * @param <V>         第二个类型。
     * @return 第一个类型的PagedData。
     */
    public static <U extends Bean, V extends Bean> PagedData<U>
    reverseTransform(@NonNull PagedData<V> pagedData, @NonNull BeanTransformer<U, V> transformer) {
        PagedData<U> p = new PagedData<>();
        p.setCount(pagedData.getCount());
        p.setCurrentPage(pagedData.getCurrentPage());
        p.setRows(pagedData.getRows());
        p.setTotalPages(pagedData.getTotalPages());
        p.setData(pagedData.getData().stream().map(transformer::reverseTransform).collect(Collectors.toList()));
        return p;
    }
}
