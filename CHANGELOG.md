# ChangeLog

## Release_1.7.0_20251023_build_A

### 功能构建

- Wiki 编写。
  - docs/wiki/zh_CN/BehaviorAnalyse.md。

- 登录和权限相关接口的大版本更新（**不兼容更新**）。
  - 优化 `LoginRequiredAopManager` 接口，将方法签名从使用 `LongIdKey` 类型改为直接使用 `String` 类型。
  - 优化 `PermissionRequiredAopManager` 接口，将方法签名从使用 `StringIdKey` 类型改为直接使用 `String` 类型。
  - 更新相关实现类以适配新的接口签名：
    - com.dwarfeng.subgrade.sdk.interceptor.login.DefaultLoginRequiredAopManager。
    - com.dwarfeng.subgrade.sdk.interceptor.login.HttpLoginRequiredAopManager。
    - com.dwarfeng.subgrade.sdk.interceptor.login.TokenHandlerLoginRequiredAopManager。
    - com.dwarfeng.subgrade.sdk.interceptor.permission.DefaultPermissionRequiredAopManager。
    - com.dwarfeng.subgrade.sdk.interceptor.permission.HttpPermissionRequiredAopManager。
    - com.dwarfeng.subgrade.sdk.interceptor.permission.TokenHandlerPermissionRequiredAopManager。
  - 更新相关 Advisor 类以使用字符串类型的用户ID：
    - com.dwarfeng.subgrade.sdk.interceptor.login.LoginRequiredAdvisor。
    - com.dwarfeng.subgrade.sdk.interceptor.permission.PermissionRequiredAdvisor。
  - 更新 `TokenResolver` 接口，将 `resolve()` 方法返回类型从 `StringIdKey` 改为 `String`。
  - 更新 `LoginFailedException` 类，将字段类型从 `LongIdKey` 改为 `String`，并更新所有构造函数。

- 优化开发环境支持。
  - 在 .gitignore 中添加 VSCode 相关文件的忽略规则。
  - 在 .gitignore 中添加 Cursor IDE 相关文件的忽略规则。

### Bug 修复

- (无)

### 功能移除

- (无)

---

## Release_1.6.0_20251012_build_A

### 功能构建

- 优化 `docs/wiki` 目录结构。
  - 将 `docs/wiki/en_US` 目录重命名为 `en-US`，以符合 rfc5646 规范。
  - 将 `docs/wiki/zh_CN` 目录重命名为 `zh-CN`，以符合 rfc5646 规范。
  - 更新 `docs/wiki/README.md` 中的链接指向。
  - 更新 `README.md` 中的链接指向。

- 使部分接口继承 `Handler` 接口。
  - com.dwarfeng.subgrade.stack.handler.PermissionHandler。

- 优化部分接口的文档注释。
  - com.dwarfeng.subgrade.stack.handler.LoginHandler。
  - com.dwarfeng.subgrade.stack.handler.LoginPermHandler。
  - com.dwarfeng.subgrade.stack.handler.PermissionHandler。

- 依赖升级。
  - 升级 `zookeeper` 依赖版本为 `3.9.4` 以规避漏洞。

### Bug 修复

- (无)

### 功能移除

- (无)

---

## Release_1.5.11_20250729_build_A

### 功能构建

- Wiki 编写。
  - docs/wiki/zh_CN/ExceptionMappingBasics.md。

- 优化部分文档中的内容。
  - docs/wiki/zh_CN/Contents.md。
  - docs/wiki/zh_CN/DataAccessBasics.md。

- 优化部分异常代码的 `tip`。
  - com.dwarfeng.subgrade.sdk.exception.ServiceExceptionCodes.KEY_FETCH_FAILED。

- 依赖升级。
  - 升级 `commons-lang3` 依赖版本为 `3.18.0` 以规避漏洞。

- 依赖优化。
  - 优化部分依赖的排除项，以避免潜在的 `netty` 版本冲突问题。

### Bug 修复

- 修复部分工具方法有可能产生空指针异常的问题。
  - com.dwarfeng.subgrade.sdk.bean.dto.PagingUtil.transform。
  - com.dwarfeng.subgrade.sdk.bean.dto.PagingUtil.reverseTransform。

### 功能移除

- (无)

---

## Release_1.5.10_20250531_build_A

### 功能构建

- Wiki 编写。
  - docs/wiki/zh_CN/DataAccessBasics.md。

- 依赖升级。
  - 升级 `commons-beanutils` 依赖版本为 `1.11.0` 以规避漏洞。

### Bug 修复

- (无)

### 功能移除

- (无)

---

## Release_1.5.9_20250504_build_A

### 功能构建

- 优化部分文档中的内容。
  - README.md。
  - docs/wiki/zh_CN/Introduction.md。

- Wiki 编写。
  - docs/wiki/zh_CN/InstallBySourceCode.md。
  - docs/wiki/zh_CN/UseWithMaven.md。

- 依赖升级。
  - 升级 `netty` 依赖版本为 `4.1.119.Final` 以规避漏洞。

### Bug 修复

- (无)

### 功能移除

- (无)

---

## Release_1.5.8_20250324_build_A

### 功能构建

- 优化部分文档中的内容。
  - README.md。
  - docs/wiki/zh_CN/Introduction.md。

- Wiki 编写。
  - docs/wiki/zh_CN/VersionBlacklist.md。

- 依赖升级。
  - 升级 `kafka` 依赖版本为 `3.9.0` 以规避漏洞。
  - 升级 `hibernate-validator` 依赖版本为 `6.2.5.Final` 以规避漏洞。

### Bug 修复

- (无)

### 功能移除

- (无)

---

## Release_1.5.7_20241117_build_A

### 功能构建

- 依赖升级。
  - 升级 `spring` 依赖版本为 `5.3.39` 以规避漏洞。
  - 升级 `netty` 依赖版本为 `4.1.115.Final` 以规避漏洞。
  - 升级 `zookeeper` 依赖版本为 `3.9.3` 以规避漏洞。

### Bug 修复

- 修复服务异常代号的 bug。
  - 修复 `ServiceExceptionCodes` 设置服务异常代号的偏移量时，部分服务异常代号中的代码值未更新的的 bug。

### 功能移除

- (无)

---

## Release_1.5.6_20240807_build_A

### 功能构建

- 更新 `README.md`。

- Wiki 编写。
  - 构建 wiki 目录结构。
  - docs/wiki/en_US/Contents.md。
  - docs/wiki/en_US/Introduction.md。
  - docs/wiki/zh_CN/Contents.md。
  - docs/wiki/zh_CN/Introduction.md。

### Bug 修复

- 修正部分文档注释。
  - com.dwarfeng.subgrade.impl.service.DaoOnlyBatchWriteService。

- 添加部分类中缺失的构造器方法。
  - com.dwarfeng.subgrade.impl.service.DaoOnlyBatchWriteService。

### 功能移除

- (无)

---

## Release_1.5.5_20240730_build_A

### 功能构建

- 依赖升级。
  - 升级 `spring` 依赖版本为 `5.3.37` 以规避漏洞。

### Bug 修复

- (无)

