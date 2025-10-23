# Behavior Analyse - 行为分析

## 综述

行为分析功能是 Subgrade 框架中提供的一个强大的 AOP 增强功能，它能够自动记录方法的执行过程，
包括参数、执行时间、返回结果和异常信息。这个功能对于程序调试、性能分析和问题排查具有极大的帮助。

行为分析功能通过注解驱动的方式工作，开发者只需要在需要分析的方法或类上添加 `@BehaviorAnalyse` 注解，
框架就会自动记录该方法的执行详情。

## 核心组件

### BehaviorAnalyse 注解

`@BehaviorAnalyse` 注解是行为分析功能的核心，用于标记需要进行分析的方法或类。

```java
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface BehaviorAnalyse {

    /**
     * 日志等级，默认为 LogLevel.DEBUG。
     */
    LogLevel logLevel() default LogLevel.DEBUG;

    /**
     * 日志记录器的类。
     * 默认为 Void.class，表示使用注解所在类的日志记录器。
     */
    Class<?> loggerClass() default Void.class;
}
```

### SkipRecord 注解

`@SkipRecord` 注解用于跳过某些参数的记录，特别适用于参数或返回结果过大的场景。

```java
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.PARAMETER})
@Documented
public @interface SkipRecord {
}
```

## 功能特性

### 自动记录功能

行为分析功能会自动记录以下信息：

- **方法执行开始**：记录方法名和完整类名
- **参数信息**：记录所有方法参数的值
- **执行时间**：精确计算并记录方法执行时间
- **返回结果**：记录方法的返回对象
- **异常信息**：如果方法抛出异常，记录异常详情
- **执行结束**：记录方法执行完成状态

### 智能对象转换

框架提供了智能的对象转换功能，能够正确处理各种数据类型：

- **基本类型数组**：int[]、long[]、short[]、byte[]、char[]、double[]、float[]、boolean[]
- **对象数组**：Object[]
- **普通对象**：使用 toString() 方法转换
- **null 值**：显示为 "null"

### 日志级别控制

支持四种日志级别：

- `DEBUG`：调试级别（默认）
- `INFO`：信息级别
- `WARN`：警告级别
- `ERROR`：错误级别

### 详细日志模式

通过系统属性 `subgrade.detailedBehaviorAnalyseLog` 可以开启详细日志模式，记录更详细的调试信息：

```bash
-Dsubgrade.detailedBehaviorAnalyseLog=true
```

## 使用示例

### 基本使用

```java
@Service
public class UserService {

    @BehaviorAnalyse
    public User getUserById(Long id) {
        // 业务逻辑
        return userRepository.findById(id);
    }
}
```

### 指定日志级别

```java
@Service
public class UserService {

    @BehaviorAnalyse(logLevel = LogLevel.INFO)
    public User getUserById(Long id) {
        // 业务逻辑
        return userRepository.findById(id);
    }
}
```

### 指定日志记录器

```java
@Service
public class UserService {

    @BehaviorAnalyse(loggerClass = OtherService.class)
    public User getUserById(Long id) {
        // 业务逻辑
        return userRepository.findById(id);
    }
}
```

### 跳过记录

```java
@Service
public class UserService {

    @BehaviorAnalyse
    public User createUser(String username, @SkipRecord String password, @SkipRecord byte[] avatar) {
        // 业务逻辑
        return new User(username, password, avatar);
    }

    @BehaviorAnalyse
    @SkipRecord
    public byte[] getUserAvatar(Long userId) {
        // 返回大对象，跳过记录
        return userRepository.findAvatarById(userId);
    }
}
```

### 类级别注解

```java
@BehaviorAnalyse(logLevel = LogLevel.INFO)
@Service
public class UserService {

    public User getUserById(Long id) {
        // 所有方法都会进行行为分析
        return userRepository.findById(id);
    }

    public User createUser(String username, String password) {
        // 所有方法都会进行行为分析
        return new User(username, password);
    }
}
```

## 日志输出示例

所有的日志输出均为正式环境下的真实日志格式，部分日志的内容较长，在示例中将会使用 `etc...` 进行省略。

### 正常执行日志

```
[2025etc...] [DEBUG] [etc...] [etc...]: 行为分析开始...
[2025etc...] [DEBUG] [etc...] [etc...]: 方法执行开始: com.dwarfeng.etc......
[2025etc...] [DEBUG] [etc...] [etc...]:   参数 0/1: LongIdKey{longId=etc...}
[2025etc...] [DEBUG] [etc...] [etc...]: 方法执行结束: com.dwarfeng.etc...
[2025etc...] [DEBUG] [etc...] [etc...]:   返回对象: true
[2025etc...] [DEBUG] [etc...] [etc...]:   用时: 2 毫秒
[2025etc...] [DEBUG] [etc...] [etc...]: 行为分析结束, 方法: com.dwarfeng.etc..., 用时: 2 毫秒, 正常返回
```

