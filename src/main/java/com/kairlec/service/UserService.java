package com.kairlec.service;

import com.kairlec.pojo.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    Integer init();

    List<User> getAll();

    User getUser(String username);

    Integer insertUser(User user);

    Integer deleteUser(String username);

    Integer updateLoginInfo(User user);

    Integer updatePassword(User user);
}
