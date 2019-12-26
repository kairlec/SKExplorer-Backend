package com.kairlec.service.impl;

import com.kairlec.dao.UserMapper;
import com.kairlec.pojo.User;
import com.kairlec.service.UserService;
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
            return userMapper.getAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public User getUser(String username) {
        try {
            return userMapper.getUser(username);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Integer insertUser(User user) {
        try {
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
            return userMapper.updatePassword(user);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
