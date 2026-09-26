# Audit Record - 审计记录

## 综述

审计记录功能是 Subgrade 框架中提供的一个 AOP 增强功能，它能够在方法执行完成后记录审计信息，
包括审计类别、方法参数、返回值、异常信息以及 HTTP 请求信息等。
这个功能适用于操作留痕、安全审计、问题排查和业务行为追踪等场景。

审计记录功能通过注解驱动的方式工作，声明分为两级：方法上的 `@AuditRecord` 注解声明审计类别 ID，
方法参数与方法上的贡献注解用于指定生成审计属性的贡献器（`Contributor`）。
被审计方法执行完成后，框架使用审计记录增强管理器（`AuditRecordAopManager`）生成审计属性，
并交由审计记录处理器（`AuditRecordHandler`）提交审计信息。

审计记录功能由 `subgrade-sdk` 提供注解、AOP Advisor、审计上下文、审计记录增强管理器与贡献器，
由 `subgrade-stack` 提供审计记录处理器接口。
与直接在业务方法中编写审计代码的方式相比，审计记录功能将业务方法执行与审计记录处理分离开来，
业务方法只需要声明审计类别与贡献注解，不需要关心审计信息的具体存储方式。

审计记录属于安全敏感能力：
审计属性生成或审计信息提交失败时，相关异常会直接向调用方传播，如果业务方法同时抛出异常，审计异常具有更高的优先级。

## 核心组件

### AuditRecord 注解

`@AuditRecord` 注解是审计记录功能的入口，用于标记需要在执行后记录审计信息的方法。

```java
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AuditRecord {

    /**
     * 定义审计记录时需要的审计类别 ID。
     *
     * @return 审计记录时需要的审计类别 ID。
     */
    String value();
}
```

其中，`value` 用于标识当前审计记录所属的业务类别，是必填属性。
该注解只能标记方法，且一个方法只能声明一个审计类别；类别 ID 不能为空或只包含空白字符。

### 贡献注解

贡献注解用于把审计属性的生成工作拆分为多个可复用的贡献器。
注解中的 `contributor` 指定贡献器的 Bean 名称，`prefix` 指定该贡献器生成属性的键前缀，默认为空字符串。

| 注解                         | 目标     | 可重复 | 源对象            |
|:-----------------------------|:---------|:-------|:------------------|
| `@AuditParamContribute`      | 方法参数 | 是     | 该参数的原始值    |
| `@AuditResultContribute`     | 方法     | 是     | 方法的返回值      |
| `@AuditThrowableContribute`  | 方法     | 是     | 方法抛出的异常    |
| `@AuditInvocationContribute` | 方法     | 是     | 无，固定为 `null` |

以参数贡献注解为例，其定义如下：

```java
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Repeatable(AuditParamContributeList.class)
public @interface AuditParamContribute {

    /**
     * Contributor Bean 名称。
     *
     * @return Contributor Bean 名称。
     */
    String contributor();

    /**
     * 属性键前缀。
     *
     * @return 属性键前缀。
     */
    String prefix() default "";
}
```

四个贡献注解均提供容器注解，分别为 `AuditParamContributeList`、`AuditResultContributeList`、
`AuditThrowableContributeList` 与 `AuditInvocationContributeList`。
容器注解由编译器在重复标注时自动使用，通常不应直接标注。

### AuditRecordAdvisor

`AuditRecordAdvisor` 是审计记录功能的 AOP 切面，负责处理方法上的 `@AuditRecord` 注解。

切面的切入点为 `@annotation(com.dwarfeng.subgrade.sdk.interceptor.audit.AuditRecord)`，
并被声明为 Spring 组件与 AspectJ 切面。

切面的执行流程为：
1. 解析注解中的审计类别 ID，类别 ID 为空或只包含空白字符时抛出 `IllegalStateException`；
2. 执行代理方法并暂存返回值或异常；
3. 使用暂存的结果构建审计记录上下文；
4. 调用审计记录增强管理器生成审计属性；
5. 调用审计记录处理器提交审计信息；
6. 最后在暂存的异常非空时将其重新抛出。

