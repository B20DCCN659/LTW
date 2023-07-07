package com.example.btl_web.mapper.impl;

import com.example.btl_web.configuration.ServiceConfiguration;
import com.example.btl_web.dto.UserDto;
import com.example.btl_web.mapper.RowMapper;
import com.example.btl_web.model.Blog;
import com.example.btl_web.service.UserBlogService;
import com.example.btl_web.service.UserService;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

public class BlogMapperImpl implements RowMapper {
    private UserService userService = ServiceConfiguration.getUserService();
    private UserBlogService userBlogService = ServiceConfiguration.getUserBlogService();
    @Override
    public Object mapper(ResultSet resultSet) {
        Blog blog = new Blog();

        try {
            blog.setBlogId(resultSet.getLong("blog_id"));
            blog.setTitle(resultSet.getString("title"));
            blog.setContent(resultSet.getString("content"));
            blog.setImageTitle(resultSet.getString("image_title"));
            blog.setCreatedAt(resultSet.getLong("created_at"));
            blog.setStatus(resultSet.getInt("status"));

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            boolean hasNumLikes = false;
            boolean hasNumComments = false;

            for (int i = 1; i <= columnCount; i++) {
                String columnLabel = metaData.getColumnLabel(i);
                if (columnLabel.equalsIgnoreCase("num_likes")) {
                    hasNumLikes = true;
                } else if (columnLabel.equalsIgnoreCase("num_comments")) {
                    hasNumComments = true;
                }
            }

            if (hasNumLikes) {
                blog.setNum_Like(resultSet.getInt("num_likes"));
            } else if (hasNumComments) {
                blog.setNum_Comment(resultSet.getInt("num_comments"));
            }

            //Tìm người user viết blog này
            UserDto userOfBlog = new UserDto();
            userOfBlog.setUserId(resultSet.getLong("user_id"));
            List<UserDto> userDtos = userService.findAll(null, userOfBlog);
            UserDto user = userDtos.isEmpty() ? null: userDtos.get(0);
            blog.setUser(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return blog;
    }

//    @Override
//    public Object mapper2(ResultSet resultSet) {
//        Blog blog = new Blog();
//
//        try {
//            blog.setBlogId(resultSet.getLong("blog_id"));
//            blog.setTitle(resultSet.getString("title"));
//            blog.setContent(resultSet.getString("content"));
//            blog.setImageTitle(resultSet.getString("image_title"));
//            blog.setCreatedAt(resultSet.getLong("created_at"));
//            blog.setStatus(resultSet.getInt("status"));
//            blog.setNum_Like(resultSet.getInt("num_likes"));
//            //Tìm người user viết blog này
//            UserDto userOfBlog = new UserDto();
//            userOfBlog.setUserId(resultSet.getLong("user_id"));
//            List<UserDto> userDtos = userService.findAll(null, userOfBlog);
//            UserDto user = userDtos.isEmpty() ? null: userDtos.get(0);
//            blog.setUser(user);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return blog;
//    }
}
