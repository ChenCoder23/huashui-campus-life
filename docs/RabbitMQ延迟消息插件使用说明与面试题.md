# RabbitMQ 延迟消息插件使用说明与面试题

> 适用项目：华水校园生活平台（huashui-campus-life）
> 涉及模块：`huashui-common`、`huashui-evaluation`、`huashui-message`
> 使用插件：`rabbitmq_delayed_message_exchange`

---

## 一、背景：为什么要用延迟消息

本项目有两个典型的“到点执行”场景：

1. 评价问卷：到 `startTime` 自动开始，到 `endTime` 自动结束。
2. 校园公告：到 `publishTime` 自动发布（待发布 → 已发布）。

这类需求本质是“延迟一段时间后执行某个动作”，常见实现方式有：

| 方案 | 优点 | 缺点 |
| --- | --- | --- |
| RabbitMQ TTL + 死信队列(DLX) | 无需额外插件 | 队头阻塞、时间精度差、旧消息堵新消息 |
| RabbitMQ 延迟插件 | 按消息独立延迟、用法简单 | 需装插件、性能弱于原生队列 |
| RocketMQ 延迟消息 | 原生支持、可靠 | 只支持固定延迟等级 |
| Redis ZSet + 定时轮询 | 精确可控、可持久化 | 需要自研轮询、有扫描成本 |
| 数据库 + 定时任务(XXL-Job) | 时间以 DB 为准、幂等 | 有轮询间隔 |

本项目最初使用的是第一种（TTL + DLX），因为存在两个 bug，现改为第二种（延迟插件）。

---

## 二、插件介绍

### 1. 是什么

`rabbitmq_delayed_message_exchange` 是 RabbitMQ 官方团队维护的一个插件，它给 RabbitMQ 增加了一种新的交换机类型：

```
x-delayed-message
```

使用这种交换机后，生产者发送消息时在消息头里带上 `x-delay`（单位毫秒），交换机不会立刻投递，而是先把消息暂存，等延迟时间到了，再按原来的路由键把消息投递到绑定它的队列。

### 2. 与 TTL + DLX 的本质区别

- **TTL + DLX**：延迟是通过“消息过期 → 进入死信交换机”实现的。但 RabbitMQ 只检查**队头**消息的 TTL，队列是 FIFO 的。
- **延迟插件**：延迟是按**每一条消息**独立计算的，不依赖队列顺序，因此**没有队头阻塞**。

### 3. 队头阻塞（head-of-line blocking）是什么

在 TTL + DLX 方案里，假设同一个队列先后进入两条消息：

```
队头：A 消息，延迟 60s（旧消息，执行时间晚）
队尾：B 消息，延迟 5s （新消息，执行时间早）
```

由于 RabbitMQ 只判断队头 A 是否过期，B 必须等 A 过期（60s 后）才能被投递，于是 B 的“5s 执行”被 A 堵了 55s。**这就是本项目 Bug 2 的根因**。

延迟插件则会把 A、B 各自按自己的延迟时间投递，互不影响。

---

## 三、安装与启用

```bash
# 1. 启用插件
rabbitmq-plugins enable rabbitmq_delayed_message_exchange

# 2. 查看是否启用
rabbitmq-plugins list | grep delayed_message_exchange

# 3. 若使用 Docker，可在容器里执行，或在 Dockerfile 里加：
# RUN rabbitmq-plugins enable rabbitmq_delayed_message_exchange
```

> 本项目 RabbitMQ 地址在 `RabbitConfig` 里硬编码为 `172.25.118.113:5672`，
> 需要在该台 broker 上启用插件，否则声明 `x-delayed-message` 交换机会报错
> `unknown exchange type 'x-delayed-message'`。

---

## 四、通用使用方法（Spring AMQP）

### 1. 声明延迟交换机

```java
Map<String, Object> args = new HashMap<>();
// 底层按 direct 方式路由，也可以配 topic / fanout
args.put("x-delayed-type", "direct");

CustomExchange exchange = new CustomExchange(
        "delay.exchange",      // 交换机名
        "x-delayed-message",   // 插件提供的类型
        true,                  // durable
        false,                 // autoDelete
        args);
```

