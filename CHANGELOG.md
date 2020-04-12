# ChangeLog

### Beta_0.3.2_20200412_build_D

#### 功能构建

- (无)

#### Bug修复

- 修复RedisEntireLookupDao.lookup方法数组越界的bug。
- 修复RedisPresetLookupDao.lookup方法数组越界的bug。

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