### 异常执行日志

```
[2025etc...] [DEBUG] [etc...] [etc...]: 方法执行开始: com.dwarfeng.etc......
[2025etc...] [DEBUG] [etc...] [etc...]:   参数 0/1: etc...
[2025etc...] [DEBUG] [etc...] [etc...]: 方法执行结束: com.dwarfeng.etc...
[2025etc...] [DEBUG] [etc...] [etc...]:   抛出异常: 
com.dwarfeng.etc...: 账户 StringIdKey{stringId='etc...'} 不存在
	at com.dwarfeng.etc...
	at com.dwarfeng.etc...
[2025etc...] [DEBUG] [etc...] [etc...]:   用时: 162 毫秒
[2025etc...] [DEBUG] [etc...] [etc...]: 行为分析结束, 方法: com.dwarfeng.etc..., 用时: 162 毫秒, 抛出异常
```

### 详细日志模式

当开启详细日志模式时，会输出更详细的调试信息：

```
[2025etc...] [DEBUG] [etc...] [etc...]: 行为分析开始...
[2025etc...] [DEBUG] [etc...] [etc...]: 方法执行开始: com.dwarfeng.etc......
[2025etc...] [DEBUG] [etc...] [etc...]:   参数 0/2: etc...
[2025etc...] [DEBUG] [etc...] [etc...]:   参数 1/2: etc...
[2025etc...] [DEBUG] [etc...] [etc...]: 开始计时: com.dwarfeng.etc......
[2025etc...] [DEBUG] [etc...] [etc...]: 获取当前系统时间戳: 176etc...
[2025etc...] [DEBUG] [etc...] [etc...]: 调用原始方法: com.dwarfeng.etc......
[2025etc...] [DEBUG] [etc...] [etc...]: 结束计时: com.dwarfeng.etc......
[2025etc...] [DEBUG] [etc...] [etc...]: 获取当前系统时间戳: 176etc...
[2025etc...] [DEBUG] [etc...] [etc...]: 计算方法执行时间: 176etc... - 176etc... = 7 毫秒
[2025etc...] [DEBUG] [etc...] [etc...]: 方法执行结束: com.dwarfeng.etc...
[2025etc...] [DEBUG] [etc...] [etc...]:   返回对象: null
[2025etc...] [DEBUG] [etc...] [etc...]:   用时: 7 毫秒
[2025etc...] [DEBUG] [etc...] [etc...]: 行为分析结束, 方法: com.dwarfeng.etc..., 用时: 7 毫秒, 正常返回
```

## 配置说明

### 系统属性配置

| 属性名                                   | 默认值     | 说明            |
|:--------------------------------------|:--------|:--------------|
| `subgrade.detailedBehaviorAnalyseLog` | `false` | 是否开启详细的行为分析日志 |

### AOP 优先级

行为分析 AOP 的优先级设置为 `PriorityOrdered.HIGHEST_PRECEDENCE + 10`，
确保在几乎所有其他 AOP 之前执行，以便准确记录方法的执行时间。

## 最佳实践

### 合理使用日志级别

- 开发环境：使用 `INFO` 级别的日志输出关键方法的行为分析记录，以便调试。
- 生产环境：推荐将任何行为分析日志级别设置为 `DEBUG`，以避免产生的内容充斥更高级别的日志文件。

### 使用 SkipRecord 注解

对于包含大对象的参数或返回结果，使用 `@SkipRecord` 注解避免日志文件过大：

```java
@BehaviorAnalyse
public byte[] downloadFile(String fileName) {
    // 返回大文件内容，跳过记录
    return fileService.download(fileName);
}
```

### 类级别注解

对于需要分析所有方法的服务类，可以使用类级别注解：

```java
@BehaviorAnalyse(logLevel = LogLevel.INFO)
@Service
public class UserService {
    // 所有方法都会进行行为分析
}
```

## 注意事项

1. **日志文件大小**：详细的行为分析日志可能会产生大量的日志输出，建议在生产环境中仅使用 `DEBUG` 级别。
2. **敏感信息**：行为分析会记录所有参数和返回结果，请注意不要记录敏感信息如密码、密钥等。
3. **异常处理**：行为分析不会影响原始方法的异常处理逻辑，异常会被正确抛出。
4. **AOP 优先级**：行为分析 AOP 具有最高优先级，确保能够准确记录方法执行时间。