### 功能移除

- (无)

---

## Release_1.5.4_20240725_build_A

### 功能构建

- 依赖升级。
  - 升级 `netty` 依赖版本为 `4.1.108.Final` 以规避漏洞。

- 增加部分服务的抽象实现。
  - com.dwarfeng.subgrade.impl.service.AbstractBatchCrudService。
  - com.dwarfeng.subgrade.impl.service.AbstractCrudService。
  - com.dwarfeng.subgrade.impl.service.AbstractEntireLookupService。
  - com.dwarfeng.subgrade.impl.service.AbstractPresetLookupService。
  - com.dwarfeng.subgrade.impl.service.AbstractBatchWriteService。
  - com.dwarfeng.subgrade.impl.service.AbstractWriteService。
  - com.dwarfeng.subgrade.impl.service.AbstractBatchRelationService。
  - com.dwarfeng.subgrade.impl.service.AbstractRelationService。

- 优化部分服务的实现的继承关系。
  - com.dwarfeng.subgrade.impl.service.CustomBatchCrudService。
  - com.dwarfeng.subgrade.impl.service.CustomCrudService。
  - com.dwarfeng.subgrade.impl.service.DaoOnlyBatchCrudService。
  - com.dwarfeng.subgrade.impl.service.DaoOnlyCrudService。
  - com.dwarfeng.subgrade.impl.service.GeneralBatchCrudService。
  - com.dwarfeng.subgrade.impl.service.GeneralCrudService。
  - com.dwarfeng.subgrade.impl.service.GeneralEntireLookupService。
  - com.dwarfeng.subgrade.impl.service.DaoOnlyEntireLookupService。
  - com.dwarfeng.subgrade.impl.service.DaoOnlyPresetLookupService。
  - com.dwarfeng.subgrade.impl.service.DaoOnlyBatchWriteService。
  - com.dwarfeng.subgrade.impl.service.DaoOnlyWriteService。
  - com.dwarfeng.subgrade.impl.service.DaoOnlyBatchRelationService。
  - com.dwarfeng.subgrade.impl.service.DaoOnlyRelationService。

- 优化部分接口及其实现的参数名称。
  - com.dwarfeng.subgrade.stack.service.BatchCrudService。
  - com.dwarfeng.subgrade.stack.service.BatchWriteService。
  - com.dwarfeng.subgrade.stack.service.CrudService。
  - com.dwarfeng.subgrade.stack.service.WriteService。
  - com.dwarfeng.subgrade.impl.service.CustomBatchCrudService。
  - com.dwarfeng.subgrade.impl.service.CustomCrudService。
  - com.dwarfeng.subgrade.impl.service.DaoOnlyBatchCrudService。
  - com.dwarfeng.subgrade.impl.service.DaoOnlyBatchWriteService。
  - com.dwarfeng.subgrade.impl.service.DaoOnlyCrudService。
  - com.dwarfeng.subgrade.impl.service.DaoOnlyWriteService。
  - com.dwarfeng.subgrade.impl.service.GeneralBatchCrudService。
  - com.dwarfeng.subgrade.impl.service.GeneralCrudService。

- 优化部分文档注释。
  - com.dwarfeng.subgrade.stack.exception.ServiceExceptionMapper。
  - com.dwarfeng.subgrade.stack.service.BatchCrudService。
  - com.dwarfeng.subgrade.stack.service.BatchWriteService。
  - com.dwarfeng.subgrade.stack.service.CrudService。
  - com.dwarfeng.subgrade.stack.service.WriteService。
  - com.dwarfeng.subgrade.stack.service.EntireLookupService。
  - com.dwarfeng.subgrade.stack.service.PresetLookupService。
  - com.dwarfeng.subgrade.stack.service.RelationService。
  - com.dwarfeng.subgrade.stack.service.BatchRelationService。

### Bug 修复

- 过期部分结构中命名不规范的方法，并提供命名规范的方法。
  - com.dwarfeng.subgrade.stack.service.BatchCrudService.batchInsertIfExists。

### 功能移除

- (无)

---

## Release_1.5.3_20240503_build_A

### 功能构建

- 依赖升级。
  - 升级 `slf4j` 依赖版本为 `1.7.36` 以规避漏洞。

- 行为分析优化。
  - 优化了参数或返回值是数组的情况下，行为分析的日志输出格式。

### Bug 修复

- 修复部分 AOP Advisor 优先级不合理的 bug。
  - com.dwarfeng.subgrade.sdk.interceptor.analyse.BehaviorAnalyseAdvisor。

- 修复 `CHANGELOG.md` 中早前版本中能够引起歧义的错误描述。

### 功能移除

- (无)

---

## Release_1.5.2_20240413_build_A

### 功能构建

- 行为分析优化。
  - 优化行为分析的日志输出格式，使输出信息更加全面，且更加规范。
  - 优化行为分析的日志输出格式，在默认情况下减少不必要的输出。
  - 增加 VM 参数 `-Dsubgrade.detailedBehaviorAnalyseLog=true` 用于开启在默认情况下减少的输出。
  - 日志记录器的默认类优化为 `BehaviorAnalyse` 注解所在的类。
  - `BehaviorAnalyse` 注解增加 `loggerClass` 属性，用于指定日志记录器的类。

- 优化项目的系统属性机制。
  - 将部分类中的系统属性常量值迁移至 `SystemPropertyConstants` 工具类中。
  - 对于启用/停止项目某项功能的系统属性，统一解析为 `boolean` 类型进行判断，而不只是判断该属性是否存在。
  - 优化部分系统属性的键名。

### Bug 修复

- (无)

### 功能移除

- (无)

---

## Release_1.5.1_20240408_build_A

### 功能构建

- (无)

### Bug 修复

- 修复部分 `BatchCrudService` 在使用废弃的构造器方法实例化后部分方法无法正常工作的 bug。
  - com.dwarfeng.subgrade.impl.service.CustomBatchCrudService。
  - com.dwarfeng.subgrade.impl.service.DaoOnlyBatchCrudService。
  - com.dwarfeng.subgrade.impl.service.GeneralBatchCrudService。

### 功能移除

- (无)

---

## Release_1.5.0_20240309_build_A

### 功能构建

- 新增分页查询特性。
  - 进一步明确了分页查询时，每页行数为 0 的行为。
  - 分页查询时，如果页数为负数，则默认修正为 0。
  - 分页查询时，如果每页数量为负数，则默认修正为 `Integer.MAX_VALUE`。
  - 分页修正默认开启，可以通过添加 VM 参数 `-Dsubgrade.useStrictPaging=true` 关闭。
  - 分页修正时默认输出警告日志，可以通过添加 VM 参数 `-Dsubgrade.logPagingWarning=false` 关闭。

- 为适配新增的分页查询特性，修改服务接口文档。
  - com.dwarfeng.subgrade.stack.service.EntireLookupService。
  - com.dwarfeng.subgrade.stack.service.PresetLookupService。

- 为适配新增的分页查询特性，修改服务实现。
  - com.dwarfeng.subgrade.impl.service.DaoOnlyEntireLookupService。
  - com.dwarfeng.subgrade.impl.service.DaoOnlyPresetLookupService。
  - com.dwarfeng.subgrade.impl.service.GeneralEntireLookupService。

