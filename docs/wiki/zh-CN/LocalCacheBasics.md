# Local Cache Basics - 本地缓存基础

## 综述

本地缓存处理器是项目中用于在进程内缓存数据的核心机制，它提供了在本地内存中保存数据的能力，
仅在必要时才与数据访问层通信，从而显著提升程序效率。

在实际开发中，许多业务场景需要频繁访问某些数据，但这些数据的变化频率较低。
如果每次都从数据库或远程服务获取这些数据，不仅会增加网络开销，还会降低系统响应速度。本地缓存处理器正是为了解决这类问题而设计的。

本项目的本地缓存机制旨在提供一个灵活、可扩展的框架，使得开发者能够快速实现各种本地缓存需求，同时保证线程安全和数据一致性。

## 接口定义

### 本地缓存处理器接口

在本项目中，所有的本地缓存处理器都必须实现 `com.dwarfeng.subgrade.stack.handler.LocalCacheHandler` 接口，本接口签名如下：

```java
public interface LocalCacheHandler<K, V> {

    /**
     * 获取指定的键对应的值是否存在。
     *
     * @param key 指定的键。
     * @return 指定的键对应的值是否存在。
     * @throws HandlerException 处理器异常。
     */
    @SuppressWarnings("JavadocReference")
    boolean exists(K key) throws HandlerException;

    /**
     * 获取指定的键对应的值。
     *
     * <p>
     * 如果指定的键不存在，则返回 <code>null</code>。
     *
     * @param key 指定的键。
     * @return 指定的键对应的值。
     * @throws HandlerException 处理器异常。
     */
    @SuppressWarnings("JavadocReference")
    V get(K key) throws HandlerException;

    /**
     * 移除指定的键对应的本地缓存。
     *
     * @param key 指定的键。
     * @return 该操作是否改变了本地缓存处理器中的数据。
     * @throws HandlerException 处理器异常。
     */
    @SuppressWarnings("JavadocReference")
    boolean remove(K key) throws HandlerException;

    /**
     * 清除本地缓存。
     *
     * @throws HandlerException 处理器异常。
     */
    @SuppressWarnings("JavadocReference")
    void clear() throws HandlerException;
}
```

该接口定义了一个本地缓存处理器的基本操作方法，所有的本地缓存操作都将基于这些基础方法进行。

其中，`K` 是键的类型，通常为主键类型，必须实现 `com.dwarfeng.subgrade.stack.bean.key.Key` 接口；`V`
是值的类型，可以是任意类型，通常为业务对象或配置对象。

### 方法说明

| 方法名             | 描述             | 返回值                                     | 异常                 |
|:----------------|:---------------|:----------------------------------------|:-------------------|
| `exists(K key)` | 判断指定的键对应的值是否存在 | `boolean`：存在返回 `true`，不存在返回 `false`     | `HandlerException` |
| `get(K key)`    | 获取指定的键对应的值     | `V`：存在返回对应的值，不存在返回 `null`               | `HandlerException` |
| `remove(K key)` | 移除指定的键对应的本地缓存  | `boolean`：如果缓存被移除返回 `true`，否则返回 `false` | `HandlerException` |
| `clear()`       | 清除所有本地缓存       | `void`                                  | `HandlerException` |

## 工作方式

本地缓存处理器采用懒加载策略，在首次访问数据时从数据源获取并缓存，后续访问直接从缓存中返回，从而减少与数据访问层的通信。

### 缓存命中流程

当调用 `exists` 或 `get` 方法时，本地缓存处理器首先检查本地缓存中是否存在对应的数据：

1. 如果缓存中存在数据，直接返回缓存的结果，无需访问数据源。
2. 如果缓存中不存在数据，则调用 `Fetcher` 接口从数据源获取数据。
3. 获取到数据后，将数据存入本地缓存，并返回给调用者。
4. 如果数据源中也不存在该数据，则记录该键为不存在状态，避免重复查询。

### 线程安全机制

