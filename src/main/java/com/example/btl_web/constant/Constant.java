package com.example.btl_web.constant;

public class Constant {
    //public static final String DOMAIN = "https://btlltw.herokuapp.com";
    public static final String DOMAIN = "http://localhost:8080";
    public static final String ADMIN = "ADMIN";
    public static final String USER = "USER";
    public static final String USER_MODEL = "USER_MODEL";
    public static final String LOGIN_JSP = "/views/login/login.jsp";
    public static final String REGISTRAION_JSP = "/views/registraion/registraion.jsp";
    public static final String USER_NAME = "username";
    public static final String PASS_WORD = "password";
    public static final String STATISTICAL_JSP =  "/views/statistical/statistical.jsp";
    public static final String STATISTICAL_PAGE = "/statistical";
    public static class User
    {
        public static final String ACTION = "action";
        public static final String ACTION_NOT_LOGIN = "not_login";
        public static final String ACTION_NOT_PERMISSION = "not_permission";
        public static final String ACTION_LOG_OUT = "logout";
        public static final String LOGIN_PAGE = "/login";
        public static final String HOME_PAGE = "/";
        public static final String HOME_JSP = "/views/home/home.jsp";
        public static final String CREATE_BLOG_PAGE = "/create-blog";
        public static final String UPDATE_BLOG_PAGE = "/update-blog";
        public static final String CREATE_BLOG_JSP = "/views/blog/create_blog.jsp";
        public static final String READ_BLOG_PAGE = "/blogs/*";
        public static final String READ_BLOG_JSP = "/views/blog/read_blog.jsp";
        public static final String USER_CREATE_API = "/api-create-user";
        public static final String USER_COMMENT_API = "/api-create-comment";
        public static final String USER_LIKE_API = "/api-create-like";
        public static final String USER_DETAIL_PAGE = "/user/*";
        public static final String USER_DETAIL_JSP = "/views/user/user_detail.jsp";
        public static final String CHANGE_PASSWORD_API = "/api-change-password";
        public static final String CHANGE_PASSWORD_PAGE = "/change-password";
        public static final String CHANGE_PASSWORD_JSP = "/views/user/change_password/change_password.jsp";
    }
    public static class Admin
    {
        public static final String EDIT = "/edit";
        public static final String CATEGORIES_PAGE = "/admin/categories";
        public static final String CATEGORY_API = "/api-admin-category";
        public static final String CATEGORIES_JSP = "/views/admin/categories.jsp";
        public static final String CATEGORY_EDIT_JSP = "/views/admin/create/categories.jsp";
        public static final String BLOGS_PAGE = "/admin/blogs";
        public static final String BLOGS_API = "/api-admin-blog";
        public static final String BLOGS_JSP = "/views/admin/blogs.jsp";
        public static final String USERS_PAGE = "/admin/users";
        public static final String USER_API = "/api-admin-user";
        public static final String USERS_JSP = "/views/admin/users.jsp";
        public static final String DELETE_BLOG_API = "/api-admin-delete-blog";
        public static final String DELETE_USER_API = "/api-admin-delete-user";
        public static final String DELETE_CATEGORY_API = "/api-admin-delete-category";
    }

    public static class Dto
    {
        public static final String CREATE_DATE = "createdAt";
        public static final String REGISTRATION_AT = "registeredAt";
        public static final String MODIFIED_DATE = "modifiedAt";
        public static final String DATE_FORMAT = "dd/MM/yyyy HH:mm";
        public static final String LAST_ACTION = "lastAction";
    }

    public static class Paging
    {
        public static final int MAX_ITEMS_OF_PAGE = 5;
        public static final String PAGE_STR = "page";
        public static final String MAX_ITEMS_OF_PAGE_STR = "max-items-page";
        public static final String SORT_NAME = "sort-name";
        public static final String SORT_BY = "sort-by";
    }

    public static class Request
    {
        public static final String GET_METHOD =  "GET";
        public static final String POST_METHOD = "POST";
        public static final String PUT_METHOD = "PUT";
        public static final String DELETE_METHOD = "DELETE";
    }

    public static class Role
    {
        public static final int ADMIN = 3;
        public static final int MOD = 2;
        public static final int USER = 1;
    }
}
