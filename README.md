# 华水校园生活服务平台

面向华北水利水电大学校园生活场景的一体化服务平台，包含宿舍管理、报修、保洁任务、考勤、请假、水电缴费、评价问卷、消息公告、认证权限、系统管理等能力。

项目采用前后端分离与微服务架构：

- 前端：Vue 3 + TypeScript + Vite + Element Plus
- 后端：Spring Boot 3 + Spring Cloud + Spring Cloud Alibaba + MyBatis-Plus
- 注册中心/配置中心：Nacos
- 鉴权：Sa-Token
- 中间件：MySQL、Redis、RabbitMQ、MinIO、XXL-JOB

---

## 一、项目结构

```text
huashui-campus-life/
├── huashui-gateway/                 # 网关，端口 8080
├── huashui-auth/                    # 认证授权，端口 8081
├── huashui-system/                  # 系统管理，端口 8087
├── huashui-storage/                 # 文件存储，端口 8087
├── huashui-dormitory/               # 宿舍资源，端口 8091
├── huashui-utility/                 # 水电缴费，端口 8092
├── huashui-evaluation/              # 评价问卷，端口 8094
├── huashui-attendance-service/      # 考勤服务，端口 8101
├── huashui-leave-service/           # 请假服务，端口 8102
├── huashui-task/                    # 报修/保洁任务，端口 8103
├── huashui-message/                 # 消息公告，端口 8112
├── huashui-api/                     # 内部 Feign API 契约
├── huashui-common/                  # 公共组件与工具
├── huashui-ui/                      # Vue 3 前端
├── docs/                            # 接口测试清单等文档
└── 华水校园生活平台说明文档/         # 部署说明与 docker-compose
```

---

## 二、服务模块

| 模块 | 服务名 | 端口 | 业务库 | 主要功能 |
|------|--------|------|--------|----------|
| `huashui-gateway` | `huashui-gateway` | 8080 | — | 统一入口、路由转发、Sa-Token 鉴权 |
| `huashui-auth` | `huashui-auth` | 8081 | `huashui_rbac` | 登录、用户、角色、菜单、邮箱验证码 |
| `huashui-system` | `huashui-system` | 8087 | `huashui_system` | 字典、配置、登录日志、操作日志、异常日志 |
| `huashui-storage` | `huashui-storage` | 8087 | `huashui_storage` | 文件上传、MinIO 存储 |
| `huashui-dormitory` | `huashui-dormitory` | 8091 | `huashui_dormitory` | 校区、楼栋、房间、床位、住宿记录 |
| `huashui-utility` | `huashui-utility` | 8092 | `huashui_utility` | 水费、电费、缴费记录 |
| `huashui-evaluation` | `huashui-evaluation` | 8094 | `huashui_evaluation` | 评价问卷、答题、统计 |
| `huashui-attendance-service` | `huashui-attendance-service` | 8101 | `huashui_attendance` | 考勤打卡、考勤统计 |
| `huashui-leave-service` | `huashui-leave-service` | 8102 | `huashui_leave` | 请假申请与审批 |
| `huashui-task` | `huashui-task` | 8103 | `huashui_task` | 报修工单、保洁任务、任务模板 |
| `huashui-message` | `huashui-message-notification` | 8112 | `huashui_message` | 系统公告、站内消息 |

---

## 三、前端项目

前端位于 `huashui-ui/`。

### 技术栈

- Vue 3
- TypeScript
- Vite
- Element Plus
- Pinia
- Vue Router
- Axios

### 启动方式

```bash
cd huashui-ui
pnpm install
pnpm dev
```

默认开发地址：

```text
http://127.0.0.1:4173/
```

> 注意：当前项目将 Vite 端口配置为 `4173`，而不是常见的 `5173`。原因是 `5173` 可能被 Windows 端口排除范围占用，导致 `listen EACCES`。

前端开发服务器会将 `/api` 代理到：

```text
http://127.0.0.1:8080
```

生产构建：

```bash
cd huashui-ui
pnpm build
```

构建产物位于：

```text
huashui-ui/dist/
```

---

## 四、后端启动

### 1. 前置依赖

- JDK 17+
- Maven 3.9+
- 可用 Nacos
- 可用 MySQL
- 可用 Redis
- 可用 RabbitMQ
- 可选 MinIO、XXL-JOB

### 2. 启动云服务隧道

项目文档建议通过 SSH 隧道连接云 MySQL 与 Nacos。

示例：

```bat
ssh -L 3308:localhost:3306 -L 18848:localhost:8848 -L 19848:localhost:9848 -L 18080:localhost:8080 ubuntu@43.143.130.165 -N
```

