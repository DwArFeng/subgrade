# ChangeLog

## Release_2.1.1_20260921_build_A

### 功能构建

- Wiki 编写。
  - docs/wiki/zh-CN/BasicComponentsBasics.md。

- 规范 AOP 与 Web 拦截器实现类的包路径。
  - 将审计记录、请求属性、参数绑定、登录及权限拦截器实现迁移至 `impl` 包。
  - 将原 `sdk` 包内对应实现标记为过时并保留兼容入口。

- 优化项目的国际化机制以及消息键规范。

### Bug 修复

- (无)

### 功能移除

- 移除 `SpecificClass` 校验消息的国际化资源，改为使用注解内置的默认英文消息。

---

## Release_2.1.0_20260910_build_A

### 功能构建

- Wiki 编写。
  - docs/wiki/zh-CN/AuditRecord.md。

- Wiki 更新。
  - docs/wiki/zh-CN/Contents.md。

- 优化 JPMS 依赖读取边界。
  - subgrade-aop。
  - subgrade-basic。
  - subgrade-cache。
  - subgrade-data。
  - subgrade-expression。
  - subgrade-kafka。
  - subgrade-lifecycle。
  - subgrade-lock。
  - subgrade-web。

- 项目结构优化。
  - 将 `SystemPropertyConstants` 从 `sdk.configuration` 迁移至 `sdk.util`。
  - 将 `MapStructBeanTransformer` 及其依赖迁移至 `subgrade-basic`。

- 新增审计记录拦截能力。
  - 新增审计记录注解拦截、上下文构建与默认审计属性生成。
  - 新增贡献器式审计属性构建器，以及请求、结果、异常属性贡献器。
  - 新增审计记录处理器接口，用于统一提交审计信息。

- 扩展 HQL 谓词子句构建能力。
  - 扩展 `WhereHelper` 谓词子句构建能力。

- 新增 HQL 谓词子句。
  - com.dwarfeng.subgrade.data.sdk.hibernate.hql.clause.EqExpressionPredicateClause。
  - com.dwarfeng.subgrade.data.sdk.hibernate.hql.clause.ExistsPredicateClause。

- 优化 JPMS 依赖读取边界。
  - subgrade-basic。
  - subgrade-data。
  - subgrade-web。

- `subgrade-web` 子模块类优化注释、文档注释格式、代码换行格式。
  - com.dwarfeng.subgrade.web.sdk.bean.key.WebInputStringIdKey。
  - com.dwarfeng.subgrade.web.sdk.bean.key.WebInputLongIdKey。
  - com.dwarfeng.subgrade.web.sdk.bean.key.WebInputIntegerIdKey。
  - com.dwarfeng.subgrade.web.sdk.bean.key.WebInputDenseUuidKey。
  - com.dwarfeng.subgrade.web.sdk.bean.key.WebInputByteIdKey。
  - com.dwarfeng.subgrade.web.sdk.bean.key.JSFixedFastJsonLongIdKey。
  - com.dwarfeng.subgrade.web.sdk.bean.key.FastJsonUuidKey。
  - com.dwarfeng.subgrade.web.sdk.bean.key.FastJsonStringIdKey。
  - com.dwarfeng.subgrade.web.sdk.bean.key.FastJsonLongIdKey。
  - com.dwarfeng.subgrade.web.sdk.bean.key.FastJsonIntegerIdKey。
  - com.dwarfeng.subgrade.web.sdk.bean.key.FastJsonDenseUuidKey。
  - com.dwarfeng.subgrade.web.sdk.bean.key.FastJsonByteIdKey。
  - com.dwarfeng.subgrade.web.sdk.bean.dto.JSFixedFastJsonPagedData。
  - com.dwarfeng.subgrade.web.sdk.bean.dto.FastJsonResponseData。
  - com.dwarfeng.subgrade.web.sdk.bean.dto.FastJsonPagedData。
  - com.dwarfeng.subgrade.web.sdk.bean.key.WebInputUuidKey。

