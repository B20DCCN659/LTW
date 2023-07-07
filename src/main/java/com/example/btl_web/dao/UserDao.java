package com.example.btl_web.dao;

import com.example.btl_web.model.User;

import java.util.List;

public interface UserDao {
    List<User> findAll(String sql, Object... parameters);
    List<User> getUserByCondition(String sql, Object... parameters);
    List<User> findAllUserInclude(String sql, Object... parameters);
    long countItems(String sql, Object... parameters);
    Long saveUser(String sql, Object... parameters);
}
