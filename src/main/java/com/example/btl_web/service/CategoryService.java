package com.example.btl_web.service;

import com.example.btl_web.dto.CategoryDto;
import com.example.btl_web.paging.Pageable;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> findAll(Pageable pageable, CategoryDto dto);
    List<CategoryDto> CategoryMaxBlogs(Pageable pageable, CategoryDto dto);
    List<CategoryDto> findAllCategoryOfBlog(Long blogId, Integer status);
    CategoryDto findOneBy(CategoryDto dto);
    long countCategories(CategoryDto category);
    Long save(CategoryDto categoryDto);
    Long update(CategoryDto categoryDto);
    Long saveCategoriesOfBlog(Long blogId, List<CategoryDto> categories);
    boolean delete(Long categoryId, Long userId);
    boolean deleteCategoryOfBlog(Long blogId, Long categoryId);
    boolean validCategoryCreate(CategoryDto category, String[] error, Long userId);
    boolean validCategoryUpdate(CategoryDto category, String[] error, Long userId);
}
