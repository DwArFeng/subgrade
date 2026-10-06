# Basic Components Basics - 基础组件基础

## 综述

基础组件是 Subgrade 公共契约的核心部分，它为实体、主键、数据传输对象、生成器、异常和日志等通用概念提供统一约定。
`subgrade-basic` 模块中的接口不关注具体的数据存储、缓存、Web 或 AOP 实现，而是为其它功能模块提供可以复用的基础类型。

该模块对应的 Maven 构件为 `subgrade-basic`，JPMS 模块名为 `com.dwarfeng.subgrade.basic`。
公共接口主要位于 `com.dwarfeng.subgrade.basic.stack` 包中，便捷工具位于 `com.dwarfeng.subgrade.basic.sdk` 包中，
默认实现位于 `com.dwarfeng.subgrade.basic.impl` 包中。

基础组件与数据访问、缓存和服务实现的关系，见 [Data Access Basics](./DataAccessBasics.md)；
服务异常的统一映射方式，见 [Exception Mapping Basics](./ExceptionMappingBasics.md)。

## 接口定义

### Bean 接口

在本项目中，需要参与统一序列化的基础对象均应实现 `com.dwarfeng.subgrade.basic.stack.bean.Bean` 接口。
`Bean` 是一个标记接口，本身不声明任何业务方法，但继承了 `java.io.Serializable`，其定义如下：

```java
public interface Bean extends Serializable {
}
```

实现 `Bean` 接口意味着该类型具备 Java 序列化能力。`Key`、`Entity`、`NonKeyEntity` 和 `Dto` 等接口都继承了
`Bean`，因此基于这些接口定义的主键、实体和数据传输对象也具有序列化能力。

Bean 之间的转换由 `com.dwarfeng.subgrade.basic.stack.bean.BeanTransformer` 接口描述。
该接口定义了一组相互对应的变换方法，其签名如下：

```java
public interface BeanTransformer<U extends Bean, V extends Bean> {

    V transform(U u);

    U reverseTransform(V v);
}
```

其中，`transform` 将类型 `U` 的对象转换为类型 `V` 的对象，`reverseTransform` 执行相反方向的转换。
转换器只负责对象转换，不负责事务、缓存或数据访问，因此可以在服务、缓存和序列化场景中复用。

### Key 接口及标准主键类型

所有主键类型都必须实现 `com.dwarfeng.subgrade.basic.stack.bean.key.Key` 接口。
`Key` 是一个继承自 `Bean` 的标记接口，其定义如下：

```java
public interface Key extends Bean {
}
```

实现 `Key` 接口的类型应正确重写 `equals()` 和 `hashCode()` 方法，以保证主键在集合、缓存和持久化场景中的比较行为一致。
在本项目中，主键统一按照联合主键处理，即使主键只有一个字段，也视为由单个字段组成的联合主键。关于主键与实体的整体约定，
详见 [Data Access Basics](./DataAccessBasics.md)。

为便于直接使用常见的主键类型，本项目在 `com.dwarfeng.subgrade.basic.stack.bean.key` 包中提供了以下标准主键：

| 主键类名       | 封装字段         | 适用场景                                                         |
|:---------------|:-----------------|:-----------------------------------------------------------------|
| `ByteIdKey`    | `byte`           | 适合只有一个 `byte` 字段作为主键的场景。                         |
| `IntegerIdKey` | `int`            | 适合只有一个 `int` 字段作为主键的场景。                          |
| `LongIdKey`    | `long`           | 适合只有一个 `long` 字段作为主键的场景。                         |
| `StringIdKey`  | `String`         | 适合只有一个普通字符串字段作为主键的场景。                       |
| `UuidKey`      | `String`         | 适合使用标准 UUID 字符串作为主键的场景。                         |
| `DenseUuidKey` | `String`         | 适合使用 Base64 编码压缩后的紧凑 UUID 字符串作为主键的场景。     |

`UuidKey` 与 `DenseUuidKey` 的字段类型都是 `String`，但二者分别面向标准 UUID 文本和紧凑 UUID 文本。
主键类的构造函数可以直接接收对应的字段值，也可以通过默认构造函数创建实例后再使用 setter 赋值。

