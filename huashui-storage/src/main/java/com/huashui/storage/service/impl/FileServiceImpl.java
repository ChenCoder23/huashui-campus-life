package com.huashui.storage.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huashui.common.exception.BusinessException;
import com.huashui.common.response.PageResult;
import com.huashui.common.utils.UserContext;
import com.huashui.storage.Enum.BizType;
import com.huashui.storage.Enum.FileStatus;
import com.huashui.storage.domain.dto.FilePageDTO;
import com.huashui.storage.domain.pojo.SysFile;
import com.huashui.storage.domain.vo.FileUploadVO;
import com.huashui.storage.domain.vo.FileVO;
import com.huashui.storage.mapper.SysFileMapper;
import com.huashui.storage.service.FileService;
import com.huashui.storage.util.MinioUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import cn.hutool.crypto.digest.DigestUtil;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl extends ServiceImpl<SysFileMapper, SysFile> implements FileService {

    private final MinioUtil minioUtil;

    @Override
    public FileUploadVO upload(MultipartFile file, BizType type) {
        long size = file.getSize();
        if (!type.allowSize(size)) {
            throw new BusinessException("文件太大");
        }

        String originalFilename = file.getOriginalFilename();
        String extension = minioUtil.extractExt(originalFilename);
        if (!type.allowExt(extension)) {
            throw new BusinessException("非法的文件类型");
        }

        String objectName = minioUtil.buildObjectName(type, extension);
        String url = minioUtil.upload(file, objectName);

        SysFile sysFile = new SysFile();
        sysFile.setOriginalName(originalFilename);
        sysFile.setObjectName(objectName);
        sysFile.setAccessUrl(url);
        sysFile.setFileSize(size);
        sysFile.setMimeType(file.getContentType());
        sysFile.setFileExt(extension);
        sysFile.setBizType(type);
        sysFile.setStatus(FileStatus.NORMAL);
        try {
            sysFile.setUploaderId(UserContext.getUserId());
        } catch (Exception e) {
            sysFile.setUploaderId(1L);
        }
        try {
            sysFile.setFileHash(DigestUtil.sha256Hex(file.getBytes()));
        } catch (Exception e) {
            sysFile.setFileHash("unset");
        }
        save(sysFile);

        return new FileUploadVO(sysFile.getId(), url, originalFilename);
    }

    @Override
    public List<FileVO> getFilesByIds(String ids) {
        if (StrUtil.isBlank(ids)) {
            return List.of();
        }
        List<Long> fileIds;
        try {
            fileIds = Arrays.stream(ids.split(","))
                    .map(String::trim)
                    .filter(StrUtil::isNotBlank)
                    .map(Long::parseLong)
                    .toList();
        } catch (NumberFormatException e) {
            throw new BusinessException("文件ID格式错误");
        }

        if (fileIds.isEmpty()) {
            return List.of();
        }

        return listByIds(fileIds).stream()
                .map(this::toFileVO)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        SysFile sysFile = getById(id);
        if (sysFile == null) {
            throw new BusinessException("文件不存在");
        }
        if (StrUtil.isNotBlank(sysFile.getObjectName())) {
            try {
                minioUtil.remove(sysFile.getObjectName());
            } catch (Exception e) {
                log.error("[storage] MinIO 删除失败 id={}", id, e);
            }
        }
        sysFile.setStatus(FileStatus.DELETED);
        updateById(sysFile);
    }

    @Override
    public PageResult<FileVO> page(FilePageDTO dto) {
        LambdaQueryWrapper<SysFile> wrapper = new LambdaQueryWrapper<SysFile>()
                .like(StrUtil.isNotBlank(dto.getOriginalName()), SysFile::getOriginalName, dto.getOriginalName())
                .eq(dto.getBizType() != null, SysFile::getBizType, dto.getBizType())
                .eq(dto.getUploaderId() != null, SysFile::getUploaderId, dto.getUploaderId())
                .eq(dto.getStatus() != null, SysFile::getStatus, dto.getStatus())
                .ge(dto.getBeginTime() != null, SysFile::getCreateTime, dto.getBeginTime())
                .le(dto.getEndTime() != null, SysFile::getCreateTime, dto.getEndTime())
                .orderByDesc(SysFile::getCreateTime);

        Page<SysFile> page = page(dto.toPage(), wrapper);
        List<FileVO> records = page.getRecords().stream()
                .map(this::toFileVO)
                .toList();
        return PageResult.of(page.getTotal(), page.getPages(), page.getSize(), records);
    }

    private FileVO toFileVO(SysFile file) {
        return FileVO.builder()
                .fileId(file.getId())
                .originalName(file.getOriginalName())
                .url(file.getAccessUrl())
                .fileSize(file.getFileSize())
                .mimeType(file.getMimeType())
                .fileExt(file.getFileExt())
                .bizType(file.getBizType())
                .bizId(file.getBizId())
                .uploaderId(file.getUploaderId())
                .createTime(file.getCreateTime())
                .build();
    }
}