本地缓存处理器实现线程安全，支持多线程并发访问。实现类通常使用 `ReadWriteLock` 机制来保证线程安全：

- 读操作使用读锁，允许多个线程同时读取。
- 写操作使用写锁，确保写操作的互斥性。
- 通过双重检查锁定模式，避免重复获取数据。

### 缓存失效机制

本地缓存处理器提供两种缓存失效方式：

1. 手动移除：调用 `remove(K key)` 方法移除指定键的缓存。
2. 全部清除：调用 `clear()` 方法清除所有缓存。

当数据发生变化时，应该及时清除相关缓存，以确保数据的一致性。

### 与 Fetcher 接口的配合

本地缓存处理器需要与 `Fetcher` 接口配合使用。`Fetcher` 接口负责从数据源获取数据，本地缓存处理器负责管理缓存。
当缓存未命中时，本地缓存处理器会调用 `Fetcher` 接口的 `exists` 和 `fetch` 方法来获取数据。

## 与数据缓存接口的区别

本项目提供了两种缓存机制：本地缓存处理器（`LocalCacheHandler`）和数据缓存接口（`Cache`）。
虽然两者都用于缓存数据，但它们的定位和使用场景有所不同。

### 定位差异

| 特性   | LocalCacheHandler | Cache             |
|:-----|:------------------|:------------------|
| 缓存位置 | 进程内内存             | 分布式缓存（如 Redis）    |
| 数据共享 | 仅在同一进程内共享         | 可在多个进程间共享         |
| 性能特点 | 访问速度快，但占用进程内存     | 访问速度相对较慢，但不占用进程内存 |
| 适用场景 | 读取量大、写入量小的进程内数据   | 需要在多个进程间共享的数据     |

### 使用场景建议

- 使用 `LocalCacheHandler` 的场景：
  - 数据访问频率高，但变化频率低。
  - 数据仅在单个进程内使用，不需要跨进程共享。
  - 对访问速度要求极高，可以接受占用进程内存。

- 使用 `Cache` 的场景：
  - 数据需要在多个进程间共享。
  - 数据量较大，不适合存储在进程内存中。
  - 需要利用分布式缓存的特性（如过期时间、持久化等）。

### 性能特点

`LocalCacheHandler` 由于数据存储在进程内存中，访问速度极快，通常可以达到纳秒级的响应时间。
但这也意味着数据会占用进程内存，如果缓存的数据量过大，可能会影响进程的内存使用。

`Cache` 接口通常基于分布式缓存实现（如 Redis），访问速度相对较慢，通常需要网络通信，响应时间在毫秒级。
但分布式缓存可以存储大量数据，且不占用进程内存。

## 相关接口

### Fetcher 接口

`Fetcher` 接口是本地缓存处理器的数据获取接口，负责从数据源获取数据。该接口定义如下：

```java
public interface Fetcher<K, V> {

    /**
     * 判断指定的键对应的值是否存在。
     *
     * @param key 指定的键。
     * @return 指定的键对应的值是否存在。
     * @throws Exception 判断是否存在过程中出现的任何异常。
     */
    boolean exists(K key) throws Exception;

    /**
     * 抓取指定的键对应的值。
     *
     * @param key 指定的键。
     * @return 指定的键对应的值。
     * @throws Exception 抓取值的过程中出现的任何异常。
     */
    V fetch(K key) throws Exception;
}
```

`Fetcher` 接口包含两个方法：

- `exists(K key)`：判断指定的键对应的值是否存在。本地缓存处理器在缓存未命中时会先调用此方法判断数据是否存在。
- `fetch(K key)`：抓取指定的键对应的值。当 `exists` 方法返回 `true` 时，本地缓存处理器会调用此方法获取数据。

实现 `Fetcher` 接口时，通常需要：

1. 调用服务层接口获取实体信息。
2. 根据实体信息构建业务对象。
3. 返回构建好的业务对象。

`Fetcher` 接口的实现应该保证线程安全，因为本地缓存处理器可能会在多个线程中并发调用 `Fetcher` 的方法。
