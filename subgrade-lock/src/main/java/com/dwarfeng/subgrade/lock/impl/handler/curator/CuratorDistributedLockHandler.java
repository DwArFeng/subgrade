package com.dwarfeng.subgrade.lock.impl.handler.curator;

import com.dwarfeng.subgrade.basic.sdk.exception.HandlerExceptionHelper;
import com.dwarfeng.subgrade.basic.stack.exception.HandlerException;
import com.dwarfeng.subgrade.lifecycle.stack.handler.Worker;
import com.dwarfeng.subgrade.lock.internal.i18n.LockMessageKey;
import com.dwarfeng.subgrade.lock.internal.i18n.LockMessages;
import com.dwarfeng.subgrade.lock.stack.handler.DistributedLockHandler;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
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
public class CuratorDistributedLockHandler implements DistributedLockHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CuratorDistributedLockHandler.class);

    @NotNull
    private CuratorFramework curatorFramework;
    @NotNull
    private String leaderLatchPath;
    @NotNull
    private Worker worker;

    private final Lock lock = new ReentrantLock();
    private final InternalLeaderLatchListener leaderLatchListener = new InternalLeaderLatchListener();

    private boolean onlineFlag = false;
    private LeaderLatch leaderLatch = null;
    private boolean lockHoldingFlag = false;
    private boolean startedFlag = false;

    private boolean workingFlag = false;

    public CuratorDistributedLockHandler(
            @NotNull CuratorFramework curatorFramework,
            @NotNull String leaderLatchPath,
            @NotNull Worker worker
    ) {
        this.curatorFramework = curatorFramework;
        this.leaderLatchPath = leaderLatchPath;
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
            LOGGER.info(LockMessages.message(LockMessageKey.LOG_ONLINE));

            leaderLatch = new LeaderLatch(curatorFramework, leaderLatchPath);
            leaderLatch.addListener(leaderLatchListener);
            leaderLatch.start();
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
            LOGGER.info(LockMessages.message(LockMessageKey.LOG_OFFLINE));

            rest();
            leaderLatch.close(LeaderLatch.CloseMode.NOTIFY_LEADER);
            leaderLatch = null;
            onlineFlag = false;
        } catch (Exception e) {
            throw HandlerExceptionHelper.parse(e);
        } finally {
            lock.unlock();
        }
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
            LOGGER.info(LockMessages.message(LockMessageKey.LOG_START));

            if (lockHoldingFlag) {
                work();
            }
            startedFlag = true;
        } catch (Exception e) {
            throw HandlerExceptionHelper.parse(e);
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
            LOGGER.info(LockMessages.message(LockMessageKey.LOG_STOP));

            rest();
            startedFlag = false;
        } catch (Exception e) {
            throw HandlerExceptionHelper.parse(e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean isLockHolding() {
        lock.lock();
        try {
            return lockHoldingFlag;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean isWorking() {
        lock.lock();
        try {
            return workingFlag;
        } finally {
            lock.unlock();
        }
    }

    private void work() throws Exception {
        if (workingFlag) {
            return;
        }

        // 记录日志。
        LOGGER.info(LockMessages.message(LockMessageKey.LOG_WORK));

        worker.work();
        workingFlag = true;
    }

    private void rest() throws Exception {
        if (!workingFlag) {
            return;
        }

        // 记录日志。
        LOGGER.info(LockMessages.message(LockMessageKey.LOG_REST));

        worker.rest();
        workingFlag = false;
    }

    @NotNull
    public CuratorFramework getCuratorFramework() {
        lock.lock();
        try {
            return curatorFramework;
        } finally {
            lock.unlock();
        }
    }

    public void setCuratorFramework(@NotNull CuratorFramework curatorFramework) {
        lock.lock();
        try {
            this.curatorFramework = curatorFramework;
        } finally {
            lock.unlock();
        }
    }

    @NotNull
    public String getLeaderLatchPath() {
        lock.lock();
        try {
            return leaderLatchPath;
        } finally {
            lock.unlock();
        }
    }

    public void setLeaderLatchPath(@NotNull String leaderLatchPath) {
        lock.lock();
        try {
            this.leaderLatchPath = leaderLatchPath;
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
        return "CuratorDistributedLockHandler{" +
                "curatorFramework=" + curatorFramework +
                ", leaderLatchPath='" + leaderLatchPath + '\'' +
                ", worker=" + worker +
                ", onlineFlag=" + onlineFlag +
                ", leaderLatch=" + leaderLatch +
                ", lockHoldingFlag=" + lockHoldingFlag +
                ", startedFlag=" + startedFlag +
                ", workingFlag=" + workingFlag +
                '}';
    }

    private class InternalLeaderLatchListener implements LeaderLatchListener {

        @Override
        public void isLeader() {
            lock.lock();
            try {
                if (lockHoldingFlag) {
                    return;
                }

                // 日志记录。
                LOGGER.info(LockMessages.message(LockMessageKey.LOG_LOCK_HELD));

                try {
                    if (startedFlag) {
                        work();
                    }
                } catch (Exception e) {
                    LOGGER.warn(LockMessages.message(LockMessageKey.LOG_WORK_FAILED), e);
                }
                lockHoldingFlag = true;
            } finally {
                lock.unlock();
            }
        }

        @Override
        public void notLeader() {
            lock.lock();
            try {
                if (!lockHoldingFlag) {
                    return;
                }

                // 日志记录。
                LOGGER.info(LockMessages.message(LockMessageKey.LOG_LOCK_RELEASED));

                try {
                    rest();
                } catch (Exception e) {
                    LOGGER.error(LockMessages.message(LockMessageKey.LOG_REST_FAILED), e);
                }
                lockHoldingFlag = false;
            } finally {
                lock.unlock();
            }
        }
    }
}
