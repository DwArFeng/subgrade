# Audit Record - 审计记录

审计记录功能是 Subgrade 框架中提供的一个 AOP 增强功能，它能够在方法执行完成后记录审计信息， 包括审计类别、方法入口参数、执行结果、异常信息以及
HTTP 请求信息等。 这个功能适用于操作留痕、安全审计、问题排查和业务行为追踪等场景。

审计记录功能通过注解驱动的方式工作，开发者只需要在需要记录的方法上添加 `@AuditRecord` 注解， 并指定审计类别 ID
和审计记录增强管理器，框架就会自动生成审计属性并交由审计记录处理器提交。

---

## 综述

审计记录功能由 `subgrade-sdk` 提供注解、AOP Advisor、审计上下文和属性生成管理器， 由 `subgrade-stack` 提供审计记录处理器接口。

与直接在业务方法中编写审计代码的方式相比，审计记录功能将业务方法执行和审计记录处理分离开来。
业务方法只需要声明审计类别和属性生成管理器，不需要关心审计信息的具体存储方式。

审计记录功能属于外围增强能力。 当审计属性生成或审计信息提交失败时，框架只记录警告日志，不会修改业务方法的返回值，也不会覆盖业务方法抛出的原始异常。

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

在目标方法正常返回后，Advisor 会根据返回值构建成功状态的审计上下文；在目标方法抛出异常后， Advisor
会根据异常构建失败状态的审计上下文。随后，Advisor 根据 `manager` 属性选择管理器， 生成审计属性，并调用 `AuditRecordHandler`
提交审计信息。

### AuditRecordContext

`AuditRecordContext` 表示一次被审计方法的完整调用结果，包含以下内容：

- **调用连接点**：当前方法对应的 AspectJ 调用连接点。
- **目标方法**：当前被审计的 Java 方法。
- **入口参数**：目标方法调用时传入的参数数组。
- **返回值**：目标方法正常执行时的返回对象。
- **异常信息**：目标方法异常结束时抛出的异常。
- **执行状态**：通过 `isSuccess()` 判断方法是否正常完成。

入口参数数组在上下文构建时会进行浅复制，读取上下文参数时也会返回参数数组的副本， 从而避免外部替换数组本身影响审计处理。

### AuditRecordAopManager

`AuditRecordAopManager` 是审计记录增强管理器接口，负责根据 `AuditRecordContext` 生成审计属性。

管理器只负责审计属性生成，不负责执行代理方法，也不负责将审计信息提交到具体的审计系统。 审计信息的提交由
`AuditRecordHandler` 负责。

### PropertyContributor

`PropertyContributor` 是审计属性贡献器接口，用于生成一次审计记录中的部分属性。

贡献器通过 `supports` 方法判断是否支持当前审计上下文，再通过 `getProperties` 方法生成属性映射。 多个贡献器可以由
`ContributorAuditRecordAopManager` 组合，从而复用请求、返回值和异常等通用属性处理逻辑。

### AuditRecordHandler

`AuditRecordHandler` 是 `subgrade-stack` 中定义的审计记录处理器接口，继承自通用的 `Handler` 标记接口。 该接口通过 `record`
方法接收审计类别 ID 和审计属性映射，具体实现可以将审计信息提交到数据库、日志系统、 消息队列或其他审计平台。

## 功能特性

### 自动记录功能

在目标方法上添加 `@AuditRecord` 注解后，框架会自动完成以下操作：

- 读取注解中的审计类别 ID。
- 根据管理器 Bean 名称选择审计属性生成管理器。
- 构建包含方法、参数、返回值或异常的审计上下文。
- 生成审计属性映射。
- 调用审计记录处理器提交审计信息。

### 正常执行记录

当目标方法正常返回时，审计上下文会被标记为成功状态。 默认管理器会记录入口参数、成功标记和返回值；返回值为 `null` 时，其字符串表示为
`"null"`。

### 异常执行记录

