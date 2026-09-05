# Audit Record - 审计记录

审计记录功能是 Subgrade 框架中提供的一个 AOP 增强功能，它能够在方法执行完成后记录审计信息，
包括审计类别、方法入口参数、执行结果、异常信息以及 HTTP 请求信息等。
这个功能适用于操作留痕、安全审计、问题排查和业务行为追踪等场景。

审计记录功能通过注解驱动的方式工作，开发者在需要记录的方法上添加 `@AuditRecord` 注解，
并指定审计类别 ID 和审计记录增强管理器，框架就会生成审计属性并交由审计记录处理器提交。

---

## 综述

审计记录功能自 2.1.0 起在 JDK 25 分支提供。
`subgrade-aop` 提供注解、AOP Advisor、审计上下文、属性生成管理器、结果与异常贡献器，以及审计记录处理器接口。
`subgrade-web` 提供基于 Jakarta Servlet 的 HTTP 请求属性贡献器。

与直接在业务方法中编写审计代码的方式相比，审计记录功能将业务方法执行和审计记录处理分离开来。
业务方法只需要声明审计类别和属性生成管理器，不需要关心审计信息的具体存储方式。

审计记录功能属于外围增强能力。审计属性生成或提交过程中发生 `Exception` 时，Advisor 记录警告日志，
不会修改业务方法的返回值，也不会覆盖业务方法抛出的原始异常。审计代码自身抛出的 `Error` 不属于此隔离范围。

### 模块与包名

| 组件                          | Maven artifact | Java 包名                                                 |
|:------------------------------|:---------------|:----------------------------------------------------------|
| 注解、Advisor、上下文与管理器 | `subgrade-aop` | `com.dwarfeng.subgrade.aop.sdk.interceptor.audit`         |
| 返回值、异常贡献器            | `subgrade-aop` | `com.dwarfeng.subgrade.aop.sdk.interceptor.audit.propctr` |
| 审计处理器接口                | `subgrade-aop` | `com.dwarfeng.subgrade.aop.stack.handler`                 |
| HTTP 请求贡献器               | `subgrade-web` | `com.dwarfeng.subgrade.web.sdk.interceptor.audit.propctr` |

仅使用通用审计能力时不需要 Web 模块或 Servlet 容器。
使用 HTTP 请求贡献器时，方法参数类型为 `jakarta.servlet.http.HttpServletRequest`。

### Maven 与 JPMS

通用审计使用以下依赖：

```xml
<dependency>
    <groupId>com.dwarfeng</groupId>
    <artifactId>subgrade-aop</artifactId>
    <version>2.1.0.a</version>
</dependency>
```

使用 HTTP 请求贡献器时，额外声明相同版本的 Web 模块：

```xml
<dependency>
    <groupId>com.dwarfeng</groupId>
    <artifactId>subgrade-web</artifactId>
    <version>2.1.0.a</version>
</dependency>
```

应用使用 JPMS 时，通用审计 API 对应 `requires com.dwarfeng.subgrade.aop`；
HTTP 贡献器还对应 `requires com.dwarfeng.subgrade.web`。应用直接使用 Servlet API 时声明 `requires jakarta.servlet`。
直接使用基础处理器异常时声明 `requires com.dwarfeng.subgrade.basic`。

例如，下面的应用同时使用审计、HTTP 贡献器和 Spring 配置，业务对象位于 `com.example.auditdemo` 包：

```java

@SuppressWarnings("JavaModuleDefinition")
module com.example.auditdemo {
    requires com.dwarfeng.subgrade.aop;
    requires com.dwarfeng.subgrade.basic;
    requires com.dwarfeng.subgrade.web;
    requires jakarta.servlet;
    requires spring.context;
    requires spring.core;
    requires spring.beans;
    requires spring.aop;

    opens com.example.auditdemo to spring.core, spring.beans, spring.context, spring.aop;
}
```

应用依照实际包结构开放需要 Spring 反射访问的配置和业务包。
AOP 模块已将审计核心包定向开放给 `spring.core`、`spring.beans`，
以支持 Advisor 的字段注入。模块导出和开放不等同于 Bean 注册，应用仍需启用 Spring AOP 并注册下述组件。

## 核心组件

### AuditRecord 注解

