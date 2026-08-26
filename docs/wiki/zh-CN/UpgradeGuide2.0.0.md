# Upgrade Guide 2.0.0 - 升级指南 2.0.0

## 综述

本文档提供从 Subgrade 1.x 升级到 2.0.0 版本的详细指南。

**重要提示**：2.0.0 是一次不兼容升级。 本版本将原有的 `subgrade-stack`、`subgrade-sdk` 和 `subgrade-impl` 按功能拆分为多个
Maven artifact 和 JPMS 模块， 并同时升级到 JDK 25 与 Jakarta API。升级后需要修改 Maven 依赖、Java 包名、`module-info.java` 和
`javax.*` 导入。

2.0.0 不提供旧 artifact 的兼容转发模块。 如果应用继续声明 `subgrade-stack`、`subgrade-sdk` 或 `subgrade-impl`，Maven 和
JPMS 都无法完成升级后的依赖解析。

## 升级前准备

### 确认构建环境

2.0.0 的构建环境要求如下：

- JDK 25 或更高版本，不启用预览特性。
- Maven 3.9.16 或更高版本。
- 如果应用使用 JPMS，需要使用支持模块化编译的 Maven 构建配置。

升级前应记录当前 JDK、Maven、Subgrade 版本和所有直接依赖，并保留一份可回滚的 `pom.xml` 与 `module-info.java` 备份。

### 盘点代码使用范围

建议在升级前检索以下名称，并分别记录它们所属的功能模块：

- `com.dwarfeng.subgrade.stack`
- `com.dwarfeng.subgrade.sdk`
- `com.dwarfeng.subgrade.impl`
- `javax.servlet`、`javax.persistence`、`javax.validation` 和 `javax.xml.bind`
- `module-info.java` 中对 `com.dwarfeng.subgrade.*` 的 `requires` 和 `exports`

## 不兼容性改动

### Maven artifact 与 JPMS 模块拆分

1.x 主要使用以下三个 artifact：

- `subgrade-stack`
- `subgrade-sdk`
- `subgrade-impl`

2.0.0 按功能拆分为十个 artifact，每个 artifact 对应一个 JPMS 模块：

| Maven artifact        | JPMS 模块名                        | 主要职责                                 |
|:----------------------|:-----------------------------------|:-----------------------------------------|
| `subgrade-base`       | `com.dwarfeng.subgrade.base`       | 国际化与模块内部基础机制                 |
| `subgrade-basic`      | `com.dwarfeng.subgrade.basic`      | Bean、Key、基础异常、生成器与日志协议    |
| `subgrade-data`       | `com.dwarfeng.subgrade.data`       | Dao、Service、实体缓存及数据访问实现     |
| `subgrade-expression` | `com.dwarfeng.subgrade.expression` | 通用表达式协议与解析实现                 |
| `subgrade-aop`        | `com.dwarfeng.subgrade.aop`        | 行为分析与通用 AOP 能力                  |
| `subgrade-web`        | `com.dwarfeng.subgrade.web`        | Web 响应、登录、权限与验证               |
| `subgrade-lifecycle`  | `com.dwarfeng.subgrade.lifecycle`  | Startable、Online 与 Worker 生命周期能力 |
| `subgrade-cache`      | `com.dwarfeng.subgrade.cache`      | 进程内本地缓存                           |
| `subgrade-lock`       | `com.dwarfeng.subgrade.lock`       | 分布式锁及 Curator 实现                  |
| `subgrade-kafka`      | `com.dwarfeng.subgrade.kafka`      | Kafka 序列化与 Fastjson2 适配            |

不应将三个旧 artifact 机械替换为某一个新 artifact。应根据应用实际使用的接口和实现选择对应的新模块。

### Java 包名调整

2.0.0 将以下旧包按功能迁移到新模块。下表列出常用的迁移方向：

