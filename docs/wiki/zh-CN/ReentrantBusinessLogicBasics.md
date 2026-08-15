# Reentrant Business Logic Basics - 业务逻辑可重入基础

## 综述

业务逻辑可重入是指业务后台逻辑能够在“运行态”与“停止态”之间反复进入与退出的能力。在本项目中，该能力由 `Handler` 接口族与 `Worker` 接口协作实现，围绕四个概念展开：

- 上线（`online`）：处理器参与业务调度，进入在线状态；
- 启动（`start`）：允许业务逻辑开始工作，进入已启动状态；
- 持锁（`lockHolding`）：多实例场景下持有分布式锁（选主成为 Leader）；
- 工作（`working`）：已执行 `Worker.work()` 且尚未 `Worker.rest()`。

典型的适用场景包括：后台任务、消费流水线、定时任务聚合、组件批量启停，以及多实例部署下的选主单活。

本文档是“业务逻辑可重入”板块的综述文档，提供接口总览、状态标志与协作说明，并指向三篇实现文档；具体实现细节见：

- [General Online Handler Basics](./GeneralOnlineHandlerBasics.md)；
- [General Startable Handler Basics](./GeneralStartableHandlerBasics.md)；
- [Curator Distributed Lock Handler Basics](./CuratorDistributedLockHandlerBasics.md)。

## 接口定义

`OnlineHandler` 和 `StartableHandler` 位于 `com.dwarfeng.subgrade.lifecycle.stack.handler` 包，
`DistributedLockHandler` 位于 `com.dwarfeng.subgrade.lock.stack.handler` 包。这些接口均直接或间接继承标记接口
`com.dwarfeng.subgrade.basic.stack.handler.Handler`。接口之间的继承关系如下：

- `OnlineHandler extends Handler`；
- `StartableHandler extends Handler`；
- `DistributedLockHandler extends OnlineHandler, StartableHandler`。

### OnlineHandler 接口

`OnlineHandler` 定义了线上处理器的契约，提供上线、下线与在线状态查询能力，接口签名如下：

```java
public interface OnlineHandler extends Handler {

    /**
     * 处理器是否在线。
     *
     * @return 处理器是否在线。
     * @throws HandlerException 处理器异常。
     */
    boolean isOnline() throws HandlerException;

    /**
     * 处理器上线。
     *
     * <p>
     * 该方法重复调用安全，多次调用实际只会执行一次。
     *
     * @throws HandlerException 处理器异常。
     */
    void online() throws HandlerException;

    /**
     * 处理器下线。
     *
     * <p>
     * 该方法重复调用安全，多次调用实际只会执行一次。
     *
     * @throws HandlerException 处理器异常。
     */
    void offline() throws HandlerException;
}
```

各方法语义与幂等约定如下：

| 方法         | 说明                                     |
|:-------------|:-----------------------------------------|
| `isOnline()` | 查询处理器当前是否在线。                 |
| `online()`   | 上线处理器，通常触发业务逻辑进入运行态。 |
| `offline()`  | 下线处理器，通常触发业务逻辑退出运行态。 |

幂等约定：`online()` 与 `offline()` 重复调用安全，多次调用实际只会执行一次（接口 Javadoc 明确约定）。

### StartableHandler 接口

`StartableHandler` 定义了可启动处理器的契约，提供启动、停止与启动状态查询能力，接口签名如下：

```java
public interface StartableHandler extends Handler {

    /**
     * 处理器是否启动。
     *
     * @return 处理器是否启动。
     * @throws HandlerException 处理器异常。
     */
    boolean isStarted() throws HandlerException;

    /**
     * 启动处理器。
     *
     * <p>
     * 该方法重复调用安全，多次调用实际只会执行一次。
     *
     * @throws HandlerException 处理器异常。
     */
    void start() throws HandlerException;

    /**
     * 停止处理器。
     *
     * <p>
     * 该方法重复调用安全，多次调用实际只会执行一次。
     *
     * @throws HandlerException 处理器异常。
     */
    void stop() throws HandlerException;
}
```

| 方法          | 说明                                   |
|:--------------|:---------------------------------------|
| `isStarted()` | 查询处理器当前是否已启动。             |
| `start()`     | 启动处理器，通常触发业务逻辑开始工作。 |
| `stop()`      | 停止处理器，通常触发业务逻辑停止工作。 |

