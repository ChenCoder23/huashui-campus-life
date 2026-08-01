package com.huashui.storage.controller;



import com.huashui.common.response.Result;
import com.huashui.storage.Enum.BizType;
import com.huashui.storage.domain.vo.FileUploadVO;
import com.huashui.storage.domain.vo.FileVO;
import com.huashui.storage.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("/storage")
@RequiredArgsConstructor
public class FileController {


    private final FileService fileService;



    /**
     * 上传文件
     *
     * POST /storage/upload
     *
     * multipart/form-data
     */
    @PostMapping("/upload")
    public Result<FileUploadVO> upload(@RequestParam("file") MultipartFile file, BizType type){
        FileUploadVO vo = fileService.upload(file,type);
        return Result.ok(vo);
    }





    /**
     * 批量查询文件元数据
     *
     * GET /storage/files?ids=1,2,3
     *
     * 用于业务模块回显附件
     */
    @GetMapping("/files")
    public Result<List<FileVO>> listFiles(@RequestParam("ids") String ids){
        List <FileVO> fileVOS = fileService.getFilesByIds(ids);
        return Result.ok(fileVOS);

    }





    /**
     * 删除文件
     *
     * 逻辑删除
     *
     * status = 0
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id){

        fileService.delete(id);

        return Result.ok();

    }


}