| 1.x 包前缀                                         | 2.0.0 常用包前缀                                                      |
|:---------------------------------------------------|:----------------------------------------------------------------------|
| `com.dwarfeng.subgrade.stack.bean`                 | `com.dwarfeng.subgrade.basic.stack.bean`                              |
| `com.dwarfeng.subgrade.stack.generation`           | `com.dwarfeng.subgrade.basic.stack.generation`                        |
| `com.dwarfeng.subgrade.stack.log`                  | `com.dwarfeng.subgrade.basic.stack.log`                               |
| `com.dwarfeng.subgrade.stack.service`              | `com.dwarfeng.subgrade.basic.stack.service`                           |
| `com.dwarfeng.subgrade.stack.dao`                  | `com.dwarfeng.subgrade.data.stack.dao`                                |
| `com.dwarfeng.subgrade.stack.cache`                | `com.dwarfeng.subgrade.data.stack.cache`                              |
| `com.dwarfeng.subgrade.stack.handler`              | 根据功能迁移到 `basic`、`data`、`lifecycle`、`cache`、`lock` 或 `web` |
| `com.dwarfeng.subgrade.stack.expression`           | `com.dwarfeng.subgrade.expression.stack.parser`                       |
| `com.dwarfeng.subgrade.sdk.database`               | `com.dwarfeng.subgrade.data.sdk.database`                             |
| `com.dwarfeng.subgrade.sdk.hibernate`              | `com.dwarfeng.subgrade.data.sdk.hibernate`                            |
| `com.dwarfeng.subgrade.sdk.jdbc`                   | `com.dwarfeng.subgrade.data.sdk.jdbc`                                 |
| `com.dwarfeng.subgrade.sdk.memory`                 | `com.dwarfeng.subgrade.data.sdk.memory`                               |
| `com.dwarfeng.subgrade.sdk.mybatis`                | `com.dwarfeng.subgrade.data.sdk.mybatis`                              |
| `com.dwarfeng.subgrade.sdk.redis`                  | `com.dwarfeng.subgrade.data.sdk.redis`                                |
| `com.dwarfeng.subgrade.sdk.interceptor.analyse`    | `com.dwarfeng.subgrade.aop.sdk.interceptor.analyse`                   |
| `com.dwarfeng.subgrade.sdk.interceptor.friendly`   | `com.dwarfeng.subgrade.aop.sdk.interceptor.friendly`                  |
| `com.dwarfeng.subgrade.sdk.interceptor.login`      | `com.dwarfeng.subgrade.web.sdk.interceptor.login`                     |
| `com.dwarfeng.subgrade.sdk.interceptor.permission` | `com.dwarfeng.subgrade.web.sdk.interceptor.permission`                |
| `com.dwarfeng.subgrade.sdk.validation`             | `com.dwarfeng.subgrade.web.sdk.validation`                            |
| `com.dwarfeng.subgrade.impl.dao`                   | `com.dwarfeng.subgrade.data.impl.dao`                                 |
| `com.dwarfeng.subgrade.impl.service`               | `com.dwarfeng.subgrade.data.impl.service`                             |
| `com.dwarfeng.subgrade.impl.cache`                 | `com.dwarfeng.subgrade.data.impl.cache`                               |
| `com.dwarfeng.subgrade.impl.handler`               | 根据功能迁移到 `data`、`lifecycle`、`cache` 或 `lock`                 |

`stack.handler` 和 `impl.handler` 不能统一替换。 例如，`OnlineHandler` 和 `StartableHandler` 迁移到 `lifecycle`，
`LocalCacheHandler` 迁移到 `cache`，
`DistributedLockHandler` 迁移到 `lock`，`LoginHandler` 和 `TokenHandler` 迁移到 `web`。

### JDK 25 与 Jakarta 迁移

2.0.0 使用 JDK 25 作为编译基线，并将 Java EE 相关 API 替换为 Jakarta API。常见包名替换如下：

| 1.x                   | 2.0.0                   |
|:----------------------|:------------------------|
| `javax.servlet.*`     | `jakarta.servlet.*`     |
| `javax.persistence.*` | `jakarta.persistence.*` |
| `javax.validation.*`  | `jakarta.validation.*`  |
| `javax.xml.bind.*`    | `jakarta.xml.bind.*`    |

