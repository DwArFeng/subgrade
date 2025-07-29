package com.dwarfeng.subgrade.sdk.bean.dto;

import com.dwarfeng.subgrade.stack.bean.Bean;
import com.dwarfeng.subgrade.stack.bean.BeanTransformer;
import com.dwarfeng.subgrade.stack.bean.dto.PagedData;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 分页工具类。
 *
 * <p>
 * 需要注意的是，在 1.5.0 版本中，查询服务、缓存、数据访问层应用了新的分页特性，
 * 即当分页信息的页数小于 0 或每页行数小于 0 时，查询全部数据。<br>
 * 上述新的特性只应用在了服务、缓存、数据访问层中，而没有应用在工具类中。也就是说，您在调用工具类中的方法时，
 * 仍然需要保证分页信息的页数和行数是合法的。<br>
 *
 * <p>
 * 在 1.5.0 版本之前，该工具类默认输入数据合法，即页数大于等于 0, 行数大于等于 0。但在大量的实践中发现，
 * 部分开发人员习惯使用页数或每页行数小于 0 的分页信息
 * （即 {@link PagingInfo#getPage()} 为负数或 {@link PagingInfo#getRows()} 为负数）表示不分页。
 * 这些行为导致工具类经常以预期之外的方式工作，并产生了一些不可预期的结果。<br>
 * 在 1.5.0 版本中，该工具类不再默认输入数据合法，并且对输入数据的合法性进行了检查，在输入数据不合法时，
 * 会抛出异常。这样做的目的是为了让工具类的行为更加可预测，更加符合预期。<br>
 * 应用合法性校验的方法会在方法的文档中进行标注。在 1.5.0 版本之前已经弃用的方法保持原有的行为，不会进行合法性校验。
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
     * <p>
     * 必须保证 <code>pagingInfo</code> 参数的页数和行数是合法的，即
     * <code>page &gt;= 0</code> 且 <code>rows &gt;= 0</code>，否则会抛出异常。
     *
     * <p>
     * 必须保证 <code>count</code> 参数是合法的，即 <code>count &gt;= 0</code>，否则会抛出异常。
     *
     * @param pagingInfo 分页信息。
     * @param count      总数据量。
     * @param data       当前页的数据。
     * @param <E>        数据的类型。
     * @return 构造的分页信息。
     * @throws IllegalArgumentException 参数不合法。
     */
    public static <E> PagedData<E> pagedData(PagingInfo pagingInfo, int count, List<E> data) {
        int page = pagingInfo.getPage();
        int rows = pagingInfo.getRows();
        if (page < 0 || rows < 0) {
            throw new IllegalArgumentException("分页信息的页数必须大于等于 0, 行数必须大于等于 0");
        }
        if (count < 0) {
            throw new IllegalArgumentException("数据总量必须大于等于 0");
        }
        int totalPages;
        if (rows == 0) {
            totalPages = -1;
        } else if (count == 0) {
            totalPages = 0;
        } else {
            totalPages = (count - 1) / rows + 1;
        }
        return new PagedData<>(pagingInfo.getPage(), totalPages, rows, count, data);
    }

    /**
     * 根据所有的数据构造一个分页信息。
     *
     * @param data 所有的数据组成的列表。
     * @param <E>  数据的类型。
     * @return 构造的分页信息。
     */
    public static <E> PagedData<E> pagedData(List<E> data) {
        return new PagedData<>(0, 1, data.size(), data.size(), data);
    }

    /**
     * 获取分页信息对应的序号边界。
     *
     * @param pagingInfo 指定的分页信息。
     * @return 分页信息对应的序号边界。
     * @deprecated 请使用工具类中的 {@link #subList(List, PagingInfo)} 方法。
     */
    @Deprecated
    public static IntIndexBounds intIndexBound(PagingInfo pagingInfo) {
        return intIndexBound(pagingInfo, Integer.MAX_VALUE);
    }

    /**
     * 获取分页信息对应的序号边界。
     *
     * @param pagingInfo 指定的分页信息。
     * @param totalSize  列表的总长度。
     * @return 分页信息对应的序号边界。
     * @deprecated 请使用工具类中的 {@link #subList(List, PagingInfo)} 方法。
     */
    @Deprecated
    public static IntIndexBounds intIndexBound(PagingInfo pagingInfo, int totalSize) {
        int beginIndex = pagingInfo.getRows() * pagingInfo.getPage();
        int endIndex = Math.min(totalSize, beginIndex + pagingInfo.getRows()) - 1;
        return new IntIndexBounds(beginIndex, endIndex);
    }

    /**
     * 获取分页信息对应的序号边界。
     *
     * @param pagingInfo 指定的分页信息。
     * @return 分页信息对应的序号边界。
     * @deprecated 请使用工具类中的 {@link #subList(List, PagingInfo)} 方法。
     */
    @Deprecated
    public static LongIndexBounds longIndexBound(PagingInfo pagingInfo) {
        return longIndexBound(pagingInfo, Long.MAX_VALUE);
    }

    /**
     * 获取分页信息对应的序号边界。
     *
     * @param pagingInfo 指定的分页信息。
     * @param totalSize  列表的总长度。
     * @return 分页信息对应的序号边界。
     * @deprecated 请使用工具类中的 {@link #subList(List, PagingInfo)} 方法。
     */
    @Deprecated
    public static LongIndexBounds longIndexBound(PagingInfo pagingInfo, long totalSize) {
        long beginIndex = (long) pagingInfo.getRows() * pagingInfo.getPage();
        long endIndex = Math.min(totalSize, beginIndex + pagingInfo.getRows()) - 1;
        return new LongIndexBounds(beginIndex, endIndex);
    }

    /**
     * @deprecated 请使用工具类中的 {@link #subList(List, PagingInfo)} 方法。
     */
    @Deprecated
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

    /**
     * @deprecated 请使用工具类中的 {@link #subList(List, PagingInfo)} 方法。
     */
    @Deprecated
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
    @SuppressWarnings("DuplicatedCode")
    public static <U extends Bean, V extends Bean> PagedData<V> transform(
            @Nonnull PagedData<U> pagedData, @Nonnull BeanTransformer<U, V> transformer
    ) {
        PagedData<V> p = new PagedData<>();
        p.setCount(pagedData.getCount());
        p.setCurrentPage(pagedData.getCurrentPage());
        p.setRows(pagedData.getRows());
        p.setTotalPages(pagedData.getTotalPages());
        p.setData(
                Optional.ofNullable(pagedData.getData()).map(
                        f -> f.stream().map(transformer::transform).collect(Collectors.toList())
                ).orElse(null)
        );
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
    @SuppressWarnings("DuplicatedCode")
    public static <U extends Bean, V extends Bean> PagedData<U> reverseTransform(
            @Nonnull PagedData<V> pagedData, @Nonnull BeanTransformer<U, V> transformer
    ) {
        PagedData<U> p = new PagedData<>();
        p.setCount(pagedData.getCount());
        p.setCurrentPage(pagedData.getCurrentPage());
        p.setRows(pagedData.getRows());
        p.setTotalPages(pagedData.getTotalPages());
        p.setData(
                Optional.ofNullable(pagedData.getData()).map(
                        f -> f.stream().map(transformer::reverseTransform).collect(Collectors.toList())
                ).orElse(null)
        );
        return p;
    }

    /**
     * 返回指定的列表在指定的分页上对应的子列表。
     *
     * <p>
     * 必须保证 <code>pagingInfo</code> 参数的页数和行数是合法的，即
     * <code>page &gt;= 0</code> 且 <code>rows &gt;= 0</code>，
     * 否则会抛出异常。
     *
     * @param list       指定的列表。
     * @param pagingInfo 指定的分页。
     * @param <E>        列表中的元素类型。
     * @return 指定的列表在指定的分页上对应的子列表。
     * @throws IllegalArgumentException 参数不合法。
     */
    public static <E> List<E> subList(List<E> list, PagingInfo pagingInfo) {
        int page = pagingInfo.getPage();
        int rows = pagingInfo.getRows();
        if (page < 0 || rows < 0) {
            throw new IllegalArgumentException("分页信息的页数必须大于等于 0, 行数必须大于等于 0");
        }
        if (rows == 0) {
            return Collections.emptyList();
        }
        int beginIndex = rows * page;
        if (beginIndex >= list.size()) {
            return Collections.emptyList();
        }
        int endIndex = Math.min(list.size(), beginIndex + rows);
        return list.subList(beginIndex, endIndex);
    }
}
