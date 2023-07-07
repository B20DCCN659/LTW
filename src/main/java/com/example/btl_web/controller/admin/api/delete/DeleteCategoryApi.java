package com.example.btl_web.controller.admin.api.delete;

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

@WebServlet(urlPatterns = Admin.DELETE_CATEGORY_API)
public class DeleteCategoryApi extends HttpServlet {
    private CategoryService categoryService = ServiceConfiguration.getCategoryService();
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
            mapper.writeValue(resp.getOutputStream(), "Bạn không có quyền làm việc này");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        CategoryDto deleteCategory = HttpUtils.of(req.getReader()).toModel(CategoryDto.class);

        String[] message = new String[1];
        boolean deleteStatus = categoryService.delete(deleteCategory.getCategoryId(), null);
        if(!deleteStatus)
        {
            message[0] = "Không thể xóa thể loại này";
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        else
        {
            message[0] = "Xóa thể loại này thành công";
        }
        mapper.writeValue(resp.getOutputStream(), message);
    }
}
