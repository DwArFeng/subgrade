# Expirable Local Cache Handler Basics - 带过期时间的本地缓存处理器基础

## 综述

`ExpirableLocalCacheHandler` 是 `LocalCacheHandler` 接口的带过期时间实现，提供了线程安全的本地缓存功能，并为缓存数据提供过期时间（TTL）支持。
该实现适用于需要自动过期、减少内存占用的本地缓存场景。

`ExpirableLocalCacheHandler` 采用懒加载策略，在首次访问数据时从数据源获取并缓存，后续访问直接从缓存中返回。
该实现使用 `ReadWriteLock` 机制保证线程安全，支持多线程并发访问。
与 `GeneralLocalCacheHandler` 相比，本实现增加了缓存过期时间和定期清理机制，过期数据会自动失效并被清理。

## 实现说明

### 内部数据结构

`ExpirableLocalCacheHandler` 内部维护三个数据结构：

- `dataMap`：`Map<K, V>` 类型，用于存储已缓存的数据。键为缓存键，值为缓存的值。
- `notExistsKeys`：`Set<K>` 类型，用于记录已确认不存在的键。当从数据源确认某个键不存在时，会将该键加入此集合，避免重复查询。
- `expireTimestampMap`：`Map<K, Long>` 类型，用于记录每个键的过期时间戳。当缓存数据或不存在键记录被写入时，会根据 TTL
  计算并记录过期时间。

### 线程安全机制

`ExpirableLocalCacheHandler` 使用 `ReadWriteLock` 机制保证线程安全：

- 读操作（`exists` 和 `get` 方法）使用读锁，允许多个线程同时读取缓存。
- 写操作（缓存更新、`remove` 和 `clear` 方法）使用写锁，确保写操作的互斥性。
- 采用双重检查锁定模式，在获取写锁后再次检查缓存状态，避免重复获取数据。

### 缓存策略

`ExpirableLocalCacheHandler` 采用懒加载策略：

1. 首次访问时，如果缓存中不存在数据或数据已过期，则调用 `Fetcher` 接口从数据源获取数据。
2. 获取到数据后，将数据存入 `dataMap`，并根据 TTL 计算过期时间戳存入 `expireTimestampMap`，然后返回给调用者。
3. 如果数据源中也不存在该数据，则将该键加入 `notExistsKeys` 集合，并记录过期时间戳，返回 `null` 或 `false`。
4. 后续访问时，如果缓存中存在未过期数据，直接返回；如果键在 `notExistsKeys` 中且未过期，直接返回 `null` 或 `false`
   ；如果数据已过期，则移除后重新从数据源获取。

### 过期时间与定期清理

`ExpirableLocalCacheHandler` 提供基于 TTL 的过期机制：

- **ttl**：缓存过期时间，单位为毫秒。当 `ttl > 0` 时，新写入的缓存项在 `ttl` 毫秒后过期；当 `ttl <= 0` 时，缓存项永不过期（使用
  `Long.MAX_VALUE` 作为过期时间戳）。
- **cleanupInterval**：定期清理任务的执行间隔，单位为毫秒。当 `cleanupInterval > 0` 时，会使用 `ThreadPoolTaskScheduler`
  按固定间隔执行清理任务，移除所有已过期的缓存项；当 `cleanupInterval <= 0` 时，不启动定时清理任务。
- **访问时过期检查**：在 `exists` 和 `get` 方法中，会检查缓存项是否已过期，若已过期则移除并重新从数据源获取。

### 缓存失效机制

`ExpirableLocalCacheHandler` 提供三种缓存失效方式：

1. **手动移除**：调用 `remove(K key)` 方法移除指定键的缓存。
   该方法会同时从 `dataMap`、`notExistsKeys` 和 `expireTimestampMap` 中移除对应的键，并返回是否发生了变更。
2. **全部清除**：调用 `clear()` 方法清除所有缓存。该方法会清空 `dataMap`、`notExistsKeys` 和 `expireTimestampMap`。
3. **自动过期**：当缓存项超过 TTL 后自动失效。访问时会检测并移除过期项；若配置了 `cleanupInterval`，定时任务会批量清理过期项。

当数据发生变化时，应该及时清除相关缓存，以确保数据的一致性。

## 使用示例

以下示例展示了如何使用 `ExpirableLocalCacheHandler` 实现带过期时间的本地缓存功能。示例使用虚拟的业务逻辑，不涉及具体项目的真实业务。

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

实现 `FooLocalCacheHandler` 接口，使用 `ExpirableLocalCacheHandler` 作为内部实现：

