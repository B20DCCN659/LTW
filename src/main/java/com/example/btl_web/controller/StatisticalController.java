package com.example.btl_web.controller;

import com.example.btl_web.configuration.ServiceConfiguration;
import com.example.btl_web.constant.Constant;
import com.example.btl_web.dto.BlogDto;
import com.example.btl_web.dto.CategoryDto;
import com.example.btl_web.dto.UserDto;
import com.example.btl_web.service.BlogService;
import com.example.btl_web.service.CategoryService;
import com.example.btl_web.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(value = Constant.STATISTICAL_PAGE)
public class StatisticalController extends HttpServlet {
    private BlogService blogService = ServiceConfiguration.getBlogService();
    private CategoryService categoryService = ServiceConfiguration.getCategoryService();

    private UserService userService = ServiceConfiguration.getUserService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        List<BlogDto> Like_Max = blogService.BlogsMaxLike(null,null);
        List<BlogDto> Comment_Max = blogService.BlogsMaxComment(null,null);
        List<CategoryDto> Blog_Max = categoryService.CategoryMaxBlogs(null,null);
        List<UserDto> Count_Blog = userService.countBlog();

        request.setAttribute("Like",Like_Max);
        request.setAttribute("Cmt",Comment_Max);
        request.setAttribute("Cate",Blog_Max);
        request.setAttribute("cntBlog",Count_Blog);

        request.getRequestDispatcher(Constant.STATISTICAL_JSP).forward(request,response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
