# Subgrade

此项目是作者的个人工具库，包含多种能大幅提高发开速度的通用工具库。

---

## 特性

- 包含项目开发时所需的几乎所有接口，您可以直接实现这些接口，节省您定义接口的时间。
- 针对实体对象进行大量的工具开发，如实体映射工具、实体的数据访问层、实体的缓存等工具。
- 定义了统一的异常处理机制，使得Service服务或其它方法能够输出统一的异常，方便下序进行进一步处理。
- 实现了常用的AOP，如性能分析、登录判断、权限判断、友好性增强。
- 实现了多种中间件的序列化器，如Redis的序列化器、Kafka的序列化器，可供这些框架直接使用。
- 实现了Web后端的常用工具，如分页、对象封装等常用方法。
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

1. [简介](./docs/wiki/zh_CN/Introduction.md) - 镜像的 `README.md`，与本文件内容基本相同。
2. [目录](./docs/wiki/zh_CN/Contents.md) - 文档目录。

## 包含的工具

- 通用项目接口定义。
   - 服务接口 `Service`
   - 数据访问层接口 `Dao`
   - 缓存接口 `Cache`
   - 处理接口 `Handler`
   - Bean接口 `Bean`
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
- AOP增强。

## 安装说明

1. 下载源码。

   使用git进行源码下载。

   ```
   git clone git@github.com:DwArFeng/supgrade.git
   ```

   对于中国用户，可以使用gitee进行高速下载。

   ```
   git clone git@gitee.com:dwarfeng/supgrade.git
   ```

2. 项目安装。

   进入项目根目录，执行maven命令
   ```
   mvn clean source:jar install
   ```

3. 项目部署。

   该项目使用了 `2.8.2` 版本的 `maven-deploy-plugin`，如果您有属于自己的 maven 依赖仓库，
   可以在妥善配置 maven 的 `setting.xml` 之后，进入项目根目录，运行 maven 部署指令。
   ```
   mvn clean source:jar deploy
   ```

4. enjoy it.

## 项目使用

### 注意

该项目开发时使用的部分第三方依赖已经停止维护。因此相关的实现均不推荐使用。
开发人员在引用该项目的坐标时推荐主动排除这些第三方依赖。

相关的依赖列表：

| groupId      | artifactId   | 相关说明                                           |
|:-------------|:-------------|:-----------------------------------------------|
| net.sf.dozer | dozer        | [github](https://github.com/DozerMapper/dozer) |
| net.sf.dozer | dozer-spring | [github](https://github.com/DozerMapper/dozer) |

### maven

#### subgrade-stack

```xml
<dependency>
  <groupId>com.dwarfeng</groupId>
  <artifactId>subgrade-stack</artifactId>
  <version>${subgrade.version}</version>
</dependency>
```

#### subgrade-sdk

```xml
<dependency>
  <groupId>com.dwarfeng</groupId>
  <artifactId>subgrade-sdk</artifactId>
  <version>${subgrade.version}</version>
  <exclusions>
    <exclusion>
      <artifactId>dozer</artifactId>
      <groupId>net.sf.dozer</groupId>
    </exclusion>
    <exclusion>
      <artifactId>dozer-spring</artifactId>
      <groupId>net.sf.dozer</groupId>
    </exclusion>
  </exclusions>
</dependency>
```

#### subgrade-impl

```xml
<dependency>
  <groupId>com.dwarfeng</groupId>
  <artifactId>subgrade-impl</artifactId>
  <version>${subgrade.version}</version>
  <exclusions>
    <exclusion>
      <artifactId>dozer</artifactId>
      <groupId>net.sf.dozer</groupId>
    </exclusion>
    <exclusion>
      <artifactId>dozer-spring</artifactId>
      <groupId>net.sf.dozer</groupId>
    </exclusion>
  </exclusions>
</dependency>
```

## 推荐使用版本

- 对于任何的新项目，推荐使用不低于 `1.5.7.a` 的版本。

- 对于使用 `WriteService` 或 `BatchWriteService` 的项目，请勿使用 `1.5.4.a` `1.5.5.a` 版本。

- 对于任何项目，请勿使用 `1.4.8.a` 的版本，此版本核心类存在严重 bug。

- 低于 `1.2.13.a` 的版本，内存数据访问层分页查询逻辑存在问题，如使用内存数据访问层，则需要将版本升级至 `1.2.13.a` 以上。

- 低于 `1.2.7.a` 的版本，部分关键依赖有严重等级的 bug，使用可能会造成安全问题。

- 低于 `1.2.3.b` 的版本包含以下已经发现的 bug，如果项目使用到了下述模块，则需要将 subgrade 版本升级至 `1.2.3.b`。
  - `RedisBatchBaseDao` 执行 `batchDelete` 存在执行不成功或行为异常的 bug。

- 低于 `1.1.8.a` 的版本包含以下已经发现的 bug，如果项目使用到了下述模块，则需要将 subgrade 版本升级至 `1.1.8.a`。
  - `com.dwarfeng.subgrade.stack.service.CrudService.get` 部分实现当实体不存在时抛出意料之外的异常的 bug。

- 对于任何使用 subgrade 旧版本的项目，请酌情将 subgrade 版本升级至不低于 `1.2.3.b`。

- 所有项目勿使用 `1.1.0.a` 的版本，此版本存在严重的兼容性问题。
