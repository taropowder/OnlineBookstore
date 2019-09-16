package com.book.springboot.mapper;

import com.book.springboot.pojo.Book;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface BookMapper {

    @Select("select * from book ")
    List<Book> findAll();


    @Select("select * from book order by id desc limit 0,5")
    List<Book> getOrderById();

    @Select("select * from book order by sales desc  limit 0,5")
    List<Book> getOrderBySales();

    @Select("SELECT * FROM `book` where id = #{id}")
    public Book getBookById(int id);

    @Update("update `book` set sales=sales+1,inventory=inventory-1 where id=#{id} ")
    public int buyBook(Book book);

    @Insert(" insert into `book` (name,author,sales,inventory,price,image,description,intro) values (#{name},#{author},#{sales},#{inventory},#{price},#{image},#{description},#{intro}) ")
    @Options(useGeneratedKeys=true, keyProperty="id")
    public int save(Book book);


    @Update("update `book` set name=#{name},author=#{author},price=#{price},description=#{description},sales=#{sales},intro=#{intro} where id=#{id} ")
    public void changeWithOutImage(Book book);

    @Update("update `book` set name=#{name},author=#{author},price=#{price},description=#{description},sales=#{sales},intro=#{intro},image=#{image} where id=#{id} ")
    public void changeWithImage(Book book);

    @Select("select count(id) from `book`")
    public int countBook();


}
