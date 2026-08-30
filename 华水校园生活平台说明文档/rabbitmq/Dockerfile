# 基于官方 RabbitMQ 3.13 管理镜像，预装延迟消息交换机插件
# 用途：支持 x-delayed-message 类型交换机（评价问卷定时开始/结束、公告定时发布等延迟消息场景）
FROM rabbitmq:3.13-management-alpine

# 插件版本需与 RabbitMQ 3.13.x 匹配
ARG DELAYED_PLUGIN_VERSION=3.13.0

RUN apk add --no-cache wget ca-certificates \
    && wget -q -O /opt/rabbitmq/plugins/rabbitmq_delayed_message_exchange-${DELAYED_PLUGIN_VERSION}.ez \
         https://github.com/rabbitmq/rabbitmq-delayed-message-exchange/releases/download/v${DELAYED_PLUGIN_VERSION}/rabbitmq_delayed_message_exchange-${DELAYED_PLUGIN_VERSION}.ez \
    && rabbitmq-plugins enable --offline rabbitmq_delayed_message_exchange \
    && apk del wget
