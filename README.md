# SmartDine 智膳云 - AI智能餐饮平台

基于 Spring Cloud Alibaba 微服务架构构建的新一代智能餐饮平台，集成六大AI Agent系统，为餐饮商家提供全方位的智能化经营解决方案。

## 技术架构

### 微服务架构
- **网关层**: Spring Cloud Gateway + Sentinel 流量控制
- **注册中心**: Nacos 服务注册与配置管理
- **业务服务**: Spring Boot 3.2.4 + Java 17
- **AI服务**: LangChain4j + 多模型支持 (OpenAI/通义千问)

### 核心技术栈
- **核心框架**: Spring Boot 3.2.4 + Spring Cloud Alibaba 2023.0.1.0
- **数据持久层**: MyBatis + MySQL 8.0
- **数据库连接池**: Alibaba Druid
- **缓存**: Redis + Spring Cache
- **安全认证**: JWT (Json Web Token)
- **API 文档**: Knife4j (Swagger)
- **对象存储**: 阿里云 OSS
- **AI 能力**: LangChain4j + 多模型支持
- **聊天记录存储**: MongoDB
- **实时通信**: WebSocket
- **定时任务**: Spring Scheduling
- **其他工具**: Lombok、PageHelper、Fastjson、Apache POI

## 项目结构

```
smartdine-cloud
├── smartdine-common          # 公共模块：工具类、常量、异常、配置属性等
│   ├── constant              # 常量类
│   ├── context               # 线程上下文（BaseContext）
│   ├── enumeration           # 枚举类型
│   ├── exception             # 自定义异常
│   ├── json                  # JSON 序列化配置
│   ├── properties            # 配置属性类
│   ├── result                # 统一响应结果封装
│   └── utils                 # 工具类（JWT、OSS、HTTP、微信支付等）
│
├── smartdine-domain          # 实体模块：DTO、Entity、VO
│   ├── dto                   # 数据传输对象
│   ├── entity                # 数据库实体类
│   └── vo                    # 视图对象
│
├── smartdine-service         # 业务服务模块：核心业务逻辑
│   ├── aicsr                 # AI 智能客服模块
│   ├── annotation            # 自定义注解
│   ├── aspect                # AOP 切面（公共字段自动填充）
│   ├── config                # 配置类（Redis、WebSocket、WebMvc）
│   ├── controller            # 控制层（admin 管理端 / user 用户端 / notify 回调）
│   ├── handler               # 全局异常处理器
│   ├── interceptor           # JWT 令牌拦截器
│   ├── mapper                # 数据访问层
│   ├── service               # 业务逻辑层
│   ├── task                  # 定时任务
│   └── websocket             # WebSocket 服务
│
├── smartdine-gateway         # API 网关：路由、限流、日志
│   └── config                # 网关配置、全局过滤器
│
└── smartdine-ai-agent        # AI Agent 服务：六大智能Agent
    ├── core                  # Agent 核心接口与实现
    │   └── agent             # 六大Agent定义
    ├── client                # Feign 客户端（调用业务服务）
    ├── config                # AI 配置、MongoDB记忆存储
    └── controller            # Agent API 接口
```

## 六大AI Agent系统

### 1. 核心Agent (SmartDineAgent)
- 智能路由：理解用户意图，分发到对应Agent
- 上下文理解：维护多轮对话上下文
- 结果整合：整合多个Agent结果给出统一回复

### 2. 经营顾问Agent (BusinessAdvisorAgent)
- 营业额分析：趋势、同比环比、时段分布
- 成本控制：食材/人力/运营成本分析
- 利润优化：菜品结构优化建议
- 经营策略：定价策略、套餐设计

### 3. 调度指挥Agent (DispatchOrchestratorAgent)
- 智能派单：基于位置、负载、路线优化
- 路径优化：实时计算最优配送路线
- 运力预测：高峰时段运力调配
- 异常处理：自动识别配送异常

### 4. 用户服务Agent (CustomerServiceAgent)
- 订单查询：状态、配送进度、历史订单
- 售后处理：退款、退货、补偿
- 投诉处理：记录、安抚、跟进
- 智能推荐：菜品、商家推荐

### 5. 营销创意Agent (MarketingCreativeAgent)
- 活动策划：满减、折扣、套餐活动
- 文案生成：朋友圈、短视频、海报文案
- 推广策略：线上/线下推广方案
- 用户运营：拉新、促活、留存

