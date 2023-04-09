package com.seven.wqb.service;

import com.seven.wqb.dao.FileDBDao;
import com.seven.wqb.domain.FileDB;
import com.seven.wqb.service.utils.MD5Util;
import com.seven.wqb.service.utils.OssUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Service
public class FileService {

    @Autowired
    private OssUtil ossUtil;

    @Autowired
    private FileDBDao fileDBDao;

    public String uploadImage(MultipartFile uploadImage)throws Exception {
        String fileMD5 = MD5Util.getFileMD5(uploadImage);
        FileDB fileByMD5 = fileDBDao.getFileByMD5(fileMD5);
        if (fileByMD5 != null) {
            return fileByMD5.getUrl();
        }
        FileDB fileDB = new FileDB();
        String fileName = uploadImage.getOriginalFilename();
        String imagePath = ossUtil.upload(fileName, uploadImage.getInputStream());
        fileDB.setCreateTime(new Date());
        fileDB.setType(fileName.substring(fileName.lastIndexOf(".")));
        fileDB.setUrl(imagePath);
        fileDB.setMd5(fileMD5);
        fileDBDao.addFile(fileDB);
        return imagePath;
    }

}
