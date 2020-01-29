package com.dwarfeng.subgrade.sdk.memory.io;

import com.dwarfeng.dutil.basic.prog.ProcessException;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;

import java.util.Map;

/**
 * 映射资源桥。
 * <p>该接口用于将外部的资源映射到内存 Map 中，或者将内存 Map 中的数据保存到资源中。</p>
 * <p>对于某些只读的资源，不支持 saveMap 方法，调用该方法会抛出 UnsupportedOperationException 异常。</p>
 *
 * @author DwArFeng
 * @since 0.0.3-beta
 */
public interface MapResourceBridge<K extends Key, E extends Entity<K>> {

    /**
     * 将资源中的数据填充到指定的映射中。
     *
     * @param map 指定的映射。
     * @throws NullPointerException 入口参数为 null。
     * @throws ProcessException     过程异常。
     */
    void fillMap(Map<K, E> map) throws NullPointerException, ProcessException;

    /**
     * 将指定映射中的数据保存到资源中。
     *
     * @param map 指定的映射。
     * @throws NullPointerException          入口参数为 null。
     * @throws ProcessException              过程异常。
     * @throws UnsupportedOperationException 不支持该方法。
     */
    void saveMap(Map<K, E> map) throws NullPointerException, ProcessException, UnsupportedOperationException;
}
