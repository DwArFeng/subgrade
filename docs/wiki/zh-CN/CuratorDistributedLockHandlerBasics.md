# Curator Distributed Lock Handler Basics - Curator 分布式锁处理器基础

## 综述

`CuratorDistributedLockHandler` 是 `DistributedLockHandler` 接口的通用实现，基于 Apache Curator `LeaderLatch` 实现选主，
并与 `Worker` 协作完成可重入的上线、启动、持锁工作与停止。

多实例部署时，仅 Leader 实例在“已启动”且持锁条件下执行 `Worker.work()`；失去 Leader 时调用 `rest()`。
单实例场景亦可使用本类，但需部署可用的 Curator/ZooKeeper 客户端；
若无需选主， [General Startable Handler Basics](./GeneralStartableHandlerBasics.md) 更简单。

接口与状态说明见 [Reentrant Business Logic Basics](./ReentrantBusinessLogicBasics.md)。

## 实现说明

### 内部状态

实现类在锁内维护：

| 标志              | 含义                                    |
|:------------------|:----------------------------------------|
| `onlineFlag`      | 已调用 `online()`，`LeaderLatch` 已启动 |
| `startedFlag`     | 已调用 `start()`，允许在持锁时工作      |
| `lockHoldingFlag` | 当前实例为 Leader                       |
| `workingFlag`     | 已执行 `work()` 且尚未 `rest()`         |

另持有 `CuratorFramework`、`leaderLatchPath`、`Worker` 及 `LeaderLatch` 实例（上线后非空）。

### LeaderLatch 与选主

`online()` 创建 `LeaderLatch(curatorFramework, leaderLatchPath)`，注册 `LeaderLatchListener`，并 `start()`。
`isLeader()` 回调中：若尚未标记持锁，则置 `lockHoldingFlag`；若已 `startedFlag`，则调用内部 `work()`。
`notLeader()` 回调中：若曾持锁，则调用 `rest()` 并清除 `lockHoldingFlag`；
`rest()` 异常会记录 error 日志（锁语义可能被破坏）。

`offline()` 在关闭 latch 前先 `rest()`，再以 `LeaderLatch.CloseMode.NOTIFY_LEADER` 关闭并清空 latch 引用。

### 上线、启动、持锁与工作的关系

| 调用 / 事件 | 效果                                      |
|:------------|:------------------------------------------|
| `online()`  | 参与选主，不必然 `work()`                 |
| `start()`   | `startedFlag = true`；若已持锁则 `work()` |
| `stop()`    | `rest()`，`startedFlag = false`           |
| 成为 Leader | 若已 `start()`，则 `work()`               |
| 失去 Leader | `rest()`                                  |

因此运维上常见顺序为：先 `online()` 再 `start()`；
仅 `start()` 而不 `online()` 不会选主；
仅 `online()` 而不 `start()` 时即使成为 Leader 也不执行 `Worker`。

### 线程安全机制

所有 public 方法与 `LeaderLatchListener` 回调均在同一 `ReentrantLock` 下执行，
避免标志位与 `work()` / `rest()` 竞态。

## 使用示例

### 定义 Handler 接口

```java
package com.example.stack.handler;

import com.dwarfeng.subgrade.lock.stack.handler.DistributedLockHandler;

/**
 * Foo 分布式锁处理器。
 *
 * <p>
 * 在集群中为 Foo 后台任务提供选主与可重入启停能力。
 *
 * @author Example
 * @since 1.0.0
 */
public interface FooHandler extends DistributedLockHandler {
}
```

### 实现 Handler 接口

