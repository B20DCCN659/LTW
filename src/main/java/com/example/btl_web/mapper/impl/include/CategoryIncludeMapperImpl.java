package com.example.btl_web.mapper.impl.include;

import com.example.btl_web.mapper.RowMapper;
import com.example.btl_web.model.Category;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryIncludeMapperImpl implements RowMapper<Category> {
    @Override
    public Category mapper(ResultSet resultSet) {
        Category category = new Category();

        try {
            category.setCategoryId(resultSet.getLong("category_id"));
            category.setName(resultSet.getString("name"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return category;
    }
}
