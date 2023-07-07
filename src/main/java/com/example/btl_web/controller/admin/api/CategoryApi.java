package com.example.btl_web.controller.admin.api;

import com.example.btl_web.configuration.ServiceConfiguration;
import com.example.btl_web.constant.Constant;
import com.example.btl_web.constant.Constant.*;
import com.example.btl_web.dto.CategoryDto;
import com.example.btl_web.dto.UserDto;
import com.example.btl_web.service.CategoryService;
import com.example.btl_web.utils.HttpUtils;
import com.example.btl_web.utils.SessionUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = Admin.CATEGORY_API)
public class CategoryApi extends HttpServlet {
    private CategoryService categoryService = ServiceConfiguration.getCategoryService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        solveApi(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        solveApi(req, resp);
    }

    private void solveApi(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");

        String errors[] = new String[1];

        CategoryDto category = HttpUtils.of(req.getReader()).toModel(CategoryDto.class);
        UserDto user = (UserDto) SessionUtils.getInstance().getValue(req, Constant.USER_MODEL);
        category.setUserId(user.getUserId());

        boolean validCategory = false;
        String successMessage = "";
        if(req.getMethod().equals(Request.POST_METHOD))
        {
            validCategory = categoryService.validCategoryCreate(category, errors, user.getUserId());
            successMessage = "Thêm thể loại thành công!";
        }
        else if(req.getMethod().equals(Request.PUT_METHOD) || req.getMethod().equals(Request.DELETE_METHOD))
        {
            validCategory = categoryService.validCategoryUpdate(category, errors, user.getUserId());
            successMessage = "Cập nhật thành công!";
        }
        ObjectMapper mapper = new ObjectMapper();
        if(validCategory) {
            Long status = null;
            if(req.getMethod().equals(Request.POST_METHOD))
            {
                status = categoryService.save(category);
            }
            else if(req.getMethod().equals(Request.PUT_METHOD) || req.getMethod().equals(Request.DELETE_METHOD))
            {
                status = categoryService.update(category);
            }
            if (status != null)
            {
                mapper.writeValue(resp.getOutputStream(), successMessage);
                return;
            }
        }
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        mapper.writeValue(resp.getOutputStream(), errors);
    }
}
