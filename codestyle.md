# Java 代码规范（Backend）

本项目 Java 代码遵循 [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)。

## 主要规范摘要

### 缩进与换行
- 使用 4 个空格缩进（Spring Boot 默认），不使用 Tab。
- 每行不超过 100 个字符。

### 命名
- 类名：`PascalCase`（如 `CalculatorController`）
- 方法名、变量名：`camelCase`（如 `calculateExpression`）
- 常量：`UPPER_SNAKE_CASE`
- 包名：全小写（如 `com.example.calculator.controller`）

### Spring Boot 分层
- `controller`：只接收请求、调用 Service、返回响应，不写业务逻辑。
- `service`：业务逻辑、事务边界。
- `repository`：数据访问，只定义接口，不写 SQL（除非用 `@Query`）。
- `model`：JPA 实体与 DTO。

### 依赖注入
- 优先使用构造函数注入（本项目已使用 `final` 字段 + 构造器）。
- 不使用 `@Autowired` 字段注入。

### 异常处理
- Controller 层统一捕获业务异常，返回合适的 HTTP 状态码。
- 不向客户端暴露堆栈信息。

### 其他
- 使用 Lombok 可选（本项目未引入，手写 getter/setter）。
- 公共方法需加 Javadoc 注释。
