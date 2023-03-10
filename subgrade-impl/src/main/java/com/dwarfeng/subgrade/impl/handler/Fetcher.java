package com.dwarfeng.subgrade.impl.handler;

/**
 * 抓取器。
 *
 * @author DwArFeng
 * @since 1.3.0
 */
public interface Fetcher<K, V> {

    /**
     * 判断指定的键对应的值是否存在。
     *
     * @param key 指定的键。
     * @return 指定的键对应的值是否存在。
     * @throws Exception 判断是否存在过程中出现的任何异常。
     */
    boolean exists(K key) throws Exception;

    /**
     * 抓取指定的键对应的值。
     *
     * @param key 指定的键。
     * @return 指定的键对应的值。
     * @throws Exception 抓取值的过程中出现的任何异常。
     */
    V fetch(K key) throws Exception;
}
