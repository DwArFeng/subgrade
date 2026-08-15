# Subgrade

此项目是作者的个人工具库，包含多种能大幅提高发开速度的通用工具库。

---

## 特性

- 包含项目开发时所需的几乎所有接口，您可以直接实现这些接口，节省您定义接口的时间。
- 针对实体对象进行大量的工具开发，如实体映射工具、实体的数据访问层、实体的缓存等工具。
- 定义了统一的异常处理机制，使得 Service 服务或其它方法能够输出统一的异常，方便下序进行进一步处理。
- 实现了常用的 AOP，如性能分析、登录判断、权限判断、友好性增强。
- 实现了多种中间件的序列化器，如 Redis 的序列化器、Kafka 的序列化器，可供这些框架直接使用。
- 实现了 Web 后端的常用工具，如分页、对象封装等常用方法。
- 方便地与 `SpringFramework` 集成。

## 在实践中被证实的优秀体验

该工具被应用在几十个微服务应用、数十个 webapi 项目、数个 CS 架构项目中。均在不同程序上提高了程序的开发效率与调试效率。

- 轻松地完成带有二级缓存的实体的维护接口，实现实体的增删改查功能。
- 性能分析注解对程序调试、调优的极为便利的支持。
- 异常映射机制，快捷地将异常类映射为代码，向 web 前端传值时更为方便。
- 轻松地完成接口的权限控制，登录控制，友好性增强。

## 文档

该项目的文档位于 [docs](./docs) 目录下，包括：

### wiki

wiki 为项目的开发人员为本项目编写的详细文档，包含不同语言的版本，主要入口为：

1. [简介](docs/wiki/zh-CN/Introduction.md) - 镜像的 `README.md`，与本文件内容基本相同。
2. [目录](docs/wiki/zh-CN/Contents.md) - 文档目录。

## 包含的工具

- 通用项目接口定义。
   - 服务接口 `Service`
   - 数据访问层接口 `Dao`
   - 缓存接口 `Cache`
   - 处理接口 `Handler`
   - Bean 接口 `Bean`
   - 实体接口 `Entity`
   - 主键接口 `Key`
   - 数据传输对象接口 `Dto`  
     上述接口的定义使得一般的新项目完全无需再定义任何新接口。

- 异常处理工具。
   - 通用服务异常 `ServiceException`
   - 通用异常处理机制 `ServiceExceptionMapper`
     通用异常处理机制能将方法执行过程中的任何异常映射到 `ServiceException` 中， `ServiceException` 异常作为异常中间件，
     可方便地转换为 `ResponseData` 或方便的通过 RPC 进行传输。

- 通用数据访问层接口定义以及实现。
- 通用缓存接口定义以及实现。
- Redis, Kafka 的序列化器以及反序列化器。
- 通用服务的接口定义以及实现。
- AOP 增强。

## 安装说明

构建环境要求：

- JDK 25 或更高版本，不启用预览特性。
- Maven 3.9.16 或更高版本。

1. 下载源码。

   使用 git 进行源码下载。

   ```shell
   git clone git@github.com:DwArFeng/supgrade.git
   ```

   对于中国用户，可以使用 gitee 进行高速下载。

   ```shell
   git clone git@gitee.com:dwarfeng/supgrade.git
   ```

2. 项目安装。

   进入项目根目录，执行 maven 命令
   ```
   mvn clean source:jar install
   ```

3. 项目部署。

   该项目使用了 `3.1.4` 版本的 `maven-deploy-plugin`，如果您有属于自己的 maven 依赖仓库，
   可以在妥善配置 maven 的 `setting.xml` 之后，进入项目根目录，运行 maven 部署指令。
   ```
   mvn clean source:jar deploy
   ```

4. enjoy it.

## 项目使用

### maven

2.0.0 起，项目按功能拆分为十个 Maven artifact，每个 artifact 对应一个 JPMS 模块：

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

应用应按实际使用的功能显式声明对应 artifact。例如：

```xml
<dependency>
    <groupId>com.dwarfeng</groupId>
    <artifactId>subgrade-basic</artifactId>
    <version>${subgrade.version}</version>
</dependency>
```

## 推荐使用版本

- JDK 25 模块化版本当前为 `2.0.0.a`，其 Maven artifact、JPMS 模块和 Java 包均不兼容 1.x。
- 继续维护 1.x 项目时，应使用不低于 `1.5.7.a` 的 1.x 版本。
- 1.x 项目使用 `WriteService` 或 `BatchWriteService` 时，请勿使用 `1.5.4.a` `1.5.5.a` 版本。
- 对于任何项目，请勿使用 `1.4.8.a` 的版本，此版本核心类存在严重 bug。
- 低于 `1.2.13.a` 的版本，内存数据访问层分页查询逻辑存在问题，如使用内存数据访问层，则需要将版本升级至 `1.2.13.a` 以上。
- 低于 `1.2.7.a` 的版本，部分关键依赖有严重等级的 bug，使用可能会造成安全问题。
- 低于 `1.2.3.b` 的版本包含以下已经发现的 bug，如果项目使用到了下述模块，则需要将 subgrade 版本升级至 `1.2.3.b`。
   - `RedisBatchBaseDao` 执行 `batchDelete` 存在执行不成功或行为异常的 bug。
- 低于 `1.1.8.a` 的版本包含以下已经发现的 bug，如果项目使用到了下述模块，则需要将 subgrade 版本升级至 `1.1.8.a`。
   - `com.dwarfeng.subgrade.stack.service.CrudService.get` 部分实现当实体不存在时抛出意料之外的异常的 bug。
- 对于任何使用 subgrade 旧版本的项目，请酌情将 subgrade 版本升级至不低于 `1.2.3.b`。
- 所有项目勿使用 `1.1.0.a` 的版本，此版本存在严重的兼容性问题。
