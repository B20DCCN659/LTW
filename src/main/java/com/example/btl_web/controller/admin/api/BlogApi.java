package com.example.btl_web.controller.admin.api;

import com.example.btl_web.configuration.ServiceConfiguration;
import com.example.btl_web.constant.Constant;
import com.example.btl_web.constant.Constant.*;
import com.example.btl_web.dto.BlogDto;
import com.example.btl_web.dto.UserDto;
import com.example.btl_web.service.BlogService;
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

@WebServlet(urlPatterns = Admin.BLOGS_API)
public class BlogApi extends HttpServlet {
    private BlogService blogService = ServiceConfiguration.getBlogService();
    private CategoryService categoryService = ServiceConfiguration.getCategoryService();

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        solveApi(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        solveApi(req, resp);
    }

    private void solveApi(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        UserDto userEdit = (UserDto) SessionUtils.getInstance().getValue(req, Constant.USER_MODEL);
        BlogDto editBlot = HttpUtils.of(req.getReader()).toModel(BlogDto.class);

        boolean validBlog = blogService.validUpdateBlog(req, editBlot, userEdit.getUserId());

        ObjectMapper mapper = new ObjectMapper();
        if (validBlog) {

            Long editStatus = blogService.update(editBlot);
            if (editStatus != null)
                mapper.writeValue(resp.getOutputStream(), "Cập nhật thành công!");
            else {
                resp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
                mapper.writeValue(resp.getOutputStream(), "Cập nhật không thành công!");
            }
        } else {
            //Sửa lại message sao cho chuẩn
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            mapper.writeValue(resp.getOutputStream(), req.getAttribute("message"));
        }
    }
}