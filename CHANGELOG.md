# ChangeLog

### Release_1.2.2_20210804_build_A

#### 功能构建

- (无)

#### Bug修复

- (无)

#### 功能移除

- (无)

---

### Release_1.2.1_20201126_build_A

#### 功能构建

- (无)

#### Bug修复

- 修复了 PhoenixPresetLookupProcessor.providePresetCount 方法处理含有排序的 QueryInfo 时生成错误 SQL 语句的错误。

#### 功能移除

- (无)

---

### Release_1.2.0_20201110_build_B

#### 功能构建

- 移除 README.md 中关于 OptionalLookupService 的成熟度说明。
  
#### Bug修复

- 修复 BehaviorAnalyse AOP 日志输出接口/父类类名而不是直接类名的 bug。

#### 功能移除

- (无)

---

### Release_1.2.0_20201110_build_A

#### 功能构建

- 对项目中的单级日志相关功能提供了框架级别的功能更新。
- BehaviorAnalyse AOP 功能增强。
- 优化 com.dwarfeng.subgrade.sdk.jdbc.SQLAndParameter 的代码结构。
- 大幅调整 com.dwarfeng.subgrade.sdk.jdbc 包的结构。
  - 这将导致该项目与旧版本的直接不兼容。
- 大幅调整数据库处理器机制。
  - 这将导致该项目与旧版本的直接不兼容。
  
#### Bug修复

- 修复 Friendly AOP 注解使用在接口/父类上时抛出异常的 bug。
- 修复 LoginRequired AOP 注解使用在接口/父类上时抛出异常的 bug。
- 修复 PermissionRequired AOP 注解使用在接口/父类上时抛出异常的 bug。

#### 功能移除

- ~~移除已经过时的接口 com.dwarfeng.subgrade.stack.dao.ReadOnlyBatchDao。~~
  - 该接口已经过时很久，删除不会导致新项目的不兼容。
- ~~移除查询服务 com.dwarfeng.subgrade.stack.service.OptionalLookupService。~~
  - 预设查询接口 com.dwarfeng.subgrade.stack.service.PresetLookupService 能够完全实现功能，不需要该接口额外提供功能支持。
  - 迄今为止所有的项目均无该接口依赖，删除不会导致兼容性问题。

---

### Release_1.1.8_20201104_build_A

#### 功能构建

- 为 FastJsonKafkaDeserializer.clazz 增加 get 和 set 方法。
- 设置 LoginFailedException.loginId 字段为 final 字段。
- 新增 com.dwarfeng.subgrade.stack.exception.DaoException 的子类，与异常代码对应。
  - com.dwarfeng.subgrade.stack.exception.EntityExistedException
  - com.dwarfeng.subgrade.stack.exception.EntityNotExistException

#### Bug修复

- 修复 CrudService 实现类的 bug。
  - 修复 com.dwarfeng.subgrade.stack.service.CrudService.get 部分实现当实体不存在时抛出意料之外的异常的 bug。

#### 功能移除

- (无)

---

### Release_1.1.7_20200916_build_A

#### 功能构建

- 去除代码中的部分警告。

#### Bug修复

- 修复以下对象插入主键为 null 的元素时行为异常的 bug。
  - com.dwarfeng.subgrade.impl.service.DaoOnlyCrudService
  - com.dwarfeng.subgrade.impl.service.DaoOnlyBatchCrudService
  - com.dwarfeng.subgrade.impl.service.GeneralCrudService
  - com.dwarfeng.subgrade.impl.service.GeneralBatchCrudService

#### 功能移除

- (无)

---

### Release_1.1.6_20200912_build_A

#### 功能构建

- 添加主键抓取器。
  - com.dwarfeng.subgrade.impl.bean.key.TimeBasedSerialCodeKeyFetcher

#### Bug修复

- (无)

#### 功能移除

- (无)

---

### Release_1.1.5_20200910_build_A

#### 功能构建

- 增强枚举的序列化能力。
  - com.dwarfeng.subgrade.sdk.enumeration.IndexedEnum
  - com.dwarfeng.subgrade.sdk.enumeration.LabeledEnum
  - com.dwarfeng.subgrade.sdk.hibernate.converter.IndexedEnumConverter
  - com.dwarfeng.subgrade.sdk.fastjson.serialize.LabeledEnumCodec