`javax.annotation` 中的标注如 `Nonnull`、`Nullable` 和 `Nonnegative` 也需要按应用的依赖版本选择对应的现代标注包， 不应继续依赖旧
Java EE API。

### 配置与国际化机制调整

2.0.0 各功能模块拥有独立的消息资源和异常码空间，使用 `subgrade-base` 提供的模块感知国际化机制进行消息解析。

Web 模块提供以下系统属性名称：

- `com.dwarfeng.subgrade.web.exception_code_offset`：Web 模块的异常码偏移量。
- `com.dwarfeng.subgrade.web.permission_required.use_expression`：是否使用通用表达式解析器解析 `@PermissionRequired`
  的权限值。

## 改动原因

2.0.0 的主要目标是改善模块边界、依赖可见性和独立发布能力：

1. **按功能拆分**：将数据访问、Web、AOP、生命周期和中间件适配等能力分开维护，避免应用引入不需要的依赖。
2. **支持 JPMS**：每个功能 artifact 提供明确的模块名和导出包，方便应用控制模块可见性。
3. **升级平台基线**：使用 JDK 25 和 Jakarta API，与当前 Java 生态的模块化与依赖版本保持一致。
4. **模块内部国际化**：通过模块与资源基础名的绑定，避免不同功能模块之间的消息键冲突。

## 代码迁移指南

### 1. 迁移 Maven 依赖

1.x 项目中常见的依赖声明如下：

```xml

<dependency>
    <groupId>com.dwarfeng</groupId>
    <artifactId>subgrade-sdk</artifactId>
    <version>${subgrade.version}</version>
</dependency>
<dependency>
<groupId>com.dwarfeng</groupId>
<artifactId>subgrade-impl</artifactId>
<version>${subgrade.version}</version>
</dependency>
```

2.0.0 应根据实际用到的能力拆分声明。例如，数据访问应用可以使用：

```xml

<dependencies>
    <dependency>
        <groupId>com.dwarfeng</groupId>
        <artifactId>subgrade-basic</artifactId>
        <version>${subgrade.version}</version>
    </dependency>
    <dependency>
        <groupId>com.dwarfeng</groupId>
        <artifactId>subgrade-data</artifactId>
        <version>${subgrade.version}</version>
    </dependency>
</dependencies>
```

如果应用还使用 Web、AOP、本地缓存、分布式锁、表达式或 Kafka 能力， 则按需增加 `subgrade-web`、`subgrade-aop`、`subgrade-cache`、
`subgrade-lock`、`subgrade-expression` 或 `subgrade-kafka`。 如果直接使用了基础国际化协议，还需声明 `subgrade-base`。

### 2. 迁移 Java import

应先按照上文的包名映射修改 import，再根据编译错误补充新模块中的具体包路径。例如：

```java
// 变更前

import com.dwarfeng.subgrade.stack.dao.Dao;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;

import javax.servlet.http.HttpServletRequest;

// 变更后
import com.dwarfeng.subgrade.data.stack.dao.Dao;
import com.dwarfeng.subgrade.basic.stack.bean.entity.Entity;
import jakarta.servlet.http.HttpServletRequest;
```

`Dao` 、`Entity` 和 `HttpServletRequest` 只是示例，实际包名应以应用使用的 2.0.0 API 为准。 对于 `stack.handler` 和
`sdk.interceptor`，必须先根据业务功能判断目标模块，不建议仅通过字符串替换包名。

### 3. 迁移 module-info.java

1.x 版本本身未提供这些功能的正式 JPMS 模块声明。 如果应用自己使用 `module-info.java`，应删除对旧 artifact
自动模块名或旧包名的依赖，改为对实际功能模块的声明。

例如：

```java
// 变更后：以使用基础模块、Dao 和 Web 能力为例
module example.application {
    requires com.dwarfeng.subgrade.basic;
    requires com.dwarfeng.subgrade.data;
    requires com.dwarfeng.subgrade.web;
}
```

仅能在应用实际使用该模块导出包时添加 `requires`。 如果应用自身需要使用 `jakarta.servlet` 或 `jakarta.validation`
，还应根据自身的模块配置增加对应 API 模块的依赖。

