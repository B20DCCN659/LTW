package com.example.btl_web.dto;

import java.util.List;

public class UserDto {
    private Long userId;
    private String userName;
    private String passWord;
    private String re_password;
    private String email;
    private Integer role;
    private String address;
    private String phone;
    private String fullName;
    private String registeredAt;
    private Integer status;
    private Long lastAction;
    private Long uploadedBlog;
    private Integer countBlog;
    private List<BlogDto> blogs;
    private List<CommentDto> comments;
    private List<BlogDto> likedBlog;
    public UserDto()
    {

    }
    public UserDto(UserDto entity)
    {
        if(entity != null)
        {
            this.userId = entity.userId;
            this.userName = entity.userName;
            this.passWord = entity.passWord;
            this.re_password = entity.re_password;
            this.email = entity.email;
            this.role = entity.role;
            this.address = entity.address;
            this.phone = entity.phone;
            this.fullName = entity.fullName;
            this.registeredAt = entity.registeredAt;
            this.status = entity.status;
            this.lastAction = entity.lastAction;
            this.uploadedBlog = entity.uploadedBlog;
            this.countBlog = entity.countBlog;

            this.blogs = entity.blogs;
            this.comments = entity.comments;
            this.likedBlog = entity.likedBlog;
        }
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(String registeredAt) {
        this.registeredAt = registeredAt;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRe_password() {
        return re_password;
    }

    public void setRe_password(String re_password) {
        this.re_password = re_password;
    }

    public List<BlogDto> getBlogs() {
        return blogs;
    }

    public void setBlogs(List<BlogDto> blogs) {
        this.blogs = blogs;
    }

    public List<CommentDto> getComments() {
        return comments;
    }

    public void setComments(List<CommentDto> comments) {
        this.comments = comments;
    }

    public List<BlogDto> getLikedBlog() {
        return likedBlog;
    }

    public void setLikedBlog(List<BlogDto> likedBlog) {
        this.likedBlog = likedBlog;
    }

    public Long getLastAction() {
        return lastAction;
    }

    public void setLastAction(Long lastAction) {
        this.lastAction = lastAction;
    }

    public Long getUploadedBlog() {
        return uploadedBlog;
    }

    public void setUploadedBlog(Long uploadedBlog) {
        this.uploadedBlog = uploadedBlog;
    }

    public Integer getCountBlog() {
        return countBlog;
    }

    public void setCountBlog(Integer countBlog) {
        this.countBlog = countBlog;
    }
}
