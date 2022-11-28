package com.dwarfeng.subgrade.impl.handler;

import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.exception.HandlerException;
import com.dwarfeng.subgrade.stack.handler.LocalCacheHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 本地缓存处理器的通用实现。
 *
 * <p>
 * 本处理器实现线程安全。
 *
 * @author DwArFeng
 * @since 1.3.0
 */
public class GeneralLocalCacheHandler<K extends Key, V> implements LocalCacheHandler<K, V> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneralLocalCacheHandler.class);

    private final Fetcher<K, V> fetcher;

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Map<K, V> dataMap = new HashMap<>();
    private final Set<K> notExistsKeys = new HashSet<>();

    public GeneralLocalCacheHandler(Fetcher<K, V> fetcher) {
        this.fetcher = fetcher;
    }

    @Override
    public boolean exists(K key) throws HandlerException {
        try {
            lock.readLock().lock();
            try {
                if (dataMap.containsKey(key)) {
                    return true;
                }
                if (notExistsKeys.contains(key)) {
                    return false;
                }
            } finally {
                lock.readLock().unlock();
            }
            lock.writeLock().lock();
            try {
                if (dataMap.containsKey(key)) {
                    return true;
                }
                if (notExistsKeys.contains(key)) {
                    return false;
                }
                if (fetcher.exists(key)) {
                    V value = fetcher.fetch(key);

                    // 日志记录。
                    LOGGER.info("成功抓取数据, key = {}, value = {}", key, value);

                    dataMap.put(key, value);
                    return true;
                } else {
                    notExistsKeys.add(key);
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
                if (dataMap.containsKey(key)) {
                    return dataMap.get(key);
                }
                if (notExistsKeys.contains(key)) {
                    return null;
                }
            } finally {
                lock.readLock().unlock();
            }
            lock.writeLock().lock();
            try {
                if (dataMap.containsKey(key)) {
                    return dataMap.get(key);
                }
                if (notExistsKeys.contains(key)) {
                    return null;
                }
                if (fetcher.exists(key)) {
                    V value = fetcher.fetch(key);

                    // 日志记录。
                    LOGGER.info("成功抓取数据, key = {}, value = {}", key, value);

                    dataMap.put(key, value);
                    return value;
                } else {
                    notExistsKeys.add(key);
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
            boolean flag = dataMap.keySet().remove(key);
            flag |= notExistsKeys.remove(key);

            // 记录日志。
            LOGGER.info("清除 key = {} 对应的本地缓存, 变更标记为 {}", key, flag);

            return flag;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void clear() throws HandlerException {
        lock.writeLock().lock();
        try {
            dataMap.clear();
            notExistsKeys.clear();

            // 记录日志。
            LOGGER.info("清除所有的本地缓存");
        } finally {
            lock.writeLock().unlock();
        }
    }
}