- 为适配新增的分页查询特性，修改缓存实现。
  - com.dwarfeng.subgrade.impl.cache.RedisKeyListCache。
  - com.dwarfeng.subgrade.impl.cache.RedisListCache。

- 为适配新增的分页查询特性，修改数据访问层实现。
  - com.dwarfeng.subgrade.impl.dao.HibernateAccelerableHqlPresetLookupDao。
  - com.dwarfeng.subgrade.impl.dao.HibernateAccelerablePresetLookupDao。
  - com.dwarfeng.subgrade.impl.dao.HibernateEntireLookupDao。
  - com.dwarfeng.subgrade.impl.dao.HibernateHqlPresetLookupDao。
  - com.dwarfeng.subgrade.impl.dao.HibernatePresetLookupDao。
  - com.dwarfeng.subgrade.impl.dao.JdbcEntireLookupDao。
  - com.dwarfeng.subgrade.impl.dao.JdbcPresetLookupDao。
  - com.dwarfeng.subgrade.impl.dao.MemoryEntireLookupDao。
  - com.dwarfeng.subgrade.impl.dao.MemoryPresetLookupDao。
  - com.dwarfeng.subgrade.impl.dao.MybatisEntireLookupDao。
  - com.dwarfeng.subgrade.impl.dao.MybatisPresetLookupDao。
  - com.dwarfeng.subgrade.impl.dao.RedisEntireLookupDao。
  - com.dwarfeng.subgrade.impl.dao.RedisPresetLookupDao。

- 为适配新增的分页查询特性，增加新异常，并分配异常代码。
  - com.dwarfeng.subgrade.stack.exception.PagingException。

### Bug 修复

- 修复部分类实现 `Serializable` 的类没有 `serialVersionUID` 静态字段的问题。
  - com.dwarfeng.subgrade.sdk.hibernate.criteria.NoOrderDetachedCriteria。

### 功能移除

- (无)

---

## Release_1.4.8_20240123_build_B

### 功能构建

- 部分类代码格式优化。
  - com.dwarfeng.subgrade.impl.dao.HibernateAccelerableHqlPresetLookupDao。
  - com.dwarfeng.subgrade.impl.dao.HibernateAccelerablePresetLookupDao。
  - com.dwarfeng.subgrade.impl.dao.HibernateBaseDao。
  - com.dwarfeng.subgrade.impl.dao.HibernateDaoFactory。
  - com.dwarfeng.subgrade.impl.dao.HibernatePresetLookupDao。

### Bug 修复

- 修复部分数据访问层的错误代码。
  - com.dwarfeng.subgrade.impl.dao.HibernatePresetLookupDao。

### 功能移除

- (无)

---

## Release_1.4.8_20240123_build_A

### 功能构建

- 优化 Hibernate Dao，使部分实现类的计数查询不生成 ORDER BY 语句，提高查询效率。
  - com.dwarfeng.subgrade.impl.dao.HibernateAccelerableHqlPresetLookupDao。
  - com.dwarfeng.subgrade.impl.dao.HibernateAccelerablePresetLookupDao。
  - com.dwarfeng.subgrade.impl.dao.HibernateEntireLookupDao。
  - com.dwarfeng.subgrade.impl.dao.HibernateHqlPresetLookupDao。
  - com.dwarfeng.subgrade.impl.dao.HibernatePresetLookupDao。

### Bug 修复

- (无)

### 功能移除

- (无)

---

## Release_1.4.7_20231226_build_A

### 功能构建

- 依赖升级。
  - 升级 `spring` 依赖版本为 `5.3.31` 以规避漏洞。

- 优化 com.dwarfeng.subgrade.sdk.database.definition 包下的代码。
  - 优化 ColumnTypes 类中的注释。
  - 废弃 ColumnTypes 类。
  - 增加 MySQL8ColumnTypes 类。

### Bug 修复

- (无)

### 功能移除

- (无)

---

## Release_1.4.6_20231225_build_A

### 功能构建

- 依赖升级。
  - 升级 `kafka` 依赖版本为 `3.6.1` 以规避漏洞。
  - 升级 `dubbo` 依赖版本为 `2.7.22` 以规避漏洞。
  - 升级 `netty` 依赖版本为 `4.1.104.Final` 以规避漏洞。
  - 升级 `zookeeper` 依赖版本为 `3.7.2` 以规避漏洞。

### Bug 修复

- (无)

### 功能移除

- (无)

---

## Release_1.4.5_20230909_build_A

### 功能构建

- 优化代码格式。
  - com.dwarfeng.subgrade.impl.service.*。
  - com.dwarfeng.subgrade.impl.dao.*。

- 新增对象生成机制。
  - 新增 com.dwarfeng.subgrade.stack.generation.Generator 接口。
  - 新增 com.dwarfeng.subgrade.stack.generation.KeyGenerator 接口。
  - 过时 com.dwarfeng.subgrade.stack.bean.key.KeyFetcher 接口。
  - 使用新增接口替换工程内部的过时接口。

### Bug 修复

- (无)

### 功能移除

- (无)

---

## Release_1.4.4_20230902_build_A

### 功能构建

- 优化处理器异常的处理工具。
  - 新增 `HandlerExceptionHelper` 帮助类，在其中提供处理器异常的工具方法。
  - 过期 `HandlerUtil` 中不规范的方法，并使用新方法替换工程内的调用。

- 优化服务异常的处理工具。
  - 在 `ServiceExceptionHelper` 中新增服务异常的工具方法。
  - 过期 `ServiceExceptionHelper` 中不规范的方法，并使用新方法替换工程内的调用。

### Bug 修复

- (无)

### 功能移除

- (无)

---

## Release_1.4.3_20230901_build_A

### 功能构建

- 依赖升级。
  - 升级 `spring-kafka` 依赖版本为 `2.9.11` 以规避漏洞。

- 优化代码结构。
  - com.dwarfeng.subgrade.impl.handler.CuratorDistributedLockHandler。
  - com.dwarfeng.subgrade.impl.handler.GeneralLocalCacheHandler。
  - com.dwarfeng.subgrade.impl.handler.GeneralOnlineHandler。
  - com.dwarfeng.subgrade.impl.handler.GeneralStartableHandler。

### Bug 修复

- (无)

### 功能移除

- (无)

---

## Release_1.4.2_20230807_build_A

### 功能构建

- 增加数据访问层实现。
  - com.dwarfeng.subgrade.impl.dao.HibernateHqlPresetLookupDao。
  - com.dwarfeng.subgrade.impl.dao.HibernateAccelerableHqlPresetLookupDao。

