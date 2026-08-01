package com.huashui.storage.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.SetBucketPolicyArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MinIO 客户端配置
 * <p>
 * 1. 注册全局单例 MinioClient（线程安全）
 * 2. 启动时确保桶存在，并挂公开读策略（写仍需签名，读对所有人开放）
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(MinioProperties.class)
public class MinioConfig {

    private final MinioProperties properties;

    /**
     * 公开读策略模板：任何人可对桶内对象执行 GetObject，其余操作仍需凭证
     */
    private static final String PUBLIC_READ_POLICY = """
            {
              "Version": "2012-10-17",
              "Statement": [
                {
                  "Effect": "Allow",
                  "Principal": {"AWS": ["*"]},
                  "Action": ["s3:GetObject"],
                  "Resource": ["arn:aws:s3:::%s/*"]
                }
              ]
            }
            """;

    @Bean
    public MinioClient minioClient() {
        log.info("properties = {}",properties);
        return MinioClient.builder()
                .endpoint(properties.getEndpoint())
                .credentials(properties.getAccessKey(), properties.getSecretKey())
                .build();
    }

    /**
     * 启动时初始化桶：不存在则创建并设置公开读策略
     */
    @Bean
    public ApplicationRunner minioBucketInitializer(MinioClient minioClient) {
        return args -> {
            String bucket = properties.getBucket();
            try {
                boolean exists = minioClient.bucketExists(
                        BucketExistsArgs.builder().bucket(bucket).build());
                if (!exists) {
                    minioClient.makeBucket(
                            MakeBucketArgs.builder().bucket(bucket).build());
                    minioClient.setBucketPolicy(SetBucketPolicyArgs.builder()
                            .bucket(bucket)
                            .config(PUBLIC_READ_POLICY.formatted(bucket))
                            .build());
                    log.info("[MinIO] 桶 [{}] 不存在，已创建并设置公开读策略", bucket);
                } else {
                    log.info("[MinIO] 桶 [{}] 已存在，跳过初始化", bucket);
                }
            } catch (Exception e) {
                // 初始化失败直接快速失败：MinIO 不可用时 storage 服务没有存在意义
                log.error("[MinIO] 桶初始化失败，请检查 MinIO 是否已启动（endpoint={}）",
                        properties.getEndpoint(), e);
                throw new IllegalStateException("MinIO 初始化失败: " + e.getMessage(), e);
            }
        };
    }
}
