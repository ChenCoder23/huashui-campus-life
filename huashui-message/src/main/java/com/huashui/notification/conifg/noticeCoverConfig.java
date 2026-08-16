package com.huashui.notification.conifg;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 公告模块配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "notice")
public class noticeCoverConfig {

    /**
     * 公告默认封面
     */
    private String defaultCover;
}
