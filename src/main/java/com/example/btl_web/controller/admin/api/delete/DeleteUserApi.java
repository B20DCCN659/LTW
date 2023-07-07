package com.example.btl_web.controller.admin.api.delete;

import com.example.btl_web.configuration.ServiceConfiguration;
import com.example.btl_web.constant.Constant;
import com.example.btl_web.constant.Constant.*;
import com.example.btl_web.dto.UserDto;
import com.example.btl_web.service.UserService;
import com.example.btl_web.utils.HttpUtils;
import com.example.btl_web.utils.SessionUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = Admin.DELETE_USER_API)
public class DeleteUserApi extends HttpServlet {
    private UserService userService = ServiceConfiguration.getUserService();
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        solveApi(req,resp);
    }

    private void solveApi(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();

        UserDto deleteUser = HttpUtils.of(req.getReader()).toModel(UserDto.class);

        UserDto user = (UserDto) SessionUtils.getInstance().getValue(req, Constant.USER_MODEL);
        if(user.getRole() != Role.ADMIN && deleteUser.getUserId() != user.getUserId())
        {
            mapper.writeValue(resp.getOutputStream(), "Bạn không có quyền này");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Long deleteStatus = userService.deleteUser(deleteUser);
        String[] message = new String[1];
        if(deleteStatus == null)
        {
            message[0] = "Không thể xóa người dùng này";
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        else
        {
            message[0] = "Xóa người dùng thành công";
        }
        mapper.writeValue(resp.getOutputStream(), message);
    }
}
