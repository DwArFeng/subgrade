package com.dwarfeng.subgrade.impl.handler;

import com.dwarfeng.subgrade.stack.exception.HandlerException;
import com.dwarfeng.subgrade.stack.handler.StartableHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 可启动处理器的通用实现。
 *
 * <p>
 * 本处理器实现线程安全。
 *
 * @author DwArFeng
 * @since 1.3.0
 */
public class GeneralStartableHandler implements StartableHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneralStartableHandler.class);

    @Nonnull
    private Worker worker;

    private final Lock lock = new ReentrantLock();
    private boolean startedFlag = false;

    public GeneralStartableHandler(@Nonnull Worker worker) {
        this.worker = worker;
    }

    @Override
    public boolean isStarted() {
        lock.lock();
        try {
            return startedFlag;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void start() throws HandlerException {
        lock.lock();
        try {
            if (startedFlag) {
                return;
            }

            // 日志记录。
            LOGGER.info("驱动器启动...");

            worker.work();
            startedFlag = true;
        } catch (Exception e) {
            HandlerUtil.transformAndThrowException(e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void stop() throws HandlerException {
        lock.lock();
        try {
            if (!startedFlag) {
                return;
            }

            // 日志记录。
            LOGGER.info("驱动器停止...");

            worker.rest();
            startedFlag = false;
        } catch (Exception e) {
            HandlerUtil.transformAndThrowException(e);
        } finally {
            lock.unlock();
        }
    }

    @Nonnull
    public Worker getWorker() {
        lock.lock();
        try {
            return worker;
        } finally {
            lock.unlock();
        }
    }

    public void setWorker(@Nonnull Worker worker) {
        lock.lock();
        try {
            this.worker = worker;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String toString() {
        return "GeneralStartableHandler{" +
                "worker=" + worker +
                ", startedFlag=" + startedFlag +
                '}';
    }
}