当目标方法抛出异常时，审计上下文会被标记为失败状态。 默认管理器会记录入口参数、失败标记、异常类型和异常消息，同时将原始异常继续向上抛出。

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

`ContributorAuditRecordAopManager` 按照配置顺序执行多个 `PropertyContributor`。 不支持当前上下文的贡献器会被跳过，贡献器返回的
`null` 属性映射会被忽略，属性值为 `null` 的属性不会被加入最终结果。

如果多个贡献器生成相同的属性键，管理器会抛出异常，避免不同贡献器静默覆盖审计数据。

### HTTP 请求属性

`HttpServletRequestPropertyContributor` 会从方法入口参数中查找 `HttpServletRequest`，并记录以下稳定的请求属性：

- `requestMethod`：HTTP 请求方法。
- `requestUri`：HTTP 请求 URI。
- `remoteAddress`：客户端远程地址。

为了避免泄露凭证或产生过大的审计属性，该贡献器不会记录请求头、请求体、Token 和 Session。

### 审计能力故障隔离

审计属性生成失败、找不到管理器、审计类别 ID 为空或审计记录处理器提交失败时，Advisor 会记录警告日志。
这些失败不会改变目标方法的正常返回值，也不会覆盖目标方法抛出的原始异常。

## 使用示例

### 基本使用

在业务方法上添加 `@AuditRecord` 注解，并指定审计类别 ID 和管理器 Bean 名称：

```java

@Service
public class UserService {

    @AuditRecord(
            categoryId = "user.create",
            manager = DefaultAuditRecordAopManager.BEAN_NAME
    )
    public User createUser(String username) {
        // 业务逻辑
        return new User(username);
    }
}
```

### 使用默认审计管理器

默认管理器适用于审计属性较为简单，只需要记录入口参数、成功状态、返回值或异常信息的场景。 使用前需要将
`DefaultAuditRecordAopManager` 实例注册为 Bean，并使用其 Bean 名称配置 `manager` 属性。

```java

@Bean(DefaultAuditRecordAopManager.BEAN_NAME)
public AuditRecordAopManager defaultAuditRecordAopManager() {
    return new DefaultAuditRecordAopManager();
}
```

### 使用贡献器式审计管理器

贡献器式管理器适用于需要根据调用上下文拆分审计属性的场景。 可以按照需要组合请求、结果、异常或自定义属性贡献器，并将贡献器式管理器注册为指定名称的
Bean。

```java

@Bean(ContributorAuditRecordAopManager.BEAN_NAME)
public AuditRecordAopManager contributorAuditRecordAopManager(
        List<PropertyContributor> contributors
) {
    return new ContributorAuditRecordAopManager(contributors);
}
```

### 使用 HTTP 请求属性贡献器

当被审计方法的入口参数中包含 `HttpServletRequest` 时，可以使用 HTTP 请求属性贡献器记录请求方法、 请求 URI 和远程地址：

```java

@AuditRecord(
        categoryId = "user.query",
        manager = ContributorAuditRecordAopManager.BEAN_NAME
)
public User queryUser(HttpServletRequest request, String userId) {
    // 业务逻辑
    return userService.query(userId);
}
```

### 自定义审计属性管理器

对于需要记录业务字段、操作者信息或资源变化前后值的场景，可以实现 `AuditRecordAopManager`， 根据 `AuditRecordContext`
生成自定义属性，并将实现注册为指定 Bean 名称：

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

然后在注解中使用该 Bean 名称：

```java

@AuditRecord(categoryId = "user.update", manager = "userAuditRecordAopManager")
public void updateUser(String userId) {
    // 业务逻辑
}
```

### 自定义审计记录处理器

可以实现 `AuditRecordHandler`，将审计类别 ID 和属性映射提交到具体的审计系统：

