# ChangeLog

### Beta_0.3.0_20200226_build_A

#### 功能构建

- 强化LoginRequiredAdvisor增强。
- 实现BindingCheck增强。

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
