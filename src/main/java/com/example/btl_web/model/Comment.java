package com.example.btl_web.model;

import com.example.btl_web.dto.UserDto;

import java.io.Serializable;

public class Comment implements Serializable {
    private Long commentId;
    private String content;
    private UserDto userComment;
    private Long blogComment;
    private Long createdAt;
    public Comment() {

    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public Long getBlogComment() {
        return blogComment;
    }

    public void setBlogComment(Long blogComment) {
        this.blogComment = blogComment;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public UserDto getUserComment() {
        return userComment;
    }

    public void setUserComment(UserDto userComment) {
        this.userComment = userComment;
    }
}
