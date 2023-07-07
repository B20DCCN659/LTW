package com.example.btl_web.controller.admin.api.delete;

import com.example.btl_web.configuration.ServiceConfiguration;
import com.example.btl_web.constant.Constant;
import com.example.btl_web.constant.Constant.*;
import com.example.btl_web.dto.BlogDto;
import com.example.btl_web.dto.UserDto;
import com.example.btl_web.service.BlogService;
import com.example.btl_web.utils.HttpUtils;
import com.example.btl_web.utils.SessionUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = Admin.DELETE_BLOG_API)
public class DeleteBlogApi extends HttpServlet {
    private BlogService blogService = ServiceConfiguration.getBlogService();
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        solveApi(req, resp);
    }

    private void solveApi(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();

        UserDto user = (UserDto) SessionUtils.getInstance().getValue(req, Constant.USER_MODEL);
        if(user.getRole() != Role.ADMIN)
        {
            mapper.writeValue(resp.getOutputStream(), "Bạn không có quyền");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        BlogDto deleteBlog = HttpUtils.of(req.getReader()).toModel(BlogDto.class);

        Long deleteStatus = blogService.delete(deleteBlog.getBlogId());
        String message = "Không thể xóa truyện này";
        if(deleteStatus == null)
        {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        else
        {
            message = "Xóa truyện thành công";
        }
        mapper.writeValue(resp.getOutputStream(), message);
    }
}
