package com.example.btl_web.dao;

import com.example.btl_web.model.Blog;

import java.util.List;

public interface BlogDao {
    List<Blog> findAll(String sql, Object... parameters);
    List<Blog> findBlogByCondition(String sql, Object... parameters);
    long countBlogs(String sql, Object... parameters);
    Long save(String sql, Object... parameters);
}
