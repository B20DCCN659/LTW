package com.example.btl_web.mapper.impl;

import com.example.btl_web.mapper.RowMapper;
import com.example.btl_web.model.Category;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class CategoryMapperImpl implements RowMapper {
    @Override
    public Category mapper(ResultSet resultSet) {
        Category category = new Category();

        try {
            category.setCategoryId(resultSet.getLong("category_id"));
            category.setName(resultSet.getString("name"));
            category.setUserId(resultSet.getLong("user_id"));
            category.setCreatedAt(resultSet.getLong("created_at"));
            category.setStatus(resultSet.getInt("status"));

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            boolean hasNumBlog = false;

            for (int i = 1; i <= columnCount; i++) {
                String columnLabel = metaData.getColumnLabel(i);
                if (columnLabel.equalsIgnoreCase("num_blogs")) {
                    hasNumBlog = true;
                }
            }
            if(hasNumBlog) {
                category.setNum_blog(resultSet.getInt("num_blogs"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return category;
    }
}
