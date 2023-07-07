package com.example.btl_web.dao.impl;

import com.example.btl_web.dao.CategoryDao;
import com.example.btl_web.mapper.impl.CategoryMapperImpl;
import com.example.btl_web.mapper.impl.include.CategoryIncludeMapperImpl;
import com.example.btl_web.model.Category;

import java.util.List;

public class CategoryDaoImpl extends GeneralDaoImpl<Category> implements CategoryDao {
    private static CategoryDaoImpl categoryDao;
    public static CategoryDaoImpl getInstance()
    {
        if(categoryDao == null)
            categoryDao = new CategoryDaoImpl();
        return categoryDao;
    }
    @Override
    public List<Category> select(String sql, Object... parameters) {
        return selectSql(sql, new CategoryMapperImpl(), parameters);
    }

    @Override
    public List<Category> selectDisplay(String sql, Object... parameters) {
        return selectSql(sql, new CategoryIncludeMapperImpl(), parameters);
    }

    @Override
    public Long update(String sql, Object... parameters) {
        return updateSql(sql, parameters);
    }

    @Override
    public long count(String sql, Object... parameters) {
        return countSql(sql, parameters);
    }
}
