# 苍穹外卖 (Sky Take Out)

一个基于 Spring Boot 3 构建的外卖订餐平台后端系统，在原有功能基础上集成了基于通义大模型的 AI 智能客服助手。

## 技术栈

- **核心框架**: Spring Boot 3.2.4
- **数据持久层**: MyBatis + MySQL 8.0
- **数据库连接池**: Alibaba Druid
- **缓存**: Redis + Spring Cache
- **安全认证**: JWT (Json Web Token)
- **API 文档**: Knife4j (Swagger)
- **对象存储**: 阿里云 OSS
- **AI 能力**: LangChain4j + 阿里通义千问 (DashScope)
- **聊天记录存储**: MongoDB
- **实时通信**: WebSocket
- **定时任务**: Spring Scheduling
- **其他工具**: Lombok、PageHelper、Fastjson、Apache POI

## 项目结构

本项目采用 Maven 多模块架构：

```
sky-take-out
├── sky-common          # 公共模块：工具类、常量、异常、配置属性等
│   ├── constant        # 常量类
│   ├── context         # 线程上下文（BaseContext）
│   ├── enumeration     # 枚举类型
│   ├── exception       # 自定义异常
│   ├── json            # JSON 序列化配置
│   ├── properties      # 配置属性类
│   ├── result          # 统一响应结果封装
│   └── utils           # 工具类（JWT、OSS、HTTP、微信支付等）
│
├── sky-pojo            # 实体模块：DTO、Entity、VO
│   ├── dto             # 数据传输对象
│   ├── entity          # 数据库实体类
│   └── vo              # 视图对象
│
└── sky-server          # 业务服务模块：核心业务逻辑
    ├── aicsr           # AI 智能客服模块
    ├── annotation      # 自定义注解
    ├── aspect          # AOP 切面（公共字段自动填充）
    ├── config          # 配置类（Redis、WebSocket、WebMvc）
    ├── controller      # 控制层（admin 管理端 / user 用户端 / notify 回调）
    ├── handler         # 全局异常处理器
    ├── interceptor     # JWT 令牌拦截器
    ├── mapper          # 数据访问层
    ├── service         # 业务逻辑层
    ├── task            # 定时任务
    └── websocket       # WebSocket 服务
```

## 功能模块

### 管理端 (Admin)
- **员工管理**: 登录、新增、分页查询、状态修改、编辑、密码修改
- **分类管理**: 菜品分类 / 套餐分类的增删改查、启用禁用
- **菜品管理**: 新增菜品（含口味）、分页查询、删除、修改、启用禁用
- **套餐管理**: 新增套餐、分页查询、删除、修改、启用禁用
- **订单管理**: 订单搜索、状态统计、接单、取消、派送、完成、拒单
- **数据统计**: 营业额统计、用户统计、订单统计、销量排名 TOP10、数据导出 Excel
- **工作台**: 今日营业数据、订单/菜品/套餐总览
- **店铺管理**: 营业状态设置与查询
- **文件上传**: 阿里云 OSS 图片上传
- **AI 智能客服**: 基于通义千问的流式对话，支持数据分析工具调用

### 用户端 (User / C端)
- **微信登录**: 基于微信小程序的 OAuth 登录
- **地址簿**: 新增、查询、修改、删除、设置默认地址
- **菜品浏览**: 按分类查询菜品（含 Redis 缓存）
- **套餐浏览**: 按分类查询套餐（含 Spring Cache 缓存）
- **购物车**: 添加、展示、清空、删除商品
- **订单管理**: 提交订单、订单支付（模拟）、历史订单查询、取消订单、再来一单、催单
- **店铺状态**: 查询店铺营业状态

### AI 智能客服 (AICSR)
- 基于 LangChain4j 集成阿里通义千问大模型
- 支持流式输出 (SSE)
- 基于 MongoDB 的聊天记录持久化
- 支持工具调用：查询营业数据、查询销量排名
- 可通过修改 `CSR.txt` 和 `ChatTools.java` 自定义 AI 助手行为

## 环境要求

- JDK 17+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+
- MongoDB 5.0+（AI 客服功能需要）

## 快速开始

### 1. 克隆项目

```bash
git clone <repository-url>
cd sky-take-out
```

### 2. 初始化数据库

创建 MySQL 数据库 `sky_take_out`，并导入数据库初始化脚本（如有）。

### 3. 配置应用

