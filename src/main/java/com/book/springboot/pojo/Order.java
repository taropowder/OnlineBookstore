package com.book.springboot.pojo;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Order {
    private int id;
    private User user;
    private int user_id;
    private int book_id;
    private int address_id;
    private Address address;
    private Book book;
    private String status;
    private String comment;
    private String orderTime;
    private String commentTime;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComment() {
        if (comment==null)
            return comment;
        if (comment.equals("")){
            return null;
        }
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getOrderTime() throws ParseException {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = fmt.parse(orderTime);      //将从数据库读出来的 timestamp 类型的时间转换为java的Date类型
        String datetime = fmt.format(date);
        return datetime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getCommentTime() throws ParseException {
        if (commentTime!=null){
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = fmt.parse(commentTime);      //将从数据库读出来的 timestamp 类型的时间转换为java的Date类型
            String datetime = fmt.format(date);
            return datetime;
        }else {
            return commentTime;
        }

    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
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

    public int getAddress_id() {
        return address_id;
    }

    public void setAddress_id(int address_id) {
        this.address_id = address_id;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }
}
