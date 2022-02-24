# Subgrade

此项目是作者的个人工具库，包含多种能大幅提高发开速度的通用工具库。

---

## 安装说明

1. 下载源码

   使用git进行源码下载。
   ```
   git clone git@github.com:DwArFeng/supgrade.git
   ```
   对于中国用户，可以使用gitee进行高速下载。
   ```
   git clone git@gitee.com:dwarfeng/supgrade.git
   ```

2. 项目安装

   进入项目根目录，执行maven命令
   ```
   mvn clean source:jar install
   ```

3. enjoy it

---

## 特性

- 包含项目开发时所需的几乎所有接口，您可以直接实现这些接口，节省您定义接口的时间。
- 针对实体对象进行大量的工具开发，如实体映射工具、实体的数据访问层、实体的缓存等工具。
- 定义了同一的异常处理机制，使得Service服务或其它方法能够输出统一的异常，方便下序进行进一步处理。
- 实现了常用的AOP，如性能分析、登录判断、权限判断、友好性增强。
- 实现了多种中间件的序列化器，如Redis的序列化器、Kafka的序列化器，可供这些框架直接使用。
- 实现了Web后端的常用工具，如分页、对象封装等常用方法。
- 方便地与 `SpringFramework` 集成。

---

## 实践中获得的极佳体验

- 轻松的完成实体的维护接口。
- 性能分析注解对程序调试、调优的极为便利的支持。

---

## 使用概况与成熟度

- 通用接口定义被大量使用，完全成熟。

- 与实体相关的工具（除数据访问层和缓存）。
  - 主键的使用以`LongIdKey`和`StringIdKey`为主，完全成熟，LongIdKey在网络传输时特地考虑了JS的精度， 提供了`JSFixedLongIdKey`。`StringIdKey`
    也专门为UUID提供了`UuidKey`和`DenseUuidKey`， 后者是通过Base64编码压缩的UUID，其长度只有22位。UUID主键目前几乎很少使用，是因为`snowflake`算法下的
    `LongIdKey`主键的性能以及特性完全优于UUID主键。
  - 随着项目的实践，展现出该项目对联合主键的绝佳支持，使用联合主键就像使用单体主键那样轻松。
  - 当两个实体字段名称相同时，可以通过`DozerBeanTransformer`进行快速的实体映射，这常用于实体与 Hibernate
    PO，实体与FastJson实体，实体与DTO之间的映射。除此之外，subgrade中的实体有独立的静态方法
    进行映射，如`com.dwarfeng.subgrade.sdk.bean.key.FastJsonLongIdKey.of`与
    `com.dwarfeng.subgrade.sdk.bean.key.WebInputLongIdKey.toStackBean`，这是两种框架内典型的映射方式。

- 数据访问层框架。
  - 在使用的过程中，多数bug出现在此，意味着该框架仍然拥有潜在的不成熟的因素。但是几乎所有项目都会使用 此框架，因此任何不成熟的因素都会更快的被发现。
  - 数据访问层框架被最大量使用的是Hibernate的数据访问层框架，十分成熟。
    - com.dwarfeng.subgrade.impl.dao.HibernateBaseDao
    - com.dwarfeng.subgrade.impl.dao.HibernateBatchBaseDao
    - com.dwarfeng.subgrade.impl.dao.HibernateEntireLookupDao
    - com.dwarfeng.subgrade.impl.dao.HibernatePresetLookupDao
  - 数据写入访问层在多个项目中被高强度调用，十分成熟。
    - com.dwarfeng.subgrade.impl.dao.BatchWriteDao
    - com.dwarfeng.subgrade.impl.dao.HibernateBatchWriteDao
    - com.dwarfeng.subgrade.impl.dao.RedisBaseDao
    - com.dwarfeng.subgrade.impl.dao.RedisBatchBaseDao
  - 刚刚开发的框架，在至少一个项目中使用。
    - com.dwarfeng.subgrade.impl.dao.JdbcBaseDao
    - com.dwarfeng.subgrade.impl.dao.JdbcBatchBaseDao
    - com.dwarfeng.subgrade.impl.dao.JdbcWriteDao
    - com.dwarfeng.subgrade.impl.dao.JdbcBatchWriteDao
    - com.dwarfeng.subgrade.impl.dao.JdbcEntireLookupDao
    - com.dwarfeng.subgrade.impl.dao.JdbcPresetLookupDao
  - 刚刚开发的框架，且从来没有被使用过。
    - com.dwarfeng.subgrade.impl.dao.JdbcBaseDao
    - com.dwarfeng.subgrade.impl.dao.JdbcBatchBaseDao
    - com.dwarfeng.subgrade.impl.dao.JdbcBatchWriteDao
    - com.dwarfeng.subgrade.impl.dao.JdbcWriteDao
    - com.dwarfeng.subgrade.impl.dao.MybatisBaseDao
    - com.dwarfeng.subgrade.impl.dao.MybatisBatchBaseDao
    - com.dwarfeng.subgrade.impl.dao.MybatisRelationDao
    - com.dwarfeng.subgrade.impl.dao.MybatisBatchRelationDao
    - com.dwarfeng.subgrade.impl.dao.MybatisBatchWriteDao
    - com.dwarfeng.subgrade.impl.dao.MybatisBatchWriteDao
    - com.dwarfeng.subgrade.impl.dao.MybatisEntireLookupDao
    - com.dwarfeng.subgrade.impl.dao.MybatisPresetLookupDao
  - 其余的数据访问层框架使用情况极少或几乎没有使用，非常有可能存在bug。