切面通过字段注入获取审计记录处理器与审计记录增强管理器，
并在启用 SPEL 解析时使用可配置 Bean 工厂、表达式解析器与解析上下文。

### AuditRecordContext

`AuditRecordContext` 表示一次被审计方法的完整调用结果，包含以下内容：

- **调用连接点**：当前方法对应的 AspectJ 调用连接点，方法入参通过连接点获取。
- **返回值**：目标方法正常执行时的返回对象。
- **异常信息**：目标方法异常结束时抛出的异常。
- **执行状态**：通过 `isSuccess()` 判断方法是否正常完成，等价于异常信息为 `null`。

### AuditRecordAopManager

`AuditRecordAopManager` 是审计记录增强管理器接口，负责根据 `AuditRecordContext` 生成审计属性。

```java
public interface AuditRecordAopManager {

    /**
     * 从指定的审计记录上下文中解析审计属性。
     *
     * @param context 指定的审计记录上下文。
     * @return 从指定的审计记录上下文中解析的审计属性组成的映射。
     * @throws Exception 属性解析过程中发生异常。
     */
    Map<String, Object> resolveProperties(AuditRecordContext context) throws Exception;
}
```

管理器只负责审计属性生成，不负责执行代理方法，也不负责将审计信息提交到具体的审计系统。
审计信息的提交由 `AuditRecordHandler` 负责。
切面按类型注入唯一的实现，审计属性的生成方式不由注解选择。

### Contributor

`Contributor` 是审计属性贡献器接口，用于生成一次审计记录中的部分属性。

```java
public interface Contributor {

    /**
     * 解析审计属性。
     *
     * @param context 审计记录上下文。
     * @param source  源对象。
     * @return 审计属性组成的映射。
     * @throws Exception 属性解析过程中发生的任何异常。
     */
    @Nonnull
    Map<String, Object> resolveProperties(@Nonnull AuditRecordContext context, @Nullable Object source)
            throws Exception;
}
```

源对象由注解的挂载位置决定，可以是入口参数、返回值或异常；调用贡献注解传入的源对象固定为 `null`。
贡献器不判断自身是否适用于当前上下文，是否执行完全由注解的挂载位置决定。

### AuditRecordHandler

`AuditRecordHandler` 是 `subgrade-stack` 中定义的审计记录处理器接口，继承自通用的 `Handler` 标记接口。
该接口通过 `record` 方法接收审计类别 ID 和审计属性映射，
具体实现可以将审计信息提交到数据库、日志系统、消息队列或其他审计平台。

```java
public interface AuditRecordHandler extends Handler {

    /**
     * 记录审计信息。
     *
     * @param categoryId 审计类别 ID。
     * @param properties 审计属性组成的映射。
     * @throws HandlerException 处理器异常。
     */
    void record(String categoryId, Map<String, Object> properties) throws HandlerException;
}
```

## 功能特性

### 自动记录功能

在目标方法上添加 `@AuditRecord` 注解后，框架会自动完成以下操作：

- 读取注解中的审计类别 ID，并校验其非空。
- 执行目标方法，并暂存返回值或异常。
- 构建包含调用连接点、返回值或异常的审计记录上下文。
- 通过审计记录增强管理器执行贡献注解指定的贡献器，生成审计属性映射。
- 调用审计记录处理器提交审计信息。

### 正常执行记录

当目标方法正常返回时，审计上下文会被标记为成功状态。
此时只有 `@AuditResultContribute` 注解指定的贡献器会被执行，`@AuditThrowableContribute` 注解指定的贡献器不会执行；
`@AuditInvocationContribute` 注解指定的贡献器在两种执行结果下都会执行。

### 异常执行记录

