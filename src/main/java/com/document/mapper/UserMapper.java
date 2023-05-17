package com.document.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.document.domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {

}