- 规范代码的注解、注释、toString 方法。
  - com.dwarfeng.subgrade.impl.dao.HibernateAccelerablePresetLookupDao。
  - com.dwarfeng.subgrade.impl.dao.HibernateBaseDao。
  - com.dwarfeng.subgrade.impl.dao.HibernateBatchBaseDao。
  - com.dwarfeng.subgrade.impl.dao.HibernateBatchRelationDao。
  - com.dwarfeng.subgrade.impl.dao.HibernateBatchWriteDao。
  - com.dwarfeng.subgrade.impl.dao.HibernateEntireLookupDao。
  - com.dwarfeng.subgrade.impl.dao.HibernatePresetLookupDao。
  - com.dwarfeng.subgrade.impl.dao.HibernateRelationDao。
  - com.dwarfeng.subgrade.impl.dao.HibernateSingleObjectDao。
  - com.dwarfeng.subgrade.impl.dao.HibernateWriteDao。
  - com.dwarfeng.subgrade.impl.dao.RedisBaseDao。
  - com.dwarfeng.subgrade.impl.dao.RedisBatchBaseDao。
  - com.dwarfeng.subgrade.impl.dao.RedisEntireLookupDao。
  - com.dwarfeng.subgrade.impl.dao.RedisPresetLookupDao。
  - com.dwarfeng.subgrade.impl.dao.RedisSingleObjectDao。
  - com.dwarfeng.subgrade.impl.service.CustomBatchCrudService。
  - com.dwarfeng.subgrade.impl.service.CustomCrudService。
  - com.dwarfeng.subgrade.impl.service.DaoOnlyBatchCrudService。
  - com.dwarfeng.subgrade.impl.service.DaoOnlyBatchRelationService。
  - com.dwarfeng.subgrade.impl.service.DaoOnlyBatchWriteService。
  - com.dwarfeng.subgrade.impl.service.DaoOnlyCrudService。
  - com.dwarfeng.subgrade.impl.service.DaoOnlyEntireLookupService。
  - com.dwarfeng.subgrade.impl.service.DaoOnlyPresetLookupService。
  - com.dwarfeng.subgrade.impl.service.DaoOnlyRelationService。
  - com.dwarfeng.subgrade.impl.service.DaoOnlySingleObjectService。
  - com.dwarfeng.subgrade.impl.service.DaoOnlyWriteService。
  - com.dwarfeng.subgrade.impl.service.GeneralBatchCrudService。
  - com.dwarfeng.subgrade.impl.service.GeneralCrudService。
  - com.dwarfeng.subgrade.impl.service.GeneralEntireLookupService。
  - com.dwarfeng.subgrade.impl.service.GeneralSingleObjectService。
  - com.dwarfeng.subgrade.impl.cache.RedisBaseCache。
  - com.dwarfeng.subgrade.impl.cache.RedisSingleObjectCache。
  - com.dwarfeng.subgrade.impl.cache.RedisListCache。
  - com.dwarfeng.subgrade.impl.cache.RedisBatchBaseCache。
  - com.dwarfeng.subgrade.impl.cache.RedisKeyListCache。

- 规范约束检查注解。
  - org.springframework.lang.NonNull -> javax.annotation.Nonnull。
  - org.springframework.lang.Nullable -> javax.annotation.Nullable。

- 规范化类名。
  - HibernateAcceleratePresetLookupDao -> HibernateAccelerablePresetLookupDao，旧类名依然可用，但是已经过时。

- 依赖升级。
  - 升级 `guava` 依赖版本为 `32.0.1-jre` 以规避漏洞。

### Bug 修复

- 修复 HibernateAccelerablePresetLookupDao.lookupFirst 方法的 bug。

### 功能移除

- (无)

---

## Release_1.4.1_20230729_build_A

### 功能构建

- 增加接口方法及其实现。
  - com.dwarfeng.subgrade.stack.service.EntireLookupService.lookupCount。
  - com.dwarfeng.subgrade.stack.service.PresetLookupService.lookupCount。

### Bug 修复

- (无)

### 功能移除

- (无)

---

## Release_1.4.0_20230509_build_A

### 功能构建

- 优化 ServiceExceptionCodes 类。
  - 增加异常代码 ServiceExceptionCodes.NOT_IMPLEMENTED_YET。
  - 设置异常代号的偏移量时，以新的 EXCEPTION_CODE_OFFSET 为基准，更新异常代码的值。

- AOP 调度逻辑优化。
  - 强化 LoginRequiredAdvisor 调度逻辑。
  - 强化 PermissionRequiredAdvisor 调度逻辑（**不兼容更新**）。

### Bug 修复

- (无)

### 功能移除

- (无)

---

## Release_1.3.3_20230420_build_A

### 功能构建

- 依赖升级。
  - 升级 `spring` 依赖版本为 `5.3.27` 以规避漏洞。

### Bug 修复

- (无)

### 功能移除

- (无)

---

## Release_1.3.2_20230327_build_A

### 功能构建

- 依赖升级。
  - 升级 `spring` 依赖版本为 `5.3.26` 以规避漏洞。
  - 升级 `snakeyaml` 依赖版本为 `2.0` 以规避漏洞。

### Bug 修复

- (无)

### 功能移除

- (无)

---

## Release_1.3.1_20230310_build_A

### 功能构建

- 放宽 LocalCacheHandler 的泛型约束。

- 登陆处理器扩展方法。
  - LoginHandler.login(StringIdKey, String, Map<String,String>) throws HandlerException。

- 依赖升级。
  - 升级 `netty` 依赖版本为 `4.1.86.Final` 以规避漏洞。
  - 升级 `dubbo` 依赖版本为 `2.7.21` 以规避漏洞。

### Bug 修复

- (无)

### 功能移除

- (无)

---

## Release_1.3.0_20221129_build_A

### 功能构建

- `README.md` 优化。
  - 重新排版。
  - 删除 `使用概况与成熟度`，后期将单独建立说明文件。

- Handler 更新。
  - com.dwarfeng.subgrade.stack.handler.DistributedLockHandler。
  - com.dwarfeng.subgrade.stack.handler.LocalCacheHandler。
  - com.dwarfeng.subgrade.stack.handler.OnlineHandler。
  - com.dwarfeng.subgrade.stack.handler.StartableHandler。

- BeanTransformer 更新。
  - 引入 `MapStruct` 框架，实现 com.dwarfeng.subgrade.impl.bean.MapStructBeanTransformer。
  - 过期 com.dwarfeng.subgrade.impl.bean.DozerBeanTransformer。

- 依赖优化。
  - 更改 `dozer` 依赖 `scope` 类型为 `provided`, 停止默认引入。
  - 更改 `dozer-spring` 依赖 `scope` 类型为 `provided`, 停止默认引入。

### Bug 修复

- (无)

### 功能移除

- (无)

---

## Release_1.2.14_20221120_build_A

### 功能构建

- 增加依赖。
  - 增加依赖 `jsr305` 以规避依赖冲突，版本为 `3.0.2`。
  - 增加依赖 `commons-lang3` 以规避漏洞，版本为 `3.12.0`。
  - 增加依赖 `commons-collections4` 以规避漏洞，版本为 `4.4`。
  - 增加依赖 `guava` 以规避漏洞，版本为 `31.1-jre`。
  - 增加依赖 `curator` 以规避漏洞，版本为 `4.3.0`。
  - 增加依赖 `gson` 以规避漏洞，版本为 `2.8.9`。
  - 增加依赖 `snakeyaml` 以规避漏洞，版本为 `1.33`。

