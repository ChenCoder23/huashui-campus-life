package com.huashui.storage.config;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * MinIO 配置属性
 * <p>
 * 对应 application.yml / Nacos 中的 minio.* 配置段。
 */
@Data
@ConfigurationProperties(prefix = "minio")
@Schema(description = "MinIO配置属性")
public class MinioProperties {

    /**
     * S3 API 地址（9000端口），同时也是拼接公开访问URL的前缀
     */
    private String endpoint;

    /**
     * 访问凭证 AccessKey
     */
    private String accessKey;

    /**
     * 访问凭证 SecretKey
     */
    private String secretKey;

    /**
     * 桶名
     */
    private String bucket;
}
