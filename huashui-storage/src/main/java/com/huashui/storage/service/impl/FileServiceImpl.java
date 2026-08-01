package com.huashui.storage.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.huashui.common.exception.BusinessException;
import com.huashui.common.utils.UserContext;
import com.huashui.storage.Enum.BizType;
import com.huashui.storage.domain.pojo.SysFile;
import com.huashui.storage.domain.vo.FileUploadVO;
import com.huashui.storage.domain.vo.FileVO;
import com.huashui.storage.mapper.SysFileMapper;
import com.huashui.storage.service.FileService;

import com.huashui.storage.util.MinioUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


@Service
public class FileServiceImpl extends ServiceImpl<SysFileMapper, SysFile> implements FileService {

    @Autowired
    private MinioUtil minioUtil;


    //上传文件
    @Override
    public FileUploadVO upload(MultipartFile file, BizType type) {
       //获取文件的大小
        long size = file.getSize();
        //校验文件的大小
        if (!type.allowSize(size)){
            throw  new BusinessException("文件太大");
        }
        //获取文件的拓展名,并校验
        String originalFilename = file.getOriginalFilename();

        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        }
        if (! type.allowExt(extension)){
            throw new BusinessException("非法的文件类型");
        }
        //如果合法,调用util上传MInio并返回VO,同时MQ更新数据库
        String objectName = minioUtil.buildObjectName(type, extension);
        String url = minioUtil.upload(file, objectName);

        //  保存到file表
        SysFile sysFile = new SysFile();
        sysFile.setOriginalName(file.getOriginalFilename());
        sysFile.setObjectName(objectName);
        sysFile.setAccessUrl(url);
        sysFile.setFileSize(file.getSize());
        sysFile.setCreateTime(LocalDateTime.now());
        sysFile.setUpdateTime(LocalDateTime.now());
        sysFile.setMimeType("test");
        sysFile.setFileHash("test");
        sysFile.setUploaderId(1L);
        sysFile.setBizType(type);
        // todo MiME类型
        sysFile.setFileExt(extension);
        /*sysFile.setUploaderId(UserContext.getUserId());*/
        // todo 定义监听器回填业务id

        save(sysFile);

        //  包装vo返回
        return new FileUploadVO(sysFile.getId(),url,originalFilename);
    }


    //批量获取文件
    @Override
    public List<FileVO> getFilesByIds(String ids) {
        //解析id列表
        List<Long> fileIds = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();

        for (Long fileId : fileIds) {
            // todo 查询数据库封装VO
        }

        return null;
    }


    //删除文件
    @Override
    public void delete(Long id) {
        // todo 待实现
    }
}