### 2. 业务队列直接绑定到延迟交换机

```java
Queue queue = QueueBuilder.durable("business.queue").build();

Binding binding = BindingBuilder
        .bind(queue)
        .to(exchange)
        .with("business.key")
        .noargs();   // CustomExchange 需要以 noargs() 结尾
```

### 3. 发送消息时设置 x-delay（毫秒）

```java
long delay = Duration.between(LocalDateTime.now(), executeTime).toMillis();

rabbitTemplate.convertAndSend(
        "delay.exchange",
        "business.key",
        event,
        message -> {
            message.getMessageProperties().setHeader("x-delay", delay);
            return message;
        });
```

### 4. 消费端正常监听

```java
@RabbitListener(queues = "business.queue")
public void handle(SomeEvent event) {
    // 到点后收到消息
}
```

---

## 五、本项目里的使用方式

### 1. 涉及文件

| 文件 | 作用 |
| --- | --- |
| `huashui-common/.../constants/MQConstants.java` | 交换机 / 队列 / 路由键常量 |
| `huashui-common/.../config/RabbitConfig.java` | 声明 `x-delayed-message` 交换机与绑定 |
| `huashui-common/.../domain/mqMessage/DelayedMessage.java` | 延迟消息统一接口（携带 executeTime） |
| `huashui-common/.../domain/mqMessage/EvaluationEvent.java` | 评价事件消息体 |
| `huashui-message/.../domain/event/NoticePublishEvent.java` | 公告发布事件消息体 |
| `huashui-evaluation/.../util/DelayMessageUtil.java` | 评价模块延迟发送工具 |
| `huashui-message/.../util/DelayMessageUtil.java` | 公告模块延迟发送工具 |
| `huashui-evaluation/.../service/impl/EvaluationQuestionnaireServiceImpl.java` | 问卷开始/结束消费逻辑 |
| `huashui-message/.../service/impl/SystemNoticeServiceImpl.java` | 公告定时发布消费逻辑 |
| `huashui-message/.../Enums/NoticeStatus.java` | 公告状态（新增 `PENDING` 待发布） |

### 2. 交换机与绑定（RabbitConfig）

```java
@Bean
public CustomExchange evaluationDelayExchange() {
    return buildDelayedExchange(MQConstants.DELAY_EXCHANGE);
}

@Bean
public CustomExchange noticeDelayExchange() {
    return buildDelayedExchange(MQConstants.DELAY_EXCHANGE_NOTICE);
}

private CustomExchange buildDelayedExchange(String name) {
    Map<String, Object> args = new HashMap<>();
    args.put("x-delayed-type", "direct");
    return new CustomExchange(name, "x-delayed-message", true, false, args);
}
```

业务队列直接绑定到延迟交换机，不再有“延迟队列 + 死信交换机”。

### 3. 发送延迟消息（DelayMessageUtil）

```java
public void sendDelayMessage(String exchange, String routingKey, Object event, LocalDateTime executeTime) {
    // 统一到秒级，避免数据库 datetime 精度不一致
    LocalDateTime target = executeTime.truncatedTo(ChronoUnit.SECONDS);
    long delay = Math.max(Duration.between(LocalDateTime.now(), target).toMillis(), 0);

    // 消息体回填“计划执行时间”，消费端据此做等值校验
    if (event instanceof DelayedMessage delayedMessage) {
        delayedMessage.setExecuteTime(target);
    }

    rabbitTemplate.convertAndSend(exchange, routingKey, event, message -> {
        message.getMessageProperties().setHeader("x-delay", delay);
        return message;
    });
}
```

### 4. 消息体携带执行时间（DelayedMessage）

```java
public interface DelayedMessage {
    LocalDateTime getExecuteTime();
    void setExecuteTime(LocalDateTime executeTime);

    // 消费端与 DB 时间做等值校验，统一按秒比较
    default boolean isSameExecuteTime(LocalDateTime dbTime) {
        if (getExecuteTime() == null || dbTime == null) {
            return false;
        }
        return getExecuteTime().truncatedTo(ChronoUnit.SECONDS)
                .equals(dbTime.truncatedTo(ChronoUnit.SECONDS));
    }
}
```

