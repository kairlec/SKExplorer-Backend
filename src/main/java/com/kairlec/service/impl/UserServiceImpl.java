package com.kairlec.service.impl;

import com.kairlec.dao.UserMapper;
import com.kairlec.pojo.User;
import com.kairlec.service.UserService;
import com.kairlec.utils.PasswordCoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Integer init() {
        return userMapper.init();
    }

    @Override
    public List<User> getAll() {
        try {
            List<User> userList = userMapper.getAll();
            for (User user : userList) {
                user.setPassword(PasswordCoder.fromDatabase(user.getPassword()));
            }
            return userList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public User getUser(String username) {
        try {
            User user = userMapper.getUser(username);
            user.setPassword(PasswordCoder.fromDatabase(user.getPassword()));
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Integer insertUser(User user) {
        try {

            user.setPassword(PasswordCoder.toDatabase(user.getPassword()));
            return userMapper.insertUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Integer deleteUser(String username) {
        try {
            return userMapper.deleteUser(username);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Integer updateLoginInfo(User user) {
        try {
            return userMapper.updateLoginInfo(user);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Integer updatePassword(User user) {
        try {
            user.setPassword(PasswordCoder.toDatabase(user.getPassword()));
            return userMapper.updatePassword(user);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
