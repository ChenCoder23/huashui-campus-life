package com.huashui.storage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huashui.common.response.PageResult;
import com.huashui.storage.Enum.BizType;
import com.huashui.storage.domain.dto.FilePageDTO;
import com.huashui.storage.domain.pojo.SysFile;
import com.huashui.storage.domain.vo.FileUploadVO;
import com.huashui.storage.domain.vo.FileVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService extends IService<SysFile> {

    FileUploadVO upload(MultipartFile file, BizType type);

    List<FileVO> getFilesByIds(String ids);

    void delete(Long id);

    PageResult<FileVO> page(FilePageDTO dto);
}