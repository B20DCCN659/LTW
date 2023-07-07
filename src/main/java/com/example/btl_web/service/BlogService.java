package com.example.btl_web.service;

import com.example.btl_web.dto.BlogDto;
import com.example.btl_web.dto.UserDto;
import com.example.btl_web.paging.Pageable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface BlogService {
    List<BlogDto> getAllBlogs(Pageable pageable, BlogDto dto);
    List<BlogDto> BlogsMaxLike(Pageable pageable, BlogDto dto);
    List<BlogDto> BlogsMaxComment(Pageable pageable, BlogDto dto);
    List<BlogDto> BlogsNew(Pageable pageable, BlogDto dto);
    List<UserDto> peopleLikedBlog(Long blogId);
    BlogDto getOne(BlogDto searchBlog);
    long countBlogs(BlogDto blogDto);
    Long save(BlogDto blog);
    Long update(BlogDto blog);
    Long delete(Long blogId);
    boolean validCreateBlog(HttpServletRequest req, BlogDto blog);
    boolean validUpdateBlog(HttpServletRequest req, BlogDto blog, Long userId);
    boolean checkUserLikedBlog(BlogDto blog, Long userId);
    void addPTagContent(BlogDto blog);
    void removePTagContent(BlogDto blog);
}