```java

@Component
public class DatabaseAuditRecordHandler implements AuditRecordHandler {

    @Override
    public void record(String categoryId, Map<String, Object> properties) {
        // 将审计信息写入审计存储
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
  result: "User{username='alice'}"
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

使用 HTTP 请求属性贡献器时，可以在审计属性中得到类似以下内容：

```text
categoryId: user.query
properties:
  requestMethod: "GET"
  requestUri: "/users/missing-user"
  remoteAddress: "192.0.2.10"
  success: false
  exceptionType: "com.example.UserNotExistException"
```

## 配置说明

### 审计记录 Advisor

`AuditRecordAdvisor` 是 Spring 组件和 AspectJ 切面，应用启用对应的 Spring AOP 能力后， 会处理目标方法上的 `@AuditRecord`
注解。

### 审计记录增强管理器

`manager` 属性使用 Spring Bean 名称查找 `AuditRecordAopManager`。项目提供以下 Bean 名称常量：

| 管理器                             | Bean 名称常量                                | Bean 名称                          |
|:-----------------------------------|:---------------------------------------------|:-----------------------------------|
| `DefaultAuditRecordAopManager`     | `DefaultAuditRecordAopManager.BEAN_NAME`     | `defaultAuditRecordAopManager`     |
| `ContributorAuditRecordAopManager` | `ContributorAuditRecordAopManager.BEAN_NAME` | `contributorAuditRecordAopManager` |

上述管理器类本身不负责具体的 Spring Bean 注册，使用时需要确保对应实例已经注册到 Spring 容器中。

### 审计记录处理器

应用需要提供 `AuditRecordHandler` Bean。Advisor 会将生成的审计类别 ID 和属性映射交给该处理器，
处理器负责决定审计信息的持久化、发送或其他后续处理方式。

### AOP 优先级

审计记录 Advisor 的优先级设置为 `Ordered.HIGHEST_PRECEDENCE + 1200`，在登录和权限检查完成后再进入审计增强，
用于记录真正进入业务方法的调用。

## 最佳实践

### 使用稳定的审计类别 ID

审计类别 ID 应当能够稳定表示业务操作，建议使用清晰、唯一且不会随实现类重命名而变化的名称。

### 合理选择属性管理器

对于只需要记录参数、结果和异常的场景，使用默认审计管理器即可；对于需要拆分请求信息、业务字段和执行状态的场景，
使用贡献器式管理器或自定义管理器。

### 控制敏感信息和数据规模

审计属性可能包含入口参数、返回值和异常消息。对于密码、密钥、Token、Session、大型对象和文件内容， 应当通过自定义属性生成逻辑进行过滤或脱敏。

### 保持属性键唯一

配置多个属性贡献器时，应为每个贡献器设计不重复的属性键，避免因为键冲突导致整次审计属性生成失败。

### 保持审计处理与业务逻辑隔离

审计记录处理器不应改变业务方法的核心执行语义。对于审计系统不可用等情况，应通过日志、监控或补偿机制处理， 而不应覆盖业务方法的返回值或原始异常。

## 注意事项

1. **注解作用范围**：`@AuditRecord` 只能标记方法，不能像 `@BehaviorAnalyse` 一样直接标记类。
2. **审计类别 ID**：`categoryId` 不能为空或只包含空白字符，否则审计记录会生成失败。
3. **管理器 Bean 名称**：`manager` 必须匹配 Spring 容器中已注册的 `AuditRecordAopManager` Bean 名称。
4. **记录触发时机**：审计记录在目标方法正常返回或抛出异常之后生成，不会在方法执行前提交最终结果。
5. **异常处理**：审计属性生成或提交失败时只记录警告日志，不会改变目标方法的返回值，也不会覆盖目标方法的原始异常。
6. **参数复制范围**：审计上下文只对入口参数数组进行浅复制，不会自动复制或脱敏参数对象内部的数据。
7. **HTTP 信息范围**：HTTP 请求贡献器只记录请求方法、请求 URI 和远程地址，不记录请求头、请求体、Token 和 Session。
8. **属性键冲突**：贡献器式管理器遇到重复属性键会抛出异常，配置贡献器时需要提前规划属性命名。
