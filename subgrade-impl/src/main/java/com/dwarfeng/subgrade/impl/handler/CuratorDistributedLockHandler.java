package com.dwarfeng.subgrade.impl.handler;

import com.dwarfeng.subgrade.stack.exception.HandlerException;
import com.dwarfeng.subgrade.stack.handler.DistributedLockHandler;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
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

    private final CuratorFramework curatorFramework;
    private final String leaderLatchPath;
    private final Worker worker;

    private final Lock lock = new ReentrantLock();
    private final InternalLeaderLatchListener leaderLatchListener = new InternalLeaderLatchListener();

    private boolean onlineFlag = false;
    private LeaderLatch leaderLatch = null;
    private boolean lockHoldingFlag = false;
    private boolean startedFlag = false;

    private boolean workingFlag = false;

    public CuratorDistributedLockHandler(CuratorFramework curatorFramework, String leaderLatchPath, Worker worker) {
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
            LOGGER.info("驱动器上线...");

            leaderLatch = new LeaderLatch(curatorFramework, leaderLatchPath);
            leaderLatch.addListener(leaderLatchListener);
            leaderLatch.start();
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

            rest();
            leaderLatch.close(LeaderLatch.CloseMode.NOTIFY_LEADER);
            leaderLatch = null;
            onlineFlag = false;
        } catch (Exception e) {
            HandlerUtil.transformAndThrowException(e);
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
            LOGGER.info("驱动器启动...");

            if (lockHoldingFlag) {
                work();
            }
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

            rest();
            startedFlag = false;
        } catch (HandlerException e) {
            throw e;
        } catch (Exception e) {
            throw new HandlerException(e);
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
        LOGGER.info("驱动器开始工作...");

        worker.work();
        workingFlag = true;
    }

    private void rest() throws Exception {
        if (!workingFlag) {
            return;
        }

        // 记录日志。
        LOGGER.info("驱动器停止工作...");

        worker.rest();
        workingFlag = false;
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
                LOGGER.info("驱动器持有锁...");

                try {
                    if (startedFlag) {
                        work();
                    }
                } catch (Exception e) {
                    LOGGER.warn("驱动器开始工作调度发生异常, 工作将不会开始, 异常信息如下:", e);
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
                LOGGER.info("驱动器释放锁...");

                try {
                    rest();
                } catch (Exception e) {
                    String message = "驱动器停止工作调度发生异常, 分布式锁逻辑被破坏, 异常信息如下:";
                    LOGGER.error(message, e);
                }
                lockHoldingFlag = false;
            } finally {
                lock.unlock();
            }
        }
    }
}
