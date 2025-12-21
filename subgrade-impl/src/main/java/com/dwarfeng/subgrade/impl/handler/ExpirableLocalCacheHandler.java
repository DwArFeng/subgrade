package com.dwarfeng.subgrade.impl.handler;

import com.dwarfeng.subgrade.stack.exception.HandlerException;
import com.dwarfeng.subgrade.stack.handler.LocalCacheHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 带过期时间的本地缓存处理器。
 *
 * <p>
 * 本处理器实现线程安全，并为缓存数据提供过期时间（TTL）功能。
 *
 * @author DwArFeng
 * @since 1.6.1
 */
public class ExpirableLocalCacheHandler<K, V> implements LocalCacheHandler<K, V> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExpirableLocalCacheHandler.class);

    @Nonnull
    private Fetcher<K, V> fetcher;

    @Nonnull
    private ThreadPoolTaskScheduler scheduler;

    private long ttl;
    private long cleanupInterval;

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Map<K, V> dataMap = new HashMap<>();
    private final Set<K> notExistsKeys = new HashSet<>();
    private final Map<K, Long> expireTimestampMap = new HashMap<>();

    private ScheduledFuture<?> cleanupTaskFuture;

    public ExpirableLocalCacheHandler(
            @Nonnull Fetcher<K, V> fetcher, @Nonnull ThreadPoolTaskScheduler scheduler, long ttl, long cleanupInterval
    ) {
        this.fetcher = fetcher;
        this.scheduler = scheduler;
        this.ttl = ttl;
        this.cleanupInterval = cleanupInterval;
        refreshCleanupTask();
    }

    @Override
    public boolean exists(K key) throws HandlerException {
        try {
            lock.readLock().lock();
            try {
                if (dataMap.containsKey(key) && !isExpired(key)) {
                    return true;
                }
                if (notExistsKeys.contains(key) && !isExpired(key)) {
                    return false;
                }
            } finally {
                lock.readLock().unlock();
            }
            lock.writeLock().lock();
            try {
                // 双重检查。
                if (dataMap.containsKey(key)) {
                    if (isExpired(key)) {
                        removeData(key);
                    } else {
                        return true;
                    }
                }
                if (notExistsKeys.contains(key)) {
                    if (isExpired(key)) {
                        removeNotExistsKey(key);
                    } else {
                        return false;
                    }
                }
                if (fetcher.exists(key)) {
                    V value = fetcher.fetch(key);

                    // 日志记录。
                    LOGGER.info("成功抓取数据, key = {}, value = {}", key, value);

                    putData(key, value);
                    return true;
                } else {
                    putNotExistsKey(key);
                    return false;
                }
            } finally {
                lock.writeLock().unlock();
            }
        } catch (Exception e) {
            throw new HandlerException(e);
        }
    }

    @Override
    public V get(K key) throws HandlerException {
        try {
            lock.readLock().lock();
            try {
                if (dataMap.containsKey(key) && !isExpired(key)) {
                    return dataMap.get(key);
                }
                if (notExistsKeys.contains(key) && !isExpired(key)) {
                    return null;
                }
            } finally {
                lock.readLock().unlock();
            }
            lock.writeLock().lock();
            try {
                // 双重检查。
                if (dataMap.containsKey(key)) {
                    if (isExpired(key)) {
                        removeData(key);
                    } else {
                        return dataMap.get(key);
                    }
                }
                if (notExistsKeys.contains(key)) {
                    if (isExpired(key)) {
                        removeNotExistsKey(key);
                    } else {
                        return null;
                    }
                }
                if (fetcher.exists(key)) {
                    V value = fetcher.fetch(key);

                    // 日志记录。
                    LOGGER.info("成功抓取数据, key = {}, value = {}", key, value);

                    putData(key, value);
                    return value;
                } else {
                    putNotExistsKey(key);
                    return null;
                }
            } finally {
                lock.writeLock().unlock();
            }
        } catch (Exception e) {
            throw new HandlerException(e);
        }
    }

    @Override
    public boolean remove(K key) {
        lock.writeLock().lock();
        try {
            boolean flag = Objects.nonNull(dataMap.remove(key));
            flag |= notExistsKeys.remove(key);
            expireTimestampMap.remove(key);

            // 记录日志。
            LOGGER.info("清除 key = {} 对应的本地缓存, 变更标记为 {}", key, flag);

            return flag;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void clear() {
        lock.writeLock().lock();
        try {
            dataMap.clear();
            notExistsKeys.clear();
            expireTimestampMap.clear();

            // 记录日志。
            LOGGER.info("清除所有的本地缓存");
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Nonnull
    public Fetcher<K, V> getFetcher() {
        lock.readLock().lock();
        try {
            return fetcher;
        } finally {
            lock.readLock().unlock();
        }
    }

    public void setFetcher(@Nonnull Fetcher<K, V> fetcher) {
        lock.writeLock().lock();
        try {
            this.fetcher = fetcher;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Nonnull
    public ThreadPoolTaskScheduler getScheduler() {
        lock.readLock().lock();
        try {
            return scheduler;
        } finally {
            lock.readLock().unlock();
        }
    }

    public void setScheduler(@Nonnull ThreadPoolTaskScheduler scheduler) {
        lock.writeLock().lock();
        try {
            this.scheduler = scheduler;
            refreshCleanupTask();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public long getTtl() {
        lock.readLock().lock();
        try {
            return ttl;
        } finally {
            lock.readLock().unlock();
        }
    }

    public void setTtl(long ttl) {
        lock.writeLock().lock();
        try {
            this.ttl = ttl;
            refreshCleanupTask();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public long getCleanupInterval() {
        lock.readLock().lock();
        try {
            return cleanupInterval;
        } finally {
            lock.readLock().unlock();
        }
    }

    public void setCleanupInterval(long cleanupInterval) {
        lock.writeLock().lock();
        try {
            this.cleanupInterval = cleanupInterval;
            refreshCleanupTask();
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * 检查指定的键是否已过期。
     *
     * <p>
     * 调用该方法时，必须保证指定的键存在于缓存中。
     *
     * @param key 指定的键。
     * @return 如果键已过期，返回 <code>true</code>；否则返回 <code>false</code>。
     */
    private boolean isExpired(K key) {
        Long expireTime = expireTimestampMap.get(key);
        if (Objects.isNull(expireTime)) {
            throw new IllegalStateException("尝试检查不存在的键的过期状态: " + key);
        }
        return System.currentTimeMillis() > expireTime;
    }

    /**
     * 存储数据到缓存中。
     *
     * @param key   指定的键。
     * @param value 指定的值。
     */
    private void putData(K key, V value) {
        dataMap.put(key, value);
        long expireTimestamp = ttl > 0 ? System.currentTimeMillis() + ttl : Long.MAX_VALUE;
        expireTimestampMap.put(key, expireTimestamp);
    }

    /**
     * 添加不存在键的记录到缓存中。
     *
     * @param key 指定的键。
     */
    private void putNotExistsKey(K key) {
        notExistsKeys.add(key);
        long expireTimestamp = ttl > 0 ? System.currentTimeMillis() + ttl : Long.MAX_VALUE;
        expireTimestampMap.put(key, expireTimestamp);
    }

    /**
     * 从缓存中移除数据。
     *
     * @param key 指定的键。
     */
    private void removeData(K key) {
        dataMap.remove(key);
        expireTimestampMap.remove(key);
    }

    /**
     * 从缓存中移除不存在键的记录。
     *
     * @param key 指定的键。
     */
    private void removeNotExistsKey(K key) {
        notExistsKeys.remove(key);
        expireTimestampMap.remove(key);
    }

    /**
     * 刷新清理任务。
     */
    private void refreshCleanupTask() {
        // 取消现有的清理任务。
        if (Objects.nonNull(cleanupTaskFuture)) {
            cleanupTaskFuture.cancel(false);
            cleanupTaskFuture = null;
        }
        // 如果清理间隔大于 0，启动新的清理任务。
        if (cleanupInterval > 0) {
            cleanupTaskFuture = scheduler.scheduleWithFixedDelay(this::cleanupExpired, cleanupInterval);
        }
    }

    /**
     * 清理过期的数据。
     */
    private void cleanupExpired() {
        lock.writeLock().lock();
        try {
            // 日志记录。
            LOGGER.info("开始清理过期的缓存数据...");

            long currentTime = System.currentTimeMillis();
            List<K> expiredKeys = new ArrayList<>();
            for (Map.Entry<K, Long> entry : expireTimestampMap.entrySet()) {
                if (currentTime > entry.getValue()) {
                    expiredKeys.add(entry.getKey());
                }
            }
            for (K key : expiredKeys) {
                dataMap.remove(key);
                notExistsKeys.remove(key);
                expireTimestampMap.remove(key);
            }

            // 日志记录。
            if (!expiredKeys.isEmpty()) {
                LOGGER.info("清理了 {} 个过期的缓存项, keys = {}", expiredKeys.size(), expiredKeys);
            } else {
                LOGGER.info("未发现过期的缓存项");
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public String toString() {
        return "ExpirableLocalCacheHandler{" +
                "fetcher=" + fetcher +
                ", scheduler=" + scheduler +
                ", ttl=" + ttl +
                ", cleanupInterval=" + cleanupInterval +
                ", lock=" + lock +
                ", dataMap=" + dataMap +
                ", notExistsKeys=" + notExistsKeys +
                ", expireTimestampMap=" + expireTimestampMap +
                ", cleanupTaskFuture=" + cleanupTaskFuture +
                '}';
    }
}
