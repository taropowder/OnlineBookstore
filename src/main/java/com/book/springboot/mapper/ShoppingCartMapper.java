package com.book.springboot.mapper;

import com.book.springboot.pojo.ShoppingCart;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {

    @Select("SELECT * FROM `cart` where user_id = #{id}")
    @Results({
            @Result(property = "book", column = "book_id",
                    one = @One(select = "com.book.springboot.mapper.BookMapper.getBookById")),
            @Result(property = "user", column = "user_id",
                    one = @One(select = "com.book.springboot.mapper.UserMapper.getUserById"))
    })
    List<ShoppingCart> getCartByUserId(int id);

    @Select("SELECT * FROM `cart` where user_id = #{id} and book_id=#{book_id}")
    ShoppingCart check(ShoppingCart shoppingCart);

    @Insert(" insert into `cart` ( book_id,user_id,addTime) values (#{book_id},#{user_id},#{addTime}) ")
    public int save(ShoppingCart shoppingCart);

    @Delete(" delete from `cart` where id= #{id} ")
    public int delete(int id);

    @Delete(" delete from `cart` where user_id= #{id} ")
    public int deleteByUserId(int id);
}
