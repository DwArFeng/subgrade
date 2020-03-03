# Subgrade

此项目是作者的个人工具库，包含多种能大幅提高发开速度的通用工具库。

## 包含的工具

* 通用项目接口定义。
  * 服务接口 `Service`
  * 数据访问层接口 `Dao`
  * 缓存接口 `Cache`
  * 处理接口 `Handler`
  * 缓存接口 `Cache`
  * Bean接口 `Bean`
  * 实体接口 `Entity`
  * 主键接口 `Key`
  * 数据传输对象接口 `Dto`
  
  上述接口的定义使得一般的新项目完全无需再定义任何新接口。

* 异常处理工具。
  * 通用服务异常 `ServiceException`
  * 通用异常处理机制 `ServiceExceptionMapper`

  通用异常处理机制能将方法执行过程中的任何异常映射到 `ServiceException` 中， `ServiceException` 异常作为异常中间件，
  可方便的转换为 `ResponseData` 或方便的通过 RPC 进行传输。

* 通用数据访问层接口定义以及实现。
* 通用缓存接口定义以及实现。
* Redis, Kafka 的序列化器以及反序列化器。
* 通用服务的接口定义以及实现。
* AOP增强。

## 推荐使用版本

* 对于任何的新项目，使用 **beta-0.3.1.b**以上的版本，此版本将idea的警告注释//noinspection替换为了@SupressWarning，
解决了编译时的报警问题。
* 对于使用Hibernate数据访问层的项目，使用**beta-0.3.1.b**以上的版本，此版本修复了HibernateBaseDao更新实体的问题。
* 对于使用Redis作为数据访问层的项目，使用**beta-0.3.0.a**以上的版本，此版本修复了Redis数据访问层的诸多问题。
* 如果您的项目中涉及到实体的FastJson、WebInput、JSFixedFastJson实现，请使用 **beta-0.3.0.a**以上的版本，
该版本解决了of方法以及toStackBean方法的空值判断问题。
* 任何项目推荐不低于 **beta-0.2.3.a**，过去的版本与该版本不兼容。
