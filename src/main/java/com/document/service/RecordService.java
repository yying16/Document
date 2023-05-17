package com.document.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.document.domain.Book;
import com.document.domain.Record;
import com.document.domain.User;
import com.document.mapper.BookMapper;
import com.document.mapper.RecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class RecordService  extends ServiceImpl<RecordMapper, Record> implements IService<Record> {
    @Autowired
    RecordMapper recordMapper;

    @Autowired
    BookMapper bookMapper;

    //分页查询借阅记录
    public IPage<Record> getAllRecordsPaginated(Long currentPage, Long pageSize) {
        IPage page = new Page(currentPage, pageSize);
        recordMapper.selectPage(page,new QueryWrapper<>());
        return page;
    }

    public IPage<Record> getUnReturnRecordPaginated(User user,Long currentPage, Long pageSize) {
        IPage page = new Page(currentPage, pageSize);
        QueryWrapper<Record> wrapper = new QueryWrapper<>();
        wrapper.eq("account",user.getAccount());
        wrapper.eq("state","未归还");
        recordMapper.selectPage(page,wrapper);
        return page;
    }

    public IPage<Record> getAllRecordsClassifiedPaginated(User user,String type, String content,Long currentPage, Long pageSize) {
        IPage page = new Page(currentPage, pageSize);
        QueryWrapper<Record> wrapper = new QueryWrapper<>();
        wrapper.eq("account",user.getAccount());
        if(!content.equals("")){
            if(type.equals("bookName")) {
                wrapper.like("book_name","%"+content+"%");
            }else if(type.equals("author")) {
                wrapper.like("author","%"+content+"%");
            }else if(type.equals("publisher")) {
                wrapper.like("publisher","%"+content+"%");
            }
        }
        recordMapper.selectPage(page,wrapper);
        return page;
    }

    public IPage<Record> getAllRecordsClassifiedPaginated(String type, String content,Long currentPage, Long pageSize) {
        IPage page = new Page(currentPage, pageSize);
        QueryWrapper<Record> wrapper = new QueryWrapper<>();
        if(!content.equals("")){
            if(type.equals("bookName")) {
                wrapper.like("book_name","%"+content+"%");
            }else if(type.equals("author")) {
                wrapper.like("author","%"+content+"%");
            }else if(type.equals("publisher")) {
                wrapper.like("publisher","%"+content+"%");
            }
        }
        recordMapper.selectPage(page,wrapper);
        return page;
    }

    public IPage<Record> getAllUnReturnRecordsClassifiedPaginated(User user,String type, String content,Long currentPage, Long pageSize) {
        IPage page = new Page(currentPage, pageSize);
        QueryWrapper<Record> wrapper = new QueryWrapper<>();
        wrapper.eq("account",user.getAccount());
        wrapper.eq("state","未归还");
        if(!content.equals("")){
            if(type.equals("bookName")) {
                wrapper.like("book_name","%"+content+"%");
            }else if(type.equals("author")) {
                wrapper.like("author","%"+content+"%");
            }else if(type.equals("publisher")) {
                wrapper.like("publisher","%"+content+"%");
            }
        }
        recordMapper.selectPage(page,wrapper);
        return page;
    }

    public IPage<Record> getAllRecordsPaginated(User user,Long currentPage, Long pageSize) {
        IPage page = new Page(currentPage, pageSize);
        QueryWrapper<Record> wrapper = new QueryWrapper<>();
        wrapper.eq("account",user.getAccount());
        recordMapper.selectPage(page,wrapper);
        return page;
    }


    //返回所有用户的所有借阅记录
    //管理员-借阅记录
    public Record[] getAllRecords(){
        try {
            return recordMapper.selectList(new QueryWrapper<>()).toArray(new Record[0]);
        }catch (Exception e){
            return new Record[0];
        }
    }

    //根据书名查找借阅记录
    //借阅记录中搜索类型为书名
    public Record[] getRecordsByBookName(String content) {
        try {
            QueryWrapper<Record> wrapper = new QueryWrapper<>();
            wrapper.like("book_name","%"+content+"%");
            return recordMapper.selectList(wrapper).toArray(new Record[0]);
        } catch (Exception e) {
            return new Record[0];
        }
    }

    //根据出版社查找借阅记录
    //借阅记录中搜索类型为出版社
    public Record[] getRecordsByPublisher(String content) {
        try {
            QueryWrapper<Record> wrapper = new QueryWrapper<>();
            wrapper.like("publisher","%"+content+"%");
            return recordMapper.selectList(wrapper).toArray(new Record[0]);
        } catch (Exception e) {
            return new Record[0];
        }
    }

    //根据作者查找借阅记录
    //借阅记录中搜索类型为作者
    public Record[] getRecordsByAuthor(String content) {
        try {
            QueryWrapper<Record> wrapper = new QueryWrapper<>();
            wrapper.like("author","%"+content+"%");
            return recordMapper.selectList(wrapper).toArray(new Record[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return new Record[0];
        }
    }

    //用户的一次借阅操作
    public void borrowBooks(User user,Book book){
        try{
            Record record = new Record();
            record.setAccount(user.getAccount());
            record.setIsbn(book.getIsbn());
            record.setBorrowTime(new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis())));
            record.setDateToReturn(new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()+2592000000L)));
            record.setState("未归还");
            book.setRemain(book.getRemain()-1);
            recordMapper.insertRecord(record);
            bookMapper.updateById(book);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //用户的一次续借功能
    public void deferRecord(User user,Book book){
        Record t = new Record();
        t.setAccount(user.getAccount());
        t.setIsbn(book.getIsbn());
        Record record = recordMapper.getRecentRecordByISBNAndAccount(t);
        try{
            Date date  = new SimpleDateFormat("yyyy-MM-dd").parse(record.getDateToReturn());
            String deferDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date(date.getTime()+2592000000L));
            record.setDateToReturn(deferDate);
            recordMapper.updateRecord(record);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //用户的一次还书()
    public void returnBooks(User user,Book book){
        try{
            Record t = new Record();
            t.setAccount(user.getAccount());
            t.setIsbn(book.getIsbn());
            Record record = recordMapper.getRecentRecordByISBNAndAccount(t);
            record.setReturnTime(new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis())));
            record.setState("已归还");
            recordMapper.updateRecord(record);
            book = bookMapper.selectById(book.getIsbn());
            book.setRemain(book.getRemain() + 1);
            bookMapper.updateById(book);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //检查用户是否可以再借阅该书（！true表示没有占有，可以继续借阅！）
    public boolean checkBook(User user,Book book){
        try{
            Record t = new Record();
            t.setAccount(user.getAccount());
            t.setIsbn(book.getIsbn());
            Record[] records = recordMapper.getRecordByISBNAndAccount(t);
            if(records.length>0){ //借过这本书
                Record record = recordMapper.getRecentRecordByISBNAndAccount(t);
                return record.getReturnTime() != null;
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return true;
        }
    }

    public Record[] getRecordsClassified(User user,String type,String content){
        QueryWrapper<Record> wrapper = new QueryWrapper<>();
        wrapper.eq("account",user.getAccount());
        wrapper.eq("state","未归还");
        if(!content.equals("")){
            if(type.equals("bookName")) {
                wrapper.like("book_name","%"+content+"%");
            }else if(type.equals("author")) {
                wrapper.like("author","%"+content+"%");
            }else if(type.equals("publisher")) {
                wrapper.like("publisher","%"+content+"%");
            }
        }
        return recordMapper.selectList(wrapper).toArray(new Record[0]);
    }
}