编辑 `sky-server/src/main/resources/application-dev.yml`：

```yaml
sky:
  datasource:
    host: localhost
    port: 3306
    database: sky_take_out
    username: root
    password: your_password
```

编辑 `sky-server/src/main/resources/application.properties`：

```properties
langchain4j.community.dashscope.chat-model.api-key=your_dashscope_api_key
```

### 4. 配置外部服务（可选）

如需使用完整功能，请配置以下服务：
- **阿里云 OSS**: 在 `application.yml` 中配置 endpoint、bucketName、region，并设置环境变量 `OSS_ACCESS_KEY_ID` 和 `OSS_ACCESS_KEY_SECRET`
- **微信小程序**: 在 `application-dev.yml` 中配置 `sky.wechat.appid` 和 `sky.wechat.secret`
- **百度地图**: 在 `application-dev.yml` 中配置 `sky.baidu.ak`（用于配送范围校验）
- **微信支付**: 已跳过，如需开启请配置相关证书和参数

### 5. 编译运行

```bash
mvn clean package
java -jar sky-server/target/sky-server-1.0-SNAPSHOT.jar
```

或直接通过 IDE 运行 `SkyApplication.java`。

### 6. 访问接口文档

启动后访问：http://localhost:8080/doc.html

## 配置说明

### 核心配置文件

| 文件 | 说明 |
|------|------|
| `application.yml` | 主配置，包含端口、数据库、Redis、MongoDB、JWT、OSS 等 |
| `application-dev.yml` | 开发环境配置，包含数据库密码、微信 AppID 等敏感信息 |
| `application.properties` | AI 大模型 API Key 配置 |
| `file/CSR.txt` | AI 客服角色设定提示词 |

### JWT 配置

```yaml
sky:
  jwt:
    admin-secret-key: itcast      # 管理端密钥
    admin-ttl: 720000000          # 管理端令牌过期时间（毫秒）
    admin-token-name: token       # 管理端令牌请求头名称
    user-secret-key: itheima      # 用户端密钥
    user-ttl: 720000000           # 用户端令牌过期时间（毫秒）
    user-token-name: authentication # 用户端令牌请求头名称
```

## API 接口概览

### 管理端接口 (`/admin/**`)
- `POST /admin/employee/login` - 员工登录
- `GET /admin/employee/page` - 员工分页查询
- `POST /admin/category` - 新增分类
- `POST /admin/dish` - 新增菜品
- `POST /admin/setmeal` - 新增套餐
- `GET /admin/order/conditionSearch` - 订单搜索
- `GET /admin/workspace/businessData` - 工作台数据
- `POST /admin/aicsr/chatbot` - AI 智能客服对话（流式）

### 用户端接口 (`/user/**`)
- `POST /user/user/login` - 微信登录
- `GET /user/category/list` - 查询分类
- `GET /user/dish/list` - 查询菜品
- `GET /user/setmeal/list` - 查询套餐
- `POST /user/shoppingCart/add` - 添加购物车
- `POST /user/order/submit` - 提交订单
- `PUT /user/order/payment` - 订单支付

## 定时任务

| 任务 | Cron 表达式 | 说明 |
|------|------------|------|
| 超时订单处理 | `0 * * * * *` | 每分钟处理支付超时订单 |
| 派送超时处理 | `0 0 3 * * ?` | 每天凌晨 3 点处理派送中超时订单 |

## 缓存策略

- **菜品缓存**: Redis 手动管理，按分类 ID 缓存，增删改时清理缓存
- **套餐缓存**: Spring Cache (`@Cacheable` / `@CacheEvict`)，按分类 ID 缓存
- **店铺状态**: Redis 存储营业状态

## 安全说明

- 密码采用双重策略：兼容历史 MD5，新密码使用 BCrypt 加密
- 管理端和用户端分别使用独立的 JWT 密钥和令牌名称
- 敏感配置（密码、API Key）通过外部化配置或环境变量管理

## 自定义 AI 助手

1. 修改 `sky-server/src/main/resources/file/CSR.txt` 调整 AI 角色设定
2. 修改 `sky-server/src/main/java/com/sky/aicsr/tool/ChatTools.java` 添加新的工具方法
3. 重启服务即可生效

## 前端项目

前端代码见配套项目 `sky-take-out-fe`。

## 许可证

本项目采用 [Apache License 2.0](LICENSE) 开源协议。
