# Use with Maven - 通过 Maven 使用本项目

## 安装本项目

请参考 [Install by Source Code](./InstallBySourceCode.md) 安装本项目。

## 使用本项目

Subgrade 2.0 按功能拆分为十个 Maven artifact。应用只需声明实际使用的模块：

| Maven artifact        | JPMS 模块名                        | 主要职责                                 |
|:----------------------|:-----------------------------------|:-----------------------------------------|
| `subgrade-base`       | `com.dwarfeng.subgrade.base`       | 国际化与模块内部基础机制                 |
| `subgrade-basic`      | `com.dwarfeng.subgrade.basic`      | Bean、Key、基础异常、生成器与日志协议    |
| `subgrade-data`       | `com.dwarfeng.subgrade.data`       | DAO、Service、实体缓存及数据访问实现     |
| `subgrade-expression` | `com.dwarfeng.subgrade.expression` | 通用表达式协议与解析实现                 |
| `subgrade-aop`        | `com.dwarfeng.subgrade.aop`        | 行为分析与通用 AOP 能力                  |
| `subgrade-web`        | `com.dwarfeng.subgrade.web`        | Web 响应、登录、权限与验证               |
| `subgrade-lifecycle`  | `com.dwarfeng.subgrade.lifecycle`  | Startable、Online 与 Worker 生命周期能力 |
| `subgrade-cache`      | `com.dwarfeng.subgrade.cache`      | 进程内本地缓存                           |
| `subgrade-lock`       | `com.dwarfeng.subgrade.lock`       | 分布式锁及 Curator 实现                  |
| `subgrade-kafka`      | `com.dwarfeng.subgrade.kafka`      | Kafka 序列化与 Fastjson2 适配            |

例如，使用基础模型和数据访问能力时，可声明：

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

当前 JDK 25 模块化正式版为 `2.0.0.a`。使用 JPMS 的应用还需在自身 `module-info.java` 中声明与所选 artifact 对应的模块名。历史
`subgrade-stack`、`subgrade-sdk`、`subgrade-impl` artifact 已删除，不提供兼容转发模块。

## 参阅

- [Install by Source Code](./InstallBySourceCode.md) - 通过源码安装本项目。
