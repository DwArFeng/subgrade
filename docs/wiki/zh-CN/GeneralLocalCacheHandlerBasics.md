# General Local Cache Handler Basics - 通用本地缓存处理器基础

## 综述

`GeneralLocalCacheHandler` 是 `LocalCacheHandler` 接口的通用实现，提供了线程安全的本地缓存功能。
该实现简单高效，适用于大多数本地缓存需求。

`GeneralLocalCacheHandler` 采用懒加载策略，在首次访问数据时从数据源获取并缓存，后续访问直接从缓存中返回。
该实现使用 `ReadWriteLock` 机制保证线程安全，支持多线程并发访问。

## 实现说明

### 内部数据结构

`GeneralLocalCacheHandler` 内部维护两个数据结构：

- `dataMap`：`Map<K, V>` 类型，用于存储已缓存的数据。键为缓存键，值为缓存的值。
- `notExistsKeys`：`Set<K>` 类型，用于记录已确认不存在的键。当从数据源确认某个键不存在时，会将该键加入此集合，避免重复查询。

### 线程安全机制

`GeneralLocalCacheHandler` 使用 `ReadWriteLock` 机制保证线程安全：

- 读操作（`exists` 和 `get` 方法）使用读锁，允许多个线程同时读取缓存。
- 写操作（缓存更新、`remove` 和 `clear` 方法）使用写锁，确保写操作的互斥性。
- 采用双重检查锁定模式，在获取写锁后再次检查缓存状态，避免重复获取数据。

### 缓存策略

`GeneralLocalCacheHandler` 采用懒加载策略：

1. 首次访问时，如果缓存中不存在数据，则调用 `Fetcher` 接口从数据源获取数据。
2. 获取到数据后，将数据存入 `dataMap`，并返回给调用者。
3. 如果数据源中也不存在该数据，则将该键加入 `notExistsKeys` 集合，并返回 `null` 或 `false`。
4. 后续访问时，如果缓存中存在数据，直接返回；如果键在 `notExistsKeys` 中，直接返回 `null` 或 `false`，无需再次访问数据源。

### 缓存失效机制

`GeneralLocalCacheHandler` 提供两种缓存失效方式：

1. **手动移除**：调用 `remove(K key)` 方法移除指定键的缓存。
   该方法会同时从 `dataMap` 和 `notExistsKeys` 中移除对应的键，并返回是否发生了变更。
2. **全部清除**：调用 `clear()` 方法清除所有缓存。该方法会清空 `dataMap` 和 `notExistsKeys`。

当数据发生变化时，应该及时清除相关缓存，以确保数据的一致性。

## 使用示例

以下示例展示了如何使用 `GeneralLocalCacheHandler` 实现本地缓存功能。示例使用虚拟的业务逻辑，不涉及具体项目的真实业务。

### 定义 LocalCacheHandler 接口

首先，需要定义一个继承自 `LocalCacheHandler` 的接口，指定具体的键值类型：

```java
package com.example.stack.handler;

import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;
import com.dwarfeng.subgrade.stack.handler.LocalCacheHandler;

/**
 * Foo 本地缓存处理器。
 *
 * <p>
 * 该处理器实现了 <code>LocalCacheHandler&lt;LongIdKey, Foo&gt;</code> 接口，
 * 用于处理与 Foo 相关的本地缓存。
 *
 * @author Example
 * @see LocalCacheHandler
 * @see Foo
 * @since 1.0.0
 */
@SuppressWarnings("JavadocReference")
public interface FooLocalCacheHandler extends LocalCacheHandler<LongIdKey, Foo> {
}
```

### 实现 LocalCacheHandler

实现 `FooLocalCacheHandler` 接口，使用 `GeneralLocalCacheHandler` 作为内部实现：

