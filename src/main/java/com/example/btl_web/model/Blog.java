package com.example.btl_web.model;

import com.example.btl_web.dto.CategoryDto;
import com.example.btl_web.dto.CommentDto;
import com.example.btl_web.dto.UserDto;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class Blog implements Serializable {
    private Long blogId;
    private String title;
    private String content;
    private String imageTitle;
    private Long createdAt;
    private UserDto user;
    private Integer status;

    private Integer num_Like;

    private Integer num_Comment;
    List<CategoryDto> categories;
    List<UserDto> likedUsers;
    List<CommentDto> comments;
    //Thuộc tính dùng để tìm kiếm
    private String categorySearch;
    //Các hàm

    public Blog(){

    }
    public String timeConvert(){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String data[] =  dateFormat.format(createdAt).split("\\s+");
        String date = data[0] + " lúc " + data[1];
        return date;
    }
    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createAt) {
        this.createdAt = createAt;
    }

    public Long getBlogId() {
        return blogId;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageTitle() {
        return imageTitle;
    }

    public void setImageTitle(String imageTitle) {
        this.imageTitle = imageTitle;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public String getCategorySearch() {
        return categorySearch;
    }

    public void setCategorySearch(String categorySearch) {
        this.categorySearch = categorySearch;
    }

    public List<CategoryDto> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryDto> categories) {
        this.categories = categories;
    }

    public List<UserDto> getLikedUsers() {
        return likedUsers;
    }

    public void setLikedUsers(List<UserDto> likedUsers) {
        this.likedUsers = likedUsers;
    }

    public List<CommentDto> getComments() {
        return comments;
    }

    public void setComments(List<CommentDto> comments) {
        this.comments = comments;
    }

    public Integer getNum_Like() {
        return num_Like;
    }

    public void setNum_Like(Integer num_Like) {
        this.num_Like = num_Like;
    }

    public Integer getNum_Comment() {
        return num_Comment;
    }

    public void setNum_Comment(Integer num_Comment) {
        this.num_Comment = num_Comment;
    }
}