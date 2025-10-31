# Upgrade Guide 1.7.0 - 升级指南 1.7.0

## 综述

本文档提供了从 Subgrade 1.6.x 及更早版本升级到 1.7.0 版本的详细指南。

**重要提示**：1.7.0 版本包含**不兼容更新**，升级前请仔细阅读本文档，并按照指南进行代码迁移。

本次更新主要涉及登录和权限相关接口的类型变更，将原有的 `LongIdKey` 和 `StringIdKey` 类型改为直接使用 `String` 类型。

---

## 不兼容性改动

### 接口方法签名变更

#### LoginRequiredAopManager 接口

**变更前**：

```java
public interface LoginRequiredAopManager {
    LongIdKey getLoginId(ProceedingJoinPoint pjp) throws Throwable;
    Object onNotLogin(ProceedingJoinPoint pjp, LongIdKey loginId) throws Throwable;
}
```

**变更后**：

```java
public interface LoginRequiredAopManager {
    String getLoginId(ProceedingJoinPoint pjp) throws Throwable;
    Object onNotLogin(ProceedingJoinPoint pjp, String loginId) throws Throwable;
}
```

#### PermissionRequiredAopManager 接口

**变更前**：

```java
public interface PermissionRequiredAopManager {
    StringIdKey getUserKey(ProceedingJoinPoint pjp) throws Throwable;
    Object onMissingPermission(ProceedingJoinPoint pjp, StringIdKey userKey, List<String> missingPermissions) 
    throws Throwable;
}
```

**变更后**：

```java
public interface PermissionRequiredAopManager {
    String getUserId(ProceedingJoinPoint pjp) throws Throwable;
    Object onMissingPermission(ProceedingJoinPoint pjp, String userId, List<String> missingPermissions)
    throws Throwable;
}
```

**注意**：`getUserKey()` 方法已重命名为 `getUserId()`，且返回类型从 `StringIdKey` 改为 `String`。

#### TokenResolver 接口

**变更前**：

```java
public interface TokenResolver {
    StringIdKey resolve(String token) throws Exception;
}
```

**变更后**：

```java
public interface TokenResolver {
    String resolve(String token) throws Exception;
}
```

### Handler 接口变更

#### LoginHandler 接口

**变更前**：

```java
public interface LoginHandler extends Handler {
    boolean checkPassword(StringIdKey userKey, String password) throws HandlerException;
    LongIdKey login(StringIdKey userKey, String password) throws HandlerException;
    default LongIdKey login(StringIdKey userKey, String password, Map<String, String> extraParamMap)
    throws HandlerException;
    void logout(LongIdKey idKey) throws HandlerException;
    boolean isLogin(LongIdKey idKey) throws HandlerException;
    void postpone(LongIdKey idKey) throws HandlerException;
}
```

**变更后**：

```java
public interface LoginHandler extends Handler {
    boolean checkPassword(String userId, String password) throws HandlerException;
    String login(String userId, String password) throws HandlerException;
    default String login(String userId, String password, Map<String, String> extraParamMap) throws HandlerException;
    void logout(String loginId) throws HandlerException;
    boolean isLogin(String loginId) throws HandlerException;
    void postpone(String loginId) throws HandlerException;
}
```

#### PermissionHandler 接口

**变更前**：

```java
public interface PermissionHandler extends Handler {
    boolean hasPermission(StringIdKey userKey, String permissionNode) throws HandlerException;
    boolean hasPermission(StringIdKey userKey, List<String> permissionNodes) throws HandlerException;
    List<String> getMissingPermissions(StringIdKey userKey, List<String> permissionNodes) throws HandlerException;
}
```

**变更后**：

```java
public interface PermissionHandler extends Handler {
    boolean hasPermission(String userId, String permission) throws HandlerException;
    boolean hasPermission(String userId, List<String> permissions) throws HandlerException;
    List<String> getMissingPermissions(String userId, List<String> permissions) throws HandlerException;
}
```

#### TokenHandler 接口

**变更前**：

```java
public interface TokenHandler extends Handler {
    Long getTokenId(HttpServletRequest httpServletRequest) throws HandlerException;
    LongIdKey getLoginKey(HttpServletRequest httpServletRequest) throws HandlerException;
    StringIdKey getUserKey(HttpServletRequest httpServletRequest) throws HandlerException;
}
```

