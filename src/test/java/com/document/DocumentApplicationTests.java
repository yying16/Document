package com.document;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.document.domain.Record;
import com.document.domain.User;
import com.document.mapper.BookMapper;
import com.document.mapper.RecordMapper;
import com.document.mapper.UserMapper;
import com.document.service.BookService;
import com.document.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest
class DocumentApplicationTests {

    @Autowired
    UserService userService;

    @Autowired
    BookService bookService;


    @Autowired
    UserMapper userMapper;

    @Autowired
    RecordMapper recordMapper;

    @Autowired
    BookMapper bookMapper;

    @Test
    public void test2() {
        User user = new User();
        user.setAccount("zhangsan");
        System.out.println(Arrays.toString(userService.getAllRecords(user)));
    }
    @Test
    public void test3(){
        User user = new User();
        user.setAccount("zhangsan");
    }
}