与 `OnlineHandler` 的关系：两者平级，均直接继承 `Handler`。`OnlineHandler` 强调“上线 / 下线”（是否参与业务调度），`StartableHandler` 强调“启动 / 停止”（是否允许业务逻辑工作）；`DistributedLockHandler` 同时继承两者，使“上线”与“启动”成为两个独立阶段。`start()` 与 `stop()` 同样重复调用安全，多次调用实际只会执行一次。

### DistributedLockHandler 接口

`DistributedLockHandler` 同时继承 `OnlineHandler` 与 `StartableHandler`，并在其基础上新增持锁与工作状态查询能力，接口签名如下：

```java
public interface DistributedLockHandler extends OnlineHandler, StartableHandler {

    /**
     * 驱动处理器是否正在持有锁。
     *
     * @return 驱动处理器是否正在持有锁。
     * @throws HandlerException 处理器异常。
     */
    boolean isLockHolding() throws HandlerException;

    /**
     * 处理器是否正在工作。
     *
     * @return 处理器是否正在工作。
     * @throws HandlerException 处理器异常。
     */
    boolean isWorking() throws HandlerException;
}
```

| 方法              | 说明                                                          |
|:------------------|:--------------------------------------------------------------|
| `isLockHolding()` | 查询当前实例是否持有分布式锁（是否为 Leader）。               |
| `isWorking()`     | 查询当前实例是否正在工作（已执行 `work()` 且尚未 `rest()`）。 |

`isOnline`、`online`、`offline`、`isStarted`、`start`、`stop` 六个方法均继承自父接口。

## 状态与协作

### 状态标志

各实现类在锁内维护若干布尔状态标志，用于表达处理器当前所处阶段。四个标志的含义与置位、清除时机如下：

| 标志              | 含义                              | 置位时机                   | 清除时机                   | 归属实现                                                   |
|:------------------|:----------------------------------|:---------------------------|:---------------------------|:-----------------------------------------------------------|
| `onlineFlag`      | 已上线                            | `online()` 成功路径末尾    | `offline()` 成功路径末尾   | `GeneralOnlineHandler`、`CuratorDistributedLockHandler`    |
| `startedFlag`     | 已启动                            | `start()` 成功路径末尾     | `stop()` 成功路径末尾      | `GeneralStartableHandler`、`CuratorDistributedLockHandler` |
| `lockHoldingFlag` | 当前实例为 Leader（持有分布式锁） | `isLeader()` 回调中        | `notLeader()` 回调中       | `CuratorDistributedLockHandler`                            |
| `workingFlag`     | 已执行 `work()` 且尚未 `rest()`   | 内部 `work()` 成功路径末尾 | 内部 `rest()` 成功路径末尾 | `CuratorDistributedLockHandler`                            |

各标志均在操作成功后才翻转：若 `Worker` 调用抛出异常，对应标志保持不变，处理器停留在原状态，异常以 `HandlerException` 向外传播。

### 与 Worker 的协作

`Worker` 接口定义业务侧进入与退出运行态的具体步骤，位于 `com.dwarfeng.subgrade.lifecycle.stack.handler` 包，接口签名如下：

```java
public interface Worker {

    /**
     * 开始工作。
     *
     * @throws Exception 开始工作过程中发生的任何异常。
     */
    void work() throws Exception;

    /**
     * 停止工作。
     *
     * @throws Exception 停止工作过程中发生的任何异常。
     */
    void rest() throws Exception;
}
```

各实现类触发 `work()` / `rest()` 的时机：

| 实现类                          | 触发 `work()`                                    | 触发 `rest()`                                  |
|:--------------------------------|:-------------------------------------------------|:-----------------------------------------------|
| `GeneralOnlineHandler`          | `online()`                                       | `offline()`                                    |
| `GeneralStartableHandler`       | `start()`                                        | `stop()`                                       |
| `CuratorDistributedLockHandler` | `start()` 且已持锁；或 `isLeader()` 回调且已启动 | `stop()`；`notLeader()` 回调；`offline()` 内部 |

幂等保证：所有实现均在锁内先检查标志位再调用 `Worker`，因此：