**变更后**：

```java
public interface TokenHandler extends Handler {
    String getUserId(HttpServletRequest httpServletRequest) throws HandlerException;
    String getLoginId(HttpServletRequest httpServletRequest) throws HandlerException;
}
```

**注意**：

- `getTokenId()` 方法已被移除
- `getLoginKey()` 方法重命名为 `getLoginId()`，返回类型从 `LongIdKey` 改为 `String`
- `getUserKey()` 方法重命名为 `getUserId()`，返回类型从 `StringIdKey` 改为 `String`

#### LoginPermHandler 接口

**变更前**：

```java
public interface LoginPermHandler extends Handler {
    boolean checkPassword(StringIdKey userKey, String password) throws HandlerException;
    LongIdKey login(StringIdKey userKey, String password) throws HandlerException;
    void logout(LongIdKey idKey) throws HandlerException;
    boolean isLogin(LongIdKey idKey) throws HandlerException;
    void postpone(LongIdKey idKey) throws HandlerException;
    boolean hasPermission(LongIdKey idKey, String permissionNode) throws HandlerException;
    boolean hasPermission(LongIdKey idKey, List<String> permissionNodes) throws HandlerException;
    List<String> getMissingPermission(LongIdKey idKey, List<String> permissionNodes) throws HandlerException;
}
```

**变更后**：

```java
public interface LoginPermHandler extends Handler {
    boolean checkPassword(String userId, String password) throws HandlerException;
    String login(String userId, String password) throws HandlerException;
    void logout(String loginId) throws HandlerException;
    boolean isLogin(String loginId) throws HandlerException;
    void postpone(String loginId) throws HandlerException;
    boolean hasPermission(String loginId, String permission) throws HandlerException;
    boolean hasPermission(String loginId, List<String> permissions) throws HandlerException;
    List<String> getMissingPermission(String loginId, List<String> permissions) throws HandlerException;
}
```

### 异常类变更

#### LoginFailedException 类

**变更前**：

```java
package com.dwarfeng.subgrade.sdk.interceptor.login;

import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;

public class LoginFailedException extends Exception {
    private final LongIdKey loginId;
    
    public LoginFailedException(LongIdKey loginId);
    public LoginFailedException(String message, LongIdKey loginId);
    public LoginFailedException(String message, Throwable cause, LongIdKey loginId);
    public LoginFailedException(Throwable cause, LongIdKey loginId);
}
```

**变更后**：

```java
package com.dwarfeng.subgrade.stack.exception;

public class LoginFailedException extends Exception {
    private final String loginId;
    
    public LoginFailedException(String loginId);
    public LoginFailedException(String message, String loginId);
    public LoginFailedException(String message, Throwable cause, String loginId);
    public LoginFailedException(Throwable cause, String loginId);
}
```

### 实现类变更

以下实现类已适配新的接口签名，但如果您自定义实现了这些接口，需要进行相应的迁移：

- `com.dwarfeng.subgrade.sdk.interceptor.login.DefaultLoginRequiredAopManager`
- `com.dwarfeng.subgrade.sdk.interceptor.login.HttpLoginRequiredAopManager`
- `com.dwarfeng.subgrade.sdk.interceptor.login.TokenHandlerLoginRequiredAopManager`
- `com.dwarfeng.subgrade.sdk.interceptor.permission.DefaultPermissionRequiredAopManager`
- `com.dwarfeng.subgrade.sdk.interceptor.permission.HttpPermissionRequiredAopManager`
- `com.dwarfeng.subgrade.sdk.interceptor.permission.TokenHandlerPermissionRequiredAopManager`

---

## 改动原因

本次不兼容更新的主要原因如下：

1. **更好的通用性**：将参数类型改为 `String` 更加通用。字符串类型比 `Long`（通过 `LongIdKey` 封装）更加灵活，
   能够兼容更多的格式，包括 UUID、随机字符串、以及传统的数字字符串等。

