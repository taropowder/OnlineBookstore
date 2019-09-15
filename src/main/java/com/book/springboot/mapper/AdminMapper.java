package com.book.springboot.mapper;

import com.book.springboot.pojo.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AdminMapper {

    @Select("select * from admin where username= #{username} and password= #{password} ")
    public Admin getUserByUsername(Admin admin);
}