- 依赖升级。
  - 兼容性替换 `javax.servlet:servlet-api:2.5` 为 `javax.servlet:javax.servlet-api:4.0.1`。
  - 升级 `annotations` 依赖版本为 `3.0.1` 以规避漏洞。
  - 升级 `spring-kafka` 依赖版本为 `2.8.10` 以规避漏洞。
  - 升级 `kafka` 依赖版本为 `2.6.3` 以规避漏洞。
  - 升级 `zookeeper` 依赖版本为 `3.5.7` 以规避漏洞。
  - 升级 `spring-data-redis` 依赖版本为 `2.7.5` 以规避漏洞。
  - 升级 `dutil` 依赖版本为 `beta-0.3.2.a` 以规避漏洞。
  - 升级 `aspectj` 依赖版本为 `1.9.7` 以规避漏洞。

### Bug 修复

- (无)

### 功能移除

- 删除不需要的依赖。
  - 删除 `el` 依赖。
  - 删除 `zkclient` 依赖。

---

## Release_1.2.13_20221025_build_B

### 功能构建

- 依赖升级。
  - 升级 `dubbo` 依赖版本为 `2.7.18` 以规避漏洞。

### Bug 修复

- (无)

### 功能移除

- (无)

---

## Release_1.2.13_20221025_build_A

### 功能构建

- 优化部分代码的文档注释。

- 改进数据访问层。
  - com.dwarfeng.subgrade.impl.dao.MemorySingleObjectDao。

- FastJson/JSFixedFastJson 实体增加 `toStackBean` 方法。
  - com.dwarfeng.subgrade.sdk.bean.key.FastJsonByteIdKey。
  - com.dwarfeng.subgrade.sdk.bean.key.FastJsonDenseUuidKey。
  - com.dwarfeng.subgrade.sdk.bean.key.FastJsonIntegerIdKey。
  - com.dwarfeng.subgrade.sdk.bean.key.FastJsonLongIdKey。
  - com.dwarfeng.subgrade.sdk.bean.key.FastJsonStringIdKey。
  - com.dwarfeng.subgrade.sdk.bean.key.FastJsonUuidKey。
  - com.dwarfeng.subgrade.sdk.bean.key.JSFixedFastJsonLongIdKey。

### Bug 修复

- 修正部分数据访问层在进行分页查询时抛出异常的 bug。
  - com.dwarfeng.subgrade.impl.dao.MemoryEntireLookupDao。
  - com.dwarfeng.subgrade.impl.dao.MemoryPresetLookupDao。

### 功能移除

- (无)

---

## Release_1.2.12_20221012_build_A

### 功能构建

- 改进数据访问层，完善文档注释。
  - com.dwarfeng.subgrade.impl.dao.MemoryBaseDao。
  - com.dwarfeng.subgrade.impl.dao.MemoryBatchBaseDao。
  - com.dwarfeng.subgrade.impl.dao.MemoryEntireLookupDao。
  - com.dwarfeng.subgrade.impl.dao.MemoryPresetLookupDao。

### Bug 修复

- (无)

### 功能移除

- (无)

---

## Release_1.2.11_20221011_build_A

### 功能构建

- 改进数据访问层。
  - com.dwarfeng.subgrade.impl.dao.MemoryBatchBaseDao。
  - com.dwarfeng.subgrade.impl.dao.MemoryBaseDao。

### Bug 修复

- (无)

### 功能移除

- (无)

---

## Release_1.2.10_20220912_build_A

### 功能构建

- 升级部分依赖至新版本。
  - dutil.version: `beta-0.3.0.a` - `beta-0.3.1.a`。

### Bug 修复

- (无)

### 功能移除

- 删除不需要的依赖。
  - 删除 `snowflake` 依赖。
  - 删除 `cxf` 依赖。

---

## Release_1.2.9_20220903_build_A

### 功能构建

- 升级 `maven-deploy-plugin` 插件版本为 `2.8.2`。

### Bug 修复

- (无)

### 功能移除

- (无)

---

## Release_1.2.8_20220701_build_A

### 功能构建

- 依赖升级。
  - 升级 `dutil` 依赖版本为 `beta-0.3.0`。

- 新增 `Hibernate` 基于本地查询加速的 `PresetLookupDao` 的实现。
  - com.dwarfeng.subgrade.impl.dao.HibernateAcceleratePresetLookupDao。

- 新增基于 `Hibernate` 实现的 `PresetLookupDao` 的工厂类。
  - com.dwarfeng.subgrade.impl.dao.HibernateDaoFactory。

- 改进 `ResponseDataUtil` 中的方法。
  - 过时调一些有不必要参数的方法。
  - 新增入口参数简化的方法。

- 在实体的查询数据访问层、查询服务借口添加默认方法，并在一些实现中提供优化的实现。
  - com.dwarfeng.subgrade.stack.dao.EntireLookupDao.lookupFirst。
  - com.dwarfeng.subgrade.stack.dao.PresetLookupDao.lookupFirst。
  - com.dwarfeng.subgrade.stack.service.EntireLookupService.lookupFirst。
  - com.dwarfeng.subgrade.stack.service.PresetLookupService.lookupFirst。

- 主键抓取器新增批量抓取功能。
  - com.dwarfeng.subgrade.stack.bean.key.KeyFetcher.batchFetchKey。

### Bug 修复

- 解决部分代码调用过时的 API 的问题。

- 解决部分代码的警告。

### 功能移除

- 删除不需要的依赖。
  - 删除 `joda-time` 依赖。
  - 删除 `commons-lang3` 依赖。
  - 删除 `commons-io` 依赖。
  - 删除 `commons-net` 依赖。
  - 删除 `pagehelper` 依赖。
  - 删除 `jsqlparser` 依赖。
  - 删除 `commons-fileupload` 依赖。
  - 删除 `http-components` 依赖。
  - 删除 `noggit` 依赖。
  - 删除 `jboss-logging` 依赖。

---

## Release_1.2.7_20220525_build_A

### 功能构建

- 依赖升级。
  - 升级 `spring-kafka` 依赖版本为 `2.3.6.RELEASE` 以规避漏洞。
  - 升级 `fastjson` 依赖版本为 `1.2.83` 以规避漏洞。
  - 升级 `druid` 依赖版本为 `1.1.20` 以规避漏洞。
  - 升级 `netty` 依赖版本为 `4.1.77` 以规避漏洞。
  - 升级 `fastjson` 依赖版本为 `1.2.83` 以规避漏洞。

### Bug 修复

- 修正部分文档注释中的语法错误。

### 功能移除

- 依赖移除。
  - 移除 `mysql` 依赖。

---

## Release_1.2.6_20220521_build_A

### 功能构建

- 升级 `spring` 依赖版本为 `5.3.19` 以规避漏洞。

### Bug 修复

- (无)

### 功能移除

- (无)

---

## Release_1.2.5_20220415_build_A

### 功能构建

- 升级依赖版本，并解决冲突。

### Bug 修复

- (无)

### 功能移除

- (无)

---

## Release_1.2.4_20220224_build_A