### 4. 迁移 Jakarta API

将应用中的 Java EE import 替换为 Jakarta import，并检查框架配置、注解扫描和序列化配置。 不能只修改 import 而保留 `javax`
版本的 Maven 依赖。

### 5. 迁移登录、权限和表达式配置

Web 模块的 `LoginHandler`、`PermissionHandler` 和 `TokenHandler` 使用 `String` 表示用户 ID 与登录 ID。 如果旧代码使用
`LongIdKey` 或 `StringIdKey`，应在应用边界完成显式转换，不要将旧键对象继续传入新接口。

`@PermissionRequired` 的权限值可根据配置使用通用表达式解析器。 如果开启了表达式解析，应同时引入 `subgrade-expression`
，并确保表达式来源可信。

## 注意事项

1. **不要保留旧 artifact**：`subgrade-stack`、`subgrade-sdk` 和 `subgrade-impl` 在 2.0.0 中不再提供，应完全替换为功能化
   artifact。
2. **按功能声明依赖**：新版本不保证某个单一 artifact 包含旧版本的全部代码，应按实际 API 补充依赖。
3. **检查实现类包名**：公开实现类也随模块迁移，自定义代码中的实现类 import 和类名都需要检查。
4. **不要混用 javax 与 jakarta**：同一应用中混用两套 API 可能导致注解不生效、类型不兼容或运行时类加载失败。
5. **模块访问限制**：JPMS 模块只导出公开 API 包，应用不应依赖未导出的 `internal` 包。
6. **登录 ID 应保持随机性**：`LoginHandler.login()` 返回的登录 ID 必须具有足够的随机性，不应使用自增主键或其他有明显规律的值。
7. **使用测试环境验证**：升级后应重点测试数据访问、缓存、登录权限、AOP 和消息解析功能。

## 常见问题

### Q: 为什么替换版本后 Maven 无法找到 `subgrade-stack`？

A: 2.0.0 已删除旧 artifact，也不提供兼容转发模块。 请根据实际功能将依赖改为 `subgrade-basic`、`subgrade-data` 或其他对应模块。

### Q: 替换为新 artifact 后出现大量包不存在错误，应该如何处理？

A: 先根据包名映射表修改 import，再检查该类是否已经迁移到另一个功能模块。
`stack.handler` 和 `impl.handler` 的迁移不是一对一的，需要按接口功能判断目标包。

### Q: 为什么 `javax.servlet` 和 `jakarta.servlet` 不能互换？

A: 两者属于不同的 Java 包和 API 系统。应用的 import、Maven 依赖、注解处理和运行时框架必须使用同一套 API。

### Q: 升级后是否必须使用 JPMS？

A: 不必。应用即使不使用自己的 `module-info.java`，仍然需要使用新 artifact 和新包名。 如果应用使用 JPMS，则还需要同步更新
`requires`。

### Q: 升级后是否必须同时引入 `subgrade-base`？

A: 应用如果只使用其他模块的对外 API，通常应由 Maven 传递依赖提供基础模块。 只有在应用直接使用 `MessageCatalog`、
`MessageResolver` 或其他 `subgrade-base` 公开 API 时，才需要显式声明它。

### Q: 升级后如何确认所有旧包名都已经清理？

A: 可以在项目源码、资源和 `module-info.java` 中搜索
`com.dwarfeng.subgrade.stack`、`com.dwarfeng.subgrade.sdk`、`com.dwarfeng.subgrade.impl` 以及 `javax.`。
搜索结果为空后，再执行完整编译和测试。

## 参阅

- [Use with Maven](./UseWithMaven.md) - 通过 Maven 使用本项目。
- [Install by Source Code](./InstallBySourceCode.md) - 通过源码安装本项目。
- [Upgrade Guide 1.7.0](./UpgradeGuide1.7.0.md) - 升级指南 1.7.0，用于参考 1.x 时期的接口迁移。
- [Introduction](./Introduction.md) - Subgrade 简介与推荐使用版本。
