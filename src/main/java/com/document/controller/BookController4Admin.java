package com.document.controller;

//针对管理员admin的图书控制器

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.document.domain.Book;
import com.document.domain.Record;
import com.document.service.BookService;
import com.document.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books4admin")
public class BookController4Admin {
    @Autowired
    private BookService bookService;
    @Autowired
    private RecordService recordService;



    @GetMapping //获取所有图书数据
    public Book[] queryAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/records") //获取所有借阅数据
    public Record[] queryAllRecords() {
        return recordService.getAllRecords();
    }

    @GetMapping("{bookName}") //根据书名查询
    public Book queryOne(@PathVariable("bookName") String bookName) {
        if(bookService.getBookClassified("bookName",bookName).length>0){
            return bookService.getBookClassified("bookName",bookName)[0];
        }
        return null;
    }

    @PostMapping //添加图书
    public void saveBook(@RequestBody Book book) {
        //默认设置藏书数量100本
        book.setTotal(100);
        book.setRemain(100);

        //System.out.println("Save Book: " + book); //test Code
        bookService.InsertBook(book);
    }

    @PutMapping //修改图书(根据书名修改)
    public void updateBook(@RequestBody Book book) {
        //System.out.println("Modify Book: " + book);
        bookService.modifyBook(book);
    }

    @DeleteMapping("{isbn}") //删除单个图书(根据isbn删除)
    public void deleteBook(@PathVariable("isbn") String isbn) {

        Book book = new Book();
        book.setIsbn(isbn);

        bookService.deleteByISBN(book);
    }

    @DeleteMapping("/ds/{ISBNs}") //删除批量图书(根据ISBNs删除)
    public void deleteBooks(@PathVariable("ISBNs") String ISBNsStr) {
        String[] ISBNs = ISBNsStr.split(" ");
        Book[] books = new Book[ISBNs.length];

        for(int i = 0; i < ISBNs.length; ++i) {
            books[i] = new Book();
            books[i].setIsbn(ISBNs[i]);
        }

        bookService.deleteBooks(books);
    }

    @GetMapping("/ByCondition4Book/{currentPage}/{pageSize}/{type}/{content}") //条件查询(针对书)
    public IPage<Book> queryByCondition4Book(@PathVariable("currentPage") Long currentPage,
                                             @PathVariable("pageSize") Long pageSize,
                                             @PathVariable("type")String type,
                                             @PathVariable("content")String content) {
        return bookService.getBookClassifiedPaginated(type, content,currentPage,pageSize);
    }

    @GetMapping("/ByCondition4Record/{currentPage}/{pageSize}/{type}/{content}") //条件查询(针对借阅记录)
    public IPage<Record> queryByCondition4Record(@PathVariable("currentPage") Long currentPage,
                                                 @PathVariable("pageSize") Long pageSize,
                                                 @PathVariable("type")String type,
                                                 @PathVariable("content")String content) {

         return recordService.getAllRecordsClassifiedPaginated(type,content,currentPage,pageSize);
    }

    @GetMapping("/recordsByPage/{currentPage}/{pageSize}") //借书记录分页查询(无条件)
    public IPage<Record> queryRecordsByPage(@PathVariable("currentPage")Long currentPage,
                                            @PathVariable("pageSize")Long pageSize) {
        return recordService.getAllRecordsPaginated(currentPage, pageSize);
    }


    @GetMapping("/renewsByPage/{currentPage}/{pageSize}") //更新分页查询(无条件)
    public IPage<Book> queryBooksByPage(@PathVariable()Long currentPage,
                                            @PathVariable()Long pageSize) {
        IPage<Book> t = bookService.getAllBooksPaginated(currentPage, pageSize);
        return t;
    }


}
