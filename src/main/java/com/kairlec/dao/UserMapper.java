package com.kairlec.dao;

import com.kairlec.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    Integer init();

    List<User> getAll();

    User getUser(@Param("username") String username);

    Integer insertUser(User user);

    Integer deleteUser(@Param("username") String username);

    Integer updateLoginInfo(User user);

    Integer updatePassword(User user);
}
