# Use with Maven - 通过 Maven 使用本项目

## 安装本项目

请参考 [Install by Source Code](./InstallBySourceCode.md) 安装本项目。

## 使用本项目

在 Maven 中使用本项目非常简单，只需要在 `pom.xml` 中添加如下依赖即可：

### subgrade-stack

```xml

<dependency>
    <groupId>com.dwarfeng</groupId>
    <artifactId>subgrade-stack</artifactId>
    <version>${subgrade.version}</version>
</dependency>
```

### subgrade-sdk

```xml

<dependency>
    <groupId>com.dwarfeng</groupId>
    <artifactId>subgrade-sdk</artifactId>
    <version>${subgrade.version}</version>
    <!-- 推荐排除以下已停止维护的依赖 -->
    <exclusions>
        <exclusion>
            <artifactId>dozer</artifactId>
            <groupId>net.sf.dozer</groupId>
        </exclusion>
        <exclusion>
            <artifactId>dozer-spring</artifactId>
            <groupId>net.sf.dozer</groupId>
        </exclusion>
    </exclusions>
</dependency>
```

### subgrade-impl

```xml

<dependency>
    <groupId>com.dwarfeng</groupId>
    <artifactId>subgrade-impl</artifactId>
    <version>${subgrade.version}</version>
    <!-- 推荐排除以下已停止维护的依赖 -->
    <exclusions>
        <exclusion>
            <artifactId>dozer</artifactId>
            <groupId>net.sf.dozer</groupId>
        </exclusion>
        <exclusion>
            <artifactId>dozer-spring</artifactId>
            <groupId>net.sf.dozer</groupId>
        </exclusion>
    </exclusions>
</dependency>
```

## 注意事项

### 停止维护的依赖

该项目中的部分依赖已明确停止维护，对于这些已经停止维护的依赖，本项目在 `pom.xml` 中使用 `provided` 进行标记，
因此，这些停止维护的依赖不会被打包到最终构建的产物中，如果您确实需要在运行环境中使用这些依赖，
请您在您的 `pom.xml` 中声明这些依赖。

停止维护的依赖清单如下：

| 名称                        | 版本    | 说明                                                                                 |
|:--------------------------|:------|:-----------------------------------------------------------------------------------|
| net.sf.dozer:dozer        | 5.5.1 | [停止维护说明](https://github.com/DozerMapper/dozer?tab=readme-ov-file#project-activity) |
| net.sf.dozer:dozer-spring | 5.5.1 | [停止维护说明](https://github.com/DozerMapper/dozer?tab=readme-ov-file#project-activity) |

## 参阅

- [Install by Source Code](./InstallBySourceCode.md) - 通过源码安装本项目。