当目标方法抛出异常时，审计上下文会被标记为失败状态。
此时只有 `@AuditThrowableContribute` 注解指定的贡献器会被执行。
审计信息提交完成后，原始异常会被继续向上抛出。

### 默认审计属性

`DefaultAuditRecordAopManager` 是不依赖注解的审计记录增强管理器实现，使用固定的属性键生成审计属性：

| 属性键             | 正常执行       | 异常执行       | 说明                                |
|:-------------------|:---------------|:---------------|:------------------------------------|
| `arguments`        | 入口参数字符串 | 入口参数字符串 | 使用 `Arrays.deepToString` 表示参数 |
| `success`          | `true`         | `false`        | 表示方法是否正常完成                |
| `result`           | 返回值字符串   | 不生成         | 仅在正常执行时生成                  |
| `exceptionType`    | 不生成         | 异常的完整类名 | 仅在异常执行时生成                  |
| `exceptionMessage` | 不生成         | 异常消息字符串 | 仅在异常执行时生成                  |

对应的属性键常量定义在 `DefaultAuditRecordAopManager` 中，依次为 `PROPERTY_ARGUMENTS`、
`PROPERTY_SUCCESS`、`PROPERTY_RESULT`、`PROPERTY_EXCEPTION_TYPE` 与 `PROPERTY_EXCEPTION_MESSAGE`。
除 `success` 使用 `Boolean` 以外，其余属性值均为字符串，其中 `result` 通过 `String.valueOf` 转换，
返回值为 `null` 时其字符串表示为 `"null"`。

该管理器不解析贡献注解，也不参与属性键前缀拼接。
由于切面按类型注入唯一的审计记录增强管理器，应用只能在默认管理器与注解式管理器之间选择其一。

### 注解贡献机制

`AnnotationAuditRecordAopManager` 是注解式的审计记录增强管理器实现，
它解析方法与其参数上的贡献注解，按以下顺序执行对应的贡献器：

1. 全部方法参数，按照参数下标顺序；同一参数上的多个注解按照声明顺序。
2. 方法正常返回时的返回值贡献，或方法异常结束时的异常贡献。
3. 方法上的调用贡献，两种执行结果下都会执行。

贡献器通过 Bean 名称在管理器持有的 `Map<String, Contributor>` 中查找，
管理器为每个贡献器生成的属性统一添加注解中声明的 `prefix`，再合并到最终的有序属性映射中。

贡献过程中的失败行为如下：

- 贡献器 Bean 名称为空：抛出 `IllegalArgumentException`，消息为 `Contributor Bean 名称不能为空`。
- 找不到指定的贡献器 Bean：抛出 `IllegalStateException`，消息为 `未找到指定的 Contributor Bean: ` 加 Bean 名称。
- 贡献器返回 `null` 属性映射、`null` 属性键或 `null` 属性值：抛出 `IllegalStateException`，
  消息为 `Contributor 生成了 null 属性映射: `、`Contributor 生成了 null 属性键: ` 或
  `Contributor 生成了 null 属性值: ` 加贡献器名称。
- 合并后出现重复属性键：抛出 `IllegalStateException`，消息为 `审计记录属性键重复: ` 加属性键。
- 审计上下文中的参数数量与方法参数数量不一致：抛出 `IllegalStateException`，
  消息为 `审计记录上下文中的参数数量与方法参数数量不一致`。

### HTTP 请求属性

`GeneralHttpServletRequestContributor` 用于从入口参数中的 `HttpServletRequest` 生成审计属性。
该贡献器采用规则式配置，默认不配置任何规则，可通过规则读取标准请求信息、指定的请求头、
请求参数、请求属性以及其它派生信息。
所有非空解析结果均转换为字符串，`null` 结果不会生成对应的审计属性。

该贡献器不负责敏感信息识别、脱敏、截断或可信代理解析。
应用应根据自身安全要求配置规则，或使用专用的贡献器处理敏感信息。

