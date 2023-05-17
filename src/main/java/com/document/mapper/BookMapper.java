package com.document.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.document.domain.Book;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BookMapper extends BaseMapper<Book> {
}
