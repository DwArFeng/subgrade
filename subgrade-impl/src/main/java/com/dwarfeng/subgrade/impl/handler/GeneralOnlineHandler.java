package com.dwarfeng.subgrade.impl.handler;

import com.dwarfeng.subgrade.stack.exception.HandlerException;
import com.dwarfeng.subgrade.stack.handler.OnlineHandler;
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

    private final Worker worker;

    private final Lock lock = new ReentrantLock();
    private boolean onlineFlag = false;

    public GeneralOnlineHandler(Worker worker) {
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
            LOGGER.info("驱动器上线...");

            worker.work();
            onlineFlag = true;
        } catch (Exception e) {
            HandlerUtil.transformAndThrowException(e);
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
            LOGGER.info("驱动器下线...");

            worker.rest();
            onlineFlag = false;
        } catch (Exception e) {
            HandlerUtil.transformAndThrowException(e);
        } finally {
            lock.unlock();
        }
    }
}
