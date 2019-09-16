package com.book.springboot.mapper;

import com.book.springboot.pojo.Order;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderMapper {
    @Select("SELECT * FROM `order` where id = #{id}")
    @Results(value = {
            @Result(property = "user", column = "user_id",
                    one = @One(select = "com.book.springboot.mapper.UserMapper.getUserById")),
            @Result(property = "book", column = "book_id",
                    one = @One(select = "com.book.springboot.mapper.BookMapper.getBookById")),
            @Result(property = "address", column = "address_id",
                    one = @One(select = "com.book.springboot.mapper.AddressMapper.getAddressById"))
    })
    Order getOrderById(int id);


    @Select("SELECT * FROM `order` where user_id = #{id}")
    @Results({
            @Result(property = "user", column = "user_id",
                    one = @One(select = "com.book.springboot.mapper.UserMapper.getUserById")),
            @Result(property = "book", column = "book_id",
                    one = @One(select = "com.book.springboot.mapper.BookMapper.getBookById"))
    })
    List<Order>  getOrderByUserId(int id);

    @Select("SELECT * FROM `order` ")
    @Results({
            @Result(property = "user", column = "user_id",
                    one = @One(select = "com.book.springboot.mapper.UserMapper.getUserById")),
            @Result(property = "book", column = "book_id",
                    one = @One(select = "com.book.springboot.mapper.BookMapper.getBookById")),
            @Result(property = "address", column = "address_id",
                    one = @One(select = "com.book.springboot.mapper.AddressMapper.getAddressById"))
    })
    List<Order>  getAll();

    @Update("update `order` set status=#{status} where id=#{id} ")
    public void updateStatus(Order order);

    @Update("update `order` set comment=#{comment},commentTime=#{commentTime} where id=#{id} ")
    public void updateComment(Order order);

    @Insert(" insert into `order` ( user_id,book_id,address_id,status,orderTime ) values (#{user_id},#{book_id},${address_id},#{status},#{orderTime}) ")
    public void save(Order order);

    @Select("select count(id) from `order`")
    public int countOrder();

}