2. **向后兼容性**：`String` 类型能够兼容过去的 `Long` 格式，只需要将 `LongIdKey` 中的 `LongId`
   转换为字符串即可。这样既保持了功能的完整性，又提供了更大的灵活性。

3. **简化 API**：直接使用 `String` 类型避免了不必要的对象封装，简化了 API 的使用，减少了类型转换的开销。

4. **统一性**：统一使用 `String` 类型作为标识符类型，使得整个框架的 API 更加一致，降低了使用者的学习成本。

---

## 代码迁移指南

### 1. 迁移 LoginRequiredAopManager 实现类

**变更前**：

```java
public class CustomLoginRequiredAopManager implements LoginRequiredAopManager {
    
    @Override
    public LongIdKey getLoginId(ProceedingJoinPoint pjp) throws Throwable {
        // 获取登录 ID
        HttpServletRequest request = getRequest(pjp);
        String token = request.getHeader("token");
        Long loginId = Long.parseLong(token);
        return new LongIdKey(loginId);
    }
    
    @Override
    public Object onNotLogin(ProceedingJoinPoint pjp, LongIdKey loginId) throws Throwable {
        // 处理未登录情况
        return responseForNotLogin(loginId.getLongId());
    }
}
```

**变更后**：

```java
public class CustomLoginRequiredAopManager implements LoginRequiredAopManager {
    
    @Override
    public String getLoginId(ProceedingJoinPoint pjp) throws Throwable {
        // 获取登录 ID
        HttpServletRequest request = getRequest(pjp);
        String token = request.getHeader("token");
        // 直接返回字符串，如果原来是 Long，可以转换为字符串
        return token;
    }
    
    @Override
    public Object onNotLogin(ProceedingJoinPoint pjp, String loginId) throws Throwable {
        // 处理未登录情况
        return responseForNotLogin(loginId);
    }
}
```

**迁移要点**：

- 将返回类型从 `LongIdKey` 改为 `String`
- 将方法参数类型从 `LongIdKey` 改为 `String`
- 如果原来使用 `LongIdKey`，将其中的 `longId` 值转换为字符串：`String.valueOf(longIdKey.getLongId())`
- 如果原来直接返回 `LongIdKey`，现在直接返回字符串即可

### 2. 迁移 PermissionRequiredAopManager 实现类

**变更前**：

```java
public class CustomPermissionRequiredAopManager implements PermissionRequiredAopManager {
    
    @Override
    public StringIdKey getUserKey(ProceedingJoinPoint pjp) throws Throwable {
        // 获取用户主键
        HttpServletRequest request = getRequest(pjp);
        String userId = request.getHeader("userId");
        return new StringIdKey(userId);
    }
    
    @Override
    public Object onMissingPermission(ProceedingJoinPoint pjp, StringIdKey userKey, List<String> missingPermissions)
    throws Throwable {
        // 处理权限缺失情况
        return responseForMissingPermission(userKey.getStringId(), missingPermissions);
    }
}
```

**变更后**：

```java
public class CustomPermissionRequiredAopManager implements PermissionRequiredAopManager {
    
    @Override
    public String getUserId(ProceedingJoinPoint pjp) throws Throwable {
        // 获取用户 ID
        HttpServletRequest request = getRequest(pjp);
        String userId = request.getHeader("userId");
        // 直接返回字符串
        return userId;
    }
    
    @Override
    public Object onMissingPermission(ProceedingJoinPoint pjp, String userId, List<String> missingPermissions)
    throws Throwable {
        // 处理权限缺失情况
        return responseForMissingPermission(userId, missingPermissions);
    }
}
```

**迁移要点**：

- 将方法名从 `getUserKey()` 改为 `getUserId()`
- 将返回类型从 `StringIdKey` 改为 `String`
- 将方法参数类型从 `StringIdKey` 改为 `String`
- 如果原来使用 `StringIdKey`，将其中的 `stringId` 值取出：`stringIdKey.getStringId()`

### 3. 迁移 TokenResolver 实现类

**变更前**：

```java
public class CustomTokenResolver implements TokenResolver {
    
    @Override
    public StringIdKey resolve(String token) throws Exception {
        // 解析 token 获取用户主键
        String userId = parseToken(token);
        return new StringIdKey(userId);
    }
}
```

