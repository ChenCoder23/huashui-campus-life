package com.huashui.storage.controller;

import com.huashui.common.response.PageResult;
import com.huashui.common.response.Result;
import com.huashui.storage.Enum.BizType;
import com.huashui.storage.domain.dto.FilePageDTO;
import com.huashui.storage.domain.vo.FileUploadVO;
import com.huashui.storage.domain.vo.FileVO;
import com.huashui.storage.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/storage")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public Result<FileUploadVO> upload(@RequestParam("file") MultipartFile file, BizType type) {
        return Result.ok(fileService.upload(file, type));
    }

    @GetMapping("/files")
    public Result<List<FileVO>> listFiles(@RequestParam("ids") String ids) {
        return Result.ok(fileService.getFilesByIds(ids));
    }

    @GetMapping("/page")
    public PageResult<FileVO> page(FilePageDTO dto) {
        return fileService.page(dto);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        fileService.delete(id);
        return Result.ok();
    }
}