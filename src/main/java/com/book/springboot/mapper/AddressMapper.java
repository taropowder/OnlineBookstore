package com.book.springboot.mapper;

import com.book.springboot.pojo.Address;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface AddressMapper {

    @Select("SELECT * FROM `address` where user_id = #{id}")
    @Results({
            @Result(property = "user", column = "user_id",
                    one = @One(select = "com.book.springboot.mapper.UserMapper.getUserById"))
    })
    List<Address> getAddressByUserId(int id);

    @Insert(" insert into `address` ( user_id,content) values (#{user_id},#{content}) ")
    public int save(Address address);

    @Delete(" delete from `address` where id= #{id} ")
    public int delete(int id);

    @Select("SELECT * FROM `address` where id = #{id}")
    Address getAddressById(int id);
}