### Entity 与 NonKeyEntity 接口

带主键的实体应实现 `com.dwarfeng.subgrade.basic.stack.bean.entity.Entity` 接口。
该接口继承自 `Bean`，并要求实体提供主键的读取与设置方法，其定义如下：

```java
public interface Entity<K extends Key> extends Bean {

    K getKey();

    void setKey(K key);
}
```

其中，`K` 是实体主键的类型，必须实现 `Key` 接口。`getKey()` 返回当前实体的主键，
`setKey(K key)` 设置当前实体的主键。数据访问、缓存和服务实现通常通过该契约定位实体。

没有主键的实体应实现 `com.dwarfeng.subgrade.basic.stack.bean.entity.NonKeyEntity` 接口。
该接口同样继承自 `Bean`，但不声明主键访问方法：

```java
public interface NonKeyEntity extends Bean {
}
```

`NonKeyEntity` 适用于单对象、全局配置或不需要通过主键定位的实体。对于需要按主键查询、更新、删除或缓存的数据，
应使用 `Entity` 而不是 `NonKeyEntity`。

### Dto、PagingInfo 与 PagedData

数据传输对象应实现 `com.dwarfeng.subgrade.basic.stack.bean.dto.Dto` 接口。
`Dto` 是继承自 `Bean` 的标记接口，用于标识可以在服务之间传递的结构化数据：

```java
public interface Dto extends Bean {
}
```

`com.dwarfeng.subgrade.basic.stack.bean.dto.PagingInfo` 表示分页查询条件，包含以下字段：

| 字段   | 类型  | 说明                        |
|:-------|:------|:----------------------------|
| `page` | `int` | 查询的页数，从 0 开始计数。 |
| `rows` | `int` | 每页返回的行数。            |

`PagingInfo` 提供了 `FIRST_ONE` 常量，表示第一页且每页返回一行。该对象用于向查询服务、缓存或数据访问层传递分页参数。

`com.dwarfeng.subgrade.basic.stack.bean.dto.PagedData<E>` 表示分页查询结果，包含以下字段：

| 字段          | 类型        | 说明                                                                 |
|:--------------|:------------|:---------------------------------------------------------------------|
| `currentPage` | `int`       | 当前页数，从 0 开始计数。                                            |
| `totalPages`  | `int`       | 总页数；当该值为负数时，表示总页数无法计算。                         |
| `rows`        | `int`       | 每页返回的行数。                                                     |
| `count`       | `long`      | 数据总量。                                                           |
| `data`        | `List<E>`   | 当前页的数据。                                                       |

当每页行数设置为 0 时，`totalPages` 通常为负数，表示无法通过数据总量计算总页数。
`PagedData` 可以通过构造函数直接创建，也可以由分页工具类根据查询结果构造。

`com.dwarfeng.subgrade.basic.sdk.bean.dto.PagingUtil` 提供分页相关的静态工具方法：

| 方法                                                                          | 说明                                                                                 |
|:------------------------------------------------------------------------------|:-------------------------------------------------------------------------------------|
| `pagedData(PagingInfo pagingInfo, int count, List<E> data)`                   | 根据分页条件、数据总量和当前页数据构造 `PagedData`；要求页数、行数和数据总量均非负。 |
| `pagedData(List<E> data)`                                                     | 将完整列表包装为分页结果，当前页为 0，总页数为 1。                                   |
| `transform(PagedData<U> pagedData, BeanTransformer<U, V> transformer)`        | 保留分页元数据，将当前页数据从类型 `U` 转换为类型 `V`。                              |
| `reverseTransform(PagedData<V> pagedData, BeanTransformer<U, V> transformer)` | 保留分页元数据，将当前页数据从类型 `V` 反向转换为类型 `U`。                          |
| `subList(List<E> list, PagingInfo pagingInfo)`                                | 返回指定列表在指定分页上的子列表；要求页数和行数均非负，行数为 0 时返回空列表。      |

`subList` 内部使用 `List.subList` 返回原列表的视图，而不是一份新的数据副本。
调用方如果需要独立列表，应在获取子列表后进行复制。分页工具类要求分页参数合法；在服务、缓存和数据访问层中，
页数或行数小于 0 可能表示不分页，但该语义不属于 `PagingUtil` 的约定。

