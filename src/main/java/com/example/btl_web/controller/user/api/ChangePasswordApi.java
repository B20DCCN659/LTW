package com.example.btl_web.controller.user.api;

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

@WebServlet(urlPatterns = User.CHANGE_PASSWORD_API)
public class ChangePasswordApi extends HttpServlet {
    private UserService userService = ServiceConfiguration.getUserService();
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
        UserDto changePassWordUser = HttpUtils.of(req.getReader()).toModel(UserDto.class);
        changePassWordUser.setUserId(user.getUserId());
        changePassWordUser.setUserName(user.getUserName());

        String[] messages = new String[2];
        boolean changePasswordStatus = userService.changePassword(changePassWordUser, messages);
        if(!changePasswordStatus)
        {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        else
        {
            messages[0] = "Cập nhật mật khẩu thành công";
        }

        mapper.writeValue(resp.getOutputStream(), messages);
    }
}
