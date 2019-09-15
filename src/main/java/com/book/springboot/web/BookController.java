package com.book.springboot.web;

import com.book.springboot.mapper.BookMapper;
import com.book.springboot.pojo.Book;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import java.util.List;

@Controller
public class BookController {
    @Autowired
    BookMapper bookMapper;

    @GetMapping("/book")
    public String getOneBook(int id, Model model){
        Book book = bookMapper.getBookById(id);
        List<Book> new_books = bookMapper.getOrderById();
        model.addAttribute("new_book",new_books);
        List<Book> hot_books = bookMapper.getOrderBySales();
        model.addAttribute("hot_book",hot_books);
        model.addAttribute("book",book);
        return "oneBook";
    }


    @GetMapping("/")
    public String home(Model m, @RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "4") int size) throws Exception {
        PageHelper.startPage(start,size,"id desc");
        List<Book> books=bookMapper.findAll();
        PageInfo<Book> page = new PageInfo<>(books);
        m.addAttribute("page", page);
        List<Book> new_books = bookMapper.getOrderById();
        m.addAttribute("new_book",new_books);
        List<Book> hot_books = bookMapper.getOrderBySales();
        m.addAttribute("hot_book",hot_books);
        return "index";
    }
}