### Service 与 Handler 接口

`com.dwarfeng.subgrade.basic.stack.service.Service` 和 `com.dwarfeng.subgrade.basic.stack.handler.Handler`
都是不声明方法的标记接口：

```java
public interface Service {
}
```

```java
public interface Handler {
}
```

按照项目的一贯约定，`Service` 用于标识对外暴露或跨模块调用的服务契约，`Handler` 用于标识服务内部的处理器契约。
调用方通常通过 `Service` 发起业务操作，而 `Service` 实现可以组合一个或多个 `Handler` 完成内部处理。
这两个接口本身不包含方法，也不进行运行期校验，其职责边界由项目设计和实现约定保证。

关于数据访问、缓存与服务接口的具体层次，见 [Data Access Basics](./DataAccessBasics.md)。

### Generator 与 KeyGenerator 接口

`com.dwarfeng.subgrade.basic.stack.generation.Generator` 是通用对象生成器接口，其定义如下：

```java
public interface Generator<E> {

    E generate() throws GenerateException;

    default List<E> batchGenerate(int size) throws GenerateException {
        List<E> result = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            result.add(generate());
        }
        return result;
    }
}
```

`generate()` 用于生成单个对象。生成的对象不受类型约束，可以为 `null`。
`batchGenerate(int size)` 用于批量生成对象，默认实现会循环调用 `generate()`，将结果依次放入列表中返回。
重写该方法的实现应保证返回列表不为 `null`，且列表大小等于 `size`；列表中的元素仍然可以为 `null`。
批量生成场景应优先使用 `batchGenerate(int)`，以便实现类在需要时提供更高效的批量生成策略。

`com.dwarfeng.subgrade.basic.stack.generation.KeyGenerator` 是主键生成器接口，其类型参数必须实现 `Key`：

```java
public interface KeyGenerator<K extends Key> extends Generator<K> {
}
```

主键生成器生成的结果要么为 `null`，要么为全局唯一的 `Key` 对象。
当生成结果为 `null` 时，表示当前生成器不使用应用侧主键，而由数据访问层的主键生成机制为实体分配主键。
实现主键生成器时，不得生成可能重复的主键，也不得依赖调用顺序保证唯一性。

## 异常与日志

**基础异常**

`subgrade-basic` 定义了多个基础异常类型，分别用于服务、处理器、生成器和分页场景：

| 异常类              | 说明                                                      |
|:--------------------|:----------------------------------------------------------|
| `ServiceException`  | 服务层对外传递的统一异常，包含可序列化的 `Code` 对象。    |
| `HandlerException`  | 处理器执行失败时使用的异常，可以包装底层异常。            |
| `GenerateException` | 生成器无法完成对象生成时抛出的异常。                      |
| `PagingException`   | 分页处理失败时使用的异常，并携带导致失败的 `PagingInfo`。 |

`ServiceException` 继承自 `Exception`，其内部 `Code` 对象实现了 `Serializable`，包含 `code` 和 `tip` 字段。
`code` 表示异常代码，`tip` 表示简短的异常提示。`ServiceException.getMessage()` 会基于异常代码和提示生成统一的异常消息，
因此异常对象既可以通过 Java 序列化机制传递，也可以转换为 Web 响应或其它协议中的数据。

`ServiceException` 中的 `Code` 结构如下：

```java
public static class Code implements Serializable {

    private int code;
    private String tip;
}
```

`HandlerException`、`GenerateException` 和 `PagingException` 都继承自 `Exception`。
其中 `PagingException` 持有触发异常的分页信息，并在 `getMessage()` 中输出分页详情。
异常映射的完整规则和模块异常编码表，见 [Exception Mapping Basics](./ExceptionMappingBasics.md)。

**服务异常映射**

`com.dwarfeng.subgrade.basic.stack.exception.ServiceExceptionMapper` 用于将任意 `Exception` 统一映射为
`ServiceException`：

```java
public interface ServiceExceptionMapper {

    ServiceException map(Exception e);
}
```