### 通用贡献器

`com.dwarfeng.subgrade.sdk.interceptor.audit.contributor` 包提供了以下通用贡献器，
用于覆盖常见的属性来源：

| 贡献器                                      | 适用挂载点                       | 规则工厂                                                                                                                                                                                                         |
|:--------------------------------------------|:---------------------------------|:-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `GeneralFixedContributor`                   | 四类贡献注解                     | `Rule.of(key, supplier)`                                                                                                                                                                                         |
| `GeneralSourceContributor<T>`               | 入口参数、返回值、异常           | `Rule.of(key, resolver)`、`Rule.value(key)`                                                                                                                                                                      |
| `GeneralHttpServletRequestContributor`      | 入口参数（`HttpServletRequest`） | `Rule.of`、`Rule.header`、`Rule.parameter`、`Rule.attribute`                                                                                                                                                     |
| `GeneralMethodContributor`                  | 调用贡献（源对象必须为 `null`）  | `Rule.of`、`Rule.className`、`Rule.methodName`、`Rule.success`、`Rule.argument(key, index)`、`Rule.argument(key, parameterName)`、`Rule.arguments`、`Rule.result`、`Rule.exceptionType`、`Rule.exceptionMessage` |
| `GeneralFastJsonResponseDataContributor<T>` | 返回值（`FastJsonResponseData`） | `Rule.of(key, resolver)`；固定生成 `meta.code`（转换为 `long`）与 `meta.message`，业务属性统一添加 `data.` 前缀                                                                                                  |
| `TokenHandlerHttpServletRequestContributor` | 入口参数（`HttpServletRequest`） | 无规则；固定生成 `user_id` 与 `login_id`                                                                                                                                                                         |

通用贡献器的共性行为如下：

- 贡献器只生成规则所声明的属性，未配置的来源内容不会写入审计属性映射。
- 规则返回 `null` 时跳过该属性，非空结果保持解析器返回的原始类型；
  `GeneralHttpServletRequestContributor` 会将非空结果统一转换为字符串。
- `GeneralSourceContributor` 的源对象为 `null` 时返回空映射，且不调用规则。
- 同一贡献器内出现重复属性键时抛出 `IllegalStateException`。
- `GeneralFixedContributor` 的全部挂载点得到同一组固定值，因此可以挂在任意一类贡献注解上。
- 贡献器不负责类型转换、敏感信息识别、脱敏或截断。

### 审计失败的处理

审计记录属于安全敏感能力。审计属性生成或审计信息提交失败时，相关异常会直接向调用方传播，
审计记录处理器也可以通过抛出异常阻止业务结果被返回。
如果业务方法同时抛出异常，由于审计处理发生在原始异常重新抛出之前，审计异常具有更高的优先级，原始异常会被取代。

切面在进入审计处理时只记录 `debug` 级别的日志，不对业务方法的执行结果做任何修改。

## 使用示例

### 基本使用

在业务方法上添加 `@AuditRecord` 注解，并在需要的位置添加贡献注解：

```java
@Service
public class UserService {

    @AuditRecord("user.create")
    @AuditInvocationContribute(contributor = "methodContributor", prefix = "method.")
    @AuditResultContribute(contributor = "userResultContributor", prefix = "result.")
    public User createUser(
            @AuditParamContribute(contributor = "usernameParamContributor", prefix = "param.")
            String username
    ) {
        // 业务逻辑
        return new User(username);
    }
}
```

### 使用默认审计管理器

默认管理器适用于审计属性较为简单，只需要记录入口参数、成功状态、返回值或异常信息的场景。
使用前需要将 `DefaultAuditRecordAopManager` 实例注册为 Bean：

```java
@Bean
public AuditRecordAopManager auditRecordAopManager() {
    return new DefaultAuditRecordAopManager();
}
```

### 定义与注册贡献器

