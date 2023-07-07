package com.example.btl_web.dao;
import com.example.btl_web.model.Category;

import java.util.List;

public interface CategoryDao{
    List<Category> select(String sql, Object... parameters);
    List<Category> selectDisplay(String sql, Object... parameters);
    Long update(String sql, Object... parameters);
    long count(String sql, Object... parameters);
}
