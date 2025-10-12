# Version Blacklist - 版本黑名单

## 说明

本项目的版本黑名单，列出了本项目的版本黑名单，请注意避免使用这些版本。

列入黑名单的版本，可能是因为以下原因：

- 该版本存在严重的 Bug，可能会导致核心功能无法正常使用。
- 该版本存在严重的 Bug，可能会导致数据丢失、数据错误等严重后果。

## 版本黑名单

| 版本号     | 原因           |
|---------|--------------|
| 1.5.5.a | 核心类构造器方法存在缺陷 |
| 1.5.4.a | 核心类构造器方法存在缺陷 |
| 1.4.8.a | 核心类存在严重 bug  |

## 详细原因

### 1.5.5.a

详细版本号: Release_1.5.5_20240730_build_A。

原因： 部分类中缺失构造器方法。

- 部分类中缺失构造器方法。
  - com.dwarfeng.subgrade.impl.service.DaoOnlyBatchWriteService。

### 1.5.4.a

详细版本号: Release_1.5.4_20240725_build_A。

原因： 部分类中缺失构造器方法。

- 部分类中缺失构造器方法。
  - com.dwarfeng.subgrade.impl.service.DaoOnlyBatchWriteService。

### 1.4.8.a

详细版本号: Release_1.4.8_20240123_build_A。

原因： 核心类存在严重 bug。

- 部分数据访问层存在严重 bug，导致其数据查询功能出现数据错误。
  - com.dwarfeng.subgrade.impl.dao.HibernatePresetLookupDao。