#### Bug修复

- (无)

#### 功能移除

- (无)

---

### Release_1.1.4_20200904_build_A

#### 功能构建

- 升级log4j2依赖版本至2.13.3。
- 规范subgrade-impl模块pom.xml引用坐标格式。
- 新增文本格式化转换器。
  - com.dwarfeng.subgrade.sdk.redis.formatter.IntegerIdStringKeyFormatter

#### Bug修复

- (无)

#### 功能移除

- (无)

---

### Release_1.1.3_20200807_build_A

#### 功能构建

- 添加主键抓取器。
  - com.dwarfeng.subgrade.impl.bean.key.NullValueKeyFetcher
  - com.dwarfeng.subgrade.impl.bean.key.DenseUuidStringKeyFetcher
  - com.dwarfeng.subgrade.impl.bean.key.UuidStringKeyFetcher
- 优化部分文档注释。
- 添加工具类方法。
  - com.dwarfeng.subgrade.sdk.exception.ServiceExceptionHelper.mapAndThrow

#### Bug修复

- 修复 GeneralBatchCrudService 在批量插入时行为不正常的 bug。

#### 功能移除

- (无)

---

### Release_1.1.2_20200714_build_A

#### 功能构建

- 修改部分代码中变量名称的拼写错误。

#### Bug修复

- 修复在某些情况下 com.dwarfeng.subgrade.impl.exception.MapServiceExceptionMapper 映射错误的 bug。

#### 功能移除

- (无)

---

### Release_1.1.1_20200630_build_B

#### 功能构建

- 升级commons-beanutils依赖版本至1.9.4。

#### Bug修复

- (无)

#### 功能移除

- (无)

---

### Release_1.1.1_20200630_build_A

#### 功能构建

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

#### Bug修复

- 修复使用jdbc框架进行 Apache Phoenix 分页查询时生成错误的 SQL 语句的bug。

#### 功能移除

- (无)

---

### Release_1.1.0_20200611_build_A

#### 功能构建

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

#### Bug修复

- 修改在常规配置下 ServiceExceptionCodes.UNDEFINE 的错误代码为 0 的bug。

#### 功能移除

- ~~删除（重命名）com.dwarfeng.subgrade.stack.dao.ConstraintLookupDao~~

  该在创建之时已经做出注释警告用户不要使用，因此该类的删除不会造成过度的不兼容。

---

### Release_1.0.2_20200526_build_A

#### 功能构建

- 添加友好性AOP及其注解与实现方法。
  - com.dwarfeng.subgrade.sdk.interceptor.friendly.ControllerPagingFriendlyParamAopManager
  - com.dwarfeng.subgrade.sdk.interceptor.friendly.ControllerPagingFriendlyResultAopManager
  - com.dwarfeng.subgrade.sdk.interceptor.friendly.Friendly
  - com.dwarfeng.subgrade.sdk.interceptor.friendly.FriendlyAdvisor
  - com.dwarfeng.subgrade.sdk.interceptor.friendly.FriendlyList
  - com.dwarfeng.subgrade.sdk.interceptor.friendly.FriendlyParamAopManager
  - com.dwarfeng.subgrade.sdk.interceptor.friendly.FriendlyResultAopManager

#### Bug修复

- (无)

#### 功能移除

- (无)

---

### Release_1.0.1_20200503_build_A

#### 功能构建

- (无)

#### Bug修复

- 修正部分LookupDao分页行为不正确的bug。
  - com.dwarfeng.subgrade.impl.dao.MemoryEntireLookupDao
  - com.dwarfeng.subgrade.impl.dao.MemoryPresetLookupDao
  - com.dwarfeng.subgrade.impl.dao.RedisEntireLookupDao
  - com.dwarfeng.subgrade.impl.dao.RedisPresetLookupDao

#### 功能移除

- (无)

---

### Release_1.0.0_20200426_build_A

#### 功能构建

- 添加主键类型com.dwarfeng.subgrade.stack.bean.key.ByteIdKey。

#### Bug修复

- 修复HibernateBatchBaseDao.batchGet不抛出DaoException的代码问题。

#### 功能移除

- (无)

---

### Beta_0.3.2_20200412_build_D