**变更后**：

```java
public class CustomTokenResolver implements TokenResolver {
    
    @Override
    public String resolve(String token) throws Exception {
        // 解析 token 获取用户 ID
        String userId = parseToken(token);
        // 直接返回字符串
        return userId;
    }
}
```

**迁移要点**：

- 将返回类型从 `StringIdKey` 改为 `String`
- 直接返回解析得到的字符串值，无需封装为 `StringIdKey`

### 4. 迁移 Handler 接口实现类

#### LoginHandler 实现类

**变更前**：

```java
public class CustomLoginHandler implements LoginHandler {
    
    @Override
    public boolean checkPassword(StringIdKey userKey, String password) throws HandlerException {
        // 检查密码
        return checkUserPassword(userKey.getStringId(), password);
    }
    
    @Override
    public LongIdKey login(StringIdKey userKey, String password) throws HandlerException {
        // 登录逻辑
        Long loginId = createLoginSession(userKey.getStringId(), password);
        return new LongIdKey(loginId);
    }
    
    @Override
    public void logout(LongIdKey idKey) throws HandlerException {
        // 登出逻辑
        removeLoginSession(idKey.getLongId());
    }
    
    @Override
    public boolean isLogin(LongIdKey idKey) throws HandlerException {
        // 检查登录状态
        return checkLoginSession(idKey.getLongId());
    }
    
    @Override
    public void postpone(LongIdKey idKey) throws HandlerException {
        // 延长登录时间
        extendLoginSession(idKey.getLongId());
    }
}
```

**变更后**：

```java
public class CustomLoginHandler implements LoginHandler {
    
    @Override
    public boolean checkPassword(String userId, String password) throws HandlerException {
        // 检查密码
        return checkUserPassword(userId, password);
    }
    
    @Override
    public String login(String userId, String password) throws HandlerException {
        // 登录逻辑
        // 注意：返回的登录 ID 必须具有一定的随机性，不得具有较为显著的规律
        String loginId = createLoginSession(userId, password);
        return loginId;
    }
    
    @Override
    public void logout(String loginId) throws HandlerException {
        // 登出逻辑
        removeLoginSession(loginId);
    }
    
    @Override
    public boolean isLogin(String loginId) throws HandlerException {
        // 检查登录状态
        return checkLoginSession(loginId);
    }
    
    @Override
    public void postpone(String loginId) throws HandlerException {
        // 延长登录时间
        extendLoginSession(loginId);
    }
}
```

**迁移要点**：

- 将所有方法的 `StringIdKey userKey` 参数改为 `String userId`
- 将所有方法的 `LongIdKey idKey` 参数改为 `String loginId`
- 将 `login()` 方法的返回类型从 `LongIdKey` 改为 `String`
- 如果原来使用 `LongIdKey`，需要将 `longId` 转换为字符串：`String.valueOf(longIdKey.getLongId())`
- 如果原来使用 `StringIdKey`，需要获取其中的 `stringId` 值：`stringIdKey.getStringId()`
- **重要**：`login()` 方法返回的登录 ID 必须是随机生成的字符串，不能使用自增主键或雪花 ID 等有明显规律的值，以确保安全性

#### PermissionHandler 实现类

**变更前**：

```java
public class CustomPermissionHandler implements PermissionHandler {
    
    @Override
    public boolean hasPermission(StringIdKey userKey, String permission) throws HandlerException {
        // 检查权限
        return checkUserPermission(userKey.getStringId(), permission);
    }
    
    @Override
    public List<String> getMissingPermissions(StringIdKey userKey, List<String> permissions) throws HandlerException {
        // 获取缺失的权限
        return findMissingPermissions(userKey.getStringId(), permissions);
    }
}
```

**变更后**：

```java
public class CustomPermissionHandler implements PermissionHandler {
    
    @Override
    public boolean hasPermission(String userId, String permission) throws HandlerException {
        // 检查权限
        return checkUserPermission(userId, permission);
    }
    
    @Override
    public List<String> getMissingPermissions(String userId, List<String> permissions) throws HandlerException {
        // 获取缺失的权限
        return findMissingPermissions(userId, permissions);
    }
}
```