`@AuditRecord` 注解是审计记录功能的核心，用于标记需要记录审计信息的方法。

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface AuditRecord {

    /**
     * 审计类别 ID。
     */
    String categoryId();

    /**
     * 审计记录增强管理器的 Bean 名称。
     */
    String manager();
}
```

其中，`categoryId` 用于标识当前审计记录所属的业务类别，`manager` 用于指定生成审计属性的
`AuditRecordAopManager` Bean 名称。两个属性均为必填属性。

### AuditRecordAdvisor

`AuditRecordAdvisor` 是审计记录功能的 AOP 切面，负责处理方法上的 `@AuditRecord` 注解。

在目标方法正常返回后，Advisor 会根据返回值构建成功状态的审计上下文；
在目标方法抛出异常后，Advisor 会根据异常构建失败状态的审计上下文。
随后，Advisor 根据 `manager` 属性选择管理器，生成审计属性，并调用 `AuditRecordHandler` 提交审计信息。

### AuditRecordContext

`AuditRecordContext` 表示一次被审计方法的完整调用结果，包含以下内容：

- **调用连接点**：当前方法对应的 AspectJ 调用连接点。
- **目标方法**：当前被审计的 Java 方法。
- **入口参数**：目标方法调用时传入的参数数组。
- **返回值**：目标方法正常执行时的返回对象。
- **异常信息**：目标方法异常结束时抛出的异常。
- **执行状态**：通过 `isSuccess()` 判断方法是否正常完成。

入口参数数组在上下文构建时会进行浅复制，读取上下文参数时也会返回参数数组的副本，从而避免外部替换数组本身影响审计处理。
数组中的对象仍是原对象，并不是业务执行前的深度快照。

### AuditRecordAopManager

`AuditRecordAopManager` 是审计记录增强管理器接口，负责根据 `AuditRecordContext` 生成审计属性。

管理器只负责审计属性生成，不负责执行代理方法，也不负责将审计信息提交到具体的审计系统。
审计信息的提交由 `AuditRecordHandler` 负责。

### PropertyContributor

`PropertyContributor` 是审计属性贡献器接口，用于生成一次审计记录中的部分属性。

贡献器通过 `supports` 方法判断是否支持当前审计上下文，再通过 `getProperties` 方法生成属性映射。
多个贡献器可以由 `ContributorAuditRecordAopManager` 组合，从而复用请求、返回值和异常等通用属性处理逻辑。

### AuditRecordHandler

`AuditRecordHandler` 是 `subgrade-aop` 中定义的审计记录处理器接口，继承自 Basic 模块的通用 `Handler` 标记接口。
该接口通过 `record(String categoryId, Map<String, Object> properties)` 接收审计类别 ID 和审计属性映射，
声明可抛出 `com.dwarfeng.subgrade.basic.stack.exception.HandlerException`。
具体实现可以将审计信息提交到数据库、日志系统、消息队列或其他审计平台。

## 功能特性

### 自动记录功能

在目标方法上添加 `@AuditRecord` 注解并完成 Spring 配置后，框架会完成以下操作：

- 读取注解中的审计类别 ID。
- 根据管理器 Bean 名称选择审计属性生成管理器。
- 构建包含方法、参数、返回值或异常的审计上下文。
- 生成审计属性映射。
- 调用审计记录处理器提交审计信息。

### 正常执行记录

当目标方法正常返回时，审计上下文会被标记为成功状态。默认管理器会记录入口参数、成功标记和返回值；
返回值为 `null` 或方法返回类型为 `void` 时，结果的字符串表示为 `"null"`。

### 异常执行记录

当目标方法抛出异常时，审计上下文会被标记为失败状态。默认管理器会记录入口参数、失败标记、异常类型和异常消息，
同时将原始异常继续向上抛出。

### 默认审计属性

`DefaultAuditRecordAopManager` 使用固定的属性键生成审计属性：

| 属性键             | 正常执行       | 异常执行       | 说明                       |
|:-------------------|:---------------|:---------------|:---------------------------|
| `arguments`        | 入口参数字符串 | 入口参数字符串 | 使用深度字符串形式表示参数 |
| `success`          | `true`         | `false`        | 表示方法是否正常完成       |
| `result`           | 返回值字符串   | 不生成         | 仅在正常执行时生成         |
| `exceptionType`    | 不生成         | 异常的完整类名 | 仅在异常执行时生成         |
| `exceptionMessage` | 不生成         | 异常消息字符串 | 仅在异常执行时生成         |

### 贡献器式属性生成

`ContributorAuditRecordAopManager` 按照配置顺序执行多个 `PropertyContributor`。
不支持当前上下文的贡献器会被跳过，贡献器返回的 `null` 属性映射会被忽略，属性值为 `null` 的属性不会被加入最终结果。

如果贡献器生成 `null` 属性键，或多个贡献器生成相同的非空值属性键，管理器会抛出异常，避免不同贡献器静默覆盖审计数据。
配置的贡献器列表会被复制并以不可修改的形式保存。

### HTTP 请求属性

`HttpServletRequestPropertyContributor` 会从方法入口参数中查找第一个 `jakarta.servlet.http.HttpServletRequest`，
并记录以下稳定的请求属性：

- `requestMethod`：HTTP 请求方法。
- `requestUri`：HTTP 请求 URI。
- `remoteAddress`：请求对象提供的远程地址。

没有请求参数时该贡献器不适用；请求属性为 `null` 时省略对应属性。
为了避免泄露凭证或产生过大的审计属性，该贡献器不会记录请求头、请求体、Token 和 Session。

### 审计能力故障隔离

审计属性生成失败、找不到管理器、审计类别 ID 为空或审计记录处理器提交失败时，Advisor 会捕获 `Exception` 并记录警告日志。
这些失败不会改变目标方法的正常返回值，也不会覆盖目标方法抛出的原始异常。

此隔离发生在属性生成和提交阶段，不涵盖 Spring 容器启动失败，也不吞掉审计代码自身的 `Error`。
审计提交在当前调用线程同步进行，不自带重试或事务提交后回调。

## 使用示例

### 基本使用

在业务方法上添加 `@AuditRecord` 注解，并指定审计类别 ID 和管理器 Bean 名称。以下用简化的用户对象展示调用方式：

```java
import com.dwarfeng.subgrade.aop.sdk.interceptor.audit.AuditRecord;
import com.dwarfeng.subgrade.aop.sdk.interceptor.audit.DefaultAuditRecordAopManager;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @AuditRecord(
            categoryId = "user.create",
            manager = DefaultAuditRecordAopManager.BEAN_NAME
    )
    public User createUser(String username) {
        return new User(username);
    }

    public record User(String username) {
    }
}
```

### 使用默认审计管理器

默认管理器适用于审计属性较为简单，只需要记录入口参数、成功状态、返回值或异常信息的场景。
以下配置启用 Spring AOP、注册 Advisor 和默认管理器；业务 Bean 与应用提供的 Handler 仍需注册到同一个容器。

```java
import com.dwarfeng.subgrade.aop.sdk.interceptor.audit.AuditRecordAdvisor;
import com.dwarfeng.subgrade.aop.sdk.interceptor.audit.AuditRecordAopManager;
import com.dwarfeng.subgrade.aop.sdk.interceptor.audit.DefaultAuditRecordAopManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@EnableAspectJAutoProxy
@Import(AuditRecordAdvisor.class)
public class AuditConfiguration {

