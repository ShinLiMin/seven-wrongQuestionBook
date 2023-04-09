package com.seven.wqb.dao;

import com.seven.wqb.domain.SensitiveWord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SensitiveWordDao {
    SensitiveWord getWordByVal(String value);

    Integer insertWord(SensitiveWord word);

    Integer deleteWord(String value);

    List<SensitiveWord> getAllWord();

}
