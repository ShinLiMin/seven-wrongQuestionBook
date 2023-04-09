package com.seven.wqb.api;

import com.seven.wqb.domain.JsonResponse;
import com.seven.wqb.service.FileService;
import com.seven.wqb.service.utils.OssUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class FileApi {

    @Autowired
    private FileService fileService;

    //图片上传
    @PutMapping("/upload/image")
    public JsonResponse<String> uploadImage(MultipartFile uploadImage) throws Exception {
        String path = fileService.uploadImage(uploadImage);
        return new JsonResponse<>(path);
    }


}
