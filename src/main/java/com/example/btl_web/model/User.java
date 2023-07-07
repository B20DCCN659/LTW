package com.example.btl_web.model;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {
    private Long userId;
    private String userName;
    private String passWord;
    private String email;
    private Integer role;
    private String address;
    private String phone;
    private String fullName;
    private Long registeredAt;
    private Integer status;
    private Long lastAction;
    private List<Blog> blogs;
    private List<Comment> comments;
    private List<Blog> likedBlog;
    private Integer countBlog;
    public User(){

    }

    public Long getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(Long registeredAt) {
        this.registeredAt = registeredAt;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public List<Blog> getBlogs() {
        return blogs;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public List<Blog> getLikedBlog() {
        return likedBlog;
    }

    public Long getLastAction() {
        return lastAction;
    }

    public void setLastAction(Long lastAction) {
        this.lastAction = lastAction;
    }

    public void setBlogs(List<Blog> blogs) {
        this.blogs = blogs;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void setLikedBlog(List<Blog> likedBlog) {
        this.likedBlog = likedBlog;
    }

    public Integer getCountBlog() {
        return countBlog;
    }

    public void setCountBlog(Integer countBlog) {
        this.countBlog = countBlog;
    }
}
