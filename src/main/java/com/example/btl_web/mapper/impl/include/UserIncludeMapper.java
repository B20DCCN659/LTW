package com.example.btl_web.mapper.impl.include;

import com.example.btl_web.mapper.RowMapper;
import com.example.btl_web.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserIncludeMapper implements RowMapper {
    @Override
    public Object mapper(ResultSet resultSet) {
        User user = new User();

        try {
            user.setUserId(resultSet.getLong("user_id"));
            user.setFullName(resultSet.getString("full_name"));
            user.setUserName(resultSet.getString("username"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
}