**迁移要点**：

- 将所有方法的 `StringIdKey userKey` 参数改为 `String userId`
- 如果原来使用 `StringIdKey`，需要获取其中的 `stringId` 值：`stringIdKey.getStringId()`

#### TokenHandler 实现类

**变更前**：

```java
public class CustomTokenHandler implements TokenHandler {
    
    @Override
    public Long getTokenId(HttpServletRequest request) throws HandlerException {
        // 获取 Token ID（已废弃）
        return null;
    }
    
    @Override
    public LongIdKey getLoginKey(HttpServletRequest request) throws HandlerException {
        // 获取登录主键
        String token = request.getHeader("token");
        Long loginId = parseTokenToLoginId(token);
        return new LongIdKey(loginId);
    }
    
    @Override
    public StringIdKey getUserKey(HttpServletRequest request) throws HandlerException {
        // 获取用户主键
        String token = request.getHeader("token");
        String userId = parseTokenToUserId(token);
        return new StringIdKey(userId);
    }
}
```

**变更后**：

```java
public class CustomTokenHandler implements TokenHandler {
    
    @Override
    public String getUserId(HttpServletRequest request) throws HandlerException {
        // 获取用户 ID
        String token = request.getHeader("token");
        String userId = parseTokenToUserId(token);
        return userId;
    }
    
    @Override
    public String getLoginId(HttpServletRequest request) throws HandlerException {
        // 获取登录 ID
        String token = request.getHeader("token");
        String loginId = parseTokenToLoginId(token);
        return loginId;
    }
}
```

**迁移要点**：

- 移除 `getTokenId()` 方法
- 将 `getLoginKey()` 方法重命名为 `getLoginId()`，返回类型从 `LongIdKey` 改为 `String`
- 将 `getUserKey()` 方法重命名为 `getUserId()`，返回类型从 `StringIdKey` 改为 `String`
- 如果原来返回 `LongIdKey`，需要将 `longId` 转换为字符串：`String.valueOf(longIdKey.getLongId())`
- 如果原来返回 `StringIdKey`，需要获取其中的 `stringId` 值：`stringIdKey.getStringId()`

### 5. 迁移 LoginFailedException 使用场景

**变更前**：

```java
import com.dwarfeng.subgrade.sdk.interceptor.login.LoginFailedException;
import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;

try {
    // 业务逻辑
} catch (LoginFailedException e) {
    LongIdKey loginId = e.getLoginId();
    // 处理异常
    log.error("登录失败: {}", loginId.getLongId());
}
```

**变更后**：

```java
import com.dwarfeng.subgrade.stack.exception.LoginFailedException;

try {
    // 业务逻辑
} catch (LoginFailedException e) {
    String loginId = e.getLoginId();
    // 处理异常
    log.error("登录失败: {}", loginId);
}
```

**迁移要点**：

- 更新导入语句：从 `com.dwarfeng.subgrade.sdk.interceptor.login.LoginFailedException` 改为
  `com.dwarfeng.subgrade.stack.exception.LoginFailedException`
- 移除对 `LongIdKey` 的导入（如果不再需要）
- `getLoginId()` 方法现在返回 `String` 类型，直接使用即可
- 如果原来需要使用 `longId` 值，现在直接使用字符串即可

### 6. 迁移使用注解的代码

如果您的代码中使用了 `@RequestLogin` 和 `@RequestUser` 注解，需要确保注解参数的类型为 `String`：

**变更前**：

```java
@LoginRequired
public ResponseData<User> getUserInfo(@RequestLogin LongIdKey loginId) {
    // 获取用户信息
    Long id = loginId.getLongId();
    User user = userService.getUser(id);
    return ResponseDataUtil.good(user);
}

@PermissionRequired("user:view")
public ResponseData<List<User>> listUsers(@RequestUser StringIdKey userKey) {
    // 获取用户列表
    String userId = userKey.getStringId();
    List<User> users = userService.listUsers(userId);
    return ResponseDataUtil.good(users);
}
```

**变更后**：