- 每次有效 `online()` / `start()` 至多触发一次 `work()`；
- 每次有效 `offline()` / `stop()` 至多触发一次 `rest()`；
- 重复调用不会重复触发 `work()` / `rest()`。

## 默认实现总览

`GeneralOnlineHandler` 和 `GeneralStartableHandler` 位于 `com.dwarfeng.subgrade.lifecycle.impl.handler` 包，
`CuratorDistributedLockHandler` 位于 `com.dwarfeng.subgrade.lock.impl.handler.curator` 包。它们与接口的对应关系、依赖与适用场景如下：

| 实现类                          | 实现的接口               | 外部依赖                                         | 维护的标志                                                    | 适用场景          |
|:--------------------------------|:-------------------------|:-------------------------------------------------|:--------------------------------------------------------------|:------------------|
| `GeneralOnlineHandler`          | `OnlineHandler`          | 无（仅 `Worker`）                                | `onlineFlag`                                                  | 单实例上线 / 下线 |
| `GeneralStartableHandler`       | `StartableHandler`       | 无（仅 `Worker`）                                | `startedFlag`                                                 | 单实例启动 / 停止 |
| `CuratorDistributedLockHandler` | `DistributedLockHandler` | `CuratorFramework` 与 ZooKeeper（`LeaderLatch`） | `onlineFlag`、`startedFlag`、`lockHoldingFlag`、`workingFlag` | 多实例选主单活    |

三个实现类的共同特性：

- 均使用 `ReentrantLock` 保证线程安全，所有 public 方法在锁内读写标志位或调用 `Worker`；
- 均支持 `setWorker` 在锁内热替换 `Worker` 实例；
- `CuratorDistributedLockHandler` 额外支持 `setCuratorFramework`、`setLeaderLatchPath` 热替换。

## 使用示例

以下示例使用虚拟业务 `Foo`，展示定义 Handler 接口、组合默认实现与 `Worker`，以及在 QoS / 启动器中使用的常见写法。

### 定义 Handler 接口

在 stack 层定义业务 Handler 接口，继承对应的契约接口：

```java
package com.example.stack.handler;

import com.dwarfeng.subgrade.lifecycle.stack.handler.OnlineHandler;

/**
 * Foo 线上处理器。
 *
 * <p>
 * 为 Foo 相关后台任务提供可重入的上线 / 下线能力。
 *
 * @author Example
 * @since 1.0.0
 */
public interface FooOnlineHandler extends OnlineHandler {
}
```

若业务同时需要“上线”与“启动”两阶段能力（多实例选主单活场景），接口应继承 `DistributedLockHandler`，示例见 [Curator Distributed Lock Handler Basics](./CuratorDistributedLockHandlerBasics.md)。

### 实现 Handler 接口

在 impl 层组合默认实现与 `Worker`。`GeneralOnlineHandler` 只负责线程安全地触发 `work()` / `rest()`，业务步骤由 `Worker` 表达：

```java
package com.example.impl.handler;

import com.dwarfeng.subgrade.aop.sdk.interceptor.analyse.BehaviorAnalyse;
import com.dwarfeng.subgrade.basic.stack.exception.HandlerException;
import com.dwarfeng.subgrade.lifecycle.impl.handler.GeneralOnlineHandler;
import com.dwarfeng.subgrade.lifecycle.stack.handler.Worker;
import com.example.stack.handler.FooOnlineHandler;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class FooOnlineHandlerImpl implements FooOnlineHandler {

    private final GeneralOnlineHandler onlineHandler;

    private final Lock lock = new ReentrantLock();

    public FooOnlineHandlerImpl(FooWorker fooWorker) {
        this.onlineHandler = new GeneralOnlineHandler(fooWorker);
    }

    @BehaviorAnalyse
    @Override
    public boolean isOnline() {
        lock.lock();
        try {
            return onlineHandler.isOnline();
        } finally {
            lock.unlock();
        }
    }

    @BehaviorAnalyse
    @Override
    public void online() throws HandlerException {
        lock.lock();
        try {
            onlineHandler.online();
        } finally {
            lock.unlock();
        }
    }

    @BehaviorAnalyse
    @Override
    public void offline() throws HandlerException {
        lock.lock();
        try {
            onlineHandler.offline();
        } finally {
            lock.unlock();
        }
    }

    @Component
    public static class FooWorker implements Worker {

        private final BarScheduler barScheduler;

        public FooWorker(BarScheduler barScheduler) {
            this.barScheduler = barScheduler;
        }

        @Override
        public void work() throws Exception {
            barScheduler.enable();
        }

        @Override
        public void rest() throws Exception {
            barScheduler.disable();
        }
    }
}
```

