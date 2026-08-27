package com.huashui.common.domain.mqMessage;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * 延迟消息统一约定。
 *
 * <p>延迟消息体需携带“计划执行时间” executeTime，消费端拿到消息后，
 * 用 executeTime 与数据库中的对应时间字段做等值校验，
 * 从而判断该消息是否已经因为业务数据被修改而失效（陈旧消息）。</p>
 *
 * <p>这样就不必再依赖消费时刻的墙钟 now 与 DB 时间比较，
 * 避免 MQ 网络延迟 / 时钟偏差导致的误判。</p>
 */
public interface DelayedMessage {

    LocalDateTime getExecuteTime();

    void setExecuteTime(LocalDateTime executeTime);

    /**
     * 消费端校验：消息携带的计划执行时间是否与数据库当前时间一致。
     * 统一按秒比较，避免数据库 datetime 截断毫秒/纳秒导致误判。
     */
    default boolean isSameExecuteTime(LocalDateTime dbTime) {
        if (getExecuteTime() == null || dbTime == null) {
            return false;
        }
        return getExecuteTime().truncatedTo(ChronoUnit.SECONDS)
                .equals(dbTime.truncatedTo(ChronoUnit.SECONDS));
    }
}