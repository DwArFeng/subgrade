package com.dwarfeng.subgrade.stack.generation;

import com.dwarfeng.subgrade.stack.exception.GenerateException;

import java.util.ArrayList;
import java.util.List;

/**
 * 生成器。
 *
 * <p>
 * 用于生成对象。生成器生成的对象不做约束，可以为 <code>null</code> 在内的任意对象。<br>
 * 部分场景下，需要约束生成对象的规范，此时可以定义一个生成器接口的子接口，该子接口对生成的对象做出约束。
 *
 * <p>
 * 生成器可以生成单个对象，也可以生成多个对象，在某些场景下，批量生成对象的实现可能会比单个生成对象的实现更加高效。<br>
 * 在获取批量生成对象的方法时，应尽量使用 {@link #batchGenerate(int)}，而不是循环调用 {@link #generate()}。
 *
 * @author DwArFeng
 * @since 1.4.5
 */
public interface Generator<E> {

    /**
     * 生成对象。
     *
     * @return 生成的对象。
     * @throws GenerateException 生成异常。
     */
    E generate() throws GenerateException;

    /**
     * 批量生成对象。
     *
     * <p>
     * 批量生成对象的默认实现是调用 {@link #generate()} 方法，将返回的结果放入 {@link ArrayList} 中返回。
     *
     * <p>
     * 在某些场景下，批量生成对象的实现可能会比单个生成对象的实现更加高效，因此在实现该接口时，应尽量重写该方法。
     *
     * <p>
     * 重写该方法应该保证返回的列表不为 <code>null</code>，且大小为 <code>size</code>。<br>
     * 列表中的元素可以为 <code>null</code>，不做约束。
     *
     * @param size 生成的对象的数量。
     * @return 生成的对象组成的列表。
     * @throws GenerateException 生成异常。
     */
    default List<E> batchGenerate(int size) throws GenerateException {
        List<E> result = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            result.add(generate());
        }
        return result;
    }
}
