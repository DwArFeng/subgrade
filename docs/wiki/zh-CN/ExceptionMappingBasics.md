# Exception Mapping Basics - 异常映射基础

## 综述

在实际的开发实践中，不同系统、模块、方法抛出的异常都不一样。这些结构各异的异常通常不容易以直接以一种统一的形式进行传递。
本项目的异常映射机制旨在提供一个统一的异常，这个异常可序列化，具有标准的异常描述字段，
可以轻易地通过 java 的序列化机制进行传递，也十分容易与前端或其它语言进行对接，同时提供一个映射方法，
将不同模块的异常按照约定的规则映射至这个统一的异常中，进而实现异常的统一管控。

## 异常定义

本项目中，应该使用 `com.dwarfeng.subgrade.basic.stack.exception.ServiceException` 在服务之间传递异常。

ServiceException 具有一个 `Code` 字段：

```java
public class ServiceException extends Exception {

    //...

    private Code code;

    //...
}
```

`Code` 对象具有 `code` 和 `tip` 字段：

```java
public static class Code implements Serializable {

    private int code;
    private String tip;
}
```

其中，`code` 表示异常的代码，不同的代码由不同的异常映射得到，因此，运维人员可以基于异常代码迅速判断异常的类型；
`tip` 表示异常的提示，一般以简短的文本描述当前代码的描述，可以起到助记符的作用。

## 接口定义

### 异常映射器接口

在本项目中，使用 `com.dwarfeng.subgrade.basic.stack.exception.ServiceExceptionMapper` 接口对异常进行统一映射。

```java
public interface ServiceExceptionMapper {

    /**
     * 将指定的异常映射为服务异常。
     *
     * @param e 指定的异常。
     * @return 服务异常。
     */
    ServiceException map(Exception e);
}
```

## 工作方式

异常映射器接口可以将任意的异常映射为服务异常，这使得服务之间的通信可以通过异常映射机制进行异常的统一管控，具体操作如下：

服务之间通信时，被调用的服务首先调用服务内部的处理器，进行业务处理，并捕获处理器抛出的任何异常。一旦捕获了异常之后，
便交付异常映射器进行异常的统一映射，将处理器内部的各种异常统一转化为服务异常。

由于 `ServiceException` 中的 `Code` 对象实现了序列化接口，因此，整个异常对象可以进行序列化，
通过 RPC 协议在 java 服务之间传递，甚至是跨语言平台传递。

以服务实现举例：

```java

@org.springframework.stereotype.Service
public class Service {

    @Autowired
    private Handler handler;
    @Autowired
    private ServiceExceptionMapper sem;

    public User getUser(String id) throws ServiceException{
        try {
            return handler.getUser(id);
        } catch (Exception e) {
            // 直接将 Exception 映射为 ServiceException，并抛出。
            throw ServiceExceptionHelper.logParse("发生异常", LogLevel.WARN, e, sem);
        }
    }
}
```

对于前端系统或客户端页面而言，通常不希望将异常直接抛出至目标模块，这样可能会造成后端服务的调用栈的泄露。
此时，可以将异常中的 `Code` 对象拿出，作为 `Dto` 实体的一个字段进行返回，避免了上述的不期望的行为。

以 Controller 举例：

```java
@RestController
@RequestMapping("/api/v1/settingrepo")
public class SettingNodeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SettingNodeController.class);

    @Autowired
    private SettingNodeResponseService service;
    @Autowired
    private ServiceExceptionMapper sem;

    @GetMapping("/setting-node/{id}/exists")
    public FastJsonResponseData<Boolean> exists(HttpServletRequest request, @PathVariable("id") String id) {
        try {
            boolean exists = service.exists(new StringIdKey(id));
            return FastJsonResponseData.of(ResponseDataUtil.good(exists));
        } catch (Exception e) {
            LOGGER.warn("Controller 异常, 信息如下: ", e);
            return FastJsonResponseData.of(ResponseDataUtil.bad(e, sem));
        }
    }
}
```

前端调用上述 Controller，如果成功，返回的格式为：

```json
{
  "data": true,
  "meta": {
    "code": 0,
    "message": "good"
  }
}
```