#### 功能构建

- (无)

#### Bug修复

- 修复RedisEntireLookupDao.lookup方法数组越界的bug。
- 修复RedisPresetLookupDao.lookup方法数组越界的bug。
- 修复RedisEntireLookupDao.lookup方法返回结果不正确的bug。
- 修复RedisPresetLookupDao.lookup方法返回结果不正确的bug。

#### 功能移除

- (无)

---

### Beta_0.3.2_20200411_build_C

#### 功能构建

- 细化 HttpLoginRequiredAopManager 的异常处理过程。

#### Bug修复

- (无)

#### 功能移除

- (无)

---

### Beta_0.3.2_20200326_build_B

#### 功能构建

- 更新dutil依赖至beta-0.2.1.a。

#### Bug修复

- (无)

#### 功能移除

- (无)

---

### Beta_0.3.2_20200319_build_A

#### 功能构建

- 优化HibernateBaseDao的写入效率。

#### Bug修复

- (无)

#### 功能移除

- (无)

---

### Beta_0.3.1_20200307_build_C

#### 功能构建

- (无)

#### Bug修复

- 修复了CrudService.insertOrUpdate方法处理主键为null的元素时的异常表现bug。

#### 功能移除

- (无)

---

### Beta_0.3.1_20200303_build_B

#### 功能构建

- (无)

#### Bug修复

- 解决了HibernateBaseDao.update方法不正常抛出异常的bug。

#### 功能移除

- (无)

---

### Beta_0.3.1_20200302_build_A

#### 功能构建

- (无)

#### Bug修复

- 将//noinspection替换为@SupressWarning

#### 功能移除

- (无)

---

### Beta_0.3.0_20200301_build_A

#### 功能构建

- 强化LoginRequiredAdvisor增强。
- 实现BindingCheck增强。
- Key实体的WebInput实现。

#### Bug修复

- 修复了RedisBaseDao以及RedisBatchBaseDao的方法执行问题。

#### 功能移除

- ~~删除LoginPermRequiredAdvisor增强。~~

---

### Beta_0.2.5_20200225_build_A

#### 功能构建

- (无)

#### Bug修复

- 解决依赖冲突。
- com.dwarfeng.subgrade.sdk.bean.key中的键对象实现接口从Bean更改为Key。

#### 功能移除

- (无)

---

### Beta_0.2.4_20200222_build_A

#### 功能构建

- 新增主键实体IntegerIdKey。
- 添加KeyFetcher的UUID以及DenseUUID实现。
- 添加 PagingUtil.subList方法。
- 完善RelationDao以及RelationService及其实现。
- 添加ResponseDataUtil.bad方法。
- 添加FastJsonResponseData。

#### Bug修复

- 修正FastJsonStringIdKey.stringId不正确的JSON注解。
- 修正PagingUtil.pagedData中部分参数返回数据异常的bug。

#### 功能移除

- ~~移除PagingUtil中的indexBound方法。~~

---

### Beta_0.2.3_20200219_build_B

#### 功能构建

- (无)

#### Bug修复

- 修正FastJsonPagedData中错误的JSON字段。

#### 功能移除

- (无)

---

### Beta_0.2.3_20200218_build_A

#### 功能构建

- 编写与Json相关的PagedData类。
  - FastJsonPagedData
  - JSFixedFastJsonPagedData
- 为FastJson相关的对象添加 of 静态方法。

#### Bug修复

- 修正PagedData中的字段拼写问题。

#### 功能移除

- (无)

---

### Beta_0.2.2_20200216_build_B

#### 功能构建

- (无)

#### Bug修复

- 修复PageUtil.pagedData方法返回总页数不正确的bug。

#### 功能移除

- (无)

---

### Beta_0.2.2_20200216_build_A

#### 功能构建

- 优化ResponseDataUtil.bad方法，使其入口参数能接受T的超类。
- PagingUtil中增加PageData的转换方法。

#### Bug修复

- (无)

#### 功能移除

- ~~删除与PresetDelete有关的数据访问层以及服务。~~

---

### Beta_0.2.1_20200215_build_A

#### 功能构建

- 工程全目标实现，从此版本记录变更日志。

#### Bug修复

- (无)

#### 功能移除

- (无)