贡献器通过注解中的 `contributor` 属性按 Bean 名称引用，因此需要将贡献器注册为 Bean，并在注解中填写对应的 Bean 名称。
下面的贡献器使用通用源对象贡献器记录入口参数中的用户名称：

```java
@Component("usernameParamContributor")
public class UsernameParamContributor implements Contributor {

    private static final List<GeneralSourceContributor.Rule<String>> CONTRIBUTOR_RULES;

    static {
        List<GeneralSourceContributor.Rule<String>> rules = new ArrayList<>();
        rules.add(GeneralSourceContributor.Rule.of("username", source -> source));
        CONTRIBUTOR_RULES = Collections.unmodifiableList(rules);
    }

    private final GeneralSourceContributor<String> contributor;

    public UsernameParamContributor() {
        this.contributor = new GeneralSourceContributor<>(CONTRIBUTOR_RULES);
    }

    @Override
    public Map<String, Object> resolveProperties(AuditRecordContext context, Object source) throws Exception {
        return contributor.resolveProperties(context, source);
    }
}
```

使用注解贡献机制时，需要把容器中的全部贡献器交给注解式管理器：

```java
@Bean
public AuditRecordAopManager auditRecordAopManager(Map<String, Contributor> contributors) {
    AnnotationAuditRecordAopManager aopManager = new AnnotationAuditRecordAopManager();
    aopManager.setContributors(contributors);
    return aopManager;
}
```

### 使用通用贡献器

通用贡献器通过规则列表配置需要记录的内容，下面的示例在工厂方法中构造各通用贡献器，
并将其放入以 Bean 名称为键的映射中：

```java
public class AuditContributorFactory {

    public Map<String, Contributor> buildContributors(TokenHandler tokenHandler) {
        Map<String, Contributor> contributors = new LinkedHashMap<>();

        // 记录方法信息与执行结果。
        List<GeneralMethodContributor.Rule> methodRules = Arrays.asList(
                GeneralMethodContributor.Rule.className("class"),
                GeneralMethodContributor.Rule.methodName("method"),
                GeneralMethodContributor.Rule.success("success")
        );
        contributors.put("methodContributor", new GeneralMethodContributor(methodRules));

        // 记录 HTTP 请求信息。
        List<GeneralHttpServletRequestContributor.Rule> requestRules = Arrays.asList(
                GeneralHttpServletRequestContributor.Rule.of("remote_addr", HttpServletRequest::getRemoteAddr),
                GeneralHttpServletRequestContributor.Rule.header("user_agent", "User-Agent"),
                GeneralHttpServletRequestContributor.Rule.of("method", HttpServletRequest::getMethod),
                GeneralHttpServletRequestContributor.Rule.of("request_uri", HttpServletRequest::getRequestURI)
        );
        GeneralHttpServletRequestContributor requestContributor =
                new GeneralHttpServletRequestContributor(requestRules);
        contributors.put("requestContributor", requestContributor);

        // 记录入口参数中的字段。
        List<GeneralSourceContributor.Rule<String>> sourceRules = Collections.singletonList(
                GeneralSourceContributor.Rule.of("username", source -> source)
        );
        contributors.put("usernameParamContributor", new GeneralSourceContributor<>(sourceRules));

        // 记录 FastJson 响应数据中的业务字段。
        List<GeneralFastJsonResponseDataContributor.Rule<User>> responseRules = Collections.singletonList(
                GeneralFastJsonResponseDataContributor.Rule.of("username", User::getUsername)
        );
        GeneralFastJsonResponseDataContributor<User> responseContributor =
                new GeneralFastJsonResponseDataContributor<>(responseRules);
        contributors.put("userResultContributor", responseContributor);

        // 记录与上下文无关的固定值。
        List<GeneralFixedContributor.Rule> fixedRules = Collections.singletonList(
                GeneralFixedContributor.Rule.of("source", () -> "web")
        );
        contributors.put("fixedContributor", new GeneralFixedContributor(fixedRules));

        // 记录当前登录的用户 ID 与登录 ID。
        contributors.put(
                "tokenContributor", new TokenHandlerHttpServletRequestContributor(tokenHandler)
        );

        return contributors;
    }
}
```

