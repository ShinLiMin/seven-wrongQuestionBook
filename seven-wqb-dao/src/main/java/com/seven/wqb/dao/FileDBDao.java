package com.seven.wqb.dao;

import com.seven.wqb.domain.FileDB;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileDBDao {
    Integer addFile(FileDB fileDB);

    FileDB getFileByMD5(String md5);
}