默认实现 `com.dwarfeng.subgrade.basic.impl.exception.MapServiceExceptionMapper` 维护一个异常类型到异常代码供应器的映射。
映射时按照以下顺序处理：输入异常为 `null` 时返回 `null`；输入异常本身是带有代码的 `ServiceException` 时沿用该代码；
在映射表中查找异常的直接类型；未命中时查找可赋值的父类或祖先类型；仍未命中时使用缺省代码供应器。
映射过程中命中的父类映射会写入内部路由缓存，以减少后续相同异常类型的查找开销。

`com.dwarfeng.subgrade.basic.sdk.exception.ServiceExceptionHelper.putDefaultDestination` 可以向映射表中加入
Subgrade 的默认目标映射，包括 `IOException`、`ProcessException`、`HandlerException`、`GenerateException`
和 `PagingException`。这些异常分别映射到 SDK 中预置的服务异常代码供应器。

**服务异常代码**

`com.dwarfeng.subgrade.basic.sdk.exception.ServiceExceptionCodeSuppliers` 提供了基础组件使用的服务异常代码供应器。
每个供应器在获取时创建新的 `ServiceException.Code`，其代码值由基础代码与全局异常偏移量相加得到。
`subgrade-basic` 预置的异常代码如下：

| 常量名                | 基础代码 | 提示               | 说明                         |
|:----------------------|---------:|:-------------------|:-----------------------------|
| `UNDEFINED`           |        1 | 未定义错误         | 未定义的异常，作为缺省代码。 |
| `IO_EXCEPTION`        |       50 | 输入输出操作失败   | IO 处理失败。                |
| `PROCESS_FAILED`      |       60 | 过程执行失败       | 过程执行失败。               |
| `HANDLER_FAILED`      |       70 | 处理器执行失败     | 处理器执行失败。             |
| `NOT_IMPLEMENTED_YET` |      110 | 请求的能力尚未实现 | 功能尚未实现。               |
| `GENERATE_FAILED`     |      120 | 数值生成失败       | 数据或对象生成失败。         |
| `PAGING_FAILED`       |      130 | 分页操作失败       | 分页处理失败。               |

异常偏移量由 `getExceptionCodeOffset()` 读取，并可通过 `setExceptionCodeOffset(int)` 设置。
该偏移量是全局静态状态，设置后会影响后续由供应器创建的异常代码。多个模块或服务共存时，应统一规划偏移量，
避免最终异常代码发生冲突。

**单级日志协议**

`com.dwarfeng.subgrade.basic.stack.log.LogLevel` 定义了 `DEBUG`、`INFO`、`WARN` 和 `ERROR` 四个日志等级。
`com.dwarfeng.subgrade.basic.stack.log.SingleLevelLogger` 表示固定等级的日志记录器，其方法与 SLF4J 的日志方法对应，
但不要求调用方在每次记录日志时指定等级。日志等级在创建记录器时确定。

`com.dwarfeng.subgrade.basic.sdk.log.SingleLevelLoggerFactory` 提供以下工厂方法：

| 方法                                                              | 说明                                                               |
|:------------------------------------------------------------------|:-------------------------------------------------------------------|
| `newInstance(Logger delegate, LogLevel logLevel)`                 | 根据 SLF4J 记录器和日志等级创建单级日志记录器。                    |
| `newInstanceMap(Logger delegate)`                                 | 为全部 `LogLevel` 创建单级日志记录器，并放入以等级为键的映射中。   |

`ServiceExceptionHelper` 和 `HandlerExceptionHelper` 在内部使用单级日志记录器。
`parse` 只进行异常转换，`logParse` 记录日志后返回转换结果，`parseThrow` 和 `logParseThrow` 则在转换完成后立即抛出异常。
调用方在一次异常处理中应选择合适的日志等级和日志位置，避免在多个层次重复记录同一异常。

## 默认实现

**主键生成实现**

`com.dwarfeng.subgrade.basic.impl.generation` 包提供了多种主键生成器：

