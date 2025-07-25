package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@Api(tags = "文件上传")
public class UploadController {

    @Autowired
    private AliOssUtil aliOssUtil;

    @PostMapping("/admin/common/upload")
    @ApiOperation("文件上传")
    public Result<String> upload(MultipartFile file) throws Exception {
        log.info("文件上传{}", file.getOriginalFilename());

        String url = aliOssUtil.upload(file.getBytes(), file.getOriginalFilename());
        return Result.success(url);
    }
}
