package com.huashui.storage.util;

import com.huashui.common.exception.BusinessException;
import com.huashui.storage.Enum.BizType;
import com.huashui.storage.config.MinioProperties;
import io.minio.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * MinIO 操作工具类
 * <p>
 * 统一封装 MinIO SDK 的上传/下载/删除/查元数据/预签名，
 * 把 SDK 抛出的一堆受检异常统一收敛为 {@link BusinessException}，
 * 让上层 Service 无需感知 MinIO 细节。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MinioUtil {

    private final MinioClient minioClient;
    private final MinioProperties properties;

    /**
     * 上传文件到 MinIO
     *
     * @param file       前端上传的 MultipartFile
     * @param objectName 存储对象名（如 repair_image/2026/07/uuid.jpg）
     * @return 完整可直接访问的 URL：endpoint/bucket/objectName
     */
    public String upload(MultipartFile file, String objectName) {
        try (InputStream is = file.getInputStream()) {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(properties.getBucket())
                    .object(objectName)
                    .stream(is, file.getSize(), -1)
                    // 关键：写入真实 MIME，否则浏览器会当成附件下载而非预览
                    .contentType(file.getContentType())
                    .build());
            return buildUrl(objectName);
        } catch (Exception e) {
            log.error("[MinIO] 文件上传失败 objectName={}", objectName, e);
            throw new BusinessException("文件上传失败");
        }
    }

    /**
     * 上传字节流（内部生成的文件用，如缩略图/二维码）
     *
     * @param bytes       字节内容
     * @param objectName  存储对象名
     * @param contentType MIME 类型
     * @return 完整访问 URL
     */
    public String upload(byte[] bytes, String objectName, String contentType) {
        try (InputStream is = new java.io.ByteArrayInputStream(bytes)) {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(properties.getBucket())
                    .object(objectName)
                    .stream(is, bytes.length, -1)
                    .contentType(contentType)
                    .build());
            return buildUrl(objectName);
        } catch (Exception e) {
            log.error("[MinIO] 字节流上传失败 objectName={}", objectName, e);
            throw new BusinessException("文件上传失败");
        }
    }

    /**
     * 下载文件为输入流（调用方负责关闭）
     *
     * @param objectName 存储对象名
     * @return 文件输入流
     */
    public InputStream download(String objectName) {
        try {
            return minioClient.getObject(GetObjectArgs.builder()
                    .bucket(properties.getBucket())
                    .object(objectName)
                    .build());
        } catch (Exception e) {
            log.error("[MinIO] 文件下载失败 objectName={}", objectName, e);
            throw new BusinessException("文件下载失败");
        }
    }

    /**
     * 删除文件
     *
     * @param objectName 存储对象名
     */
    public void remove(String objectName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(properties.getBucket())
                    .object(objectName)
                    .build());
        } catch (Exception e) {
            log.error("[MinIO] 文件删除失败 objectName={}", objectName, e);
            throw new BusinessException("文件删除失败");
        }
    }

    /**
     * 判断对象是否存在
     *
     * @param objectName 存储对象名
     * @return true=存在
     */
    public boolean exists(String objectName) {
        try {
            minioClient.statObject(StatObjectArgs.builder()
                    .bucket(properties.getBucket())
                    .object(objectName)
                    .build());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 生成预签名下载 URL（带过期时间，用于将来敏感文件的临时授权访问）
     * <p>
     * 本期公开读策略下用不到，预留给后续切换鉴权模式使用。
     *
     * @param objectName    存储对象名
     * @param expireSeconds 有效期（秒）
     * @return 预签名 URL
     */
    public String presignedUrl(String objectName, int expireSeconds) {
        try {
            return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(properties.getBucket())
                    .object(objectName)
                    .expiry(expireSeconds, TimeUnit.SECONDS)
                    .build());
        } catch (Exception e) {
            log.error("[MinIO] 预签名URL生成失败 objectName={}", objectName, e);
            throw new BusinessException("生成访问链接失败");
        }
    }

    /**
     * 生成存储对象名：{bizType小写}/{yyyy/MM}/{uuid}.{ext}

     * @param bizType 业务类型（决定一级目录）
     * @param ext     扩展名（小写，不含点）
     * @return 存储对象名
     */
    public String buildObjectName(BizType bizType, String ext) {
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM"));
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return bizType.getCode().toLowerCase() + "/" + datePath + "/" + uuid + "." + ext;
    }

    /**
     * 从原始文件名中提取扩展名（小写，不含点）
     *
     * @param originalName 原始文件名，如 "报修图片.JPG"
     * @return 扩展名小写，如 "jpg"；无扩展名返回空串
     */
    public String extractExt(String originalName) {
        if (originalName == null) {
            return "";
        }
        int dot = originalName.lastIndexOf('.');
        if (dot < 0 || dot == originalName.length() - 1) {
            return "";
        }
        return originalName.substring(dot + 1).toLowerCase();
    }

    /**
     * 拼接完整访问 URL：endpoint/bucket/objectName
     * <p>
     * endpoint 末尾可能带斜杠，做一次归一化避免出现双斜杠。
     */
    public String buildUrl(String objectName) {
        String endpoint = properties.getEndpoint();
        if (endpoint.endsWith("/")) {
            endpoint = endpoint.substring(0, endpoint.length() - 1);
        }
        return endpoint + "/" + properties.getBucket() + "/" + objectName;
    }
}
