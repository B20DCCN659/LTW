package com.example.btl_web.dto;

public class CommentDto {
    private Long commentId;
    private String content;
    private UserDto userComment;
    private Long blogComment;
    private String createdAt;
    public CommentDto() {
    }

    public Long getCommentId() {
        return commentId;
    }

    public UserDto getUserComment() {
        return userComment;
    }

    public void setUserComment(UserDto userComment) {
        this.userComment = userComment;
    }

    public Long getBlogComment() {
        return blogComment;
    }

    public void setBlogComment(Long blogComment) {
        this.blogComment = blogComment;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