```java
package com.example.impl.handler;

import com.dwarfeng.subgrade.aop.sdk.interceptor.analyse.BehaviorAnalyse;
import com.dwarfeng.subgrade.basic.stack.exception.HandlerException;
import com.dwarfeng.subgrade.lifecycle.stack.handler.Worker;
import com.dwarfeng.subgrade.lock.impl.handler.curator.CuratorDistributedLockHandler;
import com.example.stack.handler.FooHandler;
import com.example.stack.handler.BarSession;
import com.example.stack.handler.BarSessionHoldHandler;
import com.dwarfeng.subgrade.basic.stack.bean.key.LongIdKey;
import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class FooHandlerImpl implements FooHandler {

    private static final String LEADER_LATCH_SUFFIX_DEFAULT = "default";

    private final CuratorDistributedLockHandler handler;

    public FooHandlerImpl(
            CuratorFramework curatorFramework,
            @Value("${curator.latch_path.foo.leader_latch_prefix}") String leaderLatchPathPrefix,
            @Value("${foo.tenant_id}") Integer tenantId,
            FooWorker fooWorker
    ) {
        String leaderLatchPath = leaderLatchPathPrefix + (
                Objects.isNull(tenantId) ? LEADER_LATCH_SUFFIX_DEFAULT : tenantId
        );
        handler = new CuratorDistributedLockHandler(curatorFramework, leaderLatchPath, fooWorker);
    }

    @BehaviorAnalyse
    @Override
    public boolean isOnline() {
        return handler.isOnline();
    }

    @BehaviorAnalyse
    @Override
    public void online() throws HandlerException {
        handler.online();
    }

    @BehaviorAnalyse
    @Override
    public void offline() throws HandlerException {
        handler.offline();
    }

    @BehaviorAnalyse
    @Override
    public boolean isStarted() {
        return handler.isStarted();
    }

    @BehaviorAnalyse
    @Override
    public void start() throws HandlerException {
        handler.start();
    }

    @BehaviorAnalyse
    @Override
    public void stop() throws HandlerException {
        handler.stop();
    }

    @BehaviorAnalyse
    @Override
    public boolean isLockHolding() {
        return handler.isLockHolding();
    }

    @BehaviorAnalyse
    @Override
    public boolean isWorking() {
        return handler.isWorking();
    }

    @Component
    public static class FooWorker implements Worker {

        private static final Logger LOGGER = LoggerFactory.getLogger(FooWorker.class);

        private final BarSessionHoldHandler barSessionHoldHandler;
        private final List<BarSession> startedSessions = new ArrayList<>();

        public FooWorker(BarSessionHoldHandler barSessionHoldHandler) {
            this.barSessionHoldHandler = barSessionHoldHandler;
        }

        @Override
        public void work() throws Exception {
            LOGGER.info("Foo 处理器开始工作...");
            startedSessions.clear();
            List<LongIdKey> keys = barSessionHoldHandler.allKeys();
            for (LongIdKey key : keys) {
                try {
                    BarSession session = barSessionHoldHandler.get(key);
                    session.start();
                    startedSessions.add(session);
                } catch (Exception e) {
                    LOGGER.warn("启动 Bar 会话失败, key={}, 将跳过", key, e);
                }
            }
        }

        @Override
        public void rest() {
            LOGGER.info("Foo 处理器停止工作...");
            for (BarSession session : startedSessions) {
                try {
                    session.stop();
                } catch (Exception e) {
                    LOGGER.warn("停止 Bar 会话失败, session={}", session, e);
                }
            }
            startedSessions.clear();
        }
    }
}
```

`leaderLatchPath` 应按业务维度全局唯一（如前缀 + 租户 ID），避免不同服务争用同一路径。

### 启动顺序建议

启动器中宜将“上线”与“启动”分为两阶段，并支持延迟调度：

```java
public class ExampleLauncher {

    private static void mayEnableFoo(ApplicationContext ctx) throws ServiceException {
        FooQosService fooQosService = ctx.getBean(FooQosService.class);
        // 1. 参与选主。
        fooQosService.online();
        // 2. 允许在成为 Leader 时执行 Worker。
        fooQosService.start();
    }
}
```

关闭时一般先 `stop()` 再 `offline()`，
或由 `offline()` 内部触发 `rest()` 与 latch 关闭（实现已包含停止工作逻辑）。

Telqos 或管理接口可分别暴露 `online` / `offline` / `start` / `stop` 及 `isLockHolding` / `isWorking` 查询，便于排障。

## 注意事项

### 配置与 LeaderLatch 路径

- 依赖可用的 `CuratorFramework` Bean 与正确的 ZK 连接配置。
- `leaderLatchPath` 在同一集群内必须唯一标识“一类单活任务”；多租户宜使用前缀 + 租户标识拼接。
- 修改 `leaderLatchPath` 相当于另一选主命名空间，旧 Leader 不会自动迁移。

### 异常与锁语义

- `Worker.work()` 在 `isLeader` 回调中异常时，实现会记录 warn 且可能不进入 `workingFlag`，需结合日志排查。
- `notLeader` 路径上 `rest()` 失败会记录 error，表示分布式锁语义可能不一致，应告警并人工介入。
- 业务 `Worker` 应对单个会话启停失败做容错（记录日志、继续其余会话），避免一次失败阻塞整体 `rest()`。