| 实现类                        | 生成结果      | 说明                                                                       |
|:------------------------------|:--------------|:---------------------------------------------------------------------------|
| `UuidKeyGenerator`            | `UuidKey`     | 使用 `UUID.randomUUID().toString()` 生成标准 UUID 字符串。                 |
| `UuidStringKeyGenerator`      | `StringIdKey` | 使用标准 UUID 字符串生成 `StringIdKey`。                                   |
| `DenseUuidKeyGenerator`       | `UuidKey`     | 使用 `UUIDUtil.toDenseString` 生成紧凑 UUID 字符串。                       |
| `DenseUuidStringKeyGenerator` | `StringIdKey` | 使用紧凑 UUID 字符串生成 `StringIdKey`。                                   |
| `NullValueKeyGenerator`       | `null`        | 表示不使用应用侧主键，由数据访问层生成主键。                               |
| `ExceptionKeyGenerator`       | 不返回        | 每次调用 `generate()` 都抛出 `GenerateException`，用于禁止生成主键的场景。 |

`UuidKeyGenerator` 和 `DenseUuidKeyGenerator` 返回 `UuidKey`，而 `UuidStringKeyGenerator` 和
`DenseUuidStringKeyGenerator` 返回 `StringIdKey`。选择实现时需要同时考虑主键类型和 UUID 文本表示方式。

**Bean 转换实现**

`com.dwarfeng.subgrade.basic.impl.bean.IdentityBeanTransformer` 是最简单的 Bean 转换器，
其 `transform` 和 `reverseTransform` 都返回传入对象本身，适用于领域对象与存储对象类型相同的场景。

`com.dwarfeng.subgrade.basic.impl.bean.MapStructBeanTransformer` 基于 MapStruct 创建转换器。
其构造函数接收类型 `U` 的类、类型 `V` 的类和 MapStruct Mapper 类。实例化时会调用
`Mappers.getMapper(Class)` 获取 Mapper，并扫描 Mapper 的公开方法，寻找参数和返回值符合 `U`、`V` 类型关系的双向转换方法。

如果无法同时找到两个方向的转换方法，构造函数会抛出 `IllegalStateException`。
如果实际调用转换方法时发生异常，`transform` 或 `reverseTransform` 会将原异常包装为 `IllegalStateException` 后抛出。
因此，MapStruct Mapper 中的方法签名必须与 `BeanTransformer` 使用的类型保持一致。

**异常映射实现**

`com.dwarfeng.subgrade.basic.impl.exception.MapServiceExceptionMapper` 使用构造参数提供的
`Map<Class<? extends Exception>, Supplier<ServiceException.Code>>` 作为异常映射目标，
并使用另一个 `Supplier<ServiceException.Code>` 作为缺省代码供应器。具体构造签名如下：

```java
public MapServiceExceptionMapper(
        Map<Class<? extends Exception>, Supplier<ServiceException.Code>> destination,
        Supplier<ServiceException.Code> defaultCode
) {
    this.destination = destination;
    this.defaultCode = defaultCode;
}
```

`destination` 和 `defaultCode` 可以通过 setter 替换。映射器支持直接类型匹配、父类匹配和缺省匹配，
并将父类匹配结果缓存到内部路由中。`ServiceExceptionHelper.putDefaultDestination` 可以直接创建并填充默认映射表，
`ServiceExceptionCodeSuppliers.UNDEFINED` 通常作为缺省代码供应器使用。

**日志实现**

`com.dwarfeng.subgrade.basic.sdk.log.SingleLevelLoggerFactory` 基于传入的 SLF4J `Logger` 创建单级日志记录器。
`newInstance(Logger delegate, LogLevel logLevel)` 根据指定等级返回对应的实现；
`newInstanceMap(Logger delegate)` 一次创建四种等级的记录器，并返回 `EnumMap<LogLevel, SingleLevelLogger>`。

工厂创建的单级记录器只调用与绑定等级对应的 SLF4J 方法。例如，`LogLevel.WARN` 对应的记录器会将全部 `log` 调用委托给
SLF4J 的 `warn` 方法。该机制适合在异常帮助类等只需要固定等级记录日志的场景中使用，
不替代完整的 SLF4J 日志接口。

## 使用示例

**定义实体并使用标准主键**

以下示例使用 `LongIdKey` 作为实体主键：

```java
public class User implements Entity<LongIdKey> {

    private LongIdKey key;
    private String name;

    @Override
    public LongIdKey getKey() {
        return key;
    }

    @Override
    public void setKey(LongIdKey key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```