```java
@LoginRequired
public ResponseData<User> getUserInfo(@RequestLogin String loginId) {
    // 获取用户信息
    // 如果原来是 Long，可以转换为 Long：Long id = Long.parseLong(loginId);
    User user = userService.getUser(loginId);
    return ResponseDataUtil.good(user);
}

@PermissionRequired("user:view")
public ResponseData<List<User>> listUsers(@RequestUser String userId) {
    // 获取用户列表
    List<User> users = userService.listUsers(userId);
    return ResponseDataUtil.good(users);
}
```

**迁移要点**：

- 将 `@RequestLogin` 注解的参数类型从 `LongIdKey` 改为 `String`
- 将 `@RequestUser` 注解的参数类型从 `StringIdKey` 改为 `String`
- 如果业务逻辑需要使用 `long` 类型的值，可以使用 `Long.parseLong(loginId)` 进行转换
- 直接使用字符串作为用户 ID 和登录 ID

---

## 注意事项

1. **类型转换**：

   - 如果您的代码中原来使用 `LongIdKey`，需要将其中的 `longId` 值转换为字符串：`String.valueOf(longIdKey.getLongId())`
   - 如果您的代码中原来使用 `StringIdKey`，需要获取其中的 `stringId` 值：`stringIdKey.getStringId()`

2. **登录 ID 生成**：

   - `LoginHandler.login()` 方法返回的登录 ID 必须是随机生成的字符串，不能使用自增主键或雪花 ID 等有明显规律的值，
     以确保安全性
   - 建议使用 UUID 或其他具有足够随机性的字符串生成器

3. **方法重命名**：

   - `PermissionRequiredAopManager.getUserKey()` 已重命名为 `getUserId()`
   - `TokenHandler.getLoginKey()` 已重命名为 `getLoginId()`
   - `TokenHandler.getUserKey()` 已重命名为 `getUserId()`
   - 更新所有对这些方法的调用

4. **异常类包路径**：

   - `LoginFailedException` 的包路径已从 `com.dwarfeng.subgrade.sdk.interceptor.login` 移动到
     `com.dwarfeng.subgrade.stack.exception`
   - 更新所有导入语句

5. **测试**：

   - 升级后请充分测试所有涉及登录和权限的功能
   - 特别注意测试登录 ID 和用户 ID 的传递和存储

6. **向后兼容**：

   - 虽然接口发生了不兼容变更，但功能上保持兼容
   - 如果原来使用数字字符串作为 ID，可以直接使用
   - 如果原来使用 `LongIdKey`，只需要将其中的 `longId` 值转换为字符串即可

---

## 常见问题

### Q: 我的登录 ID 原来是 Long 类型，现在需要改为 String，会不会影响性能？

A: 不会。字符串类型的处理性能与 Long 类型相当，且现在的设计更加灵活。如果您的数据库中存储的是 Long 类型，
可以在需要时进行转换，但接口层面统一使用 String 类型更加通用。

### Q: 我使用自增主键作为登录 ID，现在改为 String 会有安全问题吗？

A: **会**。根据 `LoginHandler.login()` 方法的文档说明，返回的登录 ID **必须具有一定的随机性，不得具有较为显著的规律**。
使用自增主键或雪花 ID 等有明显规律的值会导致安全问题。请使用 UUID 或其他具有足够随机性的字符串生成器。

### Q: 我的 TokenResolver 原来返回 StringIdKey，现在需要返回 String，转换简单吗？

A: 非常简单。只需要将原来的 `return new StringIdKey(userId);` 改为 `return userId;` 即可。

### Q: 我需要修改多少文件？

A: 这取决于您项目中实现和使用了哪些接口。主要需要修改：

- 所有实现了 `LoginRequiredAopManager`、`PermissionRequiredAopManager`、`TokenResolver` 的自定义类
- 所有实现了 `LoginHandler`、`PermissionHandler`、`TokenHandler`、`LoginPermHandler` 的自定义类
- 所有使用 `@RequestLogin` 和 `@RequestUser` 注解的方法
- 所有捕获或使用 `LoginFailedException` 的代码

### Q: 升级后我的代码还能正常运行吗？

A: 如果您按照本文档的指南进行了完整的迁移，代码可以正常运行。建议在测试环境中充分测试后再部署到生产环境。
