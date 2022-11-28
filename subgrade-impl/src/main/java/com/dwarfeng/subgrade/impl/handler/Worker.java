package com.dwarfeng.subgrade.impl.handler;

/**
 * 工作器。
 *
 * @author DwArFeng
 * @since 1.3.0
 */
public interface Worker {

    /**
     * 开始工作。
     *
     * @throws Exception 开始工作过程中发生的任何异常。
     */
    void work() throws Exception;

    /**
     * 停止工作。
     *
     * @throws Exception 停止工作过程中发生的任何异常。
     */
    void rest() throws Exception;
}
