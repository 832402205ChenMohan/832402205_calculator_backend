# Calculator Backend (Spring Boot)

基于 Spring Boot 3 + Spring Data JPA + H2 的前后端分离计算器后端。

## 技术栈

- Java 17
- Spring Boot 3.2
- Spring Web（REST API）
- Spring Data JPA（ORM）
- H2 Database（文件模式持久化）

## 运行环境

- JDK 17+
- Maven 3.6+

## 编译与启动

```bash
mvn spring-boot:run
```

或打包后运行：

```bash
mvn clean package
java -jar target/calculator-backend-1.0.0.jar
```

服务默认运行在 `http://localhost:5000`。

## 数据库

使用 H2 文件模式数据库，数据持久化在项目目录下的 `calculator.mv.db` 文件中。
首次启动自动建表，无需手动配置。

H2 控制台：`http://localhost:5000/h2-console`
- JDBC URL: `jdbc:h2:file:./calculator`
- 用户名: `sa`
- 密码: （空）

数据表结构：

```sql
CREATE TABLE calculation_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    expression VARCHAR(255) NOT NULL,
    result VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL
);
```

## API 接口

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/calculate` | 计算表达式 |
| GET | `/api/history` | 获取全部历史记录 |
| DELETE | `/api/history/{id}` | 删除指定历史记录 |
| DELETE | `/api/history` | 清空全部历史记录 |

### POST /api/calculate

请求体：

```json
{ "expression": "(1+2)*3" }
```

成功响应（200）：

```json
{ "success": true, "expression": "(1+2)*3", "result": 9, "id": 1 }
```

失败响应（400）：

```json
{ "success": false, "message": "Division by zero" }
```

## 项目结构

```
calculator-backend/
├── pom.xml
├── src/main/java/com/example/calculator/
│   ├── CalculatorApplication.java        # Spring Boot 主类
│   ├── controller/
│   │   └── CalculatorController.java      # REST 控制器
│   ├── service/
│   │   ├── CalculatorService.java        # 业务逻辑
│   │   └── ExpressionParser.java          # 递归下降安全表达式解析器
│   ├── model/
│   │   └── HistoryRecord.java            # JPA 实体
│   └── repository/
│       └── HistoryRepository.java        # Spring Data JPA 接口
├── src/main/resources/
│   └── application.properties            # 配置文件
├── README.md
└── codestyle.md
```

## 前后端连接

后端默认监听 `0.0.0.0:5000`，前端通过 `http://localhost:5000/api` 访问。
已通过 `@CrossOrigin` 允许跨域。
