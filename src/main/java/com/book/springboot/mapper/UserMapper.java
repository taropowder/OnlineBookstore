package com.book.springboot.mapper;

import com.book.springboot.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface UserMapper {


    @Select("select * from  user where username= #{username} and password= #{password} ")
    public User getUserByUsername(User user);

    @Insert(" insert into user ( username,password ) values (#{username},#{password}) ")
    public int save(User user);

    @Select("select * from  user where id= #{id} ")
    public User getUserById(int id);

    @Select("select count(id) from `user`")
    public int countUser();


//    @Select("SELECT * FROM `type` where id = #{id}")
//    @Results({
//            @Result(property = "type", column = "address_id",
//                    one = @One(select = "com.car.springboot.mapper.TypeMapper.getTypeById"))
//    })
//    public Type getTypeByUser(User user);
//    @Select("select * from  user where username= #{username} and password= #{password} ")
//    public Type getTypeByUser(User user);
}
