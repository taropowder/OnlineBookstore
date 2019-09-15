package com.book.springboot.pojo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ShoppingCart {
    private int id;
    private int book_id;
    private int user_id;
    private Book book;
    private User user;
    private String addTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAddTime() throws ParseException {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = fmt.parse(addTime);      //将从数据库读出来的 timestamp 类型的时间转换为java的Date类型
        String datetime = fmt.format(date);
        return datetime;
//        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }
}