- 缓存框架。
  - 缓存框架被广泛使用，其中最常用的几个缓存已经十分成熟。
    - com.dwarfeng.subgrade.impl.cache.RedisBaseCache
    - com.dwarfeng.subgrade.impl.cache.RedisBatchBaseCache
    - com.dwarfeng.subgrade.impl.cache.RedisKeyListCache
    - com.dwarfeng.subgrade.impl.cache.RedisListCache
  - 较少使用的框架，可能含有潜在的没有被发现的问题。
    - com.dwarfeng.subgrade.impl.cache.RedisSingleObjectCache

- 异常映射框架。
  - 十分成熟，广泛使用，至今未发现任何问题。

- 服务框架。
  - 以下服务框架在多个项目中被高强度调用，十分成熟。
    - com.dwarfeng.subgrade.impl.service.CustomCrudService
    - com.dwarfeng.subgrade.impl.service.CustomBatchCrudService
    - com.dwarfeng.subgrade.impl.service.DaoOnlyCrudService
    - com.dwarfeng.subgrade.impl.service.DaoOnlyBatchRelationService
    - com.dwarfeng.subgrade.impl.service.DaoOnlyEntireLookupService
    - com.dwarfeng.subgrade.impl.service.DaoOnlyPresetLookupService
    - com.dwarfeng.subgrade.impl.service.GeneralCrudService
    - com.dwarfeng.subgrade.impl.service.GeneralBatchCrudService
  - 不常用的服务框架有可能存在潜在的未被发现的问题。
    - com.dwarfeng.subgrade.impl.service.GeneralEntireLookupService
    - com.dwarfeng.subgrade.impl.service.GeneralSingleObjectService
  - 刚刚开发的框架，在至少一个项目中使用
    - com.dwarfeng.subgrade.impl.service.DaoOnlyWriteService
    - com.dwarfeng.subgrade.impl.service.DaoOnlyBatchWriteService

- Interceptor
  - 没有任何问题的框架。
    - com.dwarfeng.subgrade.sdk.interceptor.analyse.BehaviorAnalyseAdvisor
    - com.dwarfeng.subgrade.sdk.interceptor.http.BindingCheckAdvisor
  - `com.dwarfeng.subgrade.sdk.interceptor.login.LoginRequiredAdvisor` 在web后端被偶尔使用， 暂时没有发现问题，不过随着使用频率的升高，我也许会考虑此框架的表现。
  - `com.dwarfeng.subgrade.sdk.interceptor.permission.PermissionRequiredAdvisor` 未被使用过， 其成熟度未知。
  - 刚刚开发的框架，在至少一个项目中使用
    - com.dwarfeng.subgrade.sdk.interceptor.friendly.FriendlyAdvisor

- JDBC 数据访问层不同数据库样板
  - 刚刚开发的框架，在至少一个项目中使用
    - Apache Phoenix
  - 刚刚开发的框架，且从来没有被使用过
    - MySQL8

---

## 包含的工具

- 通用项目接口定义。
  - 服务接口 `Service`
  - 数据访问层接口 `Dao`
  - 缓存接口 `Cache`
  - 处理接口 `Handler`
  - 缓存接口 `Cache`
  - Bean接口 `Bean`
  - 实体接口 `Entity`
  - 主键接口 `Key`
  - 数据传输对象接口 `Dto`

  上述接口的定义使得一般的新项目完全无需再定义任何新接口。

- 异常处理工具。
  - 通用服务异常 `ServiceException`
  - 通用异常处理机制 `ServiceExceptionMapper`

  通用异常处理机制能将方法执行过程中的任何异常映射到 `ServiceException` 中， `ServiceException` 异常作为异常中间件， 可方便的转换为 `ResponseData` 或方便的通过 RPC 进行传输。

- 通用数据访问层接口定义以及实现。
- 通用缓存接口定义以及实现。
- Redis, Kafka 的序列化器以及反序列化器。
- 通用服务的接口定义以及实现。
- AOP增强。

## 推荐使用版本

- 对于任何的新项目，使用不低于 `1.2.3.b` 的版本，推荐使用 `1.2.4.a`。
- 低于 `1.2.3.b` 的版本包含以下已经发现的 bug，如果项目使用到了下述模块，则需要将 subgrade 版本升级至 `1.2.3.b`。
  - `RedisBatchBaseDao` 执行 `batchDelete` 存在执行不成功或行为异常的 bug。
- 低于 `1.1.8.a` 的版本包含以下已经发现的 bug，如果项目使用到了下述模块，则需要将 subgrade 版本升级至 `1.1.8.a`。
  - `com.dwarfeng.subgrade.stack.service.CrudService.get` 部分实现当实体不存在时抛出意料之外的异常的 bug。
- 对于任何使用 subgrade 旧版本的项目，请酌情将 subgrade 版本升级至 `1.2.3.b`。
- 所有项目勿使用 `1.1.0.a` 的版本，此版本存在严重的兼容性问题。