    @Bean(DefaultAuditRecordAopManager.BEAN_NAME)
    public AuditRecordAopManager defaultAuditRecordAopManager() {
        return new DefaultAuditRecordAopManager();
    }
}
```

### 使用贡献器式审计管理器

贡献器式管理器适用于需要根据调用上下文拆分审计属性的场景。可以按照需要组合请求、结果、异常或自定义属性贡献器，
并将贡献器式管理器注册为指定名称的 Bean。
以下方法可放入应用的 Spring 配置类中，
通用组件从 `com.dwarfeng.subgrade.aop.sdk.interceptor.audit` 及其 `propctr` 子包导入：

```java
@Bean(ContributorAuditRecordAopManager.BEAN_NAME)
public AuditRecordAopManager contributorAuditRecordAopManager() {
    return new ContributorAuditRecordAopManager(List.of(
            new ResultPropertyContributor(),
            new ThrowablePropertyContributor()
    ));
}
```

两个贡献器分别处理成功和失败调用，因此不会因共同使用 `success` 属性键而发生冲突。
也可以把应用已注册的贡献器列表传给管理器；管理器按照传入列表的顺序执行。

### 使用 HTTP 请求属性贡献器

当被审计方法的入口参数中包含 `jakarta.servlet.http.HttpServletRequest` 时，
可以使用 HTTP 请求属性贡献器记录请求方法、请求 URI 和远程地址。
下面的 Bean 方法是上一节贡献器配置的替代方案，不要同时注册两个同名 Bean：

```java
@Bean(ContributorAuditRecordAopManager.BEAN_NAME)
public AuditRecordAopManager contributorAuditRecordAopManager() {
    return new ContributorAuditRecordAopManager(List.of(
            new HttpServletRequestPropertyContributor(),
            new ResultPropertyContributor(),
            new ThrowablePropertyContributor()
    ));
}
```

其中 `HttpServletRequestPropertyContributor` 从
`com.dwarfeng.subgrade.web.sdk.interceptor.audit.propctr.HttpServletRequestPropertyContributor` 导入。
例如，在业务对象的方法上使用该管理器，下面的方法返回当前请求 URI：

```java
@AuditRecord(
        categoryId = "request.inspect",
        manager = ContributorAuditRecordAopManager.BEAN_NAME
)
public String inspectRequest(jakarta.servlet.http.HttpServletRequest request) {
    return request.getRequestURI();
}
```

### 自定义审计属性管理器

对于需要记录业务字段、操作者信息或资源变化前后值的场景，可以实现 `AuditRecordAopManager`，
根据 `AuditRecordContext` 生成自定义属性，并将实现注册为指定 Bean 名称：

```java
@Component("userAuditRecordAopManager")
public class UserAuditRecordAopManager implements AuditRecordAopManager {

