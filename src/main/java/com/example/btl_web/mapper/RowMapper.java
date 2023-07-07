package com.example.btl_web.mapper;

import java.sql.ResultSet;

public interface RowMapper<T> {
    T mapper(ResultSet resultSet);
//    T mapper2(ResultSet resultSet);
}
