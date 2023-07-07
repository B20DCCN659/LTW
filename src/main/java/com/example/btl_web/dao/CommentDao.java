package com.example.btl_web.dao;

import com.example.btl_web.model.Comment;

import java.util.List;

public interface CommentDao {
    List<Comment> findAll(String sql, Object... parameters);
    Long update(String sql, Object... parameters);
    long count(String sql, Object... parameters);
}