```java
package com.example.impl.handler;

import com.example.stack.bean.entity.FooInfo;
import com.example.stack.handler.Foo;
import com.example.stack.handler.FooHandler;
import com.example.stack.handler.FooLocalCacheHandler;
import com.example.stack.service.FooInfoMaintainService;
import com.dwarfeng.subgrade.impl.handler.ExpirableLocalCacheHandler;
import com.dwarfeng.subgrade.impl.handler.Fetcher;
import com.dwarfeng.subgrade.sdk.interceptor.analyse.BehaviorAnalyse;
import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;
import com.dwarfeng.subgrade.stack.exception.HandlerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Component
public class FooLocalCacheHandlerImpl implements FooLocalCacheHandler {

    private final ExpirableLocalCacheHandler<LongIdKey, Foo> handler;

    public FooLocalCacheHandlerImpl(
            FooFetcher fooFetcher,
            ThreadPoolTaskScheduler scheduler,
            @Value("${local_cache.foo.ttl}") long ttl,
            @Value("${local_cache.foo.cleanup_interval}") long cleanupInterval
    ) {
        handler = new ExpirableLocalCacheHandler<>(fooFetcher, scheduler, ttl, cleanupInterval);
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

### 配置 ThreadPoolTaskScheduler 与缓存参数

`ExpirableLocalCacheHandler` 需要 `ThreadPoolTaskScheduler` 作为 Spring Bean 提供，用于执行定期清理任务。应用需在配置类中定义该
Bean。

同时，需要在配置文件中设置 TTL 和清理间隔：

```properties
# Foo 本地缓存的 ttl，单位为毫秒。
local_cache.foo.ttl=3600000
# Foo 本地缓存的清理间隔，单位为毫秒。
local_cache.foo.cleanup_interval=600000
```

### 使用 LocalCacheHandler

在其他组件中使用 `FooLocalCacheHandler`：

```java

@Service
public class FooService {

    @Autowired
    private FooLocalCacheHandler fooLocalCacheHandler;

    public Foo getFoo(LongIdKey key) throws HandlerException {
        /*
         * 从本地缓存获取 Foo 对象:
         *   如果缓存中不存在，Fetcher 会自动从数据源获取并缓存。
         *   如果数据源中不存在该数据，则返回 null。
         *   如果数据已经存在且未过期，则直接返回缓存中的数据。
         *   如果数据的主键在 notExistsKeys 中且未过期，则直接返回 null。
         *   如果数据已过期，则移除后重新从数据源获取并缓存。
         */
        return fooLocalCacheHandler.get(key);
    }

    public void invalidateCache(LongIdKey key) {
        // 清除指定的键相关的缓存。
        fooLocalCacheHandler.remove(key);
    }

    public void clearAllCache() {
        // 清除所有缓存。
        fooLocalCacheHandler.clear();
    }
}
```

## 注意事项

### 缓存生命周期管理

`ExpirableLocalCacheHandler` 的缓存项具有过期时间，超过 TTL 后会自动失效。因此，与 `GeneralLocalCacheHandler`
相比，本实现可以自动释放内存，减少长期占用。
但需要注意：

- 当数据发生变化时，仍应及时清除相关缓存，以确保数据的一致性。
- 合理设置 TTL 和清理间隔，平衡数据新鲜度与内存占用。

### 内存使用

`ExpirableLocalCacheHandler` 将数据存储在进程内存中，但通过 TTL 和定期清理机制，过期数据会被自动移除，有助于控制内存占用。建议：

- 仅对访问频率高、变化频率低的数据使用本地缓存。
- 根据业务场景合理设置 TTL，避免过期数据长期占用内存。
- 监控进程的内存使用情况，避免内存溢出。

### 线程安全

`ExpirableLocalCacheHandler` 已经实现了线程安全，可以在多线程环境中安全使用。但需要注意：

- `Fetcher` 接口的实现也应该保证线程安全。
- 如果 `Fetcher` 的实现涉及数据库操作，应该使用事务管理确保数据一致性。

### TTL 与清理间隔的配置建议

- **ttl**：应根据数据的更新频率设置。数据变化越频繁，TTL 应越短；反之可适当延长。
- **cleanupInterval**：清理间隔不宜过短，以免频繁执行清理任务影响性能；也不宜过长，建议小于或等于 TTL，以便及时释放过期数据占用的内存。
- 当 `cleanupInterval <= 0` 时，不会启动定时清理任务，过期数据仅在下次访问时被移除，适合对内存不敏感的场景。

### 缓存一致性

`ExpirableLocalCacheHandler` 提供基于 TTL 的自动过期机制，但仍需手动管理缓存的一致性。建议：

- 在数据更新、删除操作后，及时清除相关缓存。
- 在批量操作后，考虑清除所有缓存或相关缓存。
- 若数据变化频繁且对一致性要求高，可考虑缩短 TTL 或使用 `GeneralLocalCacheHandler` 并配合手动清除。