### 自定义审计属性管理器

对于需要记录业务字段、操作者信息或资源变化前后值的场景，可以实现 `AuditRecordAopManager`，
根据 `AuditRecordContext` 生成自定义属性，并将实现注册为 Bean：

```java
@Component
public class UserAuditRecordAopManager implements AuditRecordAopManager {

    @Override
    public Map<String, Object> resolveProperties(AuditRecordContext context) {
        Map<String, Object> properties = new LinkedHashMap<>();
        properties.put("arguments", Arrays.deepToString(context.getPjp().getArgs()));
        properties.put("success", context.isSuccess());
        return properties;
    }
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

以下示例均用于说明属性结构，实际属性内容取决于审计记录增强管理器、贡献器与目标方法参数。

### 正常执行记录

使用默认管理器时，正常执行可以得到类似以下结构的审计记录：

```text
category: user.create
properties:
  arguments: "[alice]"
  success: true
  result: "User{username='alice'}"
```

### 异常执行记录

使用默认管理器时，异常执行可以得到类似以下结构的审计记录：

```text
category: user.query
properties:
  arguments: "[missing-user]"
  success: false
  exceptionType: "com.example.UserNotExistException"
  exceptionMessage: "用户不存在"
```

### HTTP 请求记录

使用调用贡献器记录执行结果、并使用 HTTP 请求贡献器记录请求信息时，可以得到类似以下内容：

```text
category: user.query
properties:
  remote_addr: "192.0.2.10"
  user_agent: "Mozilla/5.0"
  method: "GET"
  request_uri: "/users/missing-user"
  success: false
```

### 注解贡献示例

使用注解贡献机制时，属性键为注解前缀与贡献器属性名的拼接结果，属性顺序与贡献顺序一致：

```text
category: user.create
properties:
  param.username: "alice"
  result.name: "alice"
  method.method: "createUser"