创建实体并设置主键：

```java
class Example {

    void createUser() {
        User user = new User();
        user.setKey(new LongIdKey(1001L));
        user.setName("Ada");
    }
}
```

如果希望由数据访问层生成主键，可以使用 `NullValueKeyGenerator`：

```java
class Example {

    void createKeyGenerator() {
        KeyGenerator<LongIdKey> keyGenerator = new NullValueKeyGenerator<>();
    }
}
```

该生成器的 `generate()` 返回 `null`，数据访问层在插入实体时会使用自身的生成机制。

**组装分页结果**

以下示例根据分页条件和当前页数据构造 `PagedData`：

```java
class Example {

    void createPagedData(User user) {
        PagingInfo pagingInfo = new PagingInfo(0, 20);
        List<User> users = List.of(user);
        PagedData<User> pagedData = PagingUtil.pagedData(pagingInfo, users.size(), users);
    }
}
```

如果需要将实体分页结果转换为 DTO 分页结果，可以使用 `BeanTransformer`：

```java
class Example {

    void transformPagedData(PagedData<User> pagedData, BeanTransformer<User, UserDto> userTransformer) {
        PagedData<UserDto> dtoPagedData = PagingUtil.transform(pagedData, userTransformer);
    }
}
```

`PagingUtil.transform` 会保留 `currentPage`、`totalPages`、`rows` 和 `count`，只转换 `data` 中的元素。

**处理服务异常**

以下示例在服务方法中捕获处理器抛出的异常，记录日志并将异常统一映射为 `ServiceException`：

```java
class UserService {

    private UserHandler handler;
    private ServiceExceptionMapper serviceExceptionMapper;

    public User getUser(LongIdKey key) throws ServiceException {
        try {
            return handler.get(key);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("获取用户失败", LogLevel.WARN, e, serviceExceptionMapper);
        }
    }
}
```

`serviceExceptionMapper` 应使用已配置的 `ServiceExceptionMapper` 实例。若只进行异常转换而不记录日志，
可以使用 `ServiceExceptionHelper.parse`；若需要转换后立即抛出异常，可以使用 `parseThrow` 或 `logParseThrow`。

## 注意事项

实现 `Bean`、`Key`、`Entity`、`NonKeyEntity` 或 `Dto` 的类型会被视为需要支持序列化的基础对象。
在实际业务类中应维护稳定的 `serialVersionUID`，并确保需要随对象一起序列化的字段本身也具备可序列化能力。
`ServiceException.Code` 已经实现 `Serializable`，因此 `ServiceException` 可以通过 RPC 或其它序列化协议传递。

实现 `Key` 时，必须基于参与主键比较的全部字段实现 `equals()` 和 `hashCode()`。
主键对象一旦用于集合、缓存或持久化索引，就不应随意修改其参与哈希计算的字段。
自定义主键生成器必须保证生成结果符合“`null` 或全局唯一”的契约。

`PagingUtil` 要求 `PagingInfo` 的页数和行数均大于等于 0，并要求数据总量非负，否则会抛出 `IllegalArgumentException`。
当 `rows` 为 0 时，`pagedData` 生成的 `totalPages` 为 -1，`subList` 返回空列表。
服务、缓存和数据访问层在页数或行数小于 0 时可能将其解释为“不分页”，使用分页工具类时不应依赖该语义。
`PagingUtil.subList` 返回的是原列表视图，修改视图可能影响原列表。

`MapServiceExceptionMapper` 优先使用输入异常自身的代码，其次使用直接类型映射，再使用父类映射，最后使用缺省代码。
异常代码偏移量是全局静态值，应用需要在生成异常代码之前统一配置。
`MapStructBeanTransformer` 在找不到双向方法或调用映射方法失败时会抛出 `IllegalStateException`，
该类异常需要由调用方或上层异常处理逻辑处理。

## 参阅

- [Data Access Basics](./DataAccessBasics.md) - 实体、主键、Dao、缓存和服务接口总览。
- [Exception Mapping Basics](./ExceptionMappingBasics.md) - 服务异常、异常映射和模块异常编码。