> 具体隧道端口请以当前服务器配置和 `启动华水隧道.bat` 为准。

### 3. 启动本地中间件

可使用 `华水校园生活平台说明文档/docker-compose.yml` 启动 Redis、RabbitMQ、MinIO、XXL-JOB 等中间件。

### 4. 启动后端服务

推荐先启动注册中心，再启动网关和业务服务。

#### 启动网关

```bash
cd huashui-gateway
mvn spring-boot:run
```

#### 启动认证服务

```bash
cd huashui-auth
mvn spring-boot:run
```

#### 启动其他服务

可按需启动，例如：

```bash
cd huashui-dormitory
mvn spring-boot:run
```

也可以在项目根目录按模块编译：

```bash
mvn -pl huashui-gateway,huashui-auth,huashui-dormitory -am -DskipTests compile
```

---

## 五、配置与账号

### Nacos

| 项目 | 值 |
|------|-----|
| 地址 | `127.0.0.1:18848` |
| 用户名 | `nacos` |
| 密码 | `Chc23pray`（以实际配置为准） |

### MySQL

| 项目 | 值 |
|------|-----|
| 云 MySQL | `127.0.0.1:3308` |
| 本地 MySQL | `127.0.0.1:3309` |
| 用户名 | `huashuiNB666` |
| 密码 | `huashuiNB666pw` |

### 中间件

| 服务 | 地址 | 用户名 | 密码 |
|------|------|--------|------|
| Redis | `localhost:6379` | — | 无密码 |
| RabbitMQ | `localhost:15672` | `huashuiNB666` | `huashuiNB666pw` |
| MinIO | `localhost:9001` | `huashuiNB666` | `huashuiNB666pw` |
| XXL-JOB | `localhost:8200/xxl-job-admin` | `admin` | `123456` |

---

## 六、数据库

主要业务库：

| 数据库 | 说明 |
|--------|------|
| `huashui_rbac` | 用户、角色、菜单、用户角色、角色菜单 |
| `huashui_system` | 字典、配置、日志 |
| `huashui_storage` | 文件记录 |
| `huashui_dormitory` | 校区、楼栋、房间、床位、住宿记录、楼栋管理员 |
| `huashui_repair` | 报修工单 |
| `huashui_utility` | 水电余额、缴费订单 |
| `huashui_evaluation` | 评价问卷、题目、答案、统计 |
| `huashui_attendance` | 考勤记录 |
| `huashui_leave` | 请假申请 |
| `huashui_task` | 报修任务、保洁任务、任务模板 |
| `huashui_message` | 系统公告、站内消息、已读记录 |

---

## 七、前端功能

- 登录：账号登录、邮箱验证码登录、图形验证码
- 角色差异化菜单
- 个人中心：头像上传、修改密码、绑定邮箱
- 系统管理：字典、配置、日志
- 宿舍管理：校区、楼栋、房间、住宿记录、我的宿舍
- 生活服务：水费、电费、缴费记录
- 考勤请假：我的考勤、考勤管理、请假管理
- 维修与保洁：我的报修、报修管理、我的维修、保洁任务、任务模板
- 评价问卷：问卷管理、待我评价、评价统计
- 消息公告：公告管理、通知中心、我的消息

---

## 八、常用角色

| 角色编码 | 说明 |
|----------|------|
| `STUDENT` | 学生 |
| `CLEANER` | 保洁人员 |
| `REPAIRER` | 维修人员 |
| `DORM_MANAGER` | 宿舍管理员 |
| `SUPER_ADMIN` | 超级管理员 |

---

## 九、文档

- 本地部署指南：`华水校园生活平台说明文档/本地环境部署指南.md`
- 接口测试清单：`docs/接口测试清单.md`
- 前端说明：`huashui-ui/README.md`

---

## 十、常见问题

### 1. 前端启动失败：`listen EACCES: permission denied 127.0.0.1:5173`

本机 `5173` 可能被 Windows 端口排除范围占用。项目已改用 `4173`：

```text
http://127.0.0.1:4173/
```

### 2. 访问 `localhost` 打不开容器

检查：

```text
C:\Users\<你的用户名>\.wslconfig
```

是否包含：

```ini
[wsl2]
networkingMode=mirrored
```

修改后执行：

```powershell
wsl --shutdown
```

### 3. 云 MySQL 或 Nacos 连不上

确认 SSH 隧道已启动并保持窗口打开，且服务器安全组允许 22 端口访问。

### 4. Vite 首次启动报 `EPERM` 写入 `node_modules/.vite-temp`

在某些受限终端环境中会出现。请使用完整本地终端或提升权限后启动。