- `EvaluationEvent`、`NoticePublishEvent` 都实现该接口，新增 `executeTime` 字段。
- **为什么不用 `LocalDateTime.now()` 比较？** 因为 MQ 有网络延迟，消费时刻的墙钟与数据库时间对不齐，容易误判。用“消息计划执行时间”与“DB 当前时间”做等值比较，稳定可靠（这正是本项目 Bug 1 的修法）。

### 5. 消费端校验（以评价开始为例）

```java
public void start(EvaluationEvent event) {
    EvaluationQuestionnaire q = getById(event.getQuestionnaireId());
    if (q == null) return;
    if (q.getStatus() != QuestionStatus.WAITING) return; // 幂等
    if (!event.isSameExecuteTime(q.getStartTime())) return; // 陈旧消息丢弃
    // ... 执行开始逻辑
}
```

公告定时发布同理：

```java
public void publish(NoticePublishEvent event) {
    SystemNotice notice = getById(event.getNoticeId());
    if (notice == null) return;
    if (notice.getStatus() != NoticeStatus.PENDING) return;   // 幂等
    if (!event.isSameExecuteTime(notice.getPublishTime())) return; // 陈旧消息丢弃
    notice.setStatus(NoticeStatus.PUBLISHED);
    updateById(notice);
}
```

### 6. 状态机

评价问卷：

```
WAITING(未开始) --startTime--> RUNNING(进行中) --endTime--> FINISHED(已结束)
```

校园公告（新增“待发布”）：

```
DRAFT(草稿) --设置发布时间--> PENDING(待发布) --publishTime--> PUBLISHED(已发布)
```

- `NoticeStatus` 新增 `PENDING("PENDING", "待发布")`。
- 创建公告即进入 `PENDING`；修改发布时间时重发延迟消息并保持 `PENDING`。

### 7. 部署/升级注意

1. **必须启用插件**，否则声明交换机失败。
2. **旧拓扑要删除重建**：原先的 `evaluation.start.delay.queue`、`evaluation.finish.delay.queue`、`notice.delay.queue`、`evaluation.dlx.exchange`、`notice.dlx.exchange` 等 TTL/DLX 队列和交换机会与新的延迟交换机冲突，需要先在 RabbitMQ 管理台删除，再启动服务重新声明。
3. 消费端队列名已调整：公告业务队列由 `notice.dlx.queue` 改为 `notice.publish.queue`。

---

## 六、面试高频问题整理

### Q1. 什么是延迟队列？有哪些实现方式？

延迟队列是“消息发送后不立即投递，而是延迟指定时间后再投递”的机制。常见实现：

- RabbitMQ：TTL + DLX、`x-delayed-message` 插件
- RocketMQ：原生延迟消息（固定延迟等级）
- Kafka：通常不支持，需借助时间轮/外部存储
- Redis：ZSet 按时间戳排序 + 定时任务弹出
- 数据库 + 定时任务（XXL-Job / Quartz）轮询

### Q2. RabbitMQ 延迟插件的原理是什么？

插件注册了一个 `x-delayed-message` 类型的交换机。消息到达交换机后，先被存入一个延迟索引（基于 Mnesia，数据本身持久化到磁盘），按 `x-delay` 计算到期时间；到期后按消息原有的路由键投递到绑定队列。

### Q3. TTL + DLX 和 x-delayed-message 的区别？

- TTL + DLX：靠消息过期进死信队列，延迟受队列头影响，有**队头阻塞**；时间精度和消息顺序易出问题。
- 延迟插件：每条消息独立延迟，互不阻塞，用法更简单，但需要安装插件。

### Q4. 什么是队头阻塞（head-of-line blocking）？为什么会发生？

RabbitMQ 对队列只检查队头消息的 TTL，且队列是 FIFO。若队头消息延迟很长，后面延迟更短的消息也必须等队头过期才能投递。延迟插件按消息独立计时，没有这个问题。

### Q5. per-message TTL 和 Queue TTL 的区别？

