package com.dwarfeng.subgrade.sdk.memory.io;

import com.dwarfeng.dutil.basic.cna.model.ReferenceModel;
import com.dwarfeng.dutil.basic.prog.ProcessException;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;

/**
 * 引用资源桥。
 *
 * <p>
 * 该接口用于将外部的资源映射到内存 Reference 中，或者将内存 Reference 中的数据保存到资源中。
 *
 * <p>
 * 对于某些只读的资源，不支持 saveRef 方法，调用该方法会抛出 UnsupportedOperationException 异常。
 *
 * @author DwArFeng
 * @since 0.0.3-beta
 */
public interface RefResourceBridge<K extends Key, E extends Entity<K>> {

    /**
     * 将资源中的数据填充到指定的映射中。
     *
     * @param referenceModel 指定的引用模型。
     * @throws NullPointerException 入口参数为 null。
     * @throws ProcessException     过程异常。
     */
    void fillRef(ReferenceModel<E> referenceModel) throws NullPointerException, ProcessException;

    /**
     * 将指定映射中的数据保存到资源中。
     *
     * @param referenceModel 指定的引用模型。
     * @throws NullPointerException          入口参数为 null。
     * @throws ProcessException              过程异常。
     * @throws UnsupportedOperationException 不支持该方法。
     */
    void saveRef(ReferenceModel<E> referenceModel) throws NullPointerException, ProcessException, UnsupportedOperationException;
}