### 6. 数据洞察Agent (DataInsightAgent)
- 报表解读：自动解读经营报表
- 趋势预测：营业额、订单量预测
- 异常检测：自动识别数据异常
- 用户洞察：行为、偏好、生命周期分析

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
- **AI Agent中心**: 六大AI Agent智能助手

### 用户端 (User / C端)
- **微信登录**: 基于微信小程序的 OAuth 登录
- **地址簿**: 新增、查询、修改、删除、设置默认地址
- **菜品浏览**: 按分类查询菜品（含 Redis 缓存）
- **套餐浏览**: 按分类查询套餐（含 Spring Cache 缓存）
- **购物车**: 添加、展示、清空、删除商品
- **订单管理**: 提交订单、订单支付（模拟）、历史订单查询、取消订单、再来一单、催单
- **店铺状态**: 查询店铺营业状态

## 环境要求

- JDK 17+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+
- MongoDB 5.0+（AI Agent功能需要）
- Nacos 2.2+（服务注册与配置中心）

## 快速开始

### 1. 克隆项目

```bash
git clone <repository-url>
cd smartdine-cloud
```

### 2. 初始化数据库

创建 MySQL 数据库 `smartdine`，并导入数据库初始化脚本。

### 3. 启动 Nacos

```bash
docker run -d -p 8848:8848 -p 9848:9848 --name nacos nacos/nacos-server:v2.2.3
```

### 4. 配置应用

编辑 `smartdine-service/src/main/resources/application-dev.yml`：

```yaml
smartdine:
  datasource:
    host: localhost
    port: 3306
    database: smartdine
    username: root
    password: your_password
```

编辑 `smartdine-ai-agent/src/main/resources/application.yml`：

```yaml
smartdine:
  ai:
    api-key: your_ai_api_key
    base-url: https://api.openai.com/v1
    model: gpt-4o-mini
```

### 5. 编译运行

```bash
mvn clean package

# 启动业务服务
java -jar smartdine-service/target/smartdine-service-1.0-SNAPSHOT.jar

# 启动AI Agent服务
java -jar smartdine-ai-agent/target/smartdine-ai-agent-1.0-SNAPSHOT.jar

# 启动网关
java -jar smartdine-gateway/target/smartdine-gateway-1.0-SNAPSHOT.jar
```

或直接通过 IDE 运行各模块的 Application 类。

### 6. 访问接口文档

启动后访问：http://localhost:8080/doc.html

## 配置说明

### 核心配置文件

| 文件 | 说明 |
|------|------|
| `application.yml` | 主配置，包含端口、数据库、Redis、MongoDB、JWT、OSS 等 |
| `application-dev.yml` | 开发环境配置，包含数据库密码、微信 AppID 等敏感信息 |
| `application.properties` | AI 大模型 API Key 配置 |
| `prompts/*.txt` | AI Agent 角色设定提示词 |

### JWT 配置

```yaml
smartdine:
  jwt:
    admin-secret-key: smartdineadmin      # 管理端密钥
    admin-ttl: 7200000                    # 管理端令牌过期时间（毫秒）
    admin-token-name: token               # 管理端令牌请求头名称
    user-secret-key: smartdineuser        # 用户端密钥
    user-ttl: 7200000                     # 用户端令牌过期时间（毫秒）
    user-token-name: authentication       # 用户端令牌请求头名称
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

### AI Agent接口 (`/api/v1/ai/**`)
- `POST /api/v1/ai/chat` - 核心Agent聊天（流式）
- `POST /api/v1/ai/business-advisor` - 经营顾问Agent
- `POST /api/v1/ai/customer-service` - 用户服务Agent
- `POST /api/v1/ai/marketing` - 营销创意Agent
- `POST /api/v1/ai/data-insight` - 数据洞察Agent

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
- 网关层统一鉴权，防止未授权访问

## 自定义 AI Agent

1. 修改 `smartdine-ai-agent/src/main/resources/prompts/*.txt` 调整 Agent 角色设定
2. 修改 `smartdine-ai-agent/src/main/java/com/smartdine/ai/agent/core/agent/*.java` 扩展 Agent 能力
3. 重启 smartdine-ai-agent 服务即可生效

## 前端项目

前端代码见配套项目 `smartdine-frontend`。

## 许可证

本项目采用 [Apache License 2.0](LICENSE) 开源协议。