- per-message TTL：每条消息单独设置过期时间，但**只在消息到达队头时才被检查**，所以会有队头阻塞。
- Queue TTL：整个队列统一过期时间，所有消息一致，通常配合死信队列做“固定延迟”。

### Q6. x-delay 的单位是什么？如何计算？

单位是**毫秒**。通常这样计算：

```java
long delay = Duration.between(now, executeTime).toMillis();
delay = Math.max(delay, 0);
```

### Q7. x-delayed-type 是什么？

它指定延迟交换机底层按哪种路由方式工作，可选 `direct`、`topic`、`fanout`，用法与原生对应类型一致。

### Q8. 延迟插件可靠吗？有什么注意事项？

- 延迟索引基于 Mnesia，消息会持久化，但**高可用/集群下插件稳定性不如原生队列**，大量延迟消息时会有性能开销。
- 延迟精度是秒级/毫秒级，不是精确到纳秒。
- 生产上要控制延迟消息数量，并做好消费幂等。

### Q9. 如何保证延迟消息消费幂等？

- 消费端先用**状态字段**做判断（如公告只有 `PENDING` 才发布，评价只有 `WAITING` 才开始）。
- 或引入**唯一业务 ID + 去重表 / Redis SETNX**。
- 本项目还加了 `executeTime` 等值校验，把“已经过期的旧消息”直接丢弃。

### Q10. 为什么本项目消息体要带 executeTime？

因为 MQ 有网络延迟，消费时用 `LocalDateTime.now()` 与数据库时间比较不可靠。改为在消息体里携带“计划执行时间”，消费端与数据库当前时间做**等值校验**，能精准识别“时间被修改后的旧消息”，从而稳定地丢弃旧消息。

### Q11. executeTime 等值校验为什么要注意时间精度？

`LocalDateTime` 默认带纳秒，而数据库 `datetime` 常是秒级/毫秒级。直接 `equals` 会因精度不一致而永远不相等，导致任务不执行。所以两边统一 `truncatedTo(ChronoUnit.SECONDS)` 后再比较。

### Q12. 本项目为什么弃用 TTL+DLX，改用延迟插件？

原实现存在两个 bug：

1. 消费端用墙钟与 DB 时间比较，受网络延迟影响会误判。
2. 同一延迟队列里“修改时间提前”时，旧的长延迟消息会堵住新的短延迟消息（队头阻塞）。

换成 `x-delayed-message` 后，消息按 `x-delay` 独立投递，彻底消除队头阻塞；再配合 `executeTime` 等值校验解决时间比较问题。

### Q13. 延迟插件 vs RocketMQ 延迟消息 vs Redis ZSet，怎么选？

- 技术栈已用 RabbitMQ：优先延迟插件，改动最小。
- 需要原生稳定延迟且固定等级：RocketMQ。
- 需要精确到秒、可查询/可改期/可持久化：Redis ZSet + 定时任务，或数据库 + XXL-Job。
- 对“到点执行”要求极高且要幂等可追溯：数据库 + 定时任务最稳。

### Q14. 延迟消息发出去后，还没到点就想取消怎么办？

延迟插件本身不支持按消息取消。常用做法：

- 消息体带业务 ID，消费端执行时再查一次 DB 状态，状态已变化则丢弃（本项目就是这样）。
- 或改用“可删除的调度表 + 定时轮询”（DB/Redis ZSet），取消时删除/更新任务即可。

### Q15. 声明 CustomExchange 时，绑定写法为什么是 `.noargs()`？

Spring AMQP 3.x 中，`BindingBuilder.bind(queue).to(Exchange).with(key)` 对自定义交换机返回的是 `GenericArgumentsConfigurer`，还需要 `.noargs()`（无额外参数）或 `.and(map)` 才能生成最终 `Binding`。

---

## 七、小结

- **Bug 1（时间比较不可靠）** → 消息体携带 `executeTime`，消费端做等值校验。
- **Bug 2（旧消息堵塞新消息）** → 改用 `x-delayed-message` 延迟插件。
- **公告状态** → 新增 `PENDING`（待发布），发布流程与评价问卷保持一致。