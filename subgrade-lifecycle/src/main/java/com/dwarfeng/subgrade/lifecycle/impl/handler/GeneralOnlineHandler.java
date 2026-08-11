package com.dwarfeng.subgrade.lifecycle.impl.handler;

import com.dwarfeng.subgrade.basic.sdk.exception.HandlerExceptionHelper;
import com.dwarfeng.subgrade.basic.stack.exception.HandlerException;
import com.dwarfeng.subgrade.lifecycle.internal.i18n.LifecycleMessageKey;
import com.dwarfeng.subgrade.lifecycle.internal.i18n.LifecycleMessages;
import com.dwarfeng.subgrade.lifecycle.stack.handler.OnlineHandler;
import com.dwarfeng.subgrade.lifecycle.stack.handler.Worker;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 线上处理器的通用实现。
 *
 * <p>
 * 本处理器实现线程安全。
 *
 * @author DwArFeng
 * @since 1.3.0
 */
public class GeneralOnlineHandler implements OnlineHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneralOnlineHandler.class);

    @NotNull
    private Worker worker;

    private final Lock lock = new ReentrantLock();
    private boolean onlineFlag = false;

    public GeneralOnlineHandler(@NotNull Worker worker) {
        this.worker = worker;
    }

    @Override
    public boolean isOnline() {
        lock.lock();
        try {
            return onlineFlag;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void online() throws HandlerException {
        lock.lock();
        try {
            if (onlineFlag) {
                return;
            }

            // 日志记录。
            LOGGER.info(LifecycleMessages.message(LifecycleMessageKey.LOG_ONLINE));

            worker.work();
            onlineFlag = true;
        } catch (Exception e) {
            throw HandlerExceptionHelper.parse(e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void offline() throws HandlerException {
        lock.lock();
        try {
            if (!onlineFlag) {
                return;
            }

            // 日志记录。
            LOGGER.info(LifecycleMessages.message(LifecycleMessageKey.LOG_OFFLINE));

            worker.rest();
            onlineFlag = false;
        } catch (Exception e) {
            throw HandlerExceptionHelper.parse(e);
        } finally {
            lock.unlock();
        }
    }

    @NotNull
    public Worker getWorker() {
        lock.lock();
        try {
            return worker;
        } finally {
            lock.unlock();
        }
    }

    public void setWorker(@NotNull Worker worker) {
        lock.lock();
        try {
            this.worker = worker;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String toString() {
        return "GeneralOnlineHandler{" +
                "worker=" + worker +
                ", onlineFlag=" + onlineFlag +
                '}';
    }
}