如果失败，返回的格式为：

```json
{
  "data": null,
  "meta": {
    "code": 12450,
    "message": "exception message here"
  }
}
```

## 默认实现

### 异常映射器接口

异常映射器的默认实现为 `com.dwarfeng.subgrade.basic.impl.exception.MapServiceExceptionMapper`。

该实现的内部维护了一个 `Map<Class<? extends Exception>, Supplier<ServiceException.Code>>`，将一般的异常与异常代码供应器进行了映射。
同时，该映射器处理了异常的继承关系，对于未知的异常，会尝试迭代查找其父类、祖父类，直到命中异常映射或没有父类。 如果一个异常没有被该实现中的
`Map` 维护，它将由缺省的异常代码供应器生成 `Code`。

## 异常偏移

异常偏移机制用来解决多个服务之间异常代号的冲突问题，以如下两个服务举例：

用户微服务:

| code | tip             |
|-----:|:----------------|
|    1 | invalid user id |
|    2 | user disabled   |

权限微服务:

| code | tip                   |
|-----:|:----------------------|
|    1 | permission not exists |
|    2 | database disconnected |

以上两个微服务，在设计的时候，都从 1 开始，对自身服务内部的异常进行编号，这导致了客户端调用用户微服务与权限微服务时，
可能会获得异常代相同，但含义不同的异常描述。

因此，设计异常编码时，通常会使用一个偏移值与内部编号相加。异常的内部编号仍然从 1 开始编号；
而偏移值作为配置项，可以通过配置文件修改。这样，如果一个系统使用了多个服务，则可以为不同的服务指定不同的偏移值，消除编号的重复。

使用偏移值后，用户微服务和权限微服务如下所示：

用户微服务

| offset | code | final code | tip             |
|-------:|-----:|-----------:|:----------------|
|    100 |    1 |        101 | invalid user id |
|    100 |    2 |        102 | user disabled   |

权限微服务:

| offset | code | final code | tip                   |
|-------:|-----:|-----------:|:----------------------|
|    200 |    1 |        201 | permission not exists |
|    200 |    2 |        202 | database disconnected |

## 默认异常编码

Subgrade 2.x 按模块分配默认异常编码。下表为各模块默认提示与编码：

| 模块       | code | tip                  | explain                            |
|:-----------|-----:|:---------------------|:-----------------------------------|
| basic      |    1 | 未定义错误           | MapServiceExceptionMapper 的缺省值 |
| basic      |   50 | 输入输出操作失败     | IO 处理失败                        |
| basic      |   60 | 过程执行失败         | 过程执行失败                       |
| basic      |   70 | 处理器执行失败       | 处理器执行失败                     |
| basic      |  110 | 请求的能力尚未实现   | 功能尚未实现                       |
| basic      |  120 | 数值生成失败         | 数据或对象生成失败                 |
| basic      |  130 | 分页操作失败         | 分页处理失败                       |
| data       | 1020 | 缓存操作失败         | 缓存操作失败                       |
| data       | 1030 | 数据访问操作失败     | DAO 操作失败                       |
| data       | 1031 | 实体已经存在         | 创建已存在的实体                   |
| data       | 1032 | 实体不存在           | 操作不存在的实体                   |
| data       | 1100 | 数据库操作失败       | 数据库操作失败                     |
| expression | 2010 | 表达式解析失败       | 表达式解析失败                     |
| aop        | 3010 | 拦截处理失败         | AOP 拦截处理失败                   |
| web        | 4040 | 参数验证失败         | Web 参数验证失败                   |
| web        | 4080 | 权限被拒绝           | 访问权限检查失败                   |
| web        | 4090 | 登录失败             | 登录或身份校验失败                 |
| lifecycle  | 5010 | 生命周期操作失败     | 生命周期操作失败                   |
| cache      | 6010 | 本地缓存操作失败     | 缓存操作失败                       |
| lock       | 7010 | 分布式锁操作失败     | 分布式锁操作失败                   |
| kafka      | 8010 | 序列化或反序列化失败 | Kafka 消息转换失败                 |
