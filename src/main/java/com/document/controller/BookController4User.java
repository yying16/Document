package com.document.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.document.domain.Book;
import com.document.domain.Record;
import com.document.domain.User;
import com.document.interceptor.UserInfoGetter;
import com.document.service.BookService;
import com.document.service.RecordService;
import com.document.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//针对普通用户(User)的图书控制器

@RestController
@RequestMapping("/books4user")
public class BookController4User {
    @Autowired
    private RecordService recordService;
    @Autowired
    private UserInfoGetter userInfoGetter;
    @Autowired
    private BookService bookService;
    @Autowired
    private UserService userService;

    //---------------------------------借阅图书相关---------------------------------//


    @GetMapping("/n/{currentPage}/{pageSize}") //获取所有图书数据(不过滤)
    public IPage<Book> queryAllBooksN(@PathVariable() Long currentPage,
                                      @PathVariable() Long pageSize) {

        IPage<Book> t = bookService.getAllBooksPaginated(currentPage, pageSize);
        return t;
    }

    @GetMapping("/y/{currentPage}/{pageSize}/{param}") //获取所有图书数据(过滤)
    public IPage<Book> queryAllBooksY(@PathVariable() Long currentPage,
                                      @PathVariable() Long pageSize) {
        return bookService.getAllBooksPaginated(currentPage, pageSize);

    }


    @GetMapping("{bookName}") //根据书名查单个
    public Book queryOne(@PathVariable("bookName") String bookName) {
        return bookService.getBookClassified("bookName", bookName)[0];
    }

    @GetMapping("/ByCondition4Book/n/{currentPage}/{pageSize}/{type}/{content}") //条件查询(针对书)(不过滤暂无库存)
    public IPage<Book> queryByCondition4BookN(@PathVariable("currentPage") Long currentPage, @PathVariable("pageSize") Long pageSize, @PathVariable("type") String type, @PathVariable("content") String content) {
        return bookService.getBookClassifiedPaginated(type, content, currentPage, pageSize);
    }

    @GetMapping("/ByCondition4Book/y/{currentPage}/{pageSize}/{type}/{content}") //条件查询(针对书)(过滤暂无库存)
    public IPage<Book> queryByCondition4BookY(@PathVariable("currentPage") Long currentPage, @PathVariable("pageSize") Long pageSize, @PathVariable("type") String type, @PathVariable("content") String content) {
        return bookService.getBookClassifiedWithFitterPaginated(type, content, currentPage, pageSize);
    }

    //---------------------------------借阅记录相关---------------------------------//

    //用户查询自己以往所有的借阅记录(无条件)
    @GetMapping("/myRecord/{currentPage}/{pageSize}")
    public IPage<Record> queryAllRecords(@PathVariable("currentPage") Long currentPage,
                                         @PathVariable("pageSize") Long pageSize) {
        return recordService.getAllRecordsPaginated(userInfoGetter.getUser(), currentPage, pageSize);
    }


    //用户查询自己以往所有的借阅记录(根据查询条件)
    @GetMapping("/searchMyRecord/{currentPage}/{pageSize}/{type}/{content}")
    public IPage<Record> queryByCondition4Record(@PathVariable("currentPage") Long currentPage,
                                                 @PathVariable("pageSize") Long pageSize,
                                                 @PathVariable("type") String type,
                                                 @PathVariable("content") String content) {
        return recordService.getAllRecordsClassifiedPaginated(userInfoGetter.getUser(),type,content,currentPage,pageSize); //输入的条件不合法(搜索类型没选、搜索内容不填，... etc),那直接返回全部借阅记录
    }

    //用户查询自己目前仍未归还书目的所有借阅记录(根据查询条件)
    @GetMapping("/ByCondition4UnReturnedRecord/{type}/{content}")
    public List<Record> queryByCondition4UnReturnedRecord(@PathVariable("type") String type,
                                                          @PathVariable("content") String content) {
        User user = userInfoGetter.getUser();//先获取当前user

        //根据搜索类型判断(搜索内容不能为空)
        if (type.equals("bookName") && !content.equals("")) {
            List<Record> records = userService.getUnreturnedRecordsByBookName(user, content);
            return records;
        } else if (type.equals("author") && !content.equals("")) {
            List<Record> records = userService.getUnreturnedRecordsByAuthor(user, content);
            return records;
        } else if (type.equals("publisher") && !content.equals("")) {
            List<Record> records = userService.getUnreturnedRecordsByPublisher(user, content);
            return records;
        } else return userService.getAllUnreturnedRecords(user); //输入的条件不合法(搜索类型没选、搜索内容不填，... etc),那直接返回全部仍未归还书目的借阅记录
    }

    //----------------------------------------归还书目界面-------------------------------------
    @GetMapping("/ByConditionForUnReturnRecord/{currentPage}/{pageSize}/{type}/{content}")
    public IPage<Record> ByConditionForUnReturnRecord(@PathVariable("currentPage") Long currentPage,
                                                      @PathVariable("pageSize") Long pageSize,
                                                      @PathVariable("type") String type,
                                                      @PathVariable("content") String content) {//归还书目界面的查询按钮

        return recordService.getAllUnReturnRecordsClassifiedPaginated(userInfoGetter.getUser(),type,content,currentPage,pageSize);
    }

    //用户查看自己目前仍未归还书目的所有借阅记录(无条件)
    @GetMapping("/myRecord/unReturned/{currentPage}/{pageSize}")
    public IPage<Record> queryAllUnreturnedRecords(@PathVariable() Long currentPage,
                                                  @PathVariable() Long pageSize) {
        return recordService.getUnReturnRecordPaginated(userInfoGetter.getUser(),currentPage,pageSize);
    }
    //---------------------------------------操作相关-----------------------------------------------

    //用户借阅图书
    @PutMapping("/borrowBook")
    public void borrowBook(@RequestBody Book book) {
        recordService.borrowBooks(userInfoGetter.getUser(), book);
    }

    //用户续借图书
    @PutMapping("/deferRecord")
    public void deferRecord(@RequestBody Book book) {
        recordService.deferRecord(userInfoGetter.getUser(), book);
    }

    //检查用户是否占有某本书未归还
    @PutMapping("/checkRecord")
    public boolean checkRecord(@RequestBody Book book) {
        return recordService.checkBook(userInfoGetter.getUser(), book);
    }

    //用户归还图书
    @PutMapping("/returnBook")
    public void returnBook(@RequestBody Book book) {
        recordService.returnBooks(userInfoGetter.getUser(), book);
    }
}
