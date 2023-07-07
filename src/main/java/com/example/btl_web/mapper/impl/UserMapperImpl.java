package com.example.btl_web.mapper.impl;

import com.example.btl_web.mapper.RowMapper;
import com.example.btl_web.model.User;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class UserMapperImpl implements RowMapper<User> {

    @Override
    public User mapper(ResultSet resultSet) {
        User user = new User();

        try {
            user.setUserId(resultSet.getLong("user_id"));
            user.setAddress(resultSet.getString("address"));
            user.setEmail(resultSet.getString("email"));
            user.setFullName(resultSet.getString("full_name"));
            user.setPassWord(resultSet.getString("password"));
            user.setPhone(resultSet.getString("phone"));
            user.setRegisteredAt(resultSet.getLong("created_at"));
            user.setRole(resultSet.getInt("role"));
            user.setUserName(resultSet.getString("username"));
            user.setStatus(resultSet.getInt("status"));
            user.setLastAction(resultSet.getLong("last_action"));

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
                user.setCountBlog(resultSet.getInt("num_blogs"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }
}