### 功能构建

- 升级 `log4j2` 依赖版本为 `2.17.1` 以规避漏洞。
  - `CVE-2021-44228`。
  - `CVE-2021-45105`。

- 升级部分依赖的版本。

- 添加查询服务的方法，优化了在部分场景下的查询速度。
  - `EntireLookupService#lookupAsList()`。
  - `EntireLookupService#lookupAsList(PagingInfo)`。
  - `PresetLookupService#lookupAsList(java.lang.String, java.lang.Object[])`。
  - `PresetLookupService#lookupAsList(java.lang.String, java.lang.Object[], PagingInfo)`。

### Bug 修复

- (无)

### 功能移除

- (无)

---

## Release_1.2.3_20211012_build_B

### 功能构建

- (无)

### Bug 修复

- 修正 `RedisBatchBaseDao` 执行 `batchDelete` 方法时当入口参数长度为 0 时行为异常的 bug。

### 功能移除

- (无)

---

## Release_1.2.3_20211012_build_A

### 功能构建

- 升级部分依赖至新版本。
  - dubbo.version: `2.7.1` - `2.7.13`。
  - httpcomponents.version: `4.4.1` - `4.5.13`。
  - hibernate-validator.version `6.0.18.Final` - `6.0.20.Final`。

### Bug 修复

- 修正 `RedisBatchBaseDao` 执行 `batchDelete` 方法时无法删除实体的 bug。

### 功能移除

- (无)

---

## Release_1.2.2_20210824_build_A

### 功能构建

- 为 HibernateBaseDao 以及 HibernateBatchBaseDao 增加字段 updateKeepFields，解决实体更新时多对多信息丢失的问题。
- 升级 mybatis 版本。

### Bug 修复

- (无)

### 功能移除

- (无)

---

## Release_1.2.1_20201126_build_A

### 功能构建

- (无)

### Bug 修复

- 修复了 PhoenixPresetLookupProcessor.providePresetCount 方法处理含有排序的 QueryInfo 时生成错误 SQL 语句的错误。

### 功能移除

- (无)

---

## Release_1.2.0_20201110_build_B

### 功能构建

- 移除 README.md 中关于 OptionalLookupService 的成熟度说明。

### Bug 修复

- 修复 BehaviorAnalyse AOP 日志输出接口/父类类名而不是直接类名的 bug。

### 功能移除

- (无)

---

## Release_1.2.0_20201110_build_A

### 功能构建

- 对项目中的单级日志相关功能提供了框架级别的功能更新。
- BehaviorAnalyse AOP 功能增强。
- 优化 com.dwarfeng.subgrade.sdk.jdbc.SQLAndParameter 的代码结构。
- 大幅调整 com.dwarfeng.subgrade.sdk.jdbc 包的结构。
  - 这将导致该项目与旧版本的直接不兼容。
- 大幅调整数据库处理器机制。
  - 这将导致该项目与旧版本的直接不兼容。

### Bug 修复

- 修复 Friendly AOP 注解使用在接口/父类上时抛出异常的 bug。
- 修复 LoginRequired AOP 注解使用在接口/父类上时抛出异常的 bug。
- 修复 PermissionRequired AOP 注解使用在接口/父类上时抛出异常的 bug。

### 功能移除

- ~~移除已经过时的接口 com.dwarfeng.subgrade.stack.dao.ReadOnlyBatchDao。~~
  - 该接口已经过时很久，删除不会导致新项目的不兼容。
- ~~移除查询服务 com.dwarfeng.subgrade.stack.service.OptionalLookupService。~~
  - 预设查询接口 com.dwarfeng.subgrade.stack.service.PresetLookupService 能够完全实现功能，不需要该接口额外提供功能支持。
  - 迄今为止所有的项目均无该接口依赖，删除不会导致兼容性问题。

---

## Release_1.1.8_20201104_build_A

### 功能构建

- 为 FastJsonKafkaDeserializer.clazz 增加 get 和 set 方法。
- 设置 LoginFailedException.loginId 字段为 final 字段。
- 新增 com.dwarfeng.subgrade.stack.exception.DaoException 的子类，与异常代码对应。
  - com.dwarfeng.subgrade.stack.exception.EntityExistedException
  - com.dwarfeng.subgrade.stack.exception.EntityNotExistException

### Bug 修复

- 修复 CrudService 实现类的 bug。
  - 修复 com.dwarfeng.subgrade.stack.service.CrudService.get 部分实现当实体不存在时抛出意料之外的异常的 bug。

### 功能移除

- (无)

---

## Release_1.1.7_20200916_build_A

### 功能构建

- 去除代码中的部分警告。

### Bug 修复

- 修复以下对象插入主键为 null 的元素时行为异常的 bug。
  - com.dwarfeng.subgrade.impl.service.DaoOnlyCrudService
  - com.dwarfeng.subgrade.impl.service.DaoOnlyBatchCrudService
  - com.dwarfeng.subgrade.impl.service.GeneralCrudService
  - com.dwarfeng.subgrade.impl.service.GeneralBatchCrudService

### 功能移除

- (无)

---

## Release_1.1.6_20200912_build_A

### 功能构建

- 添加主键抓取器。
  - com.dwarfeng.subgrade.impl.bean.key.TimeBasedSerialCodeKeyFetcher

### Bug 修复

- (无)

### 功能移除

- (无)

---

## Release_1.1.5_20200910_build_A

### 功能构建

- 增强枚举的序列化能力。
  - com.dwarfeng.subgrade.sdk.enumeration.IndexedEnum
  - com.dwarfeng.subgrade.sdk.enumeration.LabeledEnum
  - com.dwarfeng.subgrade.sdk.hibernate.converter.IndexedEnumConverter
  - com.dwarfeng.subgrade.sdk.fastjson.serialize.LabeledEnumCodec

### Bug 修复

- (无)

### 功能移除

- (无)

---

## Release_1.1.4_20200904_build_A

### 功能构建

- 升级log4j2依赖版本至2.13.3。
- 规范subgrade-impl模块pom.xml引用坐标格式。
- 新增文本格式化转换器。
  - com.dwarfeng.subgrade.sdk.redis.formatter.IntegerIdStringKeyFormatter

### Bug 修复

- (无)

### 功能移除

- (无)

---

## Release_1.1.3_20200807_build_A

### 功能构建

- 添加主键抓取器。
  - com.dwarfeng.subgrade.impl.bean.key.NullValueKeyFetcher
  - com.dwarfeng.subgrade.impl.bean.key.DenseUuidStringKeyFetcher
  - com.dwarfeng.subgrade.impl.bean.key.UuidStringKeyFetcher
- 优化部分文档注释。
- 添加工具类方法。
  - com.dwarfeng.subgrade.sdk.exception.ServiceExceptionHelper.mapAndThrow

### Bug 修复

- 修复 GeneralBatchCrudService 在批量插入时行为不正常的 bug。

### 功能移除

- (无)

---

## Release_1.1.2_20200714_build_A

### 功能构建

- 修改部分代码中变量名称的拼写错误。

### Bug 修复