```

## 配置说明

### 审计记录 Advisor

`AuditRecordAdvisor` 是 Spring 组件和 AspectJ 切面，应用启用对应的 Spring AOP 能力后，
会处理目标方法上的 `@AuditRecord` 注解。
切面可以使用 `@Component` 直接注册，也可以通过配置类显式注册为 Bean。
切面通过字段注入获取 `AuditRecordHandler` 与 `AuditRecordAopManager`，
并在启用 SPEL 解析时使用可选的 `ConfigurableBeanFactory`、`ExpressionParser` 与 `ParserContext`。

### 审计记录增强管理器

审计记录增强管理器由切面按 `AuditRecordAopManager` 类型注入，
因此 Spring 容器中必须存在唯一一个该类型的 Bean，不区分具体实现。

| 实现                              | 说明                                                                          |
|:----------------------------------|:------------------------------------------------------------------------------|
| `AnnotationAuditRecordAopManager` | 解析贡献注解，按注解执行贡献器并合并属性；需要注入 `Map<String, Contributor>` |
| `DefaultAuditRecordAopManager`    | 使用固定属性键记录入口参数与执行结果，不使用贡献注解                          |

上述实现类本身不负责 Spring Bean 注册。使用注解贡献机制时，需要把贡献器注册为 Bean，
并将容器注入的 `Map<String, Contributor>` 交给 `AnnotationAuditRecordAopManager`。

### 审计记录处理器

应用需要提供 `AuditRecordHandler` Bean。切面会将生成的审计类别 ID 和属性映射交给该处理器，
处理器负责决定审计信息的持久化、发送或其他后续处理方式。
处理器抛出的异常会向调用方传播。

### SPEL 解析

审计记录的类别 ID 支持通过 SPEL 表达式解析，是否启用由系统属性控制：

| 属性名                         | 默认值  | 说明                                       |
|:-------------------------------|:--------|:-------------------------------------------|
| `subgrade.auditRecord.useSpel` | `false` | 是否启用审计记录注解的 SPEL 表达式解析功能 |

启用后，类别 ID 会先经过可配置 Bean 工厂解析 `${...}` 占位符，再作为模板表达式交给表达式解析器求值。
此时切面需要以下三个 Bean：`ConfigurableBeanFactory`、`ExpressionParser` 与 `ParserContext`，
任一依赖缺失时切面都会抛出 `IllegalStateException`，并提示对应系统属性的名称。
系统属性在切面类加载时读取，运行期不可更改。

### AOP 优先级

审计记录 Advisor 的优先级设置为 `Ordered.HIGHEST_PRECEDENCE + 1200`，
在登录和权限检查完成后再进入审计增强，用于记录真正进入业务方法的调用。

## 最佳实践

### 使用稳定的审计类别 ID

审计类别 ID 应当能够稳定表示业务操作，建议使用清晰、唯一且不会随实现类重命名而变化的名称。

### 合理选择属性管理器

对于只需要记录入口参数、成功状态、返回值与异常信息的场景，使用默认审计管理器即可；
对于需要拆分请求信息、业务字段和执行状态的场景，使用注解贡献机制组合贡献器，
或实现自定义的审计记录增强管理器。

### 控制敏感信息和数据规模

审计属性可能包含入口参数、返回值和异常消息。对于密码、密钥、Token、Session、大型对象和文件内容，
应当通过自定义贡献器进行过滤或脱敏。

### 保持属性键唯一

注解前缀与贡献器属性名拼接后即为最终属性键，配置多个贡献器时应设计不重复的属性键，
避免因为键冲突导致整次审计属性生成失败。

### 使用前缀划分属性命名空间

注解中的 `prefix` 决定了贡献器生成属性的键前缀。
建议按属性来源划分前缀（例如方法信息、请求信息、令牌信息、参数信息、返回值信息），
使不同贡献器的属性互不冲突，出现键冲突时也更容易定位。

### 保持审计处理与业务逻辑隔离

审计记录处理器不应改变业务方法的核心执行语义。
由于审计异常默认向调用方传播，如果应用希望审计失败不阻断业务，应在处理器内部捕获异常并记录日志，
而不应在业务方法中感知审计处理过程。

## 注意事项

1. **注解作用范围**：`@AuditRecord`、`@AuditResultContribute`、`@AuditThrowableContribute` 与
   `@AuditInvocationContribute` 只能标记方法，`@AuditParamContribute` 只能标记参数。
2. **审计类别 ID**：`value` 不能为空或只包含空白字符，否则审计记录会以 `IllegalStateException` 失败。
3. **管理器 Bean 唯一性**：容器中必须存在唯一一个 `AuditRecordAopManager` Bean，切面按类型注入。
4. **记录触发时机**：审计记录在目标方法正常返回或抛出异常之后生成，不会在方法执行前提交最终结果。
5. **失败传播**：审计属性生成或提交失败时异常直接向调用方传播，且优先于业务方法抛出的原始异常。
6. **贡献器 Bean 名称**：`contributor` 必须匹配容器中已注册的 `Contributor` Bean 名称，
   为空或找不到时抛出异常。
7. **贡献器返回约束**：贡献器返回 `null` 属性映射、`null` 属性键或 `null` 属性值时抛出异常；
   贡献器也不做类型转换与脱敏。
8. **属性键冲突**：注解前缀与贡献器属性名拼接后出现重复键时抛出 `IllegalStateException`。
9. **SPEL 解析**：默认关闭，启用需要配置 `subgrade.auditRecord.useSpel` 与三个解析相关 Bean，
   缺失时抛出 `IllegalStateException`。
10. **贡献顺序**：参数贡献、结果或异常贡献、调用贡献依次执行，结果贡献与异常贡献互斥。
