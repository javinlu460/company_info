package com.company.controller.admin;

import com.company.common.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Api(tags = "文件上传")
@RestController
public class FileController {

    @Value("${file.upload-path:./uploads/}")
    private String uploadPath;

    @ApiOperation("文件上传")
    @PostMapping("/upload")
    public R<?> upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return R.fail("上传文件不能为空");
        }

        // 获取原始文件名和扩展名
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // 生成新文件名
        String newFilename = UUID.randomUUID().toString().replace("-", "") + extension;

        // 确保目录存在
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // 保存文件
        File dest = new File(uploadDir, newFilename);
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            return R.fail("文件上传失败: " + e.getMessage());
        }

        // 返回文件访问URL
        String url = "/uploads/" + newFilename;
        return R.ok(url);
    }
}
