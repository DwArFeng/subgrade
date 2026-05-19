# General Startable Handler Basics - 通用可启动处理器基础

## 综述

`GeneralStartableHandler` 是 `StartableHandler` 接口的通用实现，提供了线程安全的启动 / 停止能力。
`start()` 调用 `Worker.work()`，`stop()` 调用 `Worker.rest()`，
使业务后台逻辑可在运行态与停止态之间反复切换。

该实现适用于单实例内的消费流水线、定时任务聚合、组件批量启停等场景，
是业务逻辑可重入最常用的组合方式之一。
接口总览见 [Reentrant Business Logic Basics](./ReentrantBusinessLogicBasics.md)。

## 实现说明

### 内部状态

`GeneralStartableHandler` 内部维护：

- `worker`：非空的 `Worker` 实例，支持 `setWorker` 热替换（在锁内赋值）。
- `startedFlag`：是否已启动；仅在首次有效 `start()` 后置 `true`，在有效 `stop()` 后置 `false`。

### 线程安全机制

与 `GeneralOnlineHandler` 相同，使用 `ReentrantLock` 保护标志位与 `Worker` 调用。
`start()` / `stop()` 的幂等语义与接口 Javadoc 一致：已在目标状态时立即返回，不重复调用 `work()` / `rest()`。

### 与 Worker 的协作

业务实现类通常：

1. 定义继承 `StartableHandler` 的 stack 接口，并声明业务方法（如 `process`、`record` 等）。
2. 在 impl 中持有 `GeneralStartableHandler` 与独立的 `Processor`（执行业务调用）。
3. 在 `Worker.work()` / `rest()` 中编排子 Handler、线程池或会话的启停。

生命周期方法委托给 `GeneralStartableHandler`，业务方法在 impl 自有锁内访问 `Processor`，避免与启停锁混淆。

## 使用示例

以下示例展示可重入启停与业务方法并存的常见结构。

### 定义 Handler 接口

```java
package com.example.stack.handler;

import com.dwarfeng.subgrade.stack.exception.HandlerException;
import com.dwarfeng.subgrade.stack.handler.StartableHandler;
import com.example.stack.bean.dto.FooInfo;

/**
 * Foo 可启动处理器。
 *
 * <p>
 * 为 Foo 处理流水线提供可重入的启动 / 停止能力，并暴露业务处理方法。
 *
 * @author Example
 * @since 1.0.0
 */
public interface FooHandler extends StartableHandler {

    void process(FooInfo info) throws HandlerException;

    boolean isIdle() throws HandlerException;
}
```

### 实现 Handler 接口

```java
package com.example.impl.handler;

import com.dwarfeng.subgrade.impl.handler.GeneralStartableHandler;
import com.dwarfeng.subgrade.impl.handler.Worker;
import com.dwarfeng.subgrade.sdk.interceptor.analyse.BehaviorAnalyse;
import com.dwarfeng.subgrade.stack.exception.HandlerException;
import com.example.stack.bean.dto.FooInfo;
import com.example.stack.handler.FooHandler;
import com.example.stack.handler.BarConsumeHandler;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class FooHandlerImpl implements FooHandler {

    private final GeneralStartableHandler startableHandler;

    private final FooProcessor fooProcessor;

    private final Lock lock = new ReentrantLock();

    public FooHandlerImpl(FooWorker fooWorker, FooProcessor fooProcessor) {
        this.startableHandler = new GeneralStartableHandler(fooWorker);
        this.fooProcessor = fooProcessor;
    }

    @BehaviorAnalyse
    @Override
    public boolean isStarted() {
        lock.lock();
        try {
            return startableHandler.isStarted();
        } finally {
            lock.unlock();
        }
    }

    @BehaviorAnalyse
    @Override
    public void start() throws HandlerException {
        lock.lock();
        try {
            startableHandler.start();
        } finally {
            lock.unlock();
        }
    }

    @BehaviorAnalyse
    @Override
    public void stop() throws HandlerException {
        lock.lock();
        try {
            startableHandler.stop();
        } finally {
            lock.unlock();
        }
    }

    @BehaviorAnalyse
    @Override
    public void process(FooInfo info) throws HandlerException {
        lock.lock();
        try {
            fooProcessor.process(info);
        } finally {
            lock.unlock();
        }
    }

    @BehaviorAnalyse
    @Override
    public boolean isIdle() {
        lock.lock();
        try {
            return fooProcessor.isIdle();
        } finally {
            lock.unlock();
        }
    }

    @Component
    public static class FooWorker implements Worker {

        private final FooProcessor fooProcessor;
        private final BarConsumeHandler barConsumeHandler;

        public FooWorker(FooProcessor fooProcessor, BarConsumeHandler barConsumeHandler) {
            this.fooProcessor = fooProcessor;
            this.barConsumeHandler = barConsumeHandler;
        }

        @Override
        public void work() throws Exception {
            barConsumeHandler.start();
            fooProcessor.workerWork();
        }

        @Override
        public void rest() throws Exception {
            fooProcessor.workerRest();
            barConsumeHandler.stop();
        }
    }
}
```

### 在 QoS 或启动器中使用

```java
@Service
public class FooQosServiceImpl implements FooQosService {

    private final FooHandler fooHandler;
    private final ServiceExceptionMapper sem;

    public FooQosServiceImpl(FooHandler fooHandler, ServiceExceptionMapper sem) {
        this.fooHandler = fooHandler;
        this.sem = sem;
    }

    @PreDestroy
    public void dispose() throws Exception {
        fooHandler.stop();
    }

    @Override
    public void startFoo() throws ServiceException {
        try {
            fooHandler.start();
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("启动 Foo 服务时发生异常", LogLevel.WARN, e, sem);
        }
    }

    @Override
    public void stopFoo() throws ServiceException {
        try {
            fooHandler.stop();
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("停止 Foo 服务时发生异常", LogLevel.WARN, e, sem);
        }
    }
}
```

进程退出前通过 `@PreDestroy` 或启动器钩子调用 `stop()`，可避免后台线程泄漏。

启动器示例：

```java
public class ExampleLauncher {

    private static void mayStartFoo(ApplicationContext ctx) throws ServiceException {
        FooQosService fooQosService = ctx.getBean(FooQosService.class);
        fooQosService.startFoo();
    }

    private static void mayStopFoo(ApplicationContext ctx) throws ServiceException {
        FooQosService fooQosService = ctx.getBean(FooQosService.class);
        fooQosService.stopFoo();
    }
}
```

## 注意事项

### 启停与业务方法并发

`GeneralStartableHandler` 只序列化 `start` / `stop` 路径；
业务方法（如 `process`）若在 impl 层使用同一把锁，可避免停止过程中仍接受新任务。
若 `Processor` 内部另有队列，应在 `workerRest()` 中排空或拒绝新入队。

### 跨 Handler 可重入时的停启顺序

重置或维护流程中，常需暂时停止多个 Handler、清理缓存后再恢复。推荐模式：

1. 记录各 Handler 的 `isStarted()`（及分布式场景下的 `isStarted()` / 持锁状态）。
2. 按依赖顺序 `stop()`（例如先停止上游抓取，再停止处理）。
3. 执行清理（本地缓存 `clear`、会话关闭等）。
4. 若先前为已启动，再按依赖逆序 `start()`。

避免在未 `stop()` 的情况下清空正在被消费的数据结构。
