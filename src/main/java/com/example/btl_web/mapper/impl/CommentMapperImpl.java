package com.example.btl_web.mapper.impl;

import com.example.btl_web.configuration.ServiceConfiguration;
import com.example.btl_web.dto.UserDto;
import com.example.btl_web.mapper.RowMapper;
import com.example.btl_web.model.Comment;
import com.example.btl_web.service.UserService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CommentMapperImpl implements RowMapper {
    private UserService userService = ServiceConfiguration.getUserService();
    @Override
    public Comment mapper(ResultSet resultSet) {
        Comment comment = new Comment();

        try {
            comment.setCommentId(resultSet.getLong("comment_id"));
            comment.setContent(resultSet.getString("content"));
            comment.setCreatedAt(resultSet.getLong("created_at"));

            UserDto userDto = new UserDto();
            userDto.setUserId(resultSet.getLong("user_id"));
            List<UserDto> resultUser = userService.findAllInclude(null, userDto);
            if(!resultUser.isEmpty())
            {
                comment.setUserComment(resultUser.get(0));
            }

            comment.setBlogComment(resultSet.getLong("blog_id"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comment;
    }
}