- 修复在某些情况下 com.dwarfeng.subgrade.impl.exception.MapServiceExceptionMapper 映射错误的 bug。

### 功能移除

- (无)

---

## Release_1.1.1_20200630_build_B

### 功能构建

- 升级commons-beanutils依赖版本至1.9.4。

### Bug 修复

- (无)

### 功能移除

- (无)

---

## Release_1.1.1_20200630_build_A

### 功能构建

- com.dwarfeng.subgrade.sdk.jdbc 包结构调整。
  - **该调整将会导致此版本与 Release_1.1.0_20200611_build_A 不兼容。**
- 更改 README.md 的`推荐使用版本`条目，并且移除 git 中 Release_1.1.0_20200611_build_A 的版本标签。
- 添加 MyBatis 数据访问层支持。
  - com.dwarfeng.subgrade.impl.dao.MybatisBaseDao
  - com.dwarfeng.subgrade.impl.dao.MybatisBatchBaseDao
  - com.dwarfeng.subgrade.impl.dao.MybatisRelationDao
  - com.dwarfeng.subgrade.impl.dao.MybatisBatchRelationDao
  - com.dwarfeng.subgrade.impl.dao.MybatisBatchWriteDao
  - com.dwarfeng.subgrade.impl.dao.MybatisBatchWriteDao
  - com.dwarfeng.subgrade.impl.dao.MybatisEntireLookupDao
  - com.dwarfeng.subgrade.impl.dao.MybatisPresetLookupDao
- 对 JDBC 数据访问层支持进行大规模修改。
  - **该调整将会导致此版本与 Release_1.1.0_20200611_build_A 不兼容。**
  - com.dwarfeng.subgrade.impl.dao.JdbcBaseDao
  - com.dwarfeng.subgrade.impl.dao.JdbcBatchBaseDao
  - com.dwarfeng.subgrade.impl.dao.JdbcRelationDao
  - com.dwarfeng.subgrade.impl.dao.JdbcBatchRelationDao
  - com.dwarfeng.subgrade.impl.dao.JdbcBatchWriteDao
  - com.dwarfeng.subgrade.impl.dao.JdbcBatchWriteDao
  - com.dwarfeng.subgrade.impl.dao.JdbcEntireLookupDao
  - com.dwarfeng.subgrade.impl.dao.JdbcPresetLookupDao
- 重写 com.dwarfeng.subgrade.sdk.database.definition 包中的内容。
  - com.dwarfeng.subgrade.sdk.database.definition.ColumnDefinition
  - com.dwarfeng.subgrade.sdk.database.definition.ColumnTypes
  - com.dwarfeng.subgrade.sdk.database.definition.OptionalDefinition
  - com.dwarfeng.subgrade.sdk.database.definition.TableDefinition
- 开发 com.dwarfeng.subgrade.sdk.database.executor 实现数据库语句执行接口。
- 开发 com.dwarfeng.subgrade.sdk.database.ddl 实现部分数据库的建表执行任务。
  - com.dwarfeng.subgrade.sdk.database.ddl.PhoenixCreateTableDatabaseTask

### Bug 修复

- 修复使用jdbc框架进行 Apache Phoenix 分页查询时生成错误的 SQL 语句的bug。

### 功能移除

- (无)

---

## Release_1.1.0_20200611_build_A

### 功能构建

- 编写 JDBC 基础数据访问层的 Apache Phoenix 实现。
  - com.dwarfeng.subgrade.sdk.jdbc.mapper.PhoenixEntireLookupMapper
  - com.dwarfeng.subgrade.sdk.jdbc.template.PhoenixEntireLookupTemplate
- 编写 JDBC 整体查询数据访问层，旨在通过 JDBC 用原生 SQL直接访问数据源。
  - com.dwarfeng.subgrade.impl.dao.JdbcEntireLookupDao
  - com.dwarfeng.subgrade.sdk.jdbc.mapper.EntireLookupMapper
  - com.dwarfeng.subgrade.sdk.jdbc.template.EntireLookupTemplate
  - com.dwarfeng.subgrade.sdk.jdbc.template.GeneralEntireLookupTemplate
- 编写 JDBC 预设查询数据访问层的 Apache Phoenix 实现。
  - com.dwarfeng.subgrade.sdk.jdbc.template.PhoenixPresetLookupTemplate
- 编写 JDBC 预设查询数据访问层，旨在通过 JDBC 用原生 SQL 进行预设查询。
  - com.dwarfeng.subgrade.impl.dao.JdbcPresetLookupDao
  - com.dwarfeng.subgrade.sdk.jdbc.mapper.PresetLookupMapper
  - com.dwarfeng.subgrade.sdk.jdbc.template.PresetLookupTemplate
  - com.dwarfeng.subgrade.sdk.jdbc.template.GeneralPresetLookupTemplate
- 编写 JDBC 基础数据访问层的 Apache Phoenix 实现。
  - com.dwarfeng.subgrade.sdk.jdbc.template.PhoenixCreateTableTemplate
  - com.dwarfeng.subgrade.sdk.jdbc.template.PhoenixCrudTemplate
  - com.dwarfeng.subgrade.sdk.jdbc.database.PhoenixConstants
  - com.dwarfeng.subgrade.sdk.jdbc.mapper.PresetLookupMapper
- 编写 JDBC 基础数据访问层以及 JDBC 批处理基础数据访问层，旨在通过 JDBC 用原生 SQL直接访问数据源。
  - com.dwarfeng.subgrade.sdk.jdbc.template.CreateTableTemplate
  - com.dwarfeng.subgrade.sdk.jdbc.template.CrudTemplate
  - com.dwarfeng.subgrade.sdk.jdbc.template.GeneralCreateTableTemplate
  - com.dwarfeng.subgrade.sdk.jdbc.template.GeneralCrudTemplate
  - com.dwarfeng.subgrade.sdk.jdbc.database.TableDefinitionUtil
  - com.dwarfeng.subgrade.sdk.jdbc.database.TableDefinition
  - com.dwarfeng.subgrade.sdk.jdbc.mapper.ResultMapper
  - com.dwarfeng.subgrade.sdk.jdbc.mapper.CrudMapper
  - com.dwarfeng.subgrade.impl.dao.JdbcBaseDao
  - com.dwarfeng.subgrade.impl.dao.JdbcBatchBaseDao
- 编写可选查询服务，旨在实现无限制的自由查询。
  - com.dwarfeng.subgrade.stack.dao.OptionalLookupDao
  - com.dwarfeng.subgrade.stack.service.OptionalLookupService
  - com.dwarfeng.subgrade.impl.dao.HibernateOptionalLookupDao
  - com.dwarfeng.subgrade.impl.service.DaoOnlyOptionalLookupService
  - com.dwarfeng.subgrade.sdk.hibernate.criteria.OptionalCriteriaMaker
