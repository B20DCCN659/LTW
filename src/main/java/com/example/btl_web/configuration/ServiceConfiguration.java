package com.example.btl_web.configuration;

import com.example.btl_web.service.HashPasswordService;
import com.example.btl_web.service.impl.*;

//Dùng để trả về các bean service
public class ServiceConfiguration {
    private static BlogServiceImpl blogService;
    private static CategoryServiceImpl categoryService;
    private static HashPasswordServiceimpl hashPasswordService;
    private static UserBlogServiceImpl userBlogService;
    private static UserServiceimpl userService;

    public static BlogServiceImpl getBlogService()
    {
        return blogService == null ? new BlogServiceImpl() : blogService;
    }
    public static CategoryServiceImpl getCategoryService()
    {
        return categoryService == null ? new CategoryServiceImpl() : categoryService;
    }
    public static HashPasswordService getHashPasswordService()
    {
        return hashPasswordService == null ? new HashPasswordServiceimpl() : hashPasswordService;
    }
    public static UserBlogServiceImpl getUserBlogService()
    {
        return userBlogService == null ? new UserBlogServiceImpl() : userBlogService;
    }
    public static UserServiceimpl getUserService()
    {
        return userService == null ? new UserServiceimpl() : userService;
    }
}