    @Override
    public Map<String, Object> getProperties(AuditRecordContext context) {
        Map<String, Object> properties = new LinkedHashMap<>();
        properties.put("method", context.getMethod().getName());
        properties.put("success", context.isSuccess());
        return properties;
    }
}
```

然后在注解中使用该 Bean 名称。例如，下面的业务方法返回必需的用户 ID：

```java
@AuditRecord(categoryId = "user.identify", manager = "userAuditRecordAopManager")
public String identifyUser(String userId) {
    return Objects.requireNonNull(userId, "userId");
}
```

### 自定义审计记录处理器

可以实现 `AuditRecordHandler`，将审计类别 ID 和属性映射提交到具体的审计系统。
以下是输出到日志的简单示例，实际生产存储由应用决定；记录前应确保属性已经过必要的脱敏：

```java
import com.dwarfeng.subgrade.aop.stack.handler.AuditRecordHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class LoggingAuditRecordHandler implements AuditRecordHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAuditRecordHandler.class);

    @Override
    public void record(String categoryId, Map<String, Object> properties) {
        LOGGER.info("Audit categoryId = {}, properties = {}", categoryId, properties);
    }
}
```

## 审计属性输出示例

所有的审计属性输出示例均用于说明属性结构，实际属性内容取决于管理器、贡献器和目标方法参数。

### 正常执行记录

使用默认管理器时，正常执行可以得到类似以下结构的审计记录：

```text
categoryId: user.create
properties:
  arguments: "[alice]"
  success: true
  result: "User[username=alice]"
```

### 异常执行记录

使用默认管理器时，异常执行可以得到类似以下结构的审计记录：

```text
categoryId: user.query
properties:
  arguments: "[missing-user]"
  success: false
  exceptionType: "com.example.UserNotExistException"
  exceptionMessage: "用户不存在"
```

### HTTP 请求记录

将 HTTP 请求属性贡献器与结果、异常贡献器组合时，可以得到类似以下内容：

```text
categoryId: request.inspect
properties:
  requestMethod: "GET"
  requestUri: "/users/alice"
  remoteAddress: "192.0.2.10"
  success: true
  result: "/users/alice"