```java
package com.example.impl.handler;

import com.example.stack.bean.entity.FooInfo;
import com.example.stack.handler.Foo;
import com.example.stack.handler.FooHandler;
import com.example.stack.handler.FooLocalCacheHandler;
import com.example.stack.service.FooInfoMaintainService;
import com.dwarfeng.subgrade.impl.handler.Fetcher;
import com.dwarfeng.subgrade.impl.handler.GeneralLocalCacheHandler;
import com.dwarfeng.subgrade.sdk.interceptor.analyse.BehaviorAnalyse;
import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;
import com.dwarfeng.subgrade.stack.exception.HandlerException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Component
public class FooLocalCacheHandlerImpl implements FooLocalCacheHandler {

    private final GeneralLocalCacheHandler<LongIdKey, Foo> handler;

    public FooLocalCacheHandlerImpl(FooFetcher fooFetcher) {
        handler = new GeneralLocalCacheHandler<>(fooFetcher);
    }

    @BehaviorAnalyse
    @Override
    public boolean exists(LongIdKey key) throws HandlerException {
        return handler.exists(key);
    }

    @BehaviorAnalyse
    @Override
    public Foo get(LongIdKey key) throws HandlerException {
        return handler.get(key);
    }

    @BehaviorAnalyse
    @Override
    public boolean remove(LongIdKey key) {
        return handler.remove(key);
    }

    @BehaviorAnalyse
    @Override
    public void clear() {
        handler.clear();
    }

    @Component
    public static class FooFetcher implements Fetcher<LongIdKey, Foo> {

        private final FooInfoMaintainService fooInfoMaintainService;
        private final FooHandler fooHandler;

        public FooFetcher(
                FooInfoMaintainService fooInfoMaintainService,
                FooHandler fooHandler
        ) {
            this.fooInfoMaintainService = fooInfoMaintainService;
            this.fooHandler = fooHandler;
        }

        @Override
        @BehaviorAnalyse
        @Transactional(
                transactionManager = "hibernateTransactionManager", readOnly = true, rollbackFor = Exception.class
        )
        public boolean exists(LongIdKey key) throws Exception {
            FooInfo fooInfo = fooInfoMaintainService.getIfExists(key);
            if (Objects.isNull(fooInfo)) {
                return false;
            }
            return fooInfo.isEnabled();
        }

        @Override
        @BehaviorAnalyse
        @Transactional(
                transactionManager = "hibernateTransactionManager", readOnly = true, rollbackFor = Exception.class
        )
        public Foo fetch(LongIdKey key) throws Exception {
            FooInfo fooInfo = fooInfoMaintainService.get(key);
            return fooHandler.make(fooInfo.getType(), fooInfo.getParam());
        }
    }
}
```

### 使用 LocalCacheHandler

在其他组件中使用 `FooLocalCacheHandler`：

```java

@Service
public class FooService {

    @Autowired
    private FooLocalCacheHandler fooLocalCacheHandler;

    public Foo getFoo(LongIdKey key) throws HandlerException {
        // 从本地缓存获取 Foo 对象
        Foo foo = fooLocalCacheHandler.get(key);
        if (foo != null) {
            return foo;
        }
        // 如果缓存中不存在，Fetcher 会自动从数据源获取并缓存
        return fooLocalCacheHandler.get(key);
    }

    public void invalidateCache(LongIdKey key) {
        // 当数据发生变化时，清除相关缓存
        fooLocalCacheHandler.remove(key);
    }

    public void clearAllCache() {
        // 清除所有缓存
        fooLocalCacheHandler.clear();
    }
}
```

## 注意事项

### 缓存生命周期管理

`GeneralLocalCacheHandler` 的缓存生命周期与进程生命周期一致，缓存数据会一直保存在内存中，直到进程结束或手动清除。
因此，需要注意：

- 当数据发生变化时，应该及时清除相关缓存，以确保数据的一致性。
- 如果缓存的数据量过大，可能会占用大量内存，需要合理控制缓存的数据量。

### 内存使用

`GeneralLocalCacheHandler` 将数据存储在进程内存中，如果缓存的数据量过大，可能会影响进程的内存使用。建议：

- 仅对访问频率高、变化频率低的数据使用本地缓存。
- 定期清理不再使用的缓存数据。
- 监控进程的内存使用情况，避免内存溢出。

### 线程安全

`GeneralLocalCacheHandler` 已经实现了线程安全，可以在多线程环境中安全使用。但需要注意：

- `Fetcher` 接口的实现也应该保证线程安全。
- 如果 `Fetcher` 的实现涉及数据库操作，应该使用事务管理确保数据一致性。

### 缓存一致性

`GeneralLocalCacheHandler` 不提供自动的缓存失效机制，需要手动管理缓存的一致性。建议：

- 在数据更新、删除操作后，及时清除相关缓存。
- 在批量操作后，考虑清除所有缓存或相关缓存。
- 如果数据变化频繁，可能需要考虑使用带过期时间的本地缓存处理器。
