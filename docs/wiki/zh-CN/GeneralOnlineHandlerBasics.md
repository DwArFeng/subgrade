# General Online Handler Basics - 通用线上处理器基础

## 综述

`GeneralOnlineHandler` 是 `OnlineHandler` 接口的通用实现，提供了线程安全的上线 / 下线能力。
在单实例场景中，`online()` 会调用注入的 `Worker.work()`，`offline()` 会调用 `Worker.rest()`，
使业务逻辑可反复进入与退出运行态。

该实现不依赖 ZooKeeper 或 Curator。多实例且需选主单活时，应使用
[Curator Distributed Lock Handler Basics](./CuratorDistributedLockHandlerBasics.md) 中的 `CuratorDistributedLockHandler`。

接口总览见 [Reentrant Business Logic Basics](./ReentrantBusinessLogicBasics.md)。

## 实现说明

### 内部状态

`GeneralOnlineHandler` 内部维护：

- `worker`：非空的 `Worker` 实例，可在运行时通过 `setWorker` 替换（调用在锁内完成）。
- `onlineFlag`：是否已上线；仅在 `online()` 成功路径末尾置为 `true`，在 `offline()` 成功路径末尾置为 `false`。

### 线程安全机制

使用 `ReentrantLock`：所有 public 方法在锁内读写 `onlineFlag` 或调用 `Worker`。
`online()` 在已上线时直接返回；否则记录日志、执行 `worker.work()`，异常经 `HandlerExceptionHelper` 转为 `HandlerException`。
`offline()` 在未上线时直接返回；否则记录日志、执行 `worker.rest()`，同样包装异常。

### 与 Worker 的协作

`Worker` 定义业务侧进入 / 退出运行态的具体步骤。`GeneralOnlineHandler` 不解析业务含义，只保证：

- 每次有效 `online()` 至多触发一次 `work()`；
- 每次有效 `offline()` 至多触发一次 `rest()`；
- 与 `online` / `offline` 的幂等语义一致。

## 使用示例

以下示例使用虚拟业务，展示组合 `GeneralOnlineHandler` 的常见写法。

### 定义 Handler 接口

```java
package com.example.stack.handler;

import com.dwarfeng.subgrade.stack.handler.OnlineHandler;

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

### 实现 Handler 接口

```java
package com.example.impl.handler;

import com.dwarfeng.subgrade.impl.handler.GeneralOnlineHandler;
import com.dwarfeng.subgrade.impl.handler.Worker;
import com.dwarfeng.subgrade.sdk.interceptor.analyse.BehaviorAnalyse;
import com.dwarfeng.subgrade.stack.exception.HandlerException;
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

服务层可薄封装 Handler，供启动器或 Telqos 调用：

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

## 注意事项

### 单实例与多实例选型

- **单实例**：`GeneralOnlineHandler` 即可表达可重入的上线 / 下线。
- **多实例单活**：应使用 `CuratorDistributedLockHandler`；`GeneralOnlineHandler` 无法阻止多节点同时 `work()`。

### 异常与 HandlerException

`Worker.work()` / `rest()` 抛出受检异常时，会被包装为 `HandlerException` 向外传播。
实现 `Worker` 时应保证 `rest()` 在部分失败时仍尽可能释放资源，避免重复 `online()` 时状态不一致。