```

## 配置说明

### 审计记录 Advisor

`AuditRecordAdvisor` 是 Spring 组件和 AspectJ 切面。
应用启用 Spring AOP 后，还需通过组件扫描或 `@Import(AuditRecordAdvisor.class)` 将其注册到容器。
避免仅因为声明依赖就假定已经启用审计。业务方法调用必须经过 Spring 代理，同对象内部直接调用不会触发该代理增强。

### 审计记录增强管理器

`manager` 属性使用 Spring Bean 名称查找 `AuditRecordAopManager`。项目提供以下 Bean 名称常量：

| 管理器                             | Bean 名称常量                                | Bean 名称                          |
|:-----------------------------------|:---------------------------------------------|:-----------------------------------|
| `DefaultAuditRecordAopManager`     | `DefaultAuditRecordAopManager.BEAN_NAME`     | `defaultAuditRecordAopManager`     |
| `ContributorAuditRecordAopManager` | `ContributorAuditRecordAopManager.BEAN_NAME` | `contributorAuditRecordAopManager` |

上述管理器类本身不负责具体的 Spring Bean 注册，使用时需要确保对应实例已经注册到 Spring 容器中。
贡献器也不会自动注册；可以像示例一样显式实例化并传入列表。

### 审计记录处理器

应用需要提供可注入的 `AuditRecordHandler` Bean。
Advisor 会将生成的审计类别 ID 和属性映射交给该处理器，处理器负责决定审计信息的持久化、发送或其他后续处理方式。
缺少必要 Bean 导致的容器启动错误不属于运行时审计异常隔离范围。

### AOP 优先级

审计记录 Advisor 的优先级设置为 `Ordered.HIGHEST_PRECEDENCE + 1200`，在本项目登录和权限检查完成后再进入审计增强，
用于记录真正进入业务方法的调用。

该顺序不保证数据库事务已经提交。`isSuccess()` 表示被代理调用正常返回，不代表外层事务最终提交成功。
如需以事务提交为准，应用应在自身审计处理器中明确协调事务边界。

## 最佳实践

### 使用稳定的审计类别 ID

审计类别 ID 应当能够稳定表示业务操作，建议使用清晰、唯一且不会随实现类重命名而变化的名称。

### 合理选择属性管理器

对于只需要记录参数、结果和异常的场景，使用默认审计管理器即可；对于需要拆分请求信息、业务字段和执行状态的场景，
使用贡献器式管理器或自定义管理器。

### 控制敏感信息和数据规模

审计属性可能包含入口参数、返回值和异常消息。
对于密码、密钥、Token、Session、大型对象和文件内容，应当通过自定义属性生成逻辑进行过滤或脱敏。
HTTP 贡献器限制自己的读取范围，并不意味着默认管理器对入口参数和返回值的字符串化已自动脱敏。

### 保持属性键唯一

配置多个属性贡献器时，应为每个贡献器设计不重复的属性键，避免因为键冲突导致整次审计属性生成失败。

### 保持审计处理与业务逻辑隔离

审计记录处理器不应改变业务方法的核心执行语义。
对于审计系统不可用等情况，应通过日志、监控或补偿机制处理，而不应覆盖业务方法的返回值或原始异常。

## 注意事项

1. **注解作用范围**：`@AuditRecord` 只能标记方法，不能像 `@BehaviorAnalyse` 一样直接标记类。
2. **审计类别 ID**：`categoryId` 不能为空字符串或仅含 `String.trim()` 可移除的空白，否则审计记录会生成失败。
3. **管理器 Bean 名称**：`manager` 必须匹配 Spring 容器中已注册的 `AuditRecordAopManager` Bean 名称。
4. **记录触发时机**：审计记录在被代理调用正常返回或抛出异常之后生成，不保证处于事务提交之后。
5. **异常处理**：属性生成和提交过程的 `Exception` 会被记录并隔离；审计代码自身的 `Error` 不在此范围。
6. **参数复制范围**：审计上下文只对入口参数数组进行浅复制，不会自动复制或脱敏参数对象内部的数据。
7. **HTTP 信息范围**：HTTP 请求贡献器只记录请求方法、请求 URI 和远程地址，不记录请求头、请求体、Token 和 Session。
8. **属性键冲突**：贡献器式管理器拒绝空键和重复的非空值属性键，配置贡献器时需要提前规划属性命名。
9. **模块依赖**：通用审计位于 AOP 模块；HTTP 请求贡献器位于 Web 模块，使用 Jakarta Servlet API。