- 修改 DaoOnlyPresetLookupService 类中错误的文档注释。
- 编写写入服务，旨在更高效的处理连续写入新数据的工况。
  - com.dwarfeng.subgrade.stack.service.WriteService
  - com.dwarfeng.subgrade.stack.service.BatchWriteService
  - com.dwarfeng.subgrade.stack.dao.WriteDao
  - com.dwarfeng.subgrade.stack.dao.BatchWriteDao
  - com.dwarfeng.subgrade.impl.dao.HibernateWriteDao
  - com.dwarfeng.subgrade.impl.dao.HibernateBatchWriteDao
  - com.dwarfeng.subgrade.impl.service.DaoOnlyWriteService
  - com.dwarfeng.subgrade.impl.service.DaoOnlyBatchWriteService
  - com.dwarfeng.subgrade.impl.service.JdbcBatchWriteDao
  - com.dwarfeng.subgrade.impl.service.JdbcWriteDao
- 过时接口 com.dwarfeng.subgrade.stack.dao.ReadOnlyBatchDao。
  - 使用com.dwarfeng.subgrade.stack.dao.BatchReadOnlyDao 替代
  - 这可能会引发轻微的不兼容。

### Bug 修复

- 修改在常规配置下 ServiceExceptionCodes.UNDEFINE 的错误代码为 0 的bug。

### 功能移除

- ~~删除（重命名）com.dwarfeng.subgrade.stack.dao.ConstraintLookupDao~~

  该在创建之时已经做出注释警告用户不要使用，因此该类的删除不会造成过度的不兼容。

---

## Release_1.0.2_20200526_build_A

### 功能构建

- 添加友好性AOP及其注解与实现方法。
  - com.dwarfeng.subgrade.sdk.interceptor.friendly.ControllerPagingFriendlyParamAopManager
  - com.dwarfeng.subgrade.sdk.interceptor.friendly.ControllerPagingFriendlyResultAopManager
  - com.dwarfeng.subgrade.sdk.interceptor.friendly.Friendly
  - com.dwarfeng.subgrade.sdk.interceptor.friendly.FriendlyAdvisor
  - com.dwarfeng.subgrade.sdk.interceptor.friendly.FriendlyList
  - com.dwarfeng.subgrade.sdk.interceptor.friendly.FriendlyParamAopManager
  - com.dwarfeng.subgrade.sdk.interceptor.friendly.FriendlyResultAopManager

### Bug 修复

- (无)

### 功能移除

- (无)

---

## Release_1.0.1_20200503_build_A

### 功能构建

- (无)

### Bug 修复

- 修正部分LookupDao分页行为不正确的bug。
  - com.dwarfeng.subgrade.impl.dao.MemoryEntireLookupDao
  - com.dwarfeng.subgrade.impl.dao.MemoryPresetLookupDao
  - com.dwarfeng.subgrade.impl.dao.RedisEntireLookupDao
  - com.dwarfeng.subgrade.impl.dao.RedisPresetLookupDao

### 功能移除

- (无)

---

## Release_1.0.0_20200426_build_A

### 功能构建

- 添加主键类型com.dwarfeng.subgrade.stack.bean.key.ByteIdKey。

### Bug 修复

- 修复HibernateBatchBaseDao.batchGet不抛出DaoException的代码问题。

### 功能移除

- (无)

---

## Beta_0.3.2_20200412_build_D

### 功能构建

- (无)

### Bug 修复

- 修复RedisEntireLookupDao.lookup方法数组越界的bug。
- 修复RedisPresetLookupDao.lookup方法数组越界的bug。
- 修复RedisEntireLookupDao.lookup方法返回结果不正确的bug。
- 修复RedisPresetLookupDao.lookup方法返回结果不正确的bug。

### 功能移除

- (无)

---

## Beta_0.3.2_20200411_build_C

### 功能构建

- 细化 HttpLoginRequiredAopManager 的异常处理过程。

### Bug 修复

- (无)

### 功能移除

- (无)

---

## Beta_0.3.2_20200326_build_B

### 功能构建

- 更新dutil依赖至beta-0.2.1.a。

### Bug 修复

- (无)

### 功能移除

- (无)

---

## Beta_0.3.2_20200319_build_A

### 功能构建

- 优化HibernateBaseDao的写入效率。

### Bug 修复

- (无)

### 功能移除

- (无)

---

## Beta_0.3.1_20200307_build_C

### 功能构建

- (无)

### Bug 修复

- 修复了CrudService.insertOrUpdate方法处理主键为null的元素时的异常表现bug。

### 功能移除

- (无)

---

## Beta_0.3.1_20200303_build_B

### 功能构建

- (无)

### Bug 修复

- 解决了HibernateBaseDao.update方法不正常抛出异常的bug。

### 功能移除

- (无)

---

## Beta_0.3.1_20200302_build_A

### 功能构建

- (无)

### Bug 修复

- 将//noinspection替换为@SupressWarning

### 功能移除

- (无)

---

## Beta_0.3.0_20200301_build_A

### 功能构建

- 强化LoginRequiredAdvisor增强。
- 实现BindingCheck增强。
- Key实体的WebInput实现。

### Bug 修复

- 修复了RedisBaseDao以及RedisBatchBaseDao的方法执行问题。

### 功能移除

- ~~删除LoginPermRequiredAdvisor增强。~~

---

## Beta_0.2.5_20200225_build_A

### 功能构建

- (无)

### Bug 修复

- 解决依赖冲突。
- com.dwarfeng.subgrade.sdk.bean.key中的键对象实现接口从Bean更改为Key。

### 功能移除

- (无)

---

## Beta_0.2.4_20200222_build_A

### 功能构建

- 新增主键实体IntegerIdKey。
- 添加KeyFetcher的UUID以及DenseUUID实现。
- 添加 PagingUtil.subList方法。
- 完善RelationDao以及RelationService及其实现。
- 添加ResponseDataUtil.bad方法。
- 添加FastJsonResponseData。

### Bug 修复

- 修正FastJsonStringIdKey.stringId不正确的JSON注解。
- 修正PagingUtil.pagedData中部分参数返回数据异常的bug。

### 功能移除

- ~~移除PagingUtil中的indexBound方法。~~

---

## Beta_0.2.3_20200219_build_B

### 功能构建

- (无)

### Bug 修复

- 修正FastJsonPagedData中错误的JSON字段。

### 功能移除

- (无)

---

## Beta_0.2.3_20200218_build_A

### 功能构建

- 编写与Json相关的PagedData类。
  - FastJsonPagedData
  - JSFixedFastJsonPagedData
- 为FastJson相关的对象添加 of 静态方法。

### Bug 修复

- 修正PagedData中的字段拼写问题。

### 功能移除

- (无)

---

## Beta_0.2.2_20200216_build_B

### 功能构建

- (无)

### Bug 修复

- 修复PageUtil.pagedData方法返回总页数不正确的bug。

### 功能移除

- (无)

---

## Beta_0.2.2_20200216_build_A

### 功能构建

- 优化ResponseDataUtil.bad方法，使其入口参数能接受T的超类。
- PagingUtil中增加PageData的转换方法。

### Bug 修复

- (无)

### 功能移除

- ~~删除与PresetDelete有关的数据访问层以及服务。~~

---

## Beta_0.2.1_20200215_build_A

### 功能构建

- 工程全目标实现，从此版本记录变更日志。

### Bug 修复

- (无)

### 功能移除

- (无)