- `subgrade-data` 子模块类优化注释、文档注释格式、代码换行格式。
  - com.dwarfeng.subgrade.data.impl.dao.hibernate.HibernateAccelerableHqlPresetLookupDao。
  - com.dwarfeng.subgrade.data.impl.dao.hibernate.HibernateAccelerablePresetLookupDao。
  - com.dwarfeng.subgrade.data.impl.dao.hibernate.HibernateBaseDao。
  - com.dwarfeng.subgrade.data.impl.dao.hibernate.HibernateBatchBaseDao。
  - com.dwarfeng.subgrade.data.impl.dao.hibernate.HibernateBatchRelationDao。
  - com.dwarfeng.subgrade.data.impl.dao.hibernate.HibernateBatchWriteDao。
  - com.dwarfeng.subgrade.data.impl.dao.hibernate.HibernateDaoFactory。
  - com.dwarfeng.subgrade.data.impl.dao.hibernate.HibernateEntireLookupDao。
  - com.dwarfeng.subgrade.data.impl.dao.hibernate.HibernateHqlPresetLookupDao。
  - com.dwarfeng.subgrade.data.impl.dao.hibernate.HibernatePresetLookupDao。
  - com.dwarfeng.subgrade.data.impl.dao.hibernate.HibernateRelationDao。
  - com.dwarfeng.subgrade.data.impl.dao.hibernate.HibernateSingleObjectDao。
  - com.dwarfeng.subgrade.data.impl.dao.hibernate.HibernateWriteDao。
  - com.dwarfeng.subgrade.data.sdk.hibernate.hql.HavingHelper。
  - com.dwarfeng.subgrade.data.sdk.hibernate.operation.HibernateOperations。

- 优化文件格式。
  - 优化 `pom.xml` 文件的格式。

### Bug 修复

- 修复 Hibernate 删除实体时错误合并关联对象图的问题。
  - 删除实体时不再执行 `merge`。

- 修复部分 HQL 子句中已知的问题。
  - 修复 OrPredicateClause 在无谓词子句时错误匹配全部数据的问题。

### 功能移除

- (无)

---

## Release_2.0.1_20260826_build_A

### 功能构建

- Wiki 编写。
  - docs/wiki/zh-CN/UpgradeGuide2.0.0.md。

- Wiki 更新。
  - docs/wiki/zh-CN/VersionBlacklist.md。

### Bug 修复

- 修复部分类错误使用 JSpecify 注解的问题。
  - com.dwarfeng.subgrade.data.impl.dao.hibernate.HibernateAccelerableHqlPresetLookupDao。
  - com.dwarfeng.subgrade.data.sdk.redis.serialize.FastJsonRedisSerializer。

### 功能移除

- (无)

---

## Release_2.0.0_20260818_build_A

### 功能构建

- 更新 README.md。

- Wiki 更新。
  - docs/wiki/zh-CN/BehaviorAnalyse.md。
  - docs/wiki/zh-CN/CacheBasics.md。
  - docs/wiki/zh-CN/CuratorDistributedLockHandlerBasics.md。
  - docs/wiki/zh-CN/DaoBasics.md。
  - docs/wiki/zh-CN/DataAccessBasics.md。
  - docs/wiki/zh-CN/ExceptionMappingBasics.md。
  - docs/wiki/zh-CN/ExpirableLocalCacheHandlerBasics.md。
  - docs/wiki/zh-CN/GeneralLocalCacheHandlerBasics.md。
  - docs/wiki/zh-CN/GeneralOnlineHandlerBasics.md。
  - docs/wiki/zh-CN/GeneralStartableHandlerBasics.md。
  - docs/wiki/zh-CN/InstallBySourceCode.md。
  - docs/wiki/zh-CN/Introduction.md。
  - docs/wiki/zh-CN/LocalCacheBasics.md。
  - docs/wiki/zh-CN/ReentrantBusinessLogicBasics.md。
  - docs/wiki/zh-CN/UseWithMaven.md。

- 升级 JDK 版本至 25。

### Bug 修复

- (无)

### 功能移除

- (无)

---

## 更早的版本

[View all changelogs](./changelogs)