### 在 QoS 或启动器中使用

服务层可薄封装 Handler，供启动器或 Telqos 调用；Handler 异常经 `ServiceExceptionHelper.logParse` 包装为 `ServiceException`：

```java
@Service
public class FooQosServiceImpl implements FooQosService {

    private final FooOnlineHandler fooOnlineHandler;
    private final ServiceExceptionMapper sem;

    public FooQosServiceImpl(FooOnlineHandler fooOnlineHandler, ServiceExceptionMapper sem) {
        this.fooOnlineHandler = fooOnlineHandler;
        this.sem = sem;
    }

    @Override
    public void online() throws ServiceException {
        try {
            fooOnlineHandler.online();
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("上线 Foo 服务时发生异常", LogLevel.WARN, e, sem);
        }
    }

    @Override
    public void offline() throws ServiceException {
        try {
            fooOnlineHandler.offline();
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("下线 Foo 服务时发生异常", LogLevel.WARN, e, sem);
        }
    }
}
```

启动器可根据配置延迟调用 QoS 方法，实现与进程启动解耦的可重入上线：

```java
public class ExampleLauncher {

    private static void mayOnlineFoo(ApplicationContext ctx) throws ServiceException {
        FooQosService fooQosService = ctx.getBean(FooQosService.class);
        fooQosService.online();
    }

    private static void mayOfflineFoo(ApplicationContext ctx) throws ServiceException {
        FooQosService fooQosService = ctx.getBean(FooQosService.class);
        fooQosService.offline();
    }
}
```

对于同时具备上线与启动能力的处理器（接口继承 `DistributedLockHandler`），启停分为两阶段：先 `online()` 后 `start()`，关闭时先 `stop()` 后 `offline()`。该模式同样支持延迟调度；以下示例中的 `FooQosService` 同时暴露 `online` / `start` / `stop` / `offline`，对应继承 `DistributedLockHandler` 的处理器：

```java
public class ExampleLauncher {

    // 两阶段启停：先上线（参与选主），再启动（允许工作）。
    private static void mayEnableFoo(ApplicationContext ctx) throws ServiceException {
        FooQosService fooQosService = ctx.getBean(FooQosService.class);
        fooQosService.online();
        fooQosService.start();
    }

    // 关闭：先停止工作，再下线（释放选主）。
    private static void mayDisableFoo(ApplicationContext ctx) throws ServiceException {
        FooQosService fooQosService = ctx.getBean(FooQosService.class);
        fooQosService.stop();
        fooQosService.offline();
    }
}
```

## 注意事项

### 单实例与多实例选型

- **单实例**：`GeneralOnlineHandler` 即可表达可重入的上线 / 下线；若业务不需要“上线”概念、仅需启动 / 停止，使用 `GeneralStartableHandler` 更简单。
- **多实例单活**：必须使用 `CuratorDistributedLockHandler`；`GeneralOnlineHandler` 无法阻止多节点同时 `work()`。

### 启停顺序与异常处理

- 约定顺序：先 `online()` 后 `start()`，先 `stop()` 后 `offline()`；`CuratorDistributedLockHandler` 的 `offline()` 内部会先触发 `rest()` 再关闭 latch。
- `Worker.work()` / `rest()` 抛出的受检异常会被包装为 `HandlerException` 向外传播。
- 实现 `Worker` 时应保证 `rest()` 在部分失败时仍尽可能释放资源，避免重复 `online()` / `start()` 时状态不一致。

## 相关文档

- [General Online Handler Basics](./GeneralOnlineHandlerBasics.md) — 通用线上处理器，单实例上线 / 下线的默认实现与使用方法。
- [General Startable Handler Basics](./GeneralStartableHandlerBasics.md) — 通用可启动处理器，单实例启动 / 停止的默认实现与使用方法。
- [Curator Distributed Lock Handler Basics](./CuratorDistributedLockHandlerBasics.md) — Curator 分布式锁处理器，多实例选主单活的默认实现与使用